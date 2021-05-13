package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.exceptions.ServerIsNotReachableException;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.client.cache.CachedModel;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.IOException;
import java.util.List;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;

public class Client extends Observable<Message> {

    private final NetworkHandler networkHandler;
    private final NotificationsFIFO notifications;
    private final ViewInterface viewInterface;
    private String nickname;
    private MessageType matchModeType;
    private CachedModel cachedModel;

    public Client(ViewInterface viewInterface) throws IOException {
        super();
        networkHandler = new NetworkHandler(this);
        addObserver(networkHandler);

        notifications = new NotificationsFIFO(10);

        this.viewInterface = viewInterface;
    }

    public void viewNext() {
        handleMessages(MessageSynchronizedFIFO.getInstance().getNext());
    }

    public void liveUpdate(Message liveUpdateMessage) {
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

                case CHOICE_RESOURCE_FROM_WAREHOUSE:
                case CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY:
                case CHOICE_LEADER_ACTION:
                case CHOICE_NORMAL_ACTION:
                case CHOICE_POSITION:
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

                case CHOICE_CARDS_TO_ACTIVATE:
                    // first message contains the Player's Productions
                    List<Production> productions = castElements(Production.class, message.getListPayloads());
                    viewInterface.displayProductionActivation(productions, getSecondMessageResources(CHOICE_CARDS_TO_ACTIVATE));
                    break;

                case PLACE_IN_WAREHOUSE:
                    // first message contains the warehouse
                    Warehouse warehouse = ((Warehouse) message.getPayload());
                    viewInterface.displayWarehouseNewResourcesAssignment(warehouse, getSecondMessageResources(PLACE_IN_WAREHOUSE));
                    break;

                case CHOICE_ROW_COLUMN:
                    // message contains the Player's Resources
                    try {
                        viewInterface.displayMarketAction(cachedModel.getMarketTrayCached().getObsoleteObject(), castElements(Resource.class, message.getListPayloads()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

                case CHOICE_CARD_TO_BUY:
                    // message contains the Player's Resources
                    try {
                        viewInterface.displayDevelopmentCardBuyAction(cachedModel.getDevelopmentGridCached().getObsoleteObject(), castElements(Resource.class, message.getListPayloads()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

                case LORENZO_PLAY:
                    // message contains the stack od Action Tokens
                    List<ActionToken> actionTokens = castElements(ActionToken.class, message.getListPayloads());
                    viewInterface.displayActionTokens(actionTokens);
                    break;


                // -------------------------------------
                // --------- DISPLAY MESSAGES ----------
                // -------------------------------------

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

    public void initializeNetworkHandler(String serverIP) throws ServerIsNotReachableException {
        try {
            networkHandler.initializeNetworkNode(serverIP);
        } catch (IOException e) {
            System.out.println("Server IP is unreachable...");
            throw new ServerIsNotReachableException();
        }
    }

    public synchronized CachedModel getCachedModel() {
        return cachedModel;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        this.cachedModel = new CachedModel(nickname);
    }

    public MessageType getMatchModeType() {
        return matchModeType;
    }

    public void setMatchModeType(MessageType matchModeType) {
        this.matchModeType = matchModeType;
    }

    public void sendUndoMessage() {
        try {
            notifyObservers(new Message(QUIT_OPTION_SELECTED));
        } catch (InvalidPayloadException ignored) {
        }
    }

}