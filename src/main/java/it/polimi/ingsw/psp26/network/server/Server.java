package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.network.NetworkNode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.configurations.Configurations.DEFAULT_SERVER_PORT;

/**
 * Class to model the server.
 */
public class Server {

    private static Server instance;

    private static ServerSocket serverSocket;
    private final List<VirtualView> virtualViews; // One virtual view for each match

    /**
     * Class constructor.
     *
     * @throws IOException if server socket cannot be instantiated
     */
    private Server() throws IOException {
        serverSocket = new ServerSocket(DEFAULT_SERVER_PORT);
        System.out.println("Server is listening on port " + DEFAULT_SERVER_PORT);

        this.virtualViews = new ArrayList<>();
    }

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
     * It generated a new socket, for the client communication, and assigns
     * it to the network node.
     * A thread is executed to parallelize new node initialization.
     *
     * @throws IOException if socket cannot be instantiated
     */
    public void listening() throws IOException {
        // New client is arrived
        Socket socket = serverSocket.accept();
        // run thread to handle client setup
        NetworkNode clientNode = new NetworkNode(socket);
        System.out.println("Server - New socket!");

        new VirtualViewAssignment(clientNode).start();
    }

    /**
     * Method to close the server socket connection.
     *
     * @throws IOException if socket unable to close the connection
     */
    public void closeConnection() throws IOException {
        serverSocket.close();
    }

    public List<VirtualView> getVirtualViews() {
        return virtualViews;
    }

    public void addVirtualView(VirtualView virtualView) {
        virtualViews.add(virtualView);
    }
}