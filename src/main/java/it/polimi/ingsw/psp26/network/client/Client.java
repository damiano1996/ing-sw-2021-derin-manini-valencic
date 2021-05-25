package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.exceptions.ServerIsNotReachableException;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.client.cache.CachedModel;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.IOException;
import java.util.List;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;
import static it.polimi.ingsw.psp26.view.ViewUtils.createLeaderboard;
import static it.polimi.ingsw.psp26.view.ViewUtils.createPlayersList;

public class Client extends Observable<Message> {

    private final NetworkHandler networkHandler;
    private final ViewInterface viewInterface;
    private String nickname;
    private MessageType matchModeType;
    private CachedModel cachedModel;

    public Client(ViewInterface viewInterface) throws IOException {
        super();
        networkHandler = new NetworkHandler(this);
        addObserver(networkHandler);
        this.viewInterface = viewInterface;
    }


    /**
     * Gets the next Message from the MessageSynchronisedFIFO and uses it in the handleMessages() method
     */
    public void viewNext() {
        handleMessages(MessageSynchronizedFIFO.getInstance().getNext());
    }


    /**
     * Executes different actions based on the given Message's MessageType
     *
     * @param message The Message from where to get the MessageType to control
     */
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

                case SET_NICKNAME:
                    setNickname((String) message.getPayload());
                    viewNext();
                    break;

                // -------------------------------------
                // ----- MULTIPLE CHOICES MESSAGES -----
                // -------------------------------------

                case CHOICE_RESOURCE_FROM_WAREHOUSE:
                case CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY:
                case CHOICE_LEADER_ACTION:
                case CHOICE_NORMAL_ACTION:
                case CHOICE_DEVELOPMENT_CARD_SLOT_POSITION:
                case CHOICE_LEADERS:
                case CHOICE_PRODUCTIONS_TO_ACTIVATE:

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
                    // message contains the Player's Resources
                    viewInterface.displayMarketAction(cachedModel.getMarketTrayCached().getObject(), castElements(Resource.class, message.getListPayloads()));
                    break;

                case CHOICE_CARD_TO_BUY:
                    // message contains the Player's Resources
                    viewInterface.displayDevelopmentCardBuyAction(cachedModel.getDevelopmentGridCached().getObject(), castElements(Resource.class, message.getListPayloads()));
                    break;

                case LORENZO_PLAY:
                    // message contains the stack od Action Tokens
                    List<ActionToken> actionTokens = castElements(ActionToken.class, message.getListPayloads());
                    viewInterface.displayActionTokens(actionTokens);
                    break;

                case ENDGAME_RESULT:
                    // message contains the nickname of the player that won
                    String winner = (String) message.getPayload();
                    viewInterface.displayEndGame(createLeaderboard(createPlayersList(cachedModel)), winner);
                    break;


                // -------------------------------------
                // --------- DISPLAY MESSAGES ----------
                // -------------------------------------

                case OPPONENT_TURN:
                    viewInterface.waitForYourTurn();
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


    /**
     * Used to set a connection between the Client and the Server
     *
     * @param serverIP The Server IP
     * @throws ServerIsNotReachableException Thrown if the Server is not available
     */
    public void initializeNetworkHandler(String serverIP) throws ServerIsNotReachableException {
        try {
            networkHandler.initializeNetworkNode(serverIP);
        } catch (IOException e) {
            System.out.println("Server IP is unreachable...");
            throw new ServerIsNotReachableException();
        }
    }


    /**
     * @return The local CachedModel
     */
    public synchronized CachedModel getCachedModel() {
        return cachedModel;
    }


    /**
     * @return The nickname of the Player
     */
    public String getNickname() {
        return nickname;
    }


    /**
     * Sets a new nickname for the Player and creates a new CachedModel
     *
     * @param nickname The nickname to give to the Player
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
        this.cachedModel = new CachedModel(nickname);
    }


    /**
     * @return True if the Match is in MultiPlayer mode, false if the Match is in SinglePlayer mode
     */
    public boolean isMultiplayerMode() {
        return !matchModeType.equals(SINGLE_PLAYER_MODE);
    }


    /**
     * Used to set the MatchModeType (single/multi Player)
     *
     * @param matchModeType SINGLE_PLAYER_MODE or MULTI_PLAYER_MODE
     */
    public void setMatchModeType(MessageType matchModeType) {
        this.matchModeType = matchModeType;
    }


    /**
     * Sends an undo Message to the Server when selected by the Player
     */
    public void sendUndoMessage() {
        try {
            notifyObservers(new Message(QUIT_OPTION_SELECTED));
        } catch (InvalidPayloadException ignored) {
        }
    }

}