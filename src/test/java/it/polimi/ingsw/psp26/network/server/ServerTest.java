package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.NetworkNode;
import it.polimi.ingsw.psp26.network.server.memory.CommonNicknamePasswordChecksEnums;
import it.polimi.ingsw.psp26.network.server.memory.GameSaver;
import it.polimi.ingsw.psp26.network.server.memory.Users;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

import static it.polimi.ingsw.psp26.configurations.Configurations.*;
import static it.polimi.ingsw.psp26.controller.HeartbeatController.MAX_TIME_TO_DIE;
import static it.polimi.ingsw.psp26.network.NetworkUtils.generateSessionToken;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServerTest {

    // private static boolean runningHeartbeat;

    private int getTotalNumberOfNodeClientsInVirtualViews() {
        int totalNumber = 0;
        for (VirtualView virtualView : Server.getInstance().getVirtualViews()) {
            totalNumber += virtualView.getNumberOfNodeClients();
        }
        return totalNumber;
    }

    private NetworkNode connectionSequence(MessageType playingMode, boolean recovery) throws IOException, ClassNotFoundException, InvalidPayloadException {
        return connectionSequence(playingMode, recovery, generateSessionToken(MIN_NICKNAME_LENGTH), generateSessionToken(MIN_PASSWORD_LENGTH));
    }


    private NetworkNode connectionSequence(MessageType playingMode, boolean recovery, String nickname, String password) throws IOException, ClassNotFoundException, InvalidPayloadException {

        Socket socket = new Socket("localhost", DEFAULT_SERVER_PORT);
        NetworkNode networkNode = new NetworkNode(socket);

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

        sessionMessage = (SessionMessage) networkNode.receiveData();
        // In case of recovery the sequence of messages can change
        if (sessionMessage.getMessageType().equals(MessageType.NEW_OR_OLD)) {
            if (recovery) {
                networkNode.sendData(new SessionMessage(sessionToken, MessageType.NEW_OR_OLD, MessageType.RECOVERY_MATCH));
                return networkNode;
            } else {
                networkNode.sendData(new SessionMessage(sessionToken, MessageType.NEW_OR_OLD, MessageType.NEW_MATCH));
                // waiting request for match mode
                sessionMessage = (SessionMessage) networkNode.receiveData();
            }
        }

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


    private NetworkNode assignToVirtualView(MessageType playingMode, boolean recovery) throws InterruptedException {
        return assignToVirtualView(playingMode, recovery, false, null, null);
    }

    private NetworkNode assignToVirtualView(MessageType playingMode, boolean recovery, boolean credentials, String nickname, String password) throws InterruptedException {

        int initialNumberOfPlayers = getTotalNumberOfNodeClientsInVirtualViews();

        Thread serverThread = new Thread(() -> {
            try {
                Server.getInstance().listening(true);
                // Waiting that the player is completely assigned to the virtual view to void parallel match creations
                while (getTotalNumberOfNodeClientsInVirtualViews() == initialNumberOfPlayers) sleep(10);
            } catch (IOException | InterruptedException ignored) {
            }
        });
        serverThread.start();

        AtomicReference<NetworkNode> networkNode = new AtomicReference<>();
        Thread clientThread = new Thread(() -> {
            try {
                if (credentials) networkNode.set(connectionSequence(playingMode, recovery, nickname, password));
                else networkNode.set(connectionSequence(playingMode, recovery));
            } catch (IOException | ClassNotFoundException | InvalidPayloadException ignored) {
            }
        });

        clientThread.start();
        clientThread.join();

        serverThread.join();

        return networkNode.get();
    }

    private synchronized SessionMessage ignoreMessages(NetworkNode networkNode) throws IOException, ClassNotFoundException {
        SessionMessage message = (SessionMessage) networkNode.receiveData();
        while (message.getMessageType().equals(MessageType.NOTIFICATION_UPDATE) || message.getMessageType().equals(MessageType.MODEL_UPDATE))
            message = (SessionMessage) networkNode.receiveData();
        return message;
    }

    private synchronized void assertStartMatch(NetworkNode networkNode) throws IOException, ClassNotFoundException, EmptyPayloadException {
        SessionMessage message = ignoreMessages(networkNode);
        assertEquals(MessageType.SET_NUMBER_OF_PLAYERS, message.getMessageType());

        message = ignoreMessages(networkNode);
        assertEquals(MessageType.START_WAITING, message.getMessageType());

        message = ignoreMessages(networkNode);
        assertEquals(MessageType.STOP_WAITING, message.getMessageType());

        message = ignoreMessages(networkNode);
        assertEquals(MessageType.GENERAL_MESSAGE, message.getMessageType());
        assertTrue(((String) message.getPayload()).contains("The match can start!"));
    }

    @Test
    public void testListening() throws IOException, InterruptedException, ClassNotFoundException, EmptyPayloadException {
        NetworkNode networkNode = assignToVirtualView(MessageType.SINGLE_PLAYER_MODE, false);
        assertStartMatch(networkNode);
    }

    @Test
    public void testAssigningToExistingVirtualView() throws IOException, InterruptedException, ClassNotFoundException, EmptyPayloadException {
        assignToVirtualView(MessageType.TWO_PLAYERS_MODE, false);
        NetworkNode networkNode = assignToVirtualView(MessageType.TWO_PLAYERS_MODE, false);
        assertStartMatch(networkNode);
    }

    @Test
    public void testTwoSimilarVirtualView() throws IOException, InterruptedException, ClassNotFoundException, EmptyPayloadException {
        assignToVirtualView(MessageType.THREE_PLAYERS_MODE, false);
        assignToVirtualView(MessageType.THREE_PLAYERS_MODE, false);
        NetworkNode networkNode = assignToVirtualView(MessageType.THREE_PLAYERS_MODE, false);
        assertStartMatch(networkNode);

        assignToVirtualView(MessageType.THREE_PLAYERS_MODE, false);
        assignToVirtualView(MessageType.THREE_PLAYERS_MODE, false);
        networkNode = assignToVirtualView(MessageType.THREE_PLAYERS_MODE, false);
        assertStartMatch(networkNode);
    }

    private synchronized void assertRecoveryMatchServerDown(NetworkNode networkNode) throws IOException, ClassNotFoundException, EmptyPayloadException {
        SessionMessage message = ignoreMessages(networkNode);
        assertEquals(MessageType.GENERAL_MESSAGE, message.getMessageType());
        assertEquals("Your match has been reloaded!", message.getPayload());
    }

    private synchronized void assertRecoveryMatchClientDown(NetworkNode networkNode) throws IOException, ClassNotFoundException, EmptyPayloadException {
        SessionMessage message = ignoreMessages(networkNode);
        assertEquals(MessageType.GENERAL_MESSAGE, message.getMessageType());
        assertEquals("You can resume the match!", message.getPayload());
    }

    @Test
    public void testRecoverMatchServerAndClientDown() throws InterruptedException, IOException, EmptyPayloadException, ClassNotFoundException {
        String nickname = generateSessionToken(MIN_NICKNAME_LENGTH);
        String password = generateSessionToken(MIN_PASSWORD_LENGTH);
        String sessionToken = generateSessionToken(SESSION_TOKEN_LENGTH);
        Users.getInstance().addUser(nickname, password, sessionToken);

        VirtualView virtualView = new VirtualView();

        Match match = new Match(virtualView, GameSaver.getInstance().getLastId() + 1);
        match.addPlayer(new Player(virtualView, nickname, sessionToken));

        GameSaver.getInstance().backupMatch(
                match,
                0,
                1
        );

        Server.getInstance().closeConnection();

        NetworkNode networkNode = assignToVirtualView(MessageType.SINGLE_PLAYER_MODE, true, true, nickname, password);
        assertRecoveryMatchServerDown(networkNode);

        GameSaver.getInstance().deleteDirectoryByName("game_" + String.format("%03d", match.getId()));
    }

    @Test
    public void testRecoverMatchClientDown() throws InterruptedException, IOException, EmptyPayloadException, ClassNotFoundException {
        String nickname = generateSessionToken(MIN_NICKNAME_LENGTH);
        String password = generateSessionToken(MIN_PASSWORD_LENGTH);

        Thread thread = new Thread(() -> {
            try {
                // Assigning client to a match
                NetworkNode networkNode = assignToVirtualView(MessageType.SINGLE_PLAYER_MODE, false, true, nickname, password);
                assertStartMatch(networkNode);
                // closing client node
                networkNode.closeConnection();
            } catch (IOException | ClassNotFoundException | InterruptedException | EmptyPayloadException ignored) {
            }
        });

        thread.start();

        // waiting to die
        sleep(MAX_TIME_TO_DIE + 2000);

        thread.join();

        NetworkNode networkNode = assignToVirtualView(MessageType.SINGLE_PLAYER_MODE, true, true, nickname, password);
        assertRecoveryMatchClientDown(networkNode);

        // GameSaver.getInstance().deleteDirectoryByName("game_" + String.format("%03d", match.getId()));
    }

    @Test
    public void testCloseMatchIfNotRecovered() throws InterruptedException, IOException, EmptyPayloadException, ClassNotFoundException {
        Server.getInstance().closeConnection();

        String nickname = generateSessionToken(MIN_NICKNAME_LENGTH);
        String password = generateSessionToken(MIN_PASSWORD_LENGTH);
        String sessionToken = generateSessionToken(SESSION_TOKEN_LENGTH);
        Users.getInstance().addUser(nickname, password, sessionToken);

        int numberOfSavedMatches = GameSaver.getInstance().getSavedMatchesDirectoriesNames().size();
        int numberOfMatchWithThisPlayer = 3;

        for (int i = 0; i < numberOfMatchWithThisPlayer; i++) {
            VirtualView virtualView = new VirtualView();

            Match match = new Match(virtualView, GameSaver.getInstance().getLastId() + 1);
            match.addPlayer(new Player(virtualView, nickname, sessionToken));

            GameSaver.getInstance().backupMatch(
                    match,
                    0,
                    1
            );
        }
        // Checking that the total number of matches is the sum of the initial plus the news.
        int savedMatches = GameSaver.getInstance().getSavedMatchesDirectoriesNames().size();
        System.out.println("ServerTest - saved matches before deletion: " + savedMatches);
        assertEquals(numberOfSavedMatches + numberOfMatchWithThisPlayer, savedMatches);
        // Creating new match without recovery the olds
        NetworkNode networkNode = assignToVirtualView(MessageType.SINGLE_PLAYER_MODE, false, true, nickname, password);
        assertStartMatch(networkNode);
        // Checking that the old matches have been removed
        savedMatches = GameSaver.getInstance().getSavedMatchesDirectoriesNames().size();
        System.out.println("ServerTest - saved matches after deletion: " + savedMatches);
        assertEquals(numberOfSavedMatches, savedMatches);
    }

//
//    @Test
//    public void testRecoverMatchServerDown() throws InterruptedException, IOException, EmptyPayloadException, ClassNotFoundException, InvalidPayloadException {
//        String nickname = generateSessionToken(MIN_NICKNAME_LENGTH);
//        String password = generateSessionToken(MIN_PASSWORD_LENGTH);
//        String sessionToken = generateSessionToken(SESSION_TOKEN_LENGTH);
//        Users.getInstance().addUser(nickname, password, sessionToken);
//
//        NetworkNode networkNode = assignToVirtualView(MessageType.SINGLE_PLAYER_MODE, false);
//        // Running heartbeat
//        runningHeartbeat = true;
//        Thread heartbeatThread = new Thread(
//                () -> {
//                    while (runningHeartbeat){
//                        try {
//                            System.out.println("Sending heartbeat message");
//                            networkNode.sendData(new SessionMessage(sessionToken, HEARTBEAT));
//                            sleep(1000);
//                        } catch (IOException | InvalidPayloadException | InterruptedException ignored) {
//                        }
//                    }
//                }
//        );
//        heartbeatThread.start();
//
//        assertStartMatch(networkNode);
//
//        // playing one turn, in this way the server will save the match
//        List<MessageType> blackMessages = new ArrayList<>() {{
//            add(MessageType.NOTIFICATION_UPDATE);
//            add(MessageType.MODEL_UPDATE);
//            add(MessageType.GENERAL_MESSAGE);
//        }};
//        // choice leaders
//        SessionMessage sessionMessage = filterMessages(networkNode, blackMessages);
//        assertEquals(CHOICE_LEADERS, sessionMessage.getMessageType());
//        networkNode.sendData(new SessionMessage(sessionToken, CHOICE_LEADERS, sessionMessage.getPayload(0), sessionMessage.getPayload(1)));
//
//        sessionMessage = filterMessages(networkNode, blackMessages);
//        System.out.println(sessionMessage);
//
//        runningHeartbeat = false;
//        heartbeatThread.join();
//    }

}