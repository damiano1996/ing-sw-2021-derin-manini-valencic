package it.polimi.ingsw.psp26.network.server;

import java.io.IOException;

/**
 * Class used to start the Server.
 */
public class ServerMain {

    /**
     * Starts a new Server.
     */
    public static void main(String[] args) {
        try {
            //noinspection InfiniteLoopStatement
            while (true)
                Server.getInstance().listening(false);
        } catch (IOException ignored) {
        }
    }

}
