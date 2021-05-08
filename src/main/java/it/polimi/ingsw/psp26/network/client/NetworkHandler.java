package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observer;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.NetworkNode;

import java.io.IOException;
import java.net.Socket;

import static it.polimi.ingsw.psp26.configurations.Configurations.DEFAULT_SERVER_PORT;

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

    @Override
    public void update(Message message) {
        try {
//            System.out.println("NetworkHandler - " + message.toString());
            sendToServer(new SessionMessage(sessionToken, message.getMessageType(), message.getArrayPayloads()));
        } catch (IOException | InvalidPayloadException e) {
            e.printStackTrace();
        }
    }

    public void initializeNetworkNode(String serverIP) throws IOException {
        networkNode = new NetworkNode(new Socket(serverIP, DEFAULT_SERVER_PORT));
        sessionToken = networkNode.receiveStringData();

        startListening();
    }

    private void startListening() {
        new Thread(() -> {
            while (true) {
                try {

                    Message message = (Message) networkNode.receiveObjectData();
//                    System.out.println("NetworkHandler - message received: " + message.toString());

                    switch (message.getMessageType()) {
                        case MODEL_UPDATE:
                            // TODO: send to container
                            break;

                        case NOTIFICATION_UPDATE:
                            // if notification update message, we can directly notify the client to display the message
                            client.liveUpdate(message);
                            break;

                        default:
                            // otherwise, we can stack the message
                            MessageSynchronizedFIFO.getInstance().update(message);
                            break;
                    }

                } catch (IOException | ClassNotFoundException e) {
                    // e.printStackTrace(); // -> EOFException exception is returned at every end of the stream.
                }
            }
        }).start();
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void sendToServer(SessionMessage message) throws IOException {
        networkNode.sendData(message);
    }
}