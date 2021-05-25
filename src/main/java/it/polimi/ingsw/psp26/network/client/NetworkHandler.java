package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observer;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.network.NetworkNode;
import it.polimi.ingsw.psp26.network.server.memory.CommonNicknamePasswordChecksEnums;

import java.io.IOException;
import java.net.Socket;

import static it.polimi.ingsw.psp26.configurations.Configurations.DEFAULT_SERVER_PORT;
import static it.polimi.ingsw.psp26.network.server.memory.CommonNicknamePasswordChecksEnums.*;
import static java.lang.Thread.sleep;

/**
 * This class simulates the match controller from the point of view of the client.
 * It sends messages to the virtual view and receives updates.
 */
public class NetworkHandler implements Observer<Message> {

    private final Client client;
    private NetworkNode networkNode;
    private String sessionToken;

    public NetworkHandler(Client client) {
        super();
        this.client = client;
    }


    /**
     * Creates a new SessionMessage and sends it to the Server
     *
     * @param message Used to build the SessionMessage to send to the Server
     */
    @Override
    public void update(Message message) {
        try {
            // System.out.println("NetworkHandler - " + message);
            networkNode.sendData(new SessionMessage(sessionToken, message.getMessageType(), message.getArrayPayloads()));
            // System.out.println("NetworkHandler - Message has been sent successfully!");
        } catch (IOException | InvalidPayloadException e) {
            e.printStackTrace();
        }
    }

    public void initializeNetworkNode(String nickname, String password, String serverIP) throws IOException, PasswordNotCorrectException, NicknameTooShortException, ClassNotFoundException, NicknameAlreadyExistsException, PasswordTooShortException, InvalidPayloadException {

        networkNode = new NetworkNode(new Socket(serverIP, DEFAULT_SERVER_PORT));
        System.out.println("NetworkHandler - " + nickname + ":" + password + ":" + serverIP);
        // step: sending nickname and password to server
        networkNode.sendData(nickname);
        networkNode.sendData(password);

        // step: checking response, server agree with nickname and password?
        CommonNicknamePasswordChecksEnums response = (CommonNicknamePasswordChecksEnums) networkNode.receiveData();

        switch (response) {
            case PASSWORD_NOT_CORRECT:
                MessageSynchronizedFIFO.getInstance().update(new Message(MessageType.ERROR_MESSAGE, PASSWORD_NOT_CORRECT.getDescription()));
                throw new PasswordNotCorrectException();
            case NICKNAME_TOO_SHORT:
                MessageSynchronizedFIFO.getInstance().update(new Message(MessageType.ERROR_MESSAGE, NICKNAME_TOO_SHORT.getDescription()));
                throw new NicknameTooShortException();
            case NICKNAME_ALREADY_EXISTS:
                MessageSynchronizedFIFO.getInstance().update(new Message(MessageType.ERROR_MESSAGE, NICKNAME_ALREADY_EXISTS.getDescription()));
                throw new NicknameAlreadyExistsException();
            case PASSWORD_TOO_SHORT:
                MessageSynchronizedFIFO.getInstance().update(new Message(MessageType.ERROR_MESSAGE, PASSWORD_TOO_SHORT.getDescription()));
                throw new PasswordTooShortException();

            case NICKNAME_AND_PASSWORD_ARE_OK:
                // System.out.println("NetworkHandler - Nickname and password are ok");

                client.setNickname(nickname);
                sessionToken = (String) networkNode.receiveData();

                startListening();
                startHeartbeat();
                break;
        }
    }


    /**
     * Sends a ping Message to the Server every 1000ms to tell the Server the Client is still alive
     */
    private void startHeartbeat() {
        new Thread(() -> {
            while (true) {
                try {
                    networkNode.sendData(new SessionMessage(sessionToken, MessageType.HEARTBEAT));
                    sleep(1000);
                } catch (IOException | InvalidPayloadException | InterruptedException ignored) {
                }
            }
        }).start();
    }


    /**
     * Creates a Thread used to receive Messages from Server
     * Based on the MessageType of the received Message, performs different actions
     */
    private void startListening() {
        new Thread(() -> {
            while (true) {
                try {

                    Message message = (Message) networkNode.receiveData();
                    System.out.println("NetworkHandler - message received: " + message.toString());

                    switch (message.getMessageType()) {
                        case MODEL_UPDATE:
                            // if MODEL_UPDATE message or NOTIFICATION_UPDATE message, update the cachedModel
                            client.getCachedModel().updateCachedModel(message);
                            break;

                        case NOTIFICATION_UPDATE:
                            // if notification update message, we can directly notify the client to display the message
                            NotificationsFIFO.getInstance().pushNotification((String) message.getPayload());
                            break;

                        default:
                            // otherwise, we can stack the message
                            MessageSynchronizedFIFO.getInstance().update(message);
                            break;
                    }

                } catch (IOException | ClassNotFoundException | EmptyPayloadException e) {
                    // e.printStackTrace(); // -> EOFException exception is returned at every end of the stream.
                }
            }
        }).start();
    }


    /**
     * @return The Player's SessionToken
     */
    public String getSessionToken() {
        return sessionToken;
    }
}