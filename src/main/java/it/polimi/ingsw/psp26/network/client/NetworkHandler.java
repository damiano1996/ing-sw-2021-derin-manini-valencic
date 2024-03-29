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
import static it.polimi.ingsw.psp26.configurations.Configurations.PRINT_CLIENT_SIDE;
import static it.polimi.ingsw.psp26.network.server.memory.CommonNicknamePasswordChecksEnums.*;
import static java.lang.Thread.sleep;

/**
 * This class simulates the match controller from the point of view of the client.
 * It sends messages to the virtual view and receives updates.
 */
public class NetworkHandler implements Observer<Message> {

    private final Client client;
    private NetworkNode networkNode;
    private String serverIP;
    private String sessionToken;

    private boolean connected;
    private boolean listening;

    /**
     * Constructor of the class.
     * It sets the NetworkHandler Client.
     * It also sets the attributes connected and listening to false.
     *
     * @param client the Client related to this NetworkHandler
     */
    public NetworkHandler(Client client) {
        super();
        this.client = client;
        connected = false;
        listening = false;
    }


    /**
     * Creates a new SessionMessage and sends it to the Server.
     *
     * @param message used to build the SessionMessage to send to the Server
     */
    @Override
    public void update(Message message) {
        try {
            if (PRINT_CLIENT_SIDE) System.out.println("NetworkHandler - " + message);
            networkNode.sendData(new SessionMessage(sessionToken, message.getMessageType(), message.getArrayPayloads()));
            if (PRINT_CLIENT_SIDE) System.out.println("NetworkHandler - Message has been sent successfully!");
        } catch (IOException | InvalidPayloadException ignored) {
        }
    }


    /**
     * Method used to initialize the NetworkNode.
     * The NetworkNode is connected to the DEFAULT_SERVER_PORT of the Server.
     * The NetworkNode first sends the Player nickname and password to the Server and waits a response if they are correct or not.
     * If the nickname and password satisfy the requirements, the method resets the NotificationStackFIFO and then calls
     * the startListening() and startHeartbeat() methods.
     * If the nickname and password are not correct, a specific Exception is rise.
     *
     * @param nickname the Player's nickname
     * @param password the Player's password
     * @param serverIP the Server's IP address
     * @throws IOException                    if error in IO socket communication
     * @throws PasswordNotCorrectException    the password doesn't match with the nickname
     * @throws NicknameTooShortException      the nickname is too short
     * @throws ClassNotFoundException         if object class not found
     * @throws NicknameAlreadyExistsException the nickname already exists in the Server memory
     * @throws PasswordTooShortException      the password is too short
     * @throws InvalidPayloadException        if the given payloads are not serializable
     */
    public synchronized void initializeNetworkNode(String nickname, String password, String serverIP) throws IOException, PasswordNotCorrectException, NicknameTooShortException, ClassNotFoundException, NicknameAlreadyExistsException, PasswordTooShortException, InvalidPayloadException {
        this.serverIP = serverIP;

        networkNode = new NetworkNode(new Socket(this.serverIP, DEFAULT_SERVER_PORT));
        connected = true;
        if (PRINT_CLIENT_SIDE) System.out.println("NetworkHandler - " + nickname + ":" + password + ":" + serverIP);
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
                if (PRINT_CLIENT_SIDE) System.out.println("NetworkHandler - Nickname and password are ok");

                client.setNickname(nickname, password);
                sessionToken = (String) networkNode.receiveData();

                NotificationsFIFO.getInstance().resetFIFO();

                listening = true;
                startListening();
                startHeartbeat();
                break;

        }
    }


    /**
     * Starts a new thread that sends a ping Message to the Server every 1000ms to tell the Server the Client is still alive.
     * If the Client is connected to the Server, sends the ping message normally.
     * If the Client lost connection, the method tries to re-establish the connection. If it is not possible to the connection
     * to come back, the method notifies the Client the connection has been lost and starts a new waiting screen.
     */
    private void startHeartbeat() {
        new Thread(() -> {
            boolean running = true;
            while (running) {
                try {

                    if (!connected) {
                        // trying to recovery connection
                        // step: establishing connection and sending user credentials
                        if (PRINT_CLIENT_SIDE) System.out.println("NetworkHandler - Trying to establish connection...");
                        initializeNetworkNode(client.getNickname(), client.getPassword(), serverIP);
                        connected = true;
                        // step: stopping this thread since initializeNetworkNode() will start a new one
                        running = false;
                        // step: sending a stop message
                        MessageSynchronizedFIFO.getInstance().update(new Message(MessageType.STOP_WAITING));
                    } else {
                        if (PRINT_CLIENT_SIDE) System.out.println("NetworkHandler - HEARTBEAT message.");
                        networkNode.sendData(new SessionMessage(sessionToken, MessageType.HEARTBEAT));
                    }

                } catch (InvalidPayloadException | NicknameTooShortException |
                        PasswordTooShortException | NicknameAlreadyExistsException | ClassNotFoundException |
                        PasswordNotCorrectException ignored) {
                    // Exceptions can be ignored since we were already connected, so we know that nickname and password were ok
                } catch (IOException ioException) {
                    if (connected) {
                        connected = false;
                        listening = false;
                        if (PRINT_CLIENT_SIDE) System.out.println("NetworkHandler - Lost connection!!");
                        try {
                            MessageSynchronizedFIFO.getInstance().update(
                                    new Message(
                                            MessageType.START_WAITING,
                                            "Connection error!\n" +
                                                    "Causes can be:\n" +
                                                    "1. You have lost the connection; or\n" +
                                                    "2. The server is no more reachable.\n" +
                                                    "Waiting for automatic recovery..."
                                    )
                            );
                        } catch (InvalidPayloadException ignored) {
                        }
                    }

                }

                try {
                    sleep(1000);
                } catch (InterruptedException ignored) {
                }

            }
        }).start();
    }


    /**
     * Creates a new thread used to receive Messages from Server.
     * Based on the MessageType of the received Message, performs different actions.
     */
    private void startListening() {
        new Thread(() -> {
            while (listening) {
                try {

                    Message message = (Message) networkNode.receiveData();
                    if (PRINT_CLIENT_SIDE)
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
            if (PRINT_CLIENT_SIDE) System.out.println("NetworkHandler - Stop listening from network node.");
        }).start();
    }


    /**
     * Getter of the SessionToken.
     *
     * @return the Player's SessionToken
     */
    public String getSessionToken() {
        return sessionToken;
    }

}