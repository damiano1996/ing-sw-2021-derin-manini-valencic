package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.NetworkNode;
import it.polimi.ingsw.psp26.network.server.memory.CommonNicknamePasswordChecksEnums;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

import static it.polimi.ingsw.psp26.configurations.Configurations.*;
import static it.polimi.ingsw.psp26.network.NetworkUtils.generateSessionToken;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServerTest {

    private int getNumberOfTotalPlayers() {
        int totalNumber = 0;
        for (VirtualView virtualView : Server.getInstance().getVirtualViews()) {
            totalNumber += virtualView.getMatchController().getMatch().getPlayers().size();
        }
        return totalNumber;
    }

    private NetworkNode connectionSequence(MessageType playingMode) throws IOException, ClassNotFoundException, InvalidPayloadException {

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
        // receiving menu options
        sessionMessage = (SessionMessage) networkNode.receiveData();
        assertEquals(MessageType.MENU, sessionMessage.getMessageType());
        networkNode.sendData(new SessionMessage(sessionToken, MessageType.MENU, MessageType.PLAY));
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

        return networkNode;
    }


    private NetworkNode assignToVirtualView(MessageType playingMode) throws InterruptedException {

        int initialNumberOfPlayers = getNumberOfTotalPlayers();

        Thread serverThread = new Thread(() -> {
            try {
                Server.getInstance().listening(true);
                // Waiting that the player is completely assigned to the virtual view to void parallel match creations
                while (getNumberOfTotalPlayers() == initialNumberOfPlayers) sleep(10);
            } catch (IOException | InterruptedException ignored) {
            }
        });
        serverThread.start();

        AtomicReference<NetworkNode> networkNode = new AtomicReference<>();
        Thread clientThread = new Thread(() -> {
            try {
                networkNode.set(connectionSequence(playingMode));
            } catch (IOException | ClassNotFoundException | InvalidPayloadException ignored) {
            }
        });

        clientThread.start();
        clientThread.join();

        serverThread.join();

        return networkNode.get();
    }

    private synchronized SessionMessage ignoreJoinMessage(NetworkNode networkNode) throws IOException, ClassNotFoundException {
        SessionMessage message = (SessionMessage) networkNode.receiveData();
        while (message.getMessageType().equals(MessageType.NOTIFICATION_UPDATE))
            message = (SessionMessage) networkNode.receiveData();
        return message;
    }

    private synchronized void assertStartMatch(NetworkNode networkNode) throws IOException, ClassNotFoundException, EmptyPayloadException {
        SessionMessage message = ignoreJoinMessage(networkNode);
        assertEquals(MessageType.SET_NUMBER_OF_PLAYERS, message.getMessageType());

        message = ignoreJoinMessage(networkNode);
        assertEquals(MessageType.START_WAITING, message.getMessageType());

        message = ignoreJoinMessage(networkNode);
        assertEquals(MessageType.STOP_WAITING, message.getMessageType());

        message = ignoreJoinMessage(networkNode);
        assertEquals(MessageType.GENERAL_MESSAGE, message.getMessageType());
        assertTrue(((String) message.getPayload()).contains("The match can start!"));
    }

    @Test
    public void testListening() throws IOException, InterruptedException, ClassNotFoundException, EmptyPayloadException {
        NetworkNode networkNode = assignToVirtualView(MessageType.SINGLE_PLAYER_MODE);
        assertStartMatch(networkNode);
    }

    @Test
    public void testAssigningToExistingVirtualView() throws IOException, InterruptedException, ClassNotFoundException, EmptyPayloadException {
        assignToVirtualView(MessageType.TWO_PLAYERS_MODE);
        NetworkNode networkNode = assignToVirtualView(MessageType.TWO_PLAYERS_MODE);
        assertStartMatch(networkNode);
    }

    @Test
    public void testTwoSimilarVirtualView() throws IOException, InterruptedException, ClassNotFoundException, EmptyPayloadException {
        assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        NetworkNode networkNode = assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        assertStartMatch(networkNode);

        assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        networkNode = assignToVirtualView(MessageType.THREE_PLAYERS_MODE);
        assertStartMatch(networkNode);
    }

}