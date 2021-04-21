package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.DesiredVirtualViewDoesNotExistException;
import it.polimi.ingsw.psp26.network.NetworkNode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.configurations.Configurations.DEFAULT_SERVER_PORT;
import static it.polimi.ingsw.psp26.network.NetworkUtils.generateSessionToken;

public class Server {

    private final ServerSocket serverSocket;

    private final List<VirtualView> virtualViews; // One virtual view for each match


    public Server() throws IOException {
        serverSocket = new ServerSocket(DEFAULT_SERVER_PORT);
        System.out.println("Server is listening on port " + DEFAULT_SERVER_PORT);

        this.virtualViews = new ArrayList<>();
    }

    public void startListening() throws IOException {
        while (true) {

            // New client is arrived
            Socket socket = serverSocket.accept();
            NetworkNode nodeClient = new NetworkNode(socket);

            // run thread to handle client setup
            new Thread(() -> addNodeClient(nodeClient)).start();
        }
    }

    private void addNodeClient(NetworkNode client) {
        try {

            // step: generate session token
            String sessionToken = generateSessionToken(32);
            System.out.println("New client - sessionToken: " + sessionToken);
            // step: send session token to client
            client.sendData(sessionToken);
            // step: receive player mode message
            SessionMessage message = (SessionMessage) client.receiveObjectData();
            // step: assign player to a virtual view
            handlePlayerModeMessage(client, message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handlePlayerModeMessage(NetworkNode clientNode, SessionMessage message) {
        System.out.println(message.getPayload());

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

    private void assignNodeToVirtualView(int maxNumberOfPlayers, String sessionToken, NetworkNode clientNode) {
        try {
            assignNodeToExistingVirtualView(maxNumberOfPlayers, sessionToken, clientNode);
        } catch (DesiredVirtualViewDoesNotExistException e) {
            assignNodeToNewVirtualView(maxNumberOfPlayers, sessionToken, clientNode);
        }
    }

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