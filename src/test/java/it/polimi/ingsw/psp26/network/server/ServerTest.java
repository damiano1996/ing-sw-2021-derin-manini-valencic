package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.NetworkNode;
import it.polimi.ingsw.psp26.network.server.memory.CommonNicknamePasswordChecksEnums;

import java.io.IOException;
import java.net.Socket;

import static it.polimi.ingsw.psp26.configurations.Configurations.*;
import static it.polimi.ingsw.psp26.network.NetworkUtils.generateSessionToken;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServerTest {


    private NetworkNode assignToVirtualView(MessageType playingMode) throws IOException, InvalidPayloadException, InterruptedException, ClassNotFoundException {

        Thread thread = new Thread(() -> {
            try {
                Server.getInstance().listening();
            } catch (IOException ignored) {
            }
        });
        thread.start();

        Socket socket = new Socket("localhost", DEFAULT_SERVER_PORT);
        NetworkNode networkNode = new NetworkNode(socket);

        String nickname = generateSessionToken(MIN_NICKNAME_LENGTH);
        String password = generateSessionToken(MIN_PASSWORD_LENGTH);

        // sending user data
        networkNode.sendData(nickname);
        networkNode.sendData(password);
        // waiting confirmation
        assertEquals(CommonNicknamePasswordChecksEnums.NICKNAME_AND_PASSWORD_ARE_OK, networkNode.receiveData());
        // receiving session token
        String sessionToken = (String) networkNode.receiveData();
        // receiving welcome general message
        SessionMessage sessionMessage = (SessionMessage) networkNode.receiveData();
        assertEquals(MessageType.GENERAL_MESSAGE, sessionMessage.getMessageType());
//        // receiving request for game new or old
//        sessionMessage = (SessionMessage) networkNode.receiveObjectData();
//        assertEquals(MessageType.NEW_OR_OLD, sessionMessage.getMessageType());
//        // sending response
//        networkNode.sendData(new SessionMessage(sessionToken, MessageType.NEW_OR_OLD, MessageType.NEW_MATCH));
        // waiting request for match mode
        sessionMessage = (SessionMessage) networkNode.receiveData();
        assertEquals(MessageType.MULTI_OR_SINGLE_PLAYER_MODE, sessionMessage.getMessageType());
        // sending playing mode
        networkNode.sendData(
                new SessionMessage(
                        sessionToken,
                        MessageType.MULTI_OR_SINGLE_PLAYER_MODE,
                        playingMode
                )
        );

        thread.join();

        // delay to allow the first user to be assigned to the match
        sleep(500);

        return networkNode;
    }

    private synchronized SessionMessage ignoreJoinMessage(NetworkNode networkNode) throws IOException, ClassNotFoundException {
        SessionMessage message = (SessionMessage) networkNode.receiveData();
        while (message.getMessageType().equals(MessageType.NOTIFICATION_UPDATE))
            message = (SessionMessage) networkNode.receiveData();
        return message;
    }

    private synchronized void assertStartMatch(NetworkNode networkNode) throws IOException, ClassNotFoundException, EmptyPayloadException {
        SessionMessage message = (SessionMessage) networkNode.receiveData();
        assertEquals(MessageType.NOTIFICATION_UPDATE, message.getMessageType());
        assertTrue(((String) message.getPayload()).contains("joined the game!"));

        message = ignoreJoinMessage(networkNode);
        assertEquals(MessageType.START_WAITING, message.getMessageType());

        message = ignoreJoinMessage(networkNode);
        assertEquals(MessageType.STOP_WAITING, message.getMessageType());

        message = ignoreJoinMessage(networkNode);
        assertEquals(MessageType.GENERAL_MESSAGE, message.getMessageType());
        assertTrue(((String) message.getPayload()).contains("The match can start!"));
    }

    // The tests below are working,
    // but only if they are executed separately from the others.
    // Motivations are: they use socket and threads that can go in conflict with other tests.

/*

    @Test
    public void testListening() throws IOException, InvalidPayloadException, InterruptedException, ClassNotFoundException, EmptyPayloadException {
        NetworkNode networkNode = assignToVirtualView(MessageType.SINGLE_PLAYER_MODE);
        assertStartMatch(networkNode);
    }

    @Test
    public void testAssigningToExistingVirtualView() throws IOException, InvalidPayloadException, InterruptedException, ClassNotFoundException, EmptyPayloadException {
        assignToVirtualView(MessageType.TWO_PLAYERS_MODE);
        NetworkNode networkNode = assignToVirtualView(MessageType.TWO_PLAYERS_MODE);
        assertStartMatch(networkNode);
    }

    @Test
    public void testTwoSimilarVirtualView() throws IOException, InvalidPayloadException, InterruptedException, ClassNotFoundException, EmptyPayloadException {
        assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        NetworkNode networkNode = assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        assertStartMatch(networkNode);

        assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        networkNode = assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        assertStartMatch(networkNode);
    }

 */

}