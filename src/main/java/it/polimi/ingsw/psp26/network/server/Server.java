package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.exceptions.PlayerDoesNotExistException;
import it.polimi.ingsw.psp26.network.NetworkNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.psp26.network.NetworkUtils.generateSessionToken;

public class Server {

    private final List<VirtualView> virtualViews; // One virtual view for each match
    private final Map<String, NetworkNode> nodeClients;

    public Server() {
        this.virtualViews = new ArrayList<>();
        nodeClients = new HashMap<>();
    }

    public void addNodeClient(NetworkNode client) {
        try {
            String sessionToken = generateSessionToken(32);
            System.out.println("New client - sessionToken: " + sessionToken);
            client.sendData(sessionToken);
            nodeClients.put(sessionToken, client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToClient(Message message) throws IOException {
        if (message.getSessionToken() != null) {
            nodeClients.get(message.getSessionToken()).sendData(message);
        }
    }

    public void receiveAndForwardMessage(NetworkNode nodeClient) throws IOException, ClassNotFoundException {
        Message message = (Message) nodeClient.receiveObjectData();

        // Message must be forwarded to the virtual view of the corresponding user
        for (VirtualView virtualView : virtualViews) {
            try {

                virtualView.getMatchController().getMatch().getPlayerBySessionToken(message.getSessionToken());
                // If player exists with this sessionToken we can update the virtual view, forwarding the message,
                // otherwise we throw the exception and any update will performed.
                virtualView.forwardToMatchController(message);

            } catch (PlayerDoesNotExistException e) {
                e.printStackTrace();
            }
        }
    }

}