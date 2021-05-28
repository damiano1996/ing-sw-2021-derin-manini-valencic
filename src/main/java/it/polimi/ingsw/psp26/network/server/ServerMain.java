package it.polimi.ingsw.psp26.network.server;

import java.io.IOException;

public class ServerMain {

    /**
     * Starts a new Server
     */
    public static void main(String[] args) {
        try {
            while (true)
                Server.getInstance().listening(false);
        } catch (IOException ignored) {
        }
    }

}
