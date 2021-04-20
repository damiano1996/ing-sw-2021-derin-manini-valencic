package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;
import it.polimi.ingsw.psp26.network.NetworkNode;

import java.io.IOException;
import java.net.Socket;

import static it.polimi.ingsw.psp26.configurations.Configurations.DEFAULT_SERVER_PORT;

/**
 * This class simulates the match controller from the point of view of the client.
 * It sends messages to the virtual view and receives updates.
 */
public class NetworkHandler extends Observable<Message> implements Observer<Message> {

    private NetworkNode networkNode;

    private String sessionToken;

    public NetworkHandler(Client client) {
        super();
        addObserver(client);
    }

    @Override
    public void update(Message message) {
        try {
            System.out.println("NetworkHandler - " + message.toString());
            sendToServer(new SessionMessage(sessionToken, message.getMessageType(), message.getArrayPayloads()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeNetworkNode(String serverIP) throws IOException {
        networkNode = new NetworkNode(new Socket(serverIP, DEFAULT_SERVER_PORT));
        sessionToken = networkNode.receiveStringData();

        startListening();
    }

    private void startListening() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Message message = (Message) networkNode.receiveObjectData();
                        System.out.println("NetworkHandler - new message received: " + message.toString());
                        notifyObservers(message);
                    } catch (IOException | ClassNotFoundException e) {
                        // e.printStackTrace(); // -> EOFException exception is returned at every end of the stream.
                    }
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