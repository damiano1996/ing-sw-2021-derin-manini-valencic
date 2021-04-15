package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
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

            Message message = (Message) client.receiveObjectData();
            handleMessage(message);

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (true) {
//                        try {
//                            Message message = (Message) client.receiveObjectData();
//                            handleMessage(message);
//                        } catch (IOException | ClassNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToClient(Message message) throws IOException {
        try { // TODO: remove this try after filling the empty messages in the model.
            nodeClients.get(message.getSessionToken()).sendData(message);
        } catch (Exception e) {

        }
    }

    public void handleMessage(Message message) {

        boolean forwarded = false;
        // Message must be forwarded to the virtual view of the corresponding user
        for (VirtualView virtualView : virtualViews) {
            try {

                virtualView.getMatchController().getMatch().getPlayerBySessionToken(message.getSessionToken());
                // If player exists with this sessionToken we can update the virtual view, forwarding the message,
                // otherwise we throw the exception and any update will performed.
                virtualView.forwardToMatchController(message);
                forwarded = true;

            } catch (PlayerDoesNotExistException e) {
                e.printStackTrace();
            }
        }

        // if message has not been forwarded, we must create a new virtual view
        if (!forwarded && message.getMessageType().equals(MessageType.MULTI_OR_SINGLE_PLAYER_MODE)) {
            assignPlayerToVirtualView(message);
        }
    }

    private void assignPlayerToVirtualView(Message message) {
        switch ((MessageType) message.getPayload()) {

            case MULTIPLAYER_MODE:

                boolean assigned = false;
                // searching for a match that is waiting for new players:
                for (VirtualView virtualView : virtualViews) {
                    if (virtualView.getMatchController().isWaitingForPlayers()) {
                        virtualView.forwardToMatchController(message);
                        assigned = true;
                    }
                }

                if (!assigned) {
                    // in this case we can create a new virtual view
                    createVirtualView(message);
                }
                break;

            case SINGLE_PLAYER_MODE:
                createVirtualView(message);
                break;
        }
    }

    private void createVirtualView(Message message) {
        VirtualView virtualView = new VirtualView(this);
        virtualView.forwardToMatchController(message);
        virtualViews.add(virtualView);
    }

}