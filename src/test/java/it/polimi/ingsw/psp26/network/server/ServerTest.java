package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.NetworkNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

import static it.polimi.ingsw.psp26.configurations.Configurations.DEFAULT_SERVER_PORT;
import static it.polimi.ingsw.psp26.configurations.Configurations.SESSION_TOKEN_LENGTH;
import static org.junit.Assert.assertEquals;

public class ServerTest {

    private Server server;

    @Before
    public void setUp() throws Exception {

        server = new Server();
    }

    private void assignToVirtualView(MessageType playingMode) throws IOException, InvalidPayloadException, InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                server.listening();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        Socket socket = new Socket("localhost", DEFAULT_SERVER_PORT);
        NetworkNode networkNode = new NetworkNode(socket);
        String sessionToken = networkNode.receiveStringData();
        assertEquals(SESSION_TOKEN_LENGTH, sessionToken.length());
        networkNode.sendData(
                new SessionMessage(
                        sessionToken,
                        MessageType.MULTI_OR_SINGLE_PLAYER_MODE,
                        playingMode
                )
        );

        thread.join();
    }

    @Test
    public void testListening() throws IOException, InvalidPayloadException, InterruptedException {
        assignToVirtualView(MessageType.SINGLE_PLAYER_MODE);
        server.closeConnection();
    }

    @Test
    public void testAssigningToExistingVirtualView() throws IOException, InvalidPayloadException, InterruptedException {
        assignToVirtualView(MessageType.TWO_PLAYERS_MODE);
        assignToVirtualView(MessageType.TWO_PLAYERS_MODE);
        server.closeConnection();
    }

    @Test
    public void testTwoSimilarVirtualView() throws IOException, InvalidPayloadException, InterruptedException {
        assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        assignToVirtualView(MessageType.THREE_PLAYERS_MODE);

        assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        assignToVirtualView(MessageType.THREE_PLAYERS_MODE);

        server.closeConnection();
    }
}