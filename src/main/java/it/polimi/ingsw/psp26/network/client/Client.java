package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.IOException;
import java.util.List;

import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;

public class Client extends Observable<Message> {

    private final NetworkHandler networkHandler;
    private ViewInterface viewInterface;

    private String nickname;

    public Client() throws IOException {
        super();
        networkHandler = new NetworkHandler();
        addObserver(networkHandler);
    }

    public void viewNext() {
        handleMessages(MessageSynchronizedFIFO.getInstance().getNext());
    }

    public void handleMessages(Message message) {
        try {
            switch (message.getMessageType()) {

                // -------------------------------------
                // --------- GENERAL MESSAGES ----------
                // -------------------------------------

                case GENERAL_MESSAGE:
                    viewInterface.displayText((String) message.getPayload());
                    break;

                case ERROR_MESSAGE:
                    viewInterface.displayError((String) message.getPayload());
                    break;

                // -------------------------------------
                // ----- MULTIPLE CHOICES MESSAGES -----
                // -------------------------------------

                case CHOICE_RESOURCE:
                case CHOICE_LEADER_ACTION:
                case CHOICE_NORMAL_ACTION:
                case CHOICE_POSITION:
                case CHOICE_CARDS_TO_ACTIVATE:
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
                    // first message contains the warehouse
                    Warehouse warehouse = ((Warehouse) message.getPayload());
                    // waiting for second message containing the resources to add
                    Message secondMessage = MessageSynchronizedFIFO.getInstance().getNext();
                    while (!secondMessage.getMessageType().equals(MessageType.PLACE_IN_WAREHOUSE))
                        secondMessage = MessageSynchronizedFIFO.getInstance().getNext();
                    List<Resource> resourcesToAdd = castElements(Resource.class, secondMessage.getListPayloads());
                    // calls the method to display the warehouse with the resources to add
                    viewInterface.displayWarehouseNewResourcesAssignment(warehouse, resourcesToAdd);
                    break;
                    
                case CHOICE_ROW_COLUMN:
                    // Message contains the MarketTray to display
                    MarketTray marketTray = ((MarketTray) message.getPayload());
                    viewInterface.displayMarketAction(marketTray);
                    break;
                    
                case CHOICE_CARD_TO_BUY:
                    // Message contains the Development Grid to display
                    DevelopmentGrid developmentGrid = ((DevelopmentGrid) message.getPayload());
                    viewInterface.displayDevelopmentCardBuyAction(developmentGrid);
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
            System.out.println("Server IP is unreachable...");
            viewInterface.displayLogIn();
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