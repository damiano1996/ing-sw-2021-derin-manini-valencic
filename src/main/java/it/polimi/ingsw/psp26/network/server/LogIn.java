package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.network.NetworkNode;
import it.polimi.ingsw.psp26.network.server.memory.Users;

import java.io.IOException;

import static it.polimi.ingsw.psp26.application.messages.MessageType.GENERAL_MESSAGE;
import static it.polimi.ingsw.psp26.configurations.Configurations.SESSION_TOKEN_LENGTH;
import static it.polimi.ingsw.psp26.network.NetworkUtils.generateSessionToken;
import static it.polimi.ingsw.psp26.network.server.memory.CommonNicknamePasswordChecksEnums.*;

public class LogIn extends Thread {

    private final NetworkNode clientNode;

    public LogIn(NetworkNode clientNode) {
        this.clientNode = clientNode;
    }

    @Override
    public void run() {

        try {
            try {

                System.out.println("LogIn - New clientNode to log.");
                String sessionToken = getSessionToken(clientNode);
                System.out.println("LogIn - Adding nodeClient to waiting room.");
                Server.getInstance().addNodeClientToWaitingRoom(sessionToken, clientNode);
                System.out.println("LogIn - NodeClient has been added.");

            } catch (PasswordNotCorrectException e) {
                clientNode.sendData(PASSWORD_NOT_CORRECT);
                clientNode.closeConnection();
            } catch (NicknameTooShortException e) {
                clientNode.sendData(NICKNAME_TOO_SHORT);
                clientNode.closeConnection();
            } catch (NicknameAlreadyExistsException e) {
                clientNode.sendData(NICKNAME_ALREADY_EXISTS);
                clientNode.closeConnection();
            } catch (PasswordTooShortException e) {
                clientNode.sendData(PASSWORD_TOO_SHORT);
                clientNode.closeConnection();
            } catch (ClassNotFoundException | InvalidPayloadException ignored) {
            }
        } catch (IOException ignored) {
        }
    }

    private String getSessionToken(NetworkNode clientNode) throws IOException, PasswordNotCorrectException, NicknameTooShortException, NicknameAlreadyExistsException, PasswordTooShortException, InvalidPayloadException, ClassNotFoundException {
        boolean showNewNicknameMessage = false;

        // step: receiving nickname and password from clientNode
        String nickname = (String) clientNode.receiveData();
        String password = (String) clientNode.receiveData();
        // step: checking if it is a new user or not
        String sessionToken;
        if (Users.getInstance().getNicknamePasswords().containsKey(nickname)) {

            if (Users.getInstance().getNicknamePasswords().get(nickname).equals(password)) {
                System.out.println("LogIn - Login success!");
                // step: user exists and correctly registered, we can load the session token:
                sessionToken = Users.getInstance().getNicknameSessionTokens().get(nickname);
            } else {
                System.out.println("LogIn - Bad password!");
                // step: password is not correct, throwing exception
                throw new PasswordNotCorrectException();
            }

        } else {
            System.out.println("LogIn - Completely new user, welcome!!");
            // step: checking if nickname requirements are satisfied
            Users.checkNicknameRequirements(nickname);
            Users.checkPasswordRequirements(password);

            showNewNicknameMessage = true;
            // step: generating a new session token
            sessionToken = generateSessionToken(SESSION_TOKEN_LENGTH);
            // step: saving user data
            Users.getInstance().addUser(nickname, password, sessionToken);
        }

        // step: sending confirmation message and session token
        System.out.println("LogIn - Sending ok message for nickname and password.");
        clientNode.sendData(NICKNAME_AND_PASSWORD_ARE_OK);
        System.out.println("LogIn - Sending sessionToken");
        clientNode.sendData(sessionToken);

        System.out.println("LogIn - nickname: " + nickname + " - password: " + password + " - sessionToken: " + sessionToken);

        // step: sending a welcome message
        System.out.println("LogIn - Sending the welcome message");
        if (showNewNicknameMessage)
            clientNode.sendData(new SessionMessage(sessionToken, GENERAL_MESSAGE, "Welcome " + nickname + " in the community!"));
        else
            clientNode.sendData(new SessionMessage(sessionToken, GENERAL_MESSAGE, "Welcome back " + nickname + "!"));

        return sessionToken;
    }

}
