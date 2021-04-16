package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.application.Observer;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.IOException;

import static it.polimi.ingsw.psp26.application.messages.MessageType.MULTI_OR_SINGLE_PLAYER_MODE;

public class Client extends Observable<Message> implements Observer<Message> {

    private final NetworkHandler networkHandler;
    private ViewInterface viewInterface;

    private String nickname;

    public Client() throws IOException {
        super();
        networkHandler = new NetworkHandler(this);
        addObserver(networkHandler);
    }

    @Override
    public void update(Message message) {

        switch (message.getMessageType()) {

            case GENERAL_MESSAGE:
                viewInterface.displayText((String) message.getPayload());
                break;

            case MULTI_OR_SINGLE_PLAYER_MODE:
                viewInterface.displayChoices(
                        MULTI_OR_SINGLE_PLAYER_MODE,
                        "Choose the game mode:",
                        message.getPayloads(),
                        1, 1);
                break;

            case PERSONAL_BOARD:
                Player player = (Player) message.getPayload();
                System.out.println("player: " + player.getNickname());
                viewInterface.displayPersonalBoard(player);

            default:
                break;
        }
    }

    public void initializeNetworkHandler(String serverIP) {
        try {
            networkHandler.initializeNetworkNode(serverIP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setViewInterface(ViewInterface viewInterface) {
        this.viewInterface = viewInterface;
    }
}