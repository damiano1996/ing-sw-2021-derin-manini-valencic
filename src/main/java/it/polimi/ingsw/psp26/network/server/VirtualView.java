package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.ModelUpdateMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;
import it.polimi.ingsw.psp26.controller.HeartbeatController;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.exceptions.PlayerDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.ValueDoesNotExistsException;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.NetworkNode;
import it.polimi.ingsw.psp26.network.SpecialToken;
import it.polimi.ingsw.psp26.network.server.memory.GameSaver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.psp26.application.messages.MessageType.SET_NUMBER_OF_PLAYERS;
import static it.polimi.ingsw.psp26.application.messages.MessageType.STOP_WAITING;
import static it.polimi.ingsw.psp26.network.server.MessageUtils.*;
import static it.polimi.ingsw.psp26.utils.CollectionsUtils.getIndexOf;

public class VirtualView extends Observable<SessionMessage> implements Observer<SessionMessage> {

    private final MatchController matchController;

    private final Map<String, NetworkNode> nodeClients;
    private final List<Boolean> connectedNetworkNodes;

    private final Map<String, HeartbeatController> heartbeatControllers;

    // Map containing [sessionToken;boolean] that tells if a nodeClient must be moved to the WaitingRoom or not
    private final Map<String, Boolean> moveToWaitingRoom;

    public VirtualView() {
        super();
        nodeClients = new HashMap<>();
        connectedNetworkNodes = new ArrayList<>();

        matchController = new MatchController(this, getMatchId());

        heartbeatControllers = new HashMap<>();
        moveToWaitingRoom = new HashMap<>();

        addObserver(matchController);
    }

    public VirtualView(Match match, int turnPlayerIndex, int turnNumber) {
        super();
        nodeClients = new HashMap<>();
        connectedNetworkNodes = new ArrayList<>();

        match.recoverVirtualView(this);
        matchController = new MatchController(this, match, turnPlayerIndex, turnNumber);

        heartbeatControllers = new HashMap<>();
        moveToWaitingRoom = new HashMap<>();

        addObserver(matchController);
    }

    /**
     * Updates the virtual view.
     * Messages will be sent to the network handler of the client.
     *
     * @param message message to forward
     */
    @Override
    public synchronized void update(SessionMessage message) {
        message = filterModelUpdateMessage(message);
        sendToClient(message);
    }


    /**
     * Adds a new Client in the nodeClients Map
     * Starts the heartbeat for the added Client
     *
     * @param sessionToken The sessionToken of the Client to add
     * @param nodeClient   The Client to add
     */
    public synchronized void addNetworkNodeClient(String sessionToken, NetworkNode nodeClient) {
        System.out.println("VirtualView - new client has been added.");
        nodeClients.put(sessionToken, nodeClient);
        connectedNetworkNodes.add(true);

        if (matchController.isRecoveryMode()) sendingRecoveryMessagesAfterServerDownOrMatchContinuation(sessionToken);

        startTrackingHeartbeat(sessionToken);

        try {
            // adding player only if match is completely new, because in the recovered match the player already was present.
            if (!matchController.isRecoveryMode())
                notifyObservers(new SessionMessage(sessionToken, MessageType.ADD_PLAYER));

            sendingMainMatchComponents();

        } catch (InvalidPayloadException ignored) {
        }

        // Start to listen the messages
        startListening(nodeClient);
    }

    private synchronized void startTrackingHeartbeat(String sessionToken) {
        try {

            // In case of recovery mode (for client termination (!= server termination))
            // we don't have to create a new heartbeat controller,
            // but we can just rest it.
            if (heartbeatControllers.get(sessionToken).isDeath()) {
                System.out.println("VirtualView - Resetting (recovering) heartbeat of user.");
                // we can reset the heartbeat
                heartbeatControllers.get(sessionToken).reset(sessionToken);
                // sending recovery messages
                sendingRecoveryMessagesAfterClientNodeDown(sessionToken);
            }

        } catch (Exception e) {
            // Only in case of a new node (new match or recovered (after server down))
            // Starting to monitor the heartbeat of this network node
            HeartbeatController heartbeatController = new HeartbeatController(sessionToken, matchController);
            heartbeatController.startMonitoringHeartbeat();
            heartbeatControllers.put(sessionToken, heartbeatController);
        }
    }

    private void sendingRecoveryMessagesAfterServerDownOrMatchContinuation(String sessionToken) {
        try {
            System.out.println("VirtualView - Sending recovery messages after server restoration.");
            update(new SessionMessage(sessionToken, MessageType.GENERAL_MESSAGE, "Your match has been reloaded!"));
            update(new SessionMessage(sessionToken, SET_NUMBER_OF_PLAYERS, matchController.getMatch().getPlayers().size()));
            // Sending message to match controller to activate the RecoveringMatchPhaseState
            notifyObservers(new SessionMessage(sessionToken, MessageType.GENERAL_MESSAGE));
        } catch (InvalidPayloadException ignored) {
        }
    }

    private void sendingRecoveryMessagesAfterClientNodeDown(String sessionToken) {
        try {
            update(new SessionMessage(sessionToken, MessageType.GENERAL_MESSAGE, "You can resume the match!"));
            update(new SessionMessage(sessionToken, SET_NUMBER_OF_PLAYERS, matchController.getMatch().getPlayers().size()));
            update(new SessionMessage(sessionToken, STOP_WAITING));
        } catch (InvalidPayloadException ignored) {
        }
    }

    private void sendingMainMatchComponents() throws InvalidPayloadException {
        update(getMarketTrayModelUpdateMessage());
        update(getDevelopmentGridModelUpdateMessage());
        for (Player player : matchController.getMatch().getPlayers())
            update(getPlayerModelUpdateMessage(player.getSessionToken()));
    }


    /**
     * Starts to listen the client node. If a message is received, it will notify the observers (match controller).
     *
     * @param nodeClient network node of the client
     */
    private synchronized void startListening(NetworkNode nodeClient) {
        // it receives message from the communication channel and it has to forward the message to the controller
        new Thread(() -> {
            System.out.println("VirtualView - Starting to listen client node.");
            while (true) {
                try {
                    // If node client is no more connected we stop the loop.
                    if (!connectedNetworkNodes.get(getIndexOf(nodeClients, nodeClient))) break;

                    SessionMessage message = (SessionMessage) nodeClient.receiveData();
                    System.out.println("VirtualView - message received: " + message.toString());

                    if (message.getMessageType().equals(MessageType.HEARTBEAT))
                        heartbeatControllers.get(message.getSessionToken()).update(message);
                    else
                        notifyObservers(message);

                } catch (IOException | ClassNotFoundException | ValueDoesNotExistsException | NullPointerException e) {
                    // e.printStackTrace(); // -> EOFException exception is returned at every end of the stream.
                }
            }
            // Removing the node
            System.out.println("VirtualView - Stop to listen network node.");
            removeNetworkNode(nodeClient);
            // The thread can stop safely

        }).start();
    }


    /**
     * Method to remove the network node from the pool or put it in the waiting room.
     * It will be called from the listening thread.
     * <p>
     * The method checks if the sessionToken has to be moved in the waiting room by getting the relative boolean value
     * contained in the moveToWaitingRoom Map:
     * (moveToWaitingRoom.get(sessionToken) == true) ==> the networkNode's heartbeatController is stopped and the networkNode is moved to the waiting room.
     * (moveToWaitingRoom.get(sessionToken) == false) ==> the networkNode is removed from the nodeClients Map and its connection closed
     *
     * @param networkNode network node that must be removed
     */
    private synchronized void removeNetworkNode(NetworkNode networkNode) {
        try {
            int indexOf = getIndexOf(nodeClients, networkNode);
            String sessionToken = new ArrayList<>(nodeClients.keySet()).get(indexOf);

            if (moveToWaitingRoom.get(sessionToken)) { // throwing ValueDoesNotExistsException
                heartbeatControllers.get(sessionToken).kill(sessionToken);
                Server.getInstance().addNodeClientToWaitingRoom(sessionToken, nodeClients.remove(sessionToken));
            } else {
                nodeClients.remove(sessionToken).closeConnection();
            }
            connectedNetworkNodes.remove(indexOf);
        } catch (ValueDoesNotExistsException | IOException ignored) {
        }
    }

    /**
     * Method to stop the thread that is listening messages from client node.
     * It sets to false the associated value of the network node that is referring to the connection status.
     * As consequence, this action will stop the "listening" thread that will remove the network node from the list of network clients.
     * It also puts a new pair [sessionToken;boolean] in the moveToWaitingRoom Map
     *
     * @param sessionToken        session token of the disconnected player
     * @param onStopToWaitingRoom True if the networkNode must be moved to the waiting room, false if the networkNode must only be deleted
     */
    public synchronized void stopListeningNetworkNode(String sessionToken, boolean onStopToWaitingRoom) {
        try {
            moveToWaitingRoom.put(sessionToken, onStopToWaitingRoom);
            connectedNetworkNodes.set(getIndexOf(nodeClients, nodeClients.get(sessionToken)), false);
        } catch (ValueDoesNotExistsException ignored) {
        }
    }

    private synchronized int getMatchId() {
        return GameSaver.getInstance().getLastId() + 1;
    }


    /**
     * @return The MatchController
     */
    public synchronized MatchController getMatchController() {
        return matchController;
    }


    /**
     * Sends the SessionMessage to the Client
     * If it is a broadcast Message, sends it to all Players
     *
     * @param message The SessionMessage to send
     */
    private synchronized void sendToClient(SessionMessage message) {

        // broadcast branch
        if (message.getSessionToken().equals(SpecialToken.BROADCAST.getToken())) {

            for (Player player : matchController.getMatch().getPlayers()) {
                message.setSessionToken(player.getSessionToken());
                if (nodeClients.get(message.getSessionToken()) != null)
                    try {
                        nodeClients.get(player.getSessionToken()).sendData(message);
                        System.out.println("VirtualView - sending message to player: " + message);
                    } catch (Exception ignored) {
                    }
            }

        } else {
            // send message to the specified player
            if (nodeClients.get(message.getSessionToken()) != null)
                try {
                    nodeClients.get(message.getSessionToken()).sendData(message);
                    System.out.println("VirtualView - sending message to player: " + message);
                } catch (Exception ignored) {
                }
        }

    }


    /**
     * Creates a new ModelUpdateMessage based on the received SessionMessage's MessageType
     *
     * @param message The received SessionMessage
     * @return The new ModelUpdateMessage
     */
    private synchronized SessionMessage filterModelUpdateMessage(SessionMessage message) {
        try {

            if (message.getMessageType().equals(MessageType.PLAYER_MODEL)) {

                // Inserting a Player in the broadcast Message
                Object payload = matchController.getMatch().getPlayerBySessionToken(message.getSessionToken());
                return new ModelUpdateMessage(SpecialToken.BROADCAST.getToken(), message.getMessageType(), payload);

            } else if (message.getMessageType().equals(MessageType.MARKET_TRAY_MODEL)) {

                // Inserting the MarketTray in the broadcast Message
                Object payload = matchController.getMatch().getMarketTray();
                return new ModelUpdateMessage(SpecialToken.BROADCAST.getToken(), message.getMessageType(), payload);

            } else if (message.getMessageType().equals(MessageType.DEVELOPMENT_GRID_MODEL)) {

                // Inserting the DevelopmentCardGrid in the broadcast Message
                Object payload = matchController.getMatch().getDevelopmentGrid();
                return new ModelUpdateMessage(SpecialToken.BROADCAST.getToken(), message.getMessageType(), payload);

            }

        } catch (InvalidPayloadException | PlayerDoesNotExistException ignored) {
        }

        return message;
    }

    public synchronized int getNumberOfNodeClients() {
        return nodeClients.size();
    }

}
