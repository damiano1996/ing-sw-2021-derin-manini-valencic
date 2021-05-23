package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.DesiredVirtualViewDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.network.NetworkNode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.configurations.Configurations.DEFAULT_SERVER_PORT;
import static it.polimi.ingsw.psp26.configurations.Configurations.SESSION_TOKEN_LENGTH;
import static it.polimi.ingsw.psp26.network.NetworkUtils.generateSessionToken;

/**
 * Class to model the server.
 */
public class Server {

    private final ServerSocket serverSocket;

    private final List<VirtualView> virtualViews; // One virtual view for each match


    /**
     * Class constructor.
     *
     * @throws IOException if server socket cannot be instantiated
     */
    public Server() throws IOException {
        serverSocket = new ServerSocket(DEFAULT_SERVER_PORT);
        System.out.println("Server is listening on port " + DEFAULT_SERVER_PORT);

        this.virtualViews = new ArrayList<>();
    }

    /**
     * Method to listen for incoming clients.
     * It generated a new socket, for the client communication, and assigns
     * it to the network node.
     * A thread is executed to parallelize new node initialization.
     *
     * @throws IOException if socket cannot be instantiated
     */
    public void listening() throws IOException {
        // New client is arrived
        Socket socket = serverSocket.accept();
        NetworkNode nodeClient = new NetworkNode(socket);
        // run thread to handle client setup
        new Thread(() -> addNodeClient(nodeClient)).start();
    }

    /**
     * Method to close the server socket connection.
     *
     * @throws IOException if socket unable to close the connection
     */
    public void closeConnection() throws IOException {
        serverSocket.close();
    }

    /**
     * Method to accept a new player and add it to a virtual view.
     * By our protocol definition, the following steps are executed:
     * step 1: generates session token;
     * step 2: sends session token to client;
     * step 3: receives player mode message; and
     * step 4: assigns player to a virtual view.
     *
     * @param client node client of the new player
     */
    private void addNodeClient(NetworkNode client) {
        try {

            // step: generate session token
            String sessionToken = generateSessionToken(SESSION_TOKEN_LENGTH);
            System.out.println("Server - new client sessionToken: " + sessionToken);
            // step: send session token to client
            client.sendData(sessionToken);
            // step: receive player mode message
            SessionMessage message = (SessionMessage) client.receiveObjectData();
            while (!message.getMessageType().equals(MessageType.MULTI_OR_SINGLE_PLAYER_MODE))
                message = (SessionMessage) client.receiveObjectData();
            // step: assign player to a virtual view
            handlePlayerModeMessage(client, message);

        } catch (Exception | EmptyPayloadException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method to handle the assignment of the new network node to a virtual view.
     * Based on the playing mode selected, it checks if a match is waiting for a player.
     * If so, it assigns the node to the virtual view,
     * otherwise a new virtual view will be instantiated and the node will be assigned to it.
     *
     * @param clientNode network node of the new player
     * @param message    message containing as payload the selected playing mode
     * @throws EmptyPayloadException if message payload is empty
     */
    private void handlePlayerModeMessage(NetworkNode clientNode, SessionMessage message) throws EmptyPayloadException {
        if (message.getMessageType().equals(MessageType.MULTI_OR_SINGLE_PLAYER_MODE)) {
            System.out.println("Server - playing mode: " + message.getPayload());

            switch ((MessageType) message.getPayload()) {

                case SINGLE_PLAYER_MODE:
                    assignNodeToVirtualView(1, message.getSessionToken(), clientNode);
                    break;
                case TWO_PLAYERS_MODE:
                    assignNodeToVirtualView(2, message.getSessionToken(), clientNode);
                    break;
                case THREE_PLAYERS_MODE:
                    assignNodeToVirtualView(3, message.getSessionToken(), clientNode);
                    break;
                case FOUR_PLAYERS_MODE:
                    assignNodeToVirtualView(4, message.getSessionToken(), clientNode);
                    break;
            }
        }
    }

    /**
     * Method to assign a network node to a virtual view.
     * It tries to assign the node to an already existing one (that is waiting for a player to start the match).
     * If no virtual view is free, with the requested characteristics, the node will be assigned to a new virtual view.
     *
     * @param maxNumberOfPlayers number of players requested by the player
     * @param sessionToken       session token of the new player
     * @param clientNode         network node client of the new player
     */
    private void assignNodeToVirtualView(int maxNumberOfPlayers, String sessionToken, NetworkNode clientNode) {
        try {
            assignNodeToExistingVirtualView(maxNumberOfPlayers, sessionToken, clientNode);
        } catch (DesiredVirtualViewDoesNotExistException e) {
            assignNodeToNewVirtualView(maxNumberOfPlayers, sessionToken, clientNode);
        }
    }

    /**
     * Method to assign a network node to an already existing virtual view.
     * It checks among all the active virtual views if there is one that is waiting for a player
     * and with the requested number of players.
     * If there is, the node will be added, otherwise an exception will be thrown.
     *
     * @param maxNumberOfPlayers number of requested players for the match
     * @param sessionToken       session token of the new player
     * @param clientNode         network node client of the new player
     * @throws DesiredVirtualViewDoesNotExistException if virtual view is not waiting with the requested characteristics
     */
    private void assignNodeToExistingVirtualView(int maxNumberOfPlayers, String sessionToken, NetworkNode clientNode) throws DesiredVirtualViewDoesNotExistException {
        boolean assigned = false;
        for (VirtualView virtualView : virtualViews) {
            if (virtualView.getMatchController().isWaitingForPlayers() &&
                    virtualView.getMatchController().getMaxNumberOfPlayers() == maxNumberOfPlayers) {
                virtualView.addNetworkNodeClient(sessionToken, clientNode);
                assigned = true;
            }
        }
        if (!assigned) throw new DesiredVirtualViewDoesNotExistException();
    }

    /**
     * Method to assign the network node to a new virtual view.
     * It creates a new virtual view, and assigns the node to it.
     *
     * @param maxNumberOfPlayers number of requested players for the match
     * @param sessionToken       session token of te new player
     * @param clientNode         network node client of the new player
     */
    private void assignNodeToNewVirtualView(int maxNumberOfPlayers, String sessionToken, NetworkNode clientNode) {
        VirtualView virtualView = new VirtualView();
        // set the desired number of players
        virtualView.getMatchController().setMaxNumberOfPlayers(maxNumberOfPlayers);
        // add the node to the virtual view
        virtualView.addNetworkNodeClient(sessionToken, clientNode);
        // add virtual view to the list
        virtualViews.add(virtualView);
    }

}