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
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.NetworkNode;
import it.polimi.ingsw.psp26.network.SpecialToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class VirtualView extends Observable<SessionMessage> implements Observer<SessionMessage> {

    private final MatchController matchController;

    private final Map<String, NetworkNode> nodeClients;

    public VirtualView() {
        super();
        nodeClients = new HashMap<>();
        matchController = new MatchController(this, getMatchId());
        addObserver(matchController);
    }


    /**
     * Updates the virtual view.
     * Messages will be sent to the network handler of the client.
     *
     * @param message message to forward
     */
    @Override
    public void update(SessionMessage message) {
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
    public void addNetworkNodeClient(String sessionToken, NetworkNode nodeClient) {
        System.out.println("VirtualView - new client has been added.");
        nodeClients.put(sessionToken, nodeClient);

        // Starting to monitor the heartbeat of this network node
        HeartbeatController heartbeatController = new HeartbeatController(sessionToken, matchController);
        heartbeatController.startMonitoringHeartbeat();
        addObserver(heartbeatController);

        // Start to listen the messages
        startListening(nodeClient);
    }


    /**
     * Starts to listen the client node. If a message is received, it will notify the observers (match controller).
     *
     * @param nodeClient network node of the client
     */
    private void startListening(NetworkNode nodeClient) {
        // it receives message from the communication channel and it has to forward the message to the controller
        new Thread(() -> {
            System.out.println("VirtualView - starting to listen client node.");
            while (true) {
                try {
                    SessionMessage message = (SessionMessage) nodeClient.receiveObjectData();
                    System.out.println("VirtualView - message received: " + message.toString());
                    notifyObservers(message);
                } catch (IOException | ClassNotFoundException e) {
                    // e.printStackTrace(); // -> EOFException exception is returned at every end of the stream.
                }
            }
        }).start();
    }


    private int getMatchId() {
        return 0; // TODO: if we want an incremental id we should implement a way to retrieve the last assigned id
    }


    /**
     * @return The MatchController
     */
    public MatchController getMatchController() {
        return matchController;
    }


    /**
     * Sends the SessionMessage to the Client
     * If it is a broadcast Message, sends it to all Players
     *
     * @param message The SessionMessage to send
     */
    private void sendToClient(SessionMessage message) {

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
    private SessionMessage filterModelUpdateMessage(SessionMessage message) {
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
