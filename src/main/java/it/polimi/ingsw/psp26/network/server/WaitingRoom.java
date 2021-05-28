package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.NetworkNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.network.server.MessageUtils.filterHeartbeatMessages;

public class WaitingRoom {

    private final Map<String, NetworkNode> nodeClients;

    public WaitingRoom() {
        nodeClients = new HashMap<>();
    }

    public void addNodeClient(String sessionToken, NetworkNode nodeClient) {
        nodeClients.put(sessionToken, nodeClient);

        new Thread(() -> sendMenuMessage(sessionToken)).start();
    }

    private void sendMenuMessage(String sessionToken) {
        try {
            System.out.println("WaitingRoom - sending menu message!");
            sendToClient(
                    new MultipleChoicesMessage(
                            sessionToken,
                            MENU,
                            "Menu",
                            1, 1,
                            false,
                            PLAY, GLOBAL_LEADERBOARD, HELP, SETTINGS
                    )
            );

            System.out.println("WaitingRoom - Waiting response.");
            handleMessages(filterHeartbeatMessages(nodeClients.get(sessionToken)));

        } catch (InvalidPayloadException | ClassNotFoundException | IOException ignored) {
        }
    }

    private void handleMessages(SessionMessage message) {
        System.out.println("WaitingRoom - message: " + message);

        try {
            if (message.getMessageType().equals(MENU)) {
                switch ((MessageType) message.getPayload()) {

                    case PLAY:
                        System.out.println("WaitingRoom - PLAY has been selected.");
                        play(message.getSessionToken());
                        break;

                    case GLOBAL_LEADERBOARD:
                        // TODO: to be implemented
                        break;

                    case HELP:
                        // TODO: to be implemented
                        break;

                    case SETTINGS:
                        // TODO: to be implemented
                        break;

                    default:
                        System.out.println("WaitingRoom - Unexpected payload.");
                        sendMenuMessage(message.getSessionToken());
                        break;
                }
            } else {
                System.out.println("WaitingRoom - Unexpected MessageType.");
                sendMenuMessage(message.getSessionToken());
            }
        } catch (EmptyPayloadException ignored) {
        }
    }

    private void play(String sessionToken) {
        System.out.println("WaitingRoom - Starting virtual view assignment!");
        NetworkNode networkNode = removeNodeClient(sessionToken);
        new VirtualViewAssignment(sessionToken, networkNode).start();
    }

    private NetworkNode removeNodeClient(String sessionToken) {
        return nodeClients.remove(sessionToken);
    }

    private void sendToClient(SessionMessage message) {
        try {
            nodeClients.get(message.getSessionToken()).sendData(message);
        } catch (IOException ignored) {
        }
    }
}
