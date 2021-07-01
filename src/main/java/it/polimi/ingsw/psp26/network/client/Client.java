package it.polimi.ingsw.psp26.network.client;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.client.cache.CachedModel;
import it.polimi.ingsw.psp26.network.server.memory.LeaderBoard;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.configurations.Configurations.PRINT_CLIENT_SIDE;
import static it.polimi.ingsw.psp26.utils.CollectionsUtils.castElements;
import static it.polimi.ingsw.psp26.view.ViewUtils.createLeaderboard;
import static it.polimi.ingsw.psp26.view.ViewUtils.createPlayersList;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Class representing the Client.
 * The Client receives and sends messages to the Server and contains other useful information about the playing Player.
 */
public class Client extends Observable<Message> {

    private final NetworkHandler networkHandler;
    private final ViewInterface viewInterface;

    private String nickname;
    private String password;

    private int numberOfPlayers;
    private CachedModel cachedModel;

    private Message lastMessage;

    /**
     * Constructor of the class.
     * It initializes the Client's NetworkHandler and adds it to the list of Observers.
     * It also sets the ViewInterface.
     *
     * @param viewInterface the ViewInterface to use
     * @throws IOException if error in IO socket communication
     */
    public Client(ViewInterface viewInterface) throws IOException {
        super();
        networkHandler = new NetworkHandler(this);
        addObserver(networkHandler);
        this.viewInterface = viewInterface;
    }


    /**
     * Gets the next Message from the MessageSynchronisedFIFO and uses it in the handleMessages() method.
     */
    public void viewNext() {
        handleMessages(MessageSynchronizedFIFO.getInstance().getNext());
    }


    /**
     * Executes different actions based on the given Message's MessageType.
     *
     * @param message the Message from where to get the MessageType to control
     */
    private void handleMessages(Message message) {
        try {
            lastMessage = message;
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
                    String winner = (String) message.getPayload();
                    viewInterface.displayEndGame(createLeaderboard(createPlayersList(cachedModel)), winner);
                    // self message for reset
                    MessageSynchronizedFIFO.getInstance().update(
                            new Message(RESET)
                    );
                    break;

                case HELP:
                    openRules();
                    sendMenuUndoMessage();
                    viewNext();
                    break;

                case GLOBAL_LEADERBOARD:
                    viewInterface.displayGlobalLeaderboard((LeaderBoard) message.getPayload());
                    break;

                case RESET:
                    // resetting
                    reset();
                    viewNext();
                    break;

                case EXIT:
                    close();
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

        } catch (EmptyPayloadException | InvalidPayloadException ignored) {
        }
    }


    /**
     * Used to set a connection between the Client and the Server.
     *
     * @param serverIP the Server IP
     */
    public synchronized void initializeNetworkHandler(String nickname, String password, String serverIP) {
        try {
            networkHandler.initializeNetworkNode(nickname, password, serverIP);
        } catch (IOException e) {

            if (PRINT_CLIENT_SIDE) System.out.println("Server IP is unreachable...");
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
     * @return the local CachedModel
     */
    public synchronized CachedModel getCachedModel() {
        return cachedModel;
    }


    /**
     * Getter of the nickname of the user.
     *
     * @return the nickname of the Player
     */
    public synchronized String getNickname() {
        return nickname;
    }


    /**
     * Getter of the password of the user.
     *
     * @return the password of the user
     */
    public synchronized String getPassword() {
        return password;
    }


    /**
     * Sets a new nickname for the Player and creates a new CachedModel.
     *
     * @param nickname the nickname to give to the Player
     * @param password the password of the user
     */
    public synchronized void setNickname(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
        if (cachedModel == null) cachedModel = new CachedModel(nickname);
    }


    /**
     * Getter of the Match playing mode.
     *
     * @return true if the Match is in MultiPlayer mode, false if the Match is in SinglePlayer mode
     */
    public synchronized boolean isMultiplayerMode() {
        return numberOfPlayers > 1;
    }


    /**
     * Setter of the number of Players in the match.
     *
     * @param numberOfPlayers the number of Players in the match
     */
    public synchronized void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }


    /**
     * Sends an undo Message to the Server when selected by the Player.
     */
    public synchronized void sendUndoMessage() {
        try {
            notifyObservers(new Message(UNDO_OPTION_SELECTED));
        } catch (InvalidPayloadException ignored) {
        }
    }


    /**
     * Sends an undo Message for the waiting room to the Server when selected by the Player.
     */
    public synchronized void sendMenuUndoMessage() {
        try {
            notifyObservers(new Message(MENU, UNDO_OPTION_SELECTED));
        } catch (InvalidPayloadException ignored) {
        }
    }

    /**
     * Method to show the rules of the game.
     */
    private void openRules() {
        String errorMessage = "Unable to open the rules from your OS.";
        try {

            URL srcFileURL = getClass().getResource("/gui/rules_ITA.pdf");
            String dstFilePath = System.getProperty("user.home") + "/Desktop/rules_ITA.pdf";
            if (PRINT_CLIENT_SIDE) System.out.println(srcFileURL);
            if (PRINT_CLIENT_SIDE) System.out.println(dstFilePath);
            // Defining input stream
            InputStream srcInputStream = Objects.requireNonNull(srcFileURL).openStream();
            // copying file
            Files.copy(srcInputStream, Path.of(dstFilePath), REPLACE_EXISTING);
            // Opening file from desktop
            String osType = System.getProperty("os.name");
            if (PRINT_CLIENT_SIDE) System.out.println("Client - Detected OS: " + osType);

            if (osType.contains("Linux")) {
                Runtime.getRuntime().exec(new String[]{"xdg-open", dstFilePath});
            } else if (osType.contains("Windows")) {
                Desktop.getDesktop().open(new File(dstFilePath));
            } else {

                try {
                    MessageSynchronizedFIFO.getInstance().update(new Message(ERROR_MESSAGE, errorMessage));
                } catch (InvalidPayloadException ignored) {
                }
            }

            if (PRINT_CLIENT_SIDE) System.out.println("Client - File has been opened and showed.");

        } catch (Exception e) {
            // self message for displaying error
            try {
                MessageSynchronizedFIFO.getInstance().update(new Message(ERROR_MESSAGE, errorMessage));
            } catch (InvalidPayloadException ignored) {
            }
        }
    }


    /**
     * Method that calls the handleMessages(Message message) method with the last message already handled.
     * If no message was already handled it calls viewNext().
     */
    public synchronized void reHandleLastMessage() {
        if (lastMessage != null) handleMessages(lastMessage);
        else viewNext();
    }


    /**
     * Method used to reset the NotificationsFIFO, the CachedModel and the ViewInterface
     */
    public void reset() {
        NotificationsFIFO.getInstance().resetFIFO();
        cachedModel = new CachedModel(nickname);
        viewInterface.reset();
    }


    /**
     * Method to close the application.
     */
    public void close() {
        System.exit(0);
    }
}