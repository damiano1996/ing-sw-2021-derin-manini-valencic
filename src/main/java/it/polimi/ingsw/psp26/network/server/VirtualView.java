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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.network.server.MessageUtils.*;
import static it.polimi.ingsw.psp26.utils.CollectionsUtils.getIndexOf;

/**
 * Class that represent a VirtualView, simulating a view but only on the Server side.
 */
public class VirtualView extends Observable<SessionMessage> implements Observer<SessionMessage> {

    private final MatchController matchController;

    private final Map<String, NetworkNode> nodeClients;
    private final List<Boolean> connectedNetworkNodes;

    private final Map<String, HeartbeatController> heartbeatControllers;

    // Map containing [sessionToken;boolean] that tells if a nodeClient must be moved to the WaitingRoom or not
    private final Map<String, Boolean> moveToWaitingRoom;

    /**
     * Default constructor of the class.
     * It initializes all the class attributes.
     * It creates a new MatchController.
     * Used if a completely new Match is wanted.
     */
    public VirtualView() {
        super();
        nodeClients = new HashMap<>();
        connectedNetworkNodes = new ArrayList<>();

        matchController = new MatchController(this, getMatchId());

        heartbeatControllers = new HashMap<>();
        moveToWaitingRoom = new HashMap<>();

        addObserver(matchController);
    }

    /**
     * Constructor of the class.
     * It initializes all the class attributes using the method's parameters.
     * It creates a MatchController which contains a recovered Match.
     * Used when a recovered Match is wanted.
     *
     * @param match           the recovered Match
     * @param turnPlayerIndex the index of the Player that was playing when the Match got interrupted
     * @param turnNumber      the turnNumber of the Match when it got interrupted
     */
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
     * Adds a new Client in the nodeClients Map.
     * Starts the heartbeat for the added Client.
     * If the Match is a recovered one, it sends the main model components to the Clients.
     *
     * @param sessionToken the sessionToken of the Client to add
     * @param nodeClient   the Client to add
     */
    public synchronized void addNetworkNodeClient(String sessionToken, NetworkNode nodeClient) {
        System.out.println("VirtualView - new client has been added.");
        nodeClients.put(sessionToken, nodeClient);
        connectedNetworkNodes.add(true);

        try {

            if (matchController.isRecoveryMode() || // case left meaning: server down and virtual view restored to recover the match
                    // (client in th while could be active, waiting for server recovery).
                    // Case below meaning: the client was already in the match but it lost the connection.
                    matchController.getMatch().getPlayers().stream().map(Player::getSessionToken).anyMatch(x -> x.equals(sessionToken))) {

                sendingMainMatchComponents(sessionToken);
                sendingRecoveryMessages(sessionToken);
                // Message to trigger the match controller
                System.out.println("VirtualView - Sending general message to match controller to trigger it.");
                notifyObservers(new SessionMessage(sessionToken, GENERAL_MESSAGE));
            } else {
                notifyObservers(new SessionMessage(sessionToken, MessageType.ADD_PLAYER));
            }

        } catch (InvalidPayloadException ignored) {
        }

        startTrackingHeartbeat(sessionToken);
        // Start to listen the messages
        startListening(nodeClient);
    }


    /**
     * Method that monitors the HeartBeats of the Clients.
     * If the Client is not sending HeartBeats (is dead), the method resets the relative heartBeat controller.
     *
     * @param sessionToken the sessionToken of the Client to track the HeartBeat
     */
    private synchronized void startTrackingHeartbeat(String sessionToken) {
        try {
            // In case of recovery mode (for client termination (!= server termination))
            // we don't have to create a new heartbeat controller,
            // but we can just rest it.
            if (heartbeatControllers.get(sessionToken).isDeath()) {
                System.out.println("VirtualView - Resetting (recovering) heartbeat of user.");
                // we can reset the heartbeat
                heartbeatControllers.get(sessionToken).reset(sessionToken);
            }
        } catch (Exception e) {
            // Only in case of a new node (new match or recovered (after server down))
            // Starting to monitor the heartbeat of this network node
            HeartbeatController heartbeatController = new HeartbeatController(sessionToken, matchController);
            heartbeatController.startMonitoringHeartbeat();
            heartbeatControllers.put(sessionToken, heartbeatController);
        }
    }


    /**
     * Method that sends recovery messages to the Client.
     *
     * @param sessionToken the sessionToken of the Client that is going to be notified
     */
    private void sendingRecoveryMessages(String sessionToken) {
        try {
            System.out.println("VirtualView - Sending recovery messages.");
            // Sending a stop message to stop a waiting screen in case of player is waiting for recovery.
            update(new SessionMessage(sessionToken, STOP_WAITING));
            update(new SessionMessage(sessionToken, MessageType.GENERAL_MESSAGE, "Your match has been recovered!"));
        } catch (InvalidPayloadException ignored) {
        }
    }


    /**
     * Sends the updated version of:
     * - Players
     * - MarketTray
     * - DevelopmentGridCard
     * in order to update the Clients CacheModel.
     *
     * @param sessionToken the sessionToken of the Client that will receive the updated objects
     * @throws InvalidPayloadException the payload can't be serialized
     */
    public void sendingMainMatchComponents(String sessionToken) throws InvalidPayloadException {
        update(new SessionMessage(sessionToken, SET_NUMBER_OF_PLAYERS, matchController.getMaxNumberOfPlayers()));
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

                } catch (Exception | ValueDoesNotExistsException e) {
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
     * (moveToWaitingRoom.get(sessionToken) == false) ==> the networkNode is removed from the nodeClients Map and its connection closed.
     *
     * @param networkNode network node that must be removed
     */
    private synchronized void removeNetworkNode(NetworkNode networkNode) {
        try {
            int indexOf = getIndexOf(nodeClients, networkNode);
            String sessionToken = new ArrayList<>(nodeClients.keySet()).get(indexOf);

            if (moveToWaitingRoom.get(sessionToken)) {
                heartbeatControllers.get(sessionToken).kill(sessionToken);
                Server.getInstance().addNodeClientToWaitingRoom(sessionToken, nodeClients.remove(sessionToken));
            } else {
                nodeClients.remove(sessionToken).closeConnection();
            }
            connectedNetworkNodes.remove(indexOf);
        } catch (Exception | ValueDoesNotExistsException ignored) {
        }
    }


    /**
     * Method to stop the thread that is listening messages from client node.
     * It sets to false the associated value of the network node that is referring to the connection status.
     * As consequence, this action will stop the "listening" thread that will remove the network node from the list of network clients.
     * It also puts a new pair [sessionToken;boolean] in the moveToWaitingRoom Map.
     *
     * @param sessionToken        session token of the disconnected player
     * @param onStopToWaitingRoom true if the networkNode must be moved to the waiting room, false if the networkNode must only be deleted
     */
    public synchronized void stopListeningNetworkNode(String sessionToken, boolean onStopToWaitingRoom) {
        try {
            moveToWaitingRoom.put(sessionToken, onStopToWaitingRoom);
            connectedNetworkNodes.set(getIndexOf(nodeClients, nodeClients.get(sessionToken)), false);
        } catch (ValueDoesNotExistsException ignored) {
        }
    }


    /**
     * Generate a new MatchID by counting how many matches are saved and incrementing that value by 1.
     *
     * @return the so generated MatchID
     */
    private synchronized int getMatchId() {
        return GameSaver.getInstance().getLastId() + 1;
    }


    /**
     * Getter of the Match Controller.
     *
     * @return the MatchController
     */
    public synchronized MatchController getMatchController() {
        return matchController;
    }


    /**
     * Sends the SessionMessage to the Client.
     * If it is a broadcast Message, sends it to all Players.
     *
     * @param message the SessionMessage to send
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
     * Creates a new ModelUpdateMessage based on the received SessionMessage's MessageType.
     *
     * @param message the received SessionMessage
     * @return the new ModelUpdateMessage
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


    /**
     * Getter of the number of Client nodes.
     *
     * @return the number of Client nodes
     */
    public synchronized int getNumberOfNodeClients() {
        return nodeClients.size();
    }


    /**
     * Method that stops the heartbeatControllers.
     */
    public void killHeartbeats() {
        for (String sessionToken : heartbeatControllers.keySet()) {
            heartbeatControllers.get(sessionToken).kill(sessionToken);

        }
    }

}
