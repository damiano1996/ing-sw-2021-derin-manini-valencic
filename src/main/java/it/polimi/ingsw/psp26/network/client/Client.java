package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.network.NetworkNode;

import java.io.IOException;
import java.net.Socket;

import static it.polimi.ingsw.psp26.configurations.Configurations.DEFAULT_SERVER_PORT;

public class Client {

    private NetworkNode networkNode;

    private String nickname;
    private String sessionToken;

    public Client() throws IOException {
    }

    public void initializeNetworkNode(String serverIP) throws IOException {
        networkNode = new NetworkNode(new Socket(serverIP, DEFAULT_SERVER_PORT));
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public void sendToServer(Message message) throws IOException {
        networkNode.sendData(message);
    }
}