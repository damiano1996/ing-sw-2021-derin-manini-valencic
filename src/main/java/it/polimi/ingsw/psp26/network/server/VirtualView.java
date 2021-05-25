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
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.NetworkNode;
import it.polimi.ingsw.psp26.network.SpecialToken;

import java.io.IOException;
import java.util.*;

import static it.polimi.ingsw.psp26.network.server.MessageUtils.*;
import static it.polimi.ingsw.psp26.utils.CollectionsUtils.getIndexOf;

public class VirtualView extends Observable<SessionMessage> implements Observer<SessionMessage> {

    private final MatchController matchController;

    private final Map<String, NetworkNode> nodeClients;
    private final List<Boolean> connectedNetworkNodes;

    private final Map<String, HeartbeatController> heartbeatControllers;

    public VirtualView() {
        super();
        nodeClients = new HashMap<>();
        connectedNetworkNodes = new ArrayList<>();

        matchController = new MatchController(this, getMatchId());

        heartbeatControllers = new HashMap<>();

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

        // Start to listen the messages
        startListening(nodeClient, sessionToken);
    }

    private synchronized void startTrackingHeartbeat(String sessionToken) {
        try {
            // In case of recovery mode we don't have to create a new heartbeat controller,
            // but we can just rest it.
            heartbeatControllers.get(sessionToken).reset(sessionToken);
            // resending to client all match main data!
            sendToClient(getMarketTrayModelUpdateMessage());
            sendToClient(getDevelopmentGridModelUpdateMessage());
            for (Player player : matchController.getMatch().getPlayers())
                sendToClient(Objects.requireNonNull(getPlayerModelUpdateMessage(player.getSessionToken())));
            sendToClient(new SessionMessage(sessionToken, MessageType.START_WAITING));
            sendToClient(new SessionMessage(sessionToken, MessageType.STOP_WAITING));

        } catch (Exception | InvalidPayloadException e) {
            // Only in case of a new node
            // Starting to monitor the heartbeat of this network node
            HeartbeatController heartbeatController = new HeartbeatController(sessionToken, matchController);
            heartbeatController.startMonitoringHeartbeat();
            heartbeatControllers.put(sessionToken, heartbeatController);
            // adding player
            try {
                matchController.update(new SessionMessage(sessionToken, MessageType.ADD_PLAYER));
            } catch (InvalidPayloadException ignored) {
            }
        }
    }


    /**
     * Starts to listen the client node. If a message is received, it will notify the observers (match controller).
     *
     * @param nodeClient network node of the client
     */
    private synchronized void startListening(NetworkNode nodeClient, String sessionToken) {
        // it receives message from the communication channel and it has to forward the message to the controller
        new Thread(() -> {
            System.out.println("VirtualView - Starting to listen client node.");
            startTrackingHeartbeat(sessionToken);
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

                } catch (IOException | ClassNotFoundException | ValueDoesNotExistsException e) {
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
     * Method to remove the network node from the pool.
     * It will be called from the listening thread.
     *
     * @param networkNode network node that must be removed
     */
    private synchronized void removeNetworkNode(NetworkNode networkNode) {
        try {
            int indexOf = getIndexOf(nodeClients, networkNode);
            String sessionToken = new ArrayList<>(nodeClients.keySet()).get(indexOf);
            nodeClients.remove(sessionToken).closeConnection();
            connectedNetworkNodes.remove(indexOf);
        } catch (ValueDoesNotExistsException | IOException ignored) {
        }
    }

    /**
     * Method to stop the thread that is listening messages from client node.
     * It sets to false the associated value of the network node that is referring to the connection status.
     * This action will stop the thread that will remove the network node from the pool of network clients.
     *
     * @param sessionToken session token of the disconnected player
     */
    public synchronized void stopListeningNetworkNode(String sessionToken) {
        try {
            connectedNetworkNodes.set(getIndexOf(nodeClients, nodeClients.get(sessionToken)), false);
        } catch (ValueDoesNotExistsException ignored) {
        }
    }

    private synchronized int getMatchId() {
        return 0; // TODO: if we want an incremental id we should implement a way to retrieve the last assigned id
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

}
