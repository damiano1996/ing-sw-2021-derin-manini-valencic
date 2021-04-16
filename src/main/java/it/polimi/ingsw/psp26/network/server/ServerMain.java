package it.polimi.ingsw.psp26.network.server;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) {

        try {
            Server server = new Server();
            server.startListening();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
