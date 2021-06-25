package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.NetworkNode;
import it.polimi.ingsw.psp26.network.SpecialToken;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.network.server.MessageUtils.filterHeartbeatMessages;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Class that simulates a WaitingRoom for Clients.
 */
public class WaitingRoom {

    private final Map<String, NetworkNode> nodeClients;

    /**
     * Constructor of the class
     * It initializes the nodeClients Mal.
     */
    public WaitingRoom() {
        nodeClients = new HashMap<>();
    }


    /**
     * Adds a Client into the WaitingRoom by adding it to the nodeClients Map.
     *
     * @param sessionToken the sessionToken of the Client to add to the WaitingRoom
     * @param nodeClient   the NetworkNode of the Client to add to the WaitingRoom
     */
    public void addNodeClient(String sessionToken, NetworkNode nodeClient) {
        nodeClients.put(sessionToken, nodeClient);

        try {
            // Sending a reset message to clean the view
            nodeClients.get(sessionToken).sendData(new SessionMessage(SpecialToken.BROADCAST.getToken(), RESET));
        } catch (IOException | InvalidPayloadException ignored) {
        }

        new Thread(() -> sendMenuMessage(sessionToken)).start();
    }


    /**
     * Sends the menu message to the Client corresponding to the sessionToken parameter.
     *
     * @param sessionToken the sessionToken of the Client that wil receive the menu Message
     */
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
                            PLAY, GLOBAL_LEADERBOARD, HELP, EXIT
                    )
            );

            System.out.println("WaitingRoom - Waiting response.");
            handleMessages(filterHeartbeatMessages(nodeClients.get(sessionToken)));

        } catch (InvalidPayloadException | ClassNotFoundException | IOException ignored) {
        }
    }


    /**
     * Handles the Messages received from the Client and performs different action based on the type of the Message received.
     *
     * @param message the Message received from Client
     */
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
                        System.out.println("WaitingRoom - GLOBAL_LEADERBOARD has been selected.");
                        sendToClient(new SessionMessage(message.getSessionToken(), GLOBAL_LEADERBOARD));
                        System.out.println("WaitingRoom - Waiting response.");
                        handleMessages(filterHeartbeatMessages(nodeClients.get(message.getSessionToken())));
                        break;

                    case HELP:
                        Path desktop = Path.of(System.getProperty("user.home") + "/Desktop/rules_ITA.pdf");
                        Files.copy(Objects.requireNonNull(getClass().getResource("/gui/rules_ITA.pdf")).openStream(), desktop, REPLACE_EXISTING);
                        File rulesPdf = new File(System.getProperty("user.home") + "/Desktop/rules_ITA.pdf");
                        Desktop.getDesktop().open(rulesPdf);
                        sendMenuMessage(message.getSessionToken());
                        break;

                    case EXIT:
                        System.out.println("WaitingRoom - EXIT has been selected.");
                        sendToClient(new SessionMessage(message.getSessionToken(), EXIT));
                        // Removing client from waiting room
                        nodeClients.remove(message.getSessionToken());
                        break;

                    case UNDO_OPTION_SELECTED:
                        System.out.println("WaitingRoom - Undo payload.");
                        sendMenuMessage(message.getSessionToken());
                        break;

                    default:
                        System.out.println("WaitingRoom - Unexpected payload.");
                        sendMenuMessage(message.getSessionToken());
                        break;
                }
            } else {
                System.out.println("WaitingRoom - Unexpected MessageType... Waiting for the next message.");
                handleMessages(filterHeartbeatMessages(nodeClients.get(message.getSessionToken())));
            }
        } catch (EmptyPayloadException | IOException | ClassNotFoundException | InvalidPayloadException ignored) {
        }
    }


    /**
     * If the Client decides to play, removes it from the nodeClients Map and starts a new VirtualViewAssignment thread
     * in order to create or recover a Match for the Client.
     *
     * @param sessionToken the sessionToken of the Client that wants to play
     */
    private void play(String sessionToken) {
        System.out.println("WaitingRoom - Starting virtual view assignment!");
        NetworkNode networkNode = removeNodeClient(sessionToken);
        new VirtualViewAssignment(sessionToken, networkNode).start();
    }


    /**
     * Removes and returns a nodeClient from the nodeClients Map.
     *
     * @param sessionToken the sessionToken of the nodeClient to remove
     * @return the removed nodeClient
     */
    private NetworkNode removeNodeClient(String sessionToken) {
        return nodeClients.remove(sessionToken);
    }


    /**
     * Sends a Message to the corresponding nodeClient, retrieving it by the sessionToken contained in the Message to send.
     *
     * @param message the Message to send to Client
     */
    private void sendToClient(SessionMessage message) {
        try {
            nodeClients.get(message.getSessionToken()).sendData(message);
        } catch (IOException ignored) {
        }
    }
}
