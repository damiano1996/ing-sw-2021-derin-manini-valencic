package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.application.Observer;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.network.NetworkNode;

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
        // it receives notification from model/controller and it has to notify the "real" view
        sendToClient(message);
    }

    public void addNetworkNodeClient(String sessionToken, NetworkNode nodeClient) {
        System.out.println("A new client has been added to this virtual view.");
        nodeClients.put(sessionToken, nodeClient);
        startListening(nodeClient);
    }

    /**
     * Starts to listen the client node. If message received, it will notify the observers (match controller).
     *
     * @param nodeClient network node of the client
     */
    private void startListening(NetworkNode nodeClient) {
        // it receives message from the communication channel and it has to forward the message to the controller
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        SessionMessage message = (SessionMessage) nodeClient.receiveObjectData();
                        notifyObservers(message); // send to match controller
                    } catch (IOException | ClassNotFoundException e) {
                        // e.printStackTrace(); // -> EOFException exception is returned at every end of the stream.
                    }
                }
            }
        }).start();
    }

    private int getMatchId() {
        return 0; // TODO: if we want an incremental id we should implement a way to retrieve the last assigned id
    }

    public MatchController getMatchController() {
        return matchController;
    }

    private void sendToClient(SessionMessage message) {
        try {
            if (nodeClients.get(message.getSessionToken()) != null)
                nodeClients.get(message.getSessionToken()).sendData(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
