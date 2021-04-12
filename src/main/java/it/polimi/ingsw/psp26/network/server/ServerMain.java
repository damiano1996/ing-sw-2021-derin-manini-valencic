package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.network.NetworkNode;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static it.polimi.ingsw.psp26.configurations.Configurations.DEFAULT_SERVER_PORT;

public class ServerMain {

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(DEFAULT_SERVER_PORT);

            System.out.println("Server is listening on port " + DEFAULT_SERVER_PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client!");

                NetworkNode networkNode = new NetworkNode(socket);
                networkNode.sendData("Hello Client!!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
