package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.client.cache.CachedModel;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.IOException;
import java.util.List;

import static it.polimi.ingsw.psp26.application.messages.MessageType.DISPLAY_LOGIN;
import static it.polimi.ingsw.psp26.application.messages.MessageType.UNDO_OPTION_SELECTED;
import static it.polimi.ingsw.psp26.utils.CollectionsUtils.castElements;
import static it.polimi.ingsw.psp26.view.ViewUtils.createLeaderboard;
import static it.polimi.ingsw.psp26.view.ViewUtils.createPlayersList;

public class Client extends Observable<Message> {

    private final NetworkHandler networkHandler;
    private final ViewInterface viewInterface;

    private String nickname;
    private String password;

    private int numberOfPlayers;
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

                case SET_NUMBER_OF_PLAYERS:
                    setNumberOfPlayers((Integer) message.getPayload());
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
                case NEW_OR_OLD:
                case MULTI_OR_SINGLE_PLAYER_MODE:
                case MENU:

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
                    // message contains the resources to add; Player's Resources are taken from the CachedModel
                    viewInterface.displayWarehouseNewResourcesAssignment(
                            cachedModel.getMyPlayerCached().getObject().getPersonalBoard().getWarehouse(),
                            castElements(Resource.class, message.getListPayloads())
                    );
                    break;

                case CHOICE_ROW_COLUMN:
                    // taking the Market Tray and the Player's Resources from the CachedModel
                    viewInterface.displayMarketAction(
                            cachedModel.getMarketTrayCached().getObject(),
                            cachedModel.getMyPlayerCached().getObject().getPersonalBoard().getAllAvailableResources()
                    );
                    break;

                case CHOICE_CARD_TO_BUY:
                    // taking the Development Grid and the Player's Resources from CachedModel
                    viewInterface.displayDevelopmentCardBuyAction(
                            cachedModel.getDevelopmentGridCached().getObject(),
                            cachedModel.getMyPlayerCached().getObject().getPersonalBoard().getAllAvailableResources()
                    );
                    break;

                case LORENZO_PLAY:
                    // message contains the stack of Action Tokens
                    List<ActionToken> actionTokens = castElements(ActionToken.class, message.getListPayloads());
                    viewInterface.displayActionTokens(actionTokens);
                    break;

                case ENDGAME_RESULT:
                    // message contains the nickname of the player that won
                    NotificationsFIFO.getInstance().resetFIFO();
                    String winner = (String) message.getPayload();
                    viewInterface.displayEndGame(createLeaderboard(createPlayersList(cachedModel)), winner);
                    break;


                // -------------------------------------
                // --------- DISPLAY MESSAGES ----------
                // -------------------------------------

                case DISPLAY_LOGIN:
                    viewInterface.displayLogIn();
                    break;

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


    /**
     * Used to set a connection between the Client and the Server
     *
     * @param serverIP The Server IP
     */
    public synchronized void initializeNetworkHandler(String nickname, String password, String serverIP) {
        try {
            networkHandler.initializeNetworkNode(nickname, password, serverIP);

        } catch (IOException e) {

            System.out.println("Server IP is unreachable...");
            try {
                MessageSynchronizedFIFO.getInstance().update(new Message(MessageType.ERROR_MESSAGE, "Server is not reachable!"));
                MessageSynchronizedFIFO.getInstance().update(new Message(DISPLAY_LOGIN));
            } catch (InvalidPayloadException ignored) {
            }


        } catch (NicknameTooShortException | PasswordTooShortException | NicknameAlreadyExistsException | InvalidPayloadException | ClassNotFoundException | PasswordNotCorrectException e) {
            try {
                MessageSynchronizedFIFO.getInstance().update(new Message(DISPLAY_LOGIN));
            } catch (InvalidPayloadException ignored) {
            }
        }
    }


    /**
     * Getter of the cached model.
     *
     * @return The local CachedModel
     */
    public synchronized CachedModel getCachedModel() {
        return cachedModel;
    }


    /**
     * Getter of the nickname of the user.
     *
     * @return The nickname of the Player
     */
    public synchronized String getNickname() {
        return nickname;
    }

    /**
     * Getter of the password of the user.
     *
     * @return The password of the user
     */
    public synchronized String getPassword() {
        return password;
    }


    /**
     * Sets a new nickname for the Player and creates a new CachedModel
     *
     * @param nickname The nickname to give to the Player
     * @param password The password of the user
     */
    public synchronized void setNickname(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
        this.cachedModel = new CachedModel(nickname);
    }


    /**
     * @return True if the Match is in MultiPlayer mode, false if the Match is in SinglePlayer mode
     */
    public synchronized boolean isMultiplayerMode() {
        return numberOfPlayers > 1;
    }


    /**
     * Setter of the number of players in the match.
     *
     * @param numberOfPlayers number of players in the match
     */
    public synchronized void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }


    /**
     * Sends an undo Message to the Server when selected by the Player
     */
    public synchronized void sendUndoMessage() {
        try {
            notifyObservers(new Message(UNDO_OPTION_SELECTED));
        } catch (InvalidPayloadException ignored) {
        }
    }

}