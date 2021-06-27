package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.NetworkNode;
import it.polimi.ingsw.psp26.network.server.memory.GameSaver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.psp26.configurations.Configurations.DEFAULT_SERVER_PORT;

/**
 * Class to model the server.
 */
public class Server {

    private static Server instance;

    private static ServerSocket serverSocket;
    private final List<VirtualView> virtualViews; // One virtual view for each match
    private final WaitingRoom waitingRoom;

    /**
     * Class constructor.
     * It creates the ServerSocket, initializes the virtualViews List and initialize a new WaitingRom.
     *
     * @throws IOException if server socket cannot be instantiated
     */
    private Server() throws IOException {
        serverSocket = new ServerSocket(DEFAULT_SERVER_PORT);
        System.out.println("Server is listening on port " + DEFAULT_SERVER_PORT);

        this.virtualViews = new ArrayList<>();
        waitingRoom = new WaitingRoom();

        recoveryMatches();
    }

    /**
     * Getter of the instance of the class.
     *
     * @return the instance of the class
     */
    public synchronized static Server getInstance() {
        if (instance == null || serverSocket.isClosed()) {
            try {
                instance = new Server();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * Method to listen for incoming clients.
     * It generated a new socket, for the client communication, and assigns it to the network node.
     * A thread is executed to parallelize new node initialization.
     *
     * @param joinLogInThread boolean to join the thread that starts the login phase
     * @throws IOException if socket cannot be instantiated
     */
    public void listening(boolean joinLogInThread) throws IOException {
        // New client is arrived
        Socket socket = serverSocket.accept();
        // run thread to handle client setup
        NetworkNode clientNode = new NetworkNode(socket);
        System.out.println("Server - New socket!");

        LogIn logIn = new LogIn(clientNode);
        logIn.start();

        if (joinLogInThread) {
            try {
                logIn.join();
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * Method used to recover the saved Matches in the Server files when the Server is created.
     * It checks every saved Match file and creates a new Match using the GameSaver singleton.
     * For each Match created this way, the Server controls that all the saved Player have the correct sessionToken:
     * if this is the case creates a new VirtualView and insert the restored Match in it,
     * then adds the VirtualView to the virtualViews List;
     * otherwise, it prints an error message on terminal and repeat the cycle.
     */
    private void recoveryMatches() {
        System.out.println("Server - Recovering matches from files.");

        for (String gamePath : GameSaver.getInstance().getSavedMatchesDirectoriesNames()) {
            try {
                Match restoredMatch = GameSaver.getInstance().loadMatch(gamePath);

                // Checking if all the players have the session token correctly loaded
                boolean allPlayersHaveSessionToken = true;

                for (Player player : restoredMatch.getPlayers())
                    if (player.getSessionToken() == null) {
                        allPlayersHaveSessionToken = false;
                        break;
                    }

                if (allPlayersHaveSessionToken) {
                    VirtualView virtualView = new VirtualView(restoredMatch, GameSaver.getInstance().loadTurnPlayerIndex(gamePath), GameSaver.getInstance().loadTurnNumber(gamePath));
                    virtualViews.add(virtualView);
                } else {
                    System.out.println("Server - Corrupted match (id = " + GameSaver.getInstance().getID(gamePath) + "). At least one player has not the sessionToken.");
                }
            } catch (Exception e) {
                System.out.println("Server - Corrupted match (id = " + GameSaver.getInstance().getID(gamePath) + "). Unable to reload it!");
            }
        }
        System.out.println("Server - " + virtualViews.size() + " matches have been recovered!!");
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
     * Getter of the virtualViews List.
     *
     * @return an unmodifiable virtualViews List
     */
    public synchronized List<VirtualView> getVirtualViews() {
        return Collections.unmodifiableList(virtualViews);
    }

    /**
     * Adds a VirtualView to the virtualViews List.
     *
     * @param virtualView the VirtualView to add
     */
    public synchronized void addVirtualView(VirtualView virtualView) {
        virtualViews.add(virtualView);
    }

    /**
     * Adds a Client to the Waiting Room.
     *
     * @param sessionToken the sessionToken of the Client
     * @param nodeClient   the Network Node of the Client
     */
    public void addNodeClientToWaitingRoom(String sessionToken, NetworkNode nodeClient) {
        waitingRoom.addNodeClient(sessionToken, nodeClient);
    }

    /**
     * Removes a VirtualView from the virtualViews List.
     *
     * @param virtualView the VirtualView to remove
     */
    public synchronized void removeVirtualView(VirtualView virtualView) {
        virtualViews.remove(virtualView);
    }

}