package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.LiveUpdateMessage;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.IOException;
import java.util.List;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;

public class Client extends Observable<Message> {

    private final NetworkHandler networkHandler;
    private final NotificationsFIFO notifications;
    private ViewInterface viewInterface;
    private String nickname;
    private MessageType matchModeType;

    public Client() throws IOException {
        super();
        networkHandler = new NetworkHandler(this);
        addObserver(networkHandler);

        notifications = new NotificationsFIFO(10);
    }

    public void viewNext() {
        handleMessages(MessageSynchronizedFIFO.getInstance().getNext());
    }

    public void liveUpdate(LiveUpdateMessage liveUpdateMessage) {
        try {
            String notification = (String) liveUpdateMessage.getPayload();
            notifications.pushNotification(notification);
            viewInterface.displayNotifications(notifications.getNotifications());
        } catch (EmptyPayloadException e) {
            e.printStackTrace();
        }
    }

    private void handleMessages(Message message) {
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
                            mcm.getMinChoices(), mcm.getMaxChoices(),
                            mcm.getHasUndoOption()
                    );
                    break;

                case PLACE_IN_WAREHOUSE:
                    // first message contains the warehouse
                    Warehouse warehouse = ((Warehouse) message.getPayload());
                    viewInterface.displayWarehouseNewResourcesAssignment(warehouse, getSecondMessageResources(PLACE_IN_WAREHOUSE));
                    break;

                case CHOICE_ROW_COLUMN:
                    // first message contains the MarketTray to display
                    MarketTray marketTray = ((MarketTray) message.getPayload());
                    viewInterface.displayMarketAction(marketTray, getSecondMessageResources(CHOICE_ROW_COLUMN));
                    break;

                case CHOICE_CARD_TO_BUY:
                    // first message contains the Development Grid to display
                    DevelopmentGrid developmentGrid = ((DevelopmentGrid) message.getPayload());
                    viewInterface.displayDevelopmentCardBuyAction(developmentGrid, getSecondMessageResources(CHOICE_CARD_TO_BUY));
                    break;


                // -------------------------------------
                // --------- DISPLAY MESSAGES ----------
                // -------------------------------------

                case PERSONAL_BOARD:
                    boolean isMultiplayerMode = !matchModeType.equals(MessageType.SINGLE_PLAYER_MODE);
                    Player player = (Player) message.getPayload();
                    viewInterface.displayPersonalBoard(player, isMultiplayerMode);
                    break;

                case START_WAITING:
                    viewInterface.displayWaitingScreen(message);
                    break;

                case STOP_WAITING:
                    viewInterface.stopDisplayingWaitingScreen();
                    break;

                default:
                    break;
            }
        } catch (EmptyPayloadException ignored) {
        }
    }

    private List<Resource> getSecondMessageResources(MessageType messageType) {
        Message secondMessage = MessageSynchronizedFIFO.getInstance().getNext();
        while (!secondMessage.getMessageType().equals(messageType))
            secondMessage = MessageSynchronizedFIFO.getInstance().getNext();
        return castElements(Resource.class, secondMessage.getListPayloads());
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

    public MessageType getMatchModeType() {
        return matchModeType;
    }

    public void setMatchModeType(MessageType matchModeType) {
        this.matchModeType = matchModeType;
    }

    public void sendQuitMessage() {
        try {
            notifyObservers(new Message(QUIT_OPTION_SELECTED));
        } catch (InvalidPayloadException ignored) {
        }
    }

}