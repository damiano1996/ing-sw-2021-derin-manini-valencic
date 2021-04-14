package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.network.NetworkNode;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.IOException;
import java.net.Socket;

import static it.polimi.ingsw.psp26.application.messages.MessageType.MULTI_OR_SINGLE_PLAYER_MODE;
import static it.polimi.ingsw.psp26.configurations.Configurations.DEFAULT_SERVER_PORT;

public class Client {

    private NetworkNode networkNode;

    private ViewInterface viewInterface;

    private String nickname;
    private String sessionToken;

    public Client() throws IOException {
    }

    public void initializeNetworkNode(String serverIP) throws IOException {
        networkNode = new NetworkNode(new Socket(serverIP, DEFAULT_SERVER_PORT));
        sessionToken = networkNode.receiveStringData();
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

    public void sendToServer(Message message) throws IOException {
        networkNode.sendData(message);
    }

    public void handleMessage(Message message) {

        switch (message.getMessageType()) {
            case MULTI_OR_SINGLE_PLAYER_MODE:
                viewInterface.displayChoices(
                        MULTI_OR_SINGLE_PLAYER_MODE,
                        "Do you want to play in multi or single player mode?",
                        message.getPayloads(),
                        1, 1);
                break;
            default:
                break;
        }
    }

    public ViewInterface getViewInterface() {
        return viewInterface;
    }

    public void setViewInterface(ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }
}