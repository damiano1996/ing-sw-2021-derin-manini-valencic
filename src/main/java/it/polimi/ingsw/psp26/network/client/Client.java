package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.IOException;
import java.util.List;

import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;

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
        MultipleChoicesMessage mcm;

        switch (message.getMessageType()) {

            case GENERAL_MESSAGE:
                viewInterface.displayText((String) message.getPayload());
                break;

            case CHOICE_RESOURCE:
                mcm = (MultipleChoicesMessage) message;
                viewInterface.displayChoices(
                        mcm.getMessageType(),
                        "Choose resource:",
                        mcm.getListPayloads(),
                        mcm.getMinChoices(), mcm.getMaxChoices()
                );
                break;

            case CHOICE_LEADER_ACTION:
                mcm = (MultipleChoicesMessage) message;
                viewInterface.displayChoices(
                        mcm.getMessageType(),
                        "Choose leader action:",
                        mcm.getListPayloads(),
                        mcm.getMinChoices(), mcm.getMaxChoices()
                );
                break;

            case PERSONAL_BOARD:
                Player player = (Player) message.getPayload();
                viewInterface.displayPersonalBoard(player);
                break;

            case CHOICE_NORMAL_ACTION:
                mcm = (MultipleChoicesMessage) message;
                viewInterface.displayChoices(
                        mcm.getMessageType(),
                        "Choose normal action:",
                        mcm.getListPayloads(),
                        mcm.getMinChoices(), mcm.getMaxChoices()
                );
                break;

            case PLACE_IN_WAREHOUSE:
                List<Message> messages = castElements(Message.class, message.getListPayloads());
                Warehouse warehouse = (Warehouse) messages.get(0).getPayload();
                List<Resource> resourcesToAdd = castElements(Resource.class, messages.get(1).getListPayloads());
                viewInterface.displayWarehouseNewResourcesAssignment(warehouse, resourcesToAdd);

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