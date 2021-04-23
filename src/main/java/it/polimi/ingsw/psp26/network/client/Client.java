package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.specialpayloads.WarehousePlacerPayload;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.IOException;
import java.util.List;

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
        try {
            switch (message.getMessageType()) {

                // -------------------------------------
                // --------- GENERAL MESSAGES ----------
                // -------------------------------------

                case GENERAL_MESSAGE:
                    viewInterface.displayText((String) message.getPayload());
                    break;

                // -------------------------------------
                // ----- MULTIPLE CHOICES MESSAGES -----
                // -------------------------------------

                case CHOICE_RESOURCE:
                case CHOICE_LEADER_ACTION:
                case CHOICE_NORMAL_ACTION:
                case CHOICE_CARD_TO_BUY:
                case CHOICE_POSITION:
                case CHOICE_CARDS_TO_ACTIVATE:
                case CHOICE_ROW_COLUMN:
                case CHOICE_LEADERS:

                    MultipleChoicesMessage mcm = (MultipleChoicesMessage) message;
                    viewInterface.displayChoices(
                            mcm.getMessageType(),
                            mcm.getQuestion(),
                            mcm.getListPayloads(),
                            mcm.getMinChoices(), mcm.getMaxChoices()
                    );
                    break;

                case PLACE_IN_WAREHOUSE:
                    Warehouse warehouse = ((WarehousePlacerPayload) message.getPayload()).getWarehouse();
                    List<Resource> resourcesToAdd = ((WarehousePlacerPayload) message.getPayload()).getResourcesToAdd();
                    viewInterface.displayWarehouseNewResourcesAssignment(warehouse, resourcesToAdd);
                    break;

                // -------------------------------------
                // --------- DISPLAY MESSAGES ----------
                // -------------------------------------

                case PERSONAL_BOARD:
                    Player player = (Player) message.getPayload();
                    viewInterface.displayPersonalBoard(player);
                    break;

                default:
                    break;
            }
        } catch (EmptyPayloadException ignored) {
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