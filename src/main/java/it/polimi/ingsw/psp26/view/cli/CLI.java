package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.mutex.Mutex;
import it.polimi.ingsw.psp26.exceptions.ConfirmationException;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.exceptions.UndoOptionSelectedException;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.client.MessageSynchronizedFIFO;
import it.polimi.ingsw.psp26.network.client.NotificationsFIFO;
import it.polimi.ingsw.psp26.network.server.memory.LeaderBoard;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.utils.CollectionsUtils.castElements;
import static it.polimi.ingsw.psp26.utils.CollectionsUtils.getElementsByIndices;
import static it.polimi.ingsw.psp26.view.ViewUtils.toTitleStyle;

/**
 * The CLI class.
 * Provides an interaction with the game via terminal.
 * In order to have the best experience, the terminal width must be adjusted to show all the elements that appears on screen
 * (all the elements have been drawn using a fixed width of 237 characters).
 * Since ANSI escape sequences are used, a Unix-like terminal must be used (e.g. Windows doesn't support these escape sequences).
 */
public class CLI implements ViewInterface {

    private final Scanner in;
    private final PrintWriter pw;
    private final CliUtils cliUtils;
    private final DevelopmentCardsCli developmentCardsCli;
    private final LeaderCardsCli leaderCardsCli;
    private final MarketCli marketCli;
    private final PersonalBoardCli personalBoardCli;
    private final DisplayWarehousePlacer displayWarehousePlacer;
    private final NotificationStackPrinter notificationStackPrinter;
    private final CommonScreensCli commonScreensCli;
    private final Mutex mutex;
    private Client client;

    /**
     * Constructor of the class.
     * Creates an instance of all the objects used to draw the elements.
     */
    public CLI() {
        this.in = new Scanner(System.in);
        this.pw = new PrintWriter(System.out);
        this.cliUtils = new CliUtils(pw);
        this.developmentCardsCli = new DevelopmentCardsCli(pw);
        this.leaderCardsCli = new LeaderCardsCli(pw);
        this.marketCli = new MarketCli(pw);
        this.personalBoardCli = new PersonalBoardCli(pw);
        this.displayWarehousePlacer = new DisplayWarehousePlacer(pw);
        this.notificationStackPrinter = new NotificationStackPrinter(pw);
        this.commonScreensCli = new CommonScreensCli(pw);

        mutex = new Mutex();
    }


    //--------------------------------------//
    //          START GAME METHODS          //
    //--------------------------------------//

    /**
     * Starts a new Client and displays the login screen.
     */
    @Override
    public void start() {
        try {
            this.client = new Client(this);
            displayLogIn();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Prints the Title Screen when the program is launched.
     * Used in printTitle() method.
     */
    private void printTitle() {
        cliUtils.cls();
        pw.print(Color.GREEN.setColor());
        pw.flush();
        cliUtils.printFigure("titles/MainTitle", 1, 10);
        pw.print(Color.RESET.setColor());
        pw.flush();
    }


    /**
     * Used to ask the Player's credentials.
     * After they have been entered, starts the Notification Thread.
     */
    public void displayLogIn() {
        printTitle();
        cliUtils.printFigure("titles/PressEnterTitle", 20, 76);
        in.nextLine();

        String nickname = "";
        String password = "";

        for (int loginSteps = 0; loginSteps < 3; loginSteps++) {
            printTitle();
            cliUtils.vSpace(4);

            switch (loginSteps) {
                case 0:
                    pw.print(cliUtils.hSpace(100) + "Enter Nickname: ");
                    pw.flush();
                    nickname = in.nextLine();
                    break;

                case 1:
                    pw.println(cliUtils.hSpace(100) + "Enter Nickname: " + nickname);
                    pw.flush();
                    cliUtils.vSpace(2);
                    pw.print(cliUtils.hSpace(100) + "Enter Password: ");
                    pw.flush();
                    password = in.nextLine();
                    break;

                case 2:
                    pw.println(cliUtils.hSpace(100) + "Enter Nickname: " + nickname);
                    pw.flush();
                    cliUtils.vSpace(2);
                    pw.print(cliUtils.hSpace(100) + "Enter Password: " + password);
                    pw.flush();
                    cliUtils.vSpace(3);
                    pw.print(cliUtils.hSpace(100) + "Enter IP-Address: ");
                    pw.flush();
                    String serverIP = in.nextLine();

                    client.initializeNetworkHandler(nickname, password, serverIP);
                    break;

                default:
                    break;
            }
        }

        // Starting the Thread to print notifications
        if (client.getNickname() != null) startLiveUpdate();

        client.viewNext();
    }


    /**
     * Prints the global Leaderboard.
     */
    @Override
    public void displayGlobalLeaderboard() {
        mutex.lock();
        commonScreensCli.displayFinalScreen(LeaderBoard.getInstance().getLeaderboard(), 5);
        cliUtils.pPCS("Press Enter to go back to the menu.", Color.WHITE, 50, 4);
        mutex.unlock();

        in.nextLine();

        notificationStackPrinter.restoreStackView();
        client.sendMenuUndoMessage();
        client.viewNext();
    }


    //------------------------------------------//
    //          WAITING SCREEN METHODS          //
    //------------------------------------------//

    /**
     * Starts a waiting screen.
     *
     * @param message a String to display during the waiting screen
     */
    public void displayWaitingScreen(Message message) {
        try {
            WaitingScreenStarter.getInstance().startWaiting(message);
        } catch (EmptyPayloadException ignored) {
        }
        client.viewNext();
    }


    /**
     * Stops the waiting screen.
     */
    public void stopDisplayingWaitingScreen() {
        WaitingScreenStarter.getInstance().stopWaiting();
        client.viewNext();
    }


    //---------------------------------------//
    //          LIVE UPDATE METHODS          //
    //---------------------------------------//

    /**
     * Creates a new Thread that checks if there are new notifications.
     * In case of new Notifications, prints them in the right-bottom corner of the screen.
     */
    private void startLiveUpdate() {
        new Thread(() -> {
            while (true) {
                // Only one call to the get method, otherwise it will freeze
                List<String> notifications = NotificationsFIFO.getInstance().getNotifications();

                // If you don't give time the cursor may be in the wrong position when printing
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                notificationStackPrinter.printMessageStack(notifications);
            }
        }).start();
    }


    //-----------------------------------------//
    //          MISC ELEMENTS METHODS          //
    //-----------------------------------------//


    /**
     * Displays the List of DevelopmentCards as DevelopmentCardSlots.
     *
     * @param developmentCardsSlots the DevelopmentCards to display
     */
    private void displayDevelopmentCardsSlots(List<List<DevelopmentCard>> developmentCardsSlots) {
        cliUtils.pPCS("Enter the number of the slot in which you want to put the drawn card", Color.WHITE, 10, 4);
        personalBoardCli.displayDevelopmentCardsSlots(developmentCardsSlots, 30, 70);
        cliUtils.pPCS("Slot  1", Color.GREY, 47, 85);
        cliUtils.pPCS("Slot  2", Color.GREY, 47, 121);
        cliUtils.pPCS("Slot  3", Color.GREY, 47, 157);
        cliUtils.vSpace(3);
    }


    /**
     * Displays the ActionTokens screen.
     *
     * @param unusedTokens the Action Tokens to display
     */
    @Override
    public void displayActionTokens(List<ActionToken> unusedTokens) {
        mutex.lock();

        // Must have a restoreNotification here since the LeaderAction selection can disappear if no Action is possible
        notificationStackPrinter.restoreStackView();
        personalBoardCli.displayActionTokens(unusedTokens);

        mutex.unlock();
        displayNext();
    }


    //----------------------------------------//
    //          MAIN ACTIONS METHODS          //
    //----------------------------------------//

    /**
     * Displays the Market action screen.
     * Hides the NotificationStack view for better displaying all the elements.
     * After choices have been entered, sends them to the Server.
     *
     * @param marketTray      the MarketTray to display
     * @param playerResources the Player's current Resources
     */
    @Override
    public void displayMarketAction(MarketTray marketTray, List<Resource> playerResources) {
        notificationStackPrinter.hideNotifications();

        List<Integer> choice = new ArrayList<>();
        try {
            choice.add(marketCli.displayMarketSelection(marketTray, playerResources));
            client.notifyObservers(new Message(CHOICE_ROW_COLUMN, choice.toArray(new Object[0])));
            displayNext();
        } catch (UndoOptionSelectedException e) {
            client.sendUndoMessage();
            client.viewNext();
        } catch (InvalidPayloadException ignored) {
        }
    }


    /**
     * Displays the DevelopmentCard buy action screen.
     * Hides the NotificationStack view for better displaying all the elements.
     * After choices have been entered, sends them to the Server.
     *
     * @param developmentCardsGrid the DevelopmentGrid to display
     * @param playerResources      the Player's current Resources
     */
    @Override
    public void displayDevelopmentCardBuyAction(DevelopmentCardsGrid developmentCardsGrid, List<Resource> playerResources) {
        notificationStackPrinter.hideNotifications();

        List<DevelopmentCard> choice = new ArrayList<>();
        try {
            choice.add(developmentCardsCli.displayDevelopmentCardSelection(developmentCardsGrid, playerResources));
            client.notifyObservers(new Message(CHOICE_CARD_TO_BUY, choice.toArray(new Object[0])));
            cliUtils.setCursorBottomLeft();
            displayNext();
        } catch (UndoOptionSelectedException e) {
            client.sendUndoMessage();
            client.viewNext();
        } catch (InvalidPayloadException ignored) {
        }
    }


    /**
     * Displays the screen where the Player can modify the Depots.
     * Hides the NotificationStack view for better displaying all the elements.
     * After choices have been entered, sends them to the Server.
     *
     * @param warehouse      the Warehouse of the Player
     * @param resourcesToAdd the Resources the Player can add to the Warehouse
     */
    @Override
    public void displayWarehouseNewResourcesAssignment(Warehouse warehouse, List<Resource> resourcesToAdd) {
        notificationStackPrinter.hideNotifications();

        List<Resource> resources = displayWarehousePlacer.displayMarketResourcesSelection(warehouse, resourcesToAdd);
        try {
            client.notifyObservers(new Message(PLACE_IN_WAREHOUSE, resources.toArray(new Object[0])));
        } catch (InvalidPayloadException ignored) {
        }

        displayNext();
    }


    //-------------------------------------------//
    //          DISPLAY CHOICES METHODS          //
    //-------------------------------------------//

    /**
     * Method used to display choices using the following layout:
     * Question
     * Middle block (objects shown here changes when method is called)
     * Choices
     * <p>
     * After the layout has been printed, calls the displayInputChoice() in order to let the Player make his choices.
     * Once the Player has finished, the choices will be send to the Server.
     *
     * @param messageType   the Message Type used to get the correct representation of the screen
     * @param question      the question to ask
     * @param choices       the choices the Player has
     * @param minChoices    the minimum number of choices the Player can make
     * @param maxChoices    the maximum number of choices the Player can make
     * @param hasUndoOption true if the Player can exit from the choice screen
     */
    @Override
    public void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices, boolean hasUndoOption) {
        // Starts blocking the notifications printing
        mutex.lock();

        cliUtils.vSpace(1);
        pw.println(cliUtils.hSpace(3) + question);

        switch (messageType) {

            case MENU:
            case NEW_OR_OLD:
            case MULTI_OR_SINGLE_PLAYER_MODE:
                cliUtils.clns();
                displayMultipleStringChoices(choices);
                break;

            case CHOICE_DEVELOPMENT_CARD_SLOT_POSITION:
                choicePositionExecute(choices);
                break;

            case CHOICE_NORMAL_ACTION:
                choiceNormalLeaderActionExecute(choices, true);
                break;

            case CHOICE_LEADER_ACTION:
                choiceNormalLeaderActionExecute(choices, false);
                break;

            case CHOICE_LEADERS:
                cliUtils.clns();
                displayLeaderCardDiscardActivationSelection(castElements(LeaderCard.class, choices));
                break;

            case CHOICE_RESOURCE_FROM_WAREHOUSE:
                choiceResourceFromWarehouseExecute(choices);
                break;

            case CHOICE_PRODUCTIONS_TO_ACTIVATE:
                displayProductionSelection(castElements(Production.class, choices));
                break;

            case CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY:
                personalBoardCli.displayResourceSupply(new ResourceSupply(), castElements(Resource.class, choices), 1, 37);
                cliUtils.vSpace(10);
                cliUtils.pPCS("Please insert the number of the Resource slot you want.", Color.WHITE, 38, 4);
                break;

            default:
                break;
        }

        // Stops blocking the notification printing
        mutex.unlock();

        // send to server response
        try {

            cliUtils.vSpace(1);
            List<Object> selected = getElementsByIndices(choices, displayInputChoice(choices.size(), minChoices, maxChoices, hasUndoOption));
            client.notifyObservers(new Message(messageType, selected.toArray(new Object[0])));

            client.viewNext();

        } catch (InvalidPayloadException e) {
            e.printStackTrace();
        } catch (UndoOptionSelectedException e) {
            client.sendUndoMessage();
            client.viewNext();
        }

    }


    /**
     * Displays the LeaderCards activation/discard selection screen.
     *
     * @param leaderCards the Leader Cards to display
     */
    public void displayLeaderCardDiscardActivationSelection(List<LeaderCard> leaderCards) {
        cliUtils.clns();
        leaderCardsCli.printMultipleLeaders(leaderCards, 3);
        cliUtils.vSpace(3);
    }


    /**
     * Executes the CHOICE_DEVELOPMENT_CARD_SLOT_POSITION switch case in displayChoices().
     * The method displays the Player's Development Card slots, asking him where he wants to place the new bought DevelopmentCard.
     *
     * @param choices the choices to display. In this case, the available slots where the Player can place the DevelopmentCard
     */
    private void choicePositionExecute(List<Object> choices) {
        cliUtils.cls();
        cliUtils.printFigure("titles/DevelopmentCardSlotSelection", 1, 11);
        cliUtils.vSpace(10);
        cliUtils.pPCS("Slots numbering convention:", Color.WHITE, 12, 4);
        cliUtils.vSpace(1);
        displayMultipleStringChoices(choices);
        displayDevelopmentCardsSlots(client.getCachedModel().getMyPlayerCached().getObject().getPersonalBoard().getDevelopmentCardsSlots());
    }


    /**
     * Executes the CHOICE_NORMAL_ACTION or CHOICE_LEADER_ACTION switch case in displayChoices().
     * The method displays the Player's Personal Board and under it, the questions asking which action (normal or leader
     * he wants to make).
     *
     * @param choices           the choices to display. In this case, the normal or leader actions the Player can make
     * @param askLeadersShowing true if the method asks the Player if he wants to see its LeaderCards, false otherwise
     */
    private void choiceNormalLeaderActionExecute(List<Object> choices, boolean askLeadersShowing) {
        // Restoring the notificationStack here since previous methods may have hide it
        notificationStackPrinter.restoreStackView();

        personalBoardCli.displayPersonalBoard(client.getCachedModel().getMyPlayerCached().getObject(), client.isMultiplayerMode());

        askForLeaderShowing(askLeadersShowing);

        cliUtils.setCursorPosition(47, 1);
        displayMultipleStringChoices(choices);
    }


    /**
     * Before selecting a Leader/Normal action, asks the player if he wants to see its LeaderCards.
     * If the Player decides to see its Leader Cards, the method will call the displayPlayerLeaderCards() method.
     * In each case, the Personal Board view is restored after the method execution.
     *
     * @param habilitateShowing set it to true to habilitate the execution of the method, set it to false to not execute the method
     */
    private void askForLeaderShowing(boolean habilitateShowing) {
        // executingTask is always true while executing this method
        if (habilitateShowing && client.getCachedModel().getMyPlayerCached().getObject().getLeaderCards().size() > 0) {
            String displayLeadersChoice;

            cliUtils.setCursorPosition(48, 1);
            pw.print(cliUtils.hSpace(5) + "Type 'L' if you want to display leaders, otherwise press Enter: ");
            pw.flush();

            displayLeadersChoice = in.nextLine();

            if (displayLeadersChoice.equalsIgnoreCase("l")) {
                personalBoardCli.displayPlayerLeaderCards(client.getCachedModel().getMyPlayerCached().getObject().getLeaderCards(), 1, 1);
                cliUtils.vSpace(5);
                pw.print(cliUtils.hSpace(5) + "Press Enter to go back to the main action selection.");
                pw.flush();
                in.nextLine();
            }

            cliUtils.reverseClearLine(48, 200);
            personalBoardCli.displayPersonalBoard(client.getCachedModel().getMyPlayerCached().getObject(), client.isMultiplayerMode());
        }
    }


    /**
     * Executes the CHOICE_RESOURCE_FROM_WAREHOUSE switch case in displayChoices().
     * A List of the possible Resource the Player can choose is displayed with the displayMultipleStringChoices() method.
     *
     * @param choices the choices to display. In this case, the Resources the Player can choose
     */
    private void choiceResourceFromWarehouseExecute(List<Object> choices) {
        cliUtils.clns();
        cliUtils.printFigure("titles/ChooseResourceFromWarehouse", 1, 8);
        cliUtils.vSpace(3);
        pw.println(cliUtils.hSpace(3) + "Please type the number of the corresponding Resource you want to give");
        cliUtils.vSpace(3);
        pw.flush();
        displayMultipleStringChoices(choices);
        cliUtils.vSpace(10);
    }


    /**
     * Displays the Production Activation screen.
     *
     * @param productions the Productions to display
     */
    public void displayProductionSelection(List<Production> productions) {
        List<Resource> playerResources = client.getCachedModel().getMyPlayerCached().getObject().getPersonalBoard().getAllAvailableResources();
        personalBoardCli.displayProductionActivation(productions, playerResources);
        cliUtils.moveCursor("dn", 10);
    }


    /**
     * Used as an auxiliary method in displayChoices() to display the available choices.
     * The choices entered must be at least a maxChoices number.
     * Each user input is controlled by the getCorrectChoice() method.
     * In case of wrong choices, an error message will be printed.
     *
     * @param nChoices      the number of choices the Player can make
     * @param minChoices    the minimum number of choices the Player can make
     * @param maxChoices    the maximum number of choices the Player can make
     * @param hasUndoOption true if the Player can exit from the choice screen
     * @return a List containing the Player's choices
     * @throws UndoOptionSelectedException the Player chooses to exit from the current choice screen
     */
    private List<Integer> displayInputChoice(int nChoices, int minChoices, int maxChoices, boolean hasUndoOption) throws UndoOptionSelectedException {
        List<Integer> choices = new ArrayList<>();
        int itemInt;

        pw.print(cliUtils.hSpace(3) + "Select at least " + minChoices + " items." + ((maxChoices > minChoices) ? " Up to " + maxChoices + " items." : ""));

        while (choices.size() < maxChoices) {

            try {
                itemInt = getCorrectChoice(choices.size(), minChoices, nChoices, hasUndoOption);
                if (!choices.contains(itemInt)) choices.add(itemInt);
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                cliUtils.vSpace(1);
                pw.print(cliUtils.hSpace(3) + cliUtils.pCS("INCORRECT CHOICE INSERTED", Color.RED));
                cliUtils.vSpace(3);
            } catch (ConfirmationException e) {
                break;
            }

        }

        return choices;
    }


    /**
     * Used as an auxiliary method in displayChoices() to get the correct input from the Player.
     * After the input is entered by the user, performs several controls in order to detect an error, a correct choice or special actions.
     * The special actions are:
     * u -- a new UndoOptionSelectedException() will be thrown (if permitted) and the Player will be able to go back to the previous screen;
     * c -- a new ConfirmationException() will be thrown and the Player will be able to continue the game even if the choices
     * entered are at least a minChoices number.
     *
     * @param nChoices      the number of choices the Player can make
     * @param minChoices    the minimum number of choices the Player can make
     * @param hasUndoOption true if the Player can exit from the choice screen
     * @return a correct Integer selected by the Player
     * @throws IndexOutOfBoundsException   the index is greater/less than the maximum/minimum range of possible choices
     * @throws ConfirmationException       the Player confirms the current choices
     * @throws UndoOptionSelectedException the Player decides to exit from the current display choices screen
     */
    private int getCorrectChoice(int numOfChoices, int minChoices, int nChoices, boolean hasUndoOption) throws IndexOutOfBoundsException, ConfirmationException, UndoOptionSelectedException {
        if (hasUndoOption) pw.print("\n" + cliUtils.hSpace(3) + "Type 'u' to undo operation.");
        pw.print("\n" + cliUtils.hSpace(3) + "Enter the number of the corresponding item [" + 1 + ", " + nChoices + "] (type 'c' - to confirm selections): ");
        pw.flush();

        String item = in.nextLine();

        if (hasUndoOption)
            if (item.equals("u")) throw new UndoOptionSelectedException();

        if (item.equals("c") && numOfChoices >= minChoices) throw new ConfirmationException();
        if (item.isEmpty() || cliUtils.checkAsciiRange(item.charAt(0))) throw new IndexOutOfBoundsException();

        int itemInt = Integer.parseInt(item) - 1;
        if (itemInt < 0 || itemInt >= nChoices) throw new IndexOutOfBoundsException();

        return itemInt;
    }


    /**
     * Used as an auxiliary method in displayChoices() to correctly print the choices.
     * Each choice is printed on a new line.
     *
     * @param choices the choices to print
     */
    private void displayMultipleStringChoices(List<Object> choices) {
        for (int i = 0; i < choices.size(); i++) {
            cliUtils.vSpace(1);
            String choiceToPrint = choices.get(i).toString();
            pw.println(cliUtils.hSpace(5) + (i + 1) + " - " + toTitleStyle(choiceToPrint.replaceAll("_", " ")));
        }
    }


    //-----------------------------------//
    //          ENDGAME METHODS          //
    //-----------------------------------//

    /**
     * Displays the screen that appears when the Match stops, showing the Match Leaderboard and the win or lose title.
     *
     * @param leaderboard   it contains the Players nicknames and the points they achieved during the Match
     * @param winningPlayer the nickname of the winning Player
     */
    @Override
    public void displayEndGame(Map<String, Integer> leaderboard, String winningPlayer) {
        mutex.lock();

        commonScreensCli.displayFinalScreen(leaderboard, leaderboard.size());

        if (client.getNickname().equals(winningPlayer)) {
            pw.print(Color.GREEN.setColor());
            pw.flush();
            cliUtils.printFigure("titles/YouWonTitle", 37, 89);
        } else if (client.isMultiplayerMode() && !leaderboard.containsKey(winningPlayer)) {
            pw.print(Color.YELLOW.setColor());
            pw.flush();
            cliUtils.printFigure("titles/DisconnectionTitle", 37, 89);
        } else {
            pw.print(Color.RED.setColor());
            pw.flush();
            cliUtils.printFigure("titles/YouLostTitle", 37, 85);
        }
        pw.print(Color.RESET.setColor());
        pw.flush();

        cliUtils.pPCS("Press Enter to go back to the main screen", Color.WHITE, 50, 4);

        mutex.unlock();

        in.nextLine();
        client.viewNext();
    }


    //-------------------------------------------//
    //          GENERAL DISPLAY METHODS          //
    //-------------------------------------------//

    /**
     * Displays the given String on screen.
     *
     * @param text the String to display
     */
    @Override
    public void displayText(String text) {
        mutex.lock();

        cliUtils.clns();
        cliUtils.vSpace(1);
        pw.println(cliUtils.hSpace(3) + text);

        mutex.unlock();
        displayNext();
    }


    /**
     * Displays an error popup.
     * The size of the popup change dynamically in base of the given error String.
     *
     * @param error the error String to print on screen
     */
    @Override
    public void displayError(String error) {
        mutex.lock();

        cliUtils.clns();
        cliUtils.setCursorPosition(20, 81);
        for (int i = 0; i < error.length() + 8; i++) pw.print(cliUtils.pCS("=", Color.RED));
        cliUtils.setCursorPosition(21, 1);
        for (int i = 0; i < 2; i++)
            pw.println(cliUtils.hSpace(80) + cliUtils.pCS("║   ", Color.RED) + cliUtils.hSpace(error.length()) + cliUtils.pCS("   ║", Color.RED));
        pw.println(cliUtils.hSpace(80) + cliUtils.pCS("║   ", Color.RED) + cliUtils.pCS(error, Color.RED) + cliUtils.pCS("   ║", Color.RED));
        for (int i = 0; i < 2; i++)
            pw.println(cliUtils.hSpace(80) + cliUtils.pCS("║   ", Color.RED) + cliUtils.hSpace(error.length()) + cliUtils.pCS("   ║", Color.RED));
        cliUtils.setCursorPosition(26, 81);
        for (int i = 0; i < error.length() + 8; i++) pw.print(cliUtils.pCS("=", Color.RED));
        pw.flush();
        cliUtils.vSpace(5);

        mutex.unlock();
        displayNext();
    }


    /**
     * Used to continue between screens and to get the next Message from MessageSynchronizedFIFO.
     */
    private void displayNext() {
        cliUtils.vSpace(1);
        pw.print(cliUtils.hSpace(3) + "Press ENTER to confirm.");
        pw.flush();
        in.nextLine();
        client.viewNext();
    }


    //----------------------------------------//
    //          TURN WAITING METHODS          //
    //----------------------------------------//

    /**
     * Method used to navigate between boards while opponents are playing.
     * A new Thread is created for waiting a new Message which tells the Player its Turn has arrived: when this message is received,
     * the navigation stops and the Player must press Enter to continue with its Turn.
     * <p>
     * While the Player is in navigation mode, he can select the number of the opponents Personal Boards to see (if there are any).
     * When the Player is viewing an opponent Personal Board, he can press r+Enter to see an updated version of the Personal Board.
     */
    @Override
    public void waitForYourTurn() {
        AtomicBoolean waitingForTurn = new AtomicBoolean(true);

        // Creating a Thread that automatically notify the Player that it's his Turn
        new Thread(() -> {
            MessageSynchronizedFIFO.getInstance().lookingForNext();
            waitingForTurn.set(false);
            cliUtils.vSpace(2);
            pw.println(cliUtils.hSpace(4) + "OPPONENTS FINISHED THEIR TURN!");
            pw.print(cliUtils.hSpace(4) + "Press Enter to play your turn.");
            pw.flush();
        }).start();

        // A dummy Player used when there are no Players contained in the CachedModel
        if (client.getCachedModel().getMyPlayerCached().getObject() == null)
            client.getCachedModel().getMyPlayerCached().updateObject(new Player(null, "", ""));

        while (waitingForTurn.get()) {
            // At each iteration the current Player Personal Board will be printed
            personalBoardCli.displayPersonalBoard(client.getCachedModel().getMyPlayerCached().getObject(), client.isMultiplayerMode());

            mutex.lock();
            printOpponentViewScreenInformation();
            printOpponentsPersonalBoardList();
            mutex.unlock();

            // If there are opponents to display, the Player can choose which one to see the Personal Board
            if (client.getCachedModel().getNumberOfOpponents() > 0) {
                try {
                    displayOpponentPersonalBoard();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IndexOutOfBoundsException | NumberFormatException ignored) {
                }
            }
        }

        client.viewNext();
    }


    /**
     * Prints the instructions of the opponent viewing screen.
     */
    private void printOpponentViewScreenInformation() {
        cliUtils.pPCS("OPPONENTS TURN!", Color.WHITE, 48, 5);
        cliUtils.pPCS("Select the number of the opponents Board you want to see.", Color.WHITE, 49, 5);
    }


    /**
     * Displays the chosen opponent PersonalBoard.
     * If the Player wants to update the current opponent view it has to enter the letter 'r'.
     * If no 'r' is entered when asked, the method will stop and the Player will go back to its Personal Board view.
     *
     * @throws InterruptedException the thread is interrupted
     */
    private void displayOpponentPersonalBoard() throws InterruptedException {
        cliUtils.vSpace(2);
        pw.print(cliUtils.hSpace(4) + "Your choice: ");
        pw.flush();

        int opponentNumber = getCorrectOpponentNumber(client.getCachedModel().getNumberOfOpponents());
        String reloadOpponentChoice;

        do {
            Player opponent = client.getCachedModel().getOpponentCached(opponentNumber).getObject();

            mutex.lock();
            personalBoardCli.displayPersonalBoard(opponent, client.isMultiplayerMode());
            cliUtils.pPCS("Viewing " + opponent.getNickname() + " Personal Board.", Color.WHITE, 50, 5);
            cliUtils.pPCS("Enter 'r' if you want to reload an updated version of " + opponent.getNickname() + "Personal Board.", Color.WHITE, 51, 5);
            cliUtils.pPCS("Press Enter to go back to your Personal Board view.", Color.WHITE, 52, 5);
            cliUtils.vSpace(2);
            pw.print(cliUtils.hSpace(4) + "Your choice: ");
            pw.flush();
            mutex.unlock();

            reloadOpponentChoice = in.nextLine();
        } while (reloadOpponentChoice.equals("r"));
    }


    /**
     * Used to get the correct opponent number for displaying the opponent PersonalBoard.
     *
     * @param numberOfOpponents the total amount of opponents from which choose the PersonalBoard
     * @return the opponent number
     * @throws IndexOutOfBoundsException the opponent's number chosen exceeds the admissible bounds
     */
    private int getCorrectOpponentNumber(int numberOfOpponents) throws IndexOutOfBoundsException {
        String input = in.nextLine();

        int opponentNumber = Integer.parseInt(input) - 1;
        if (opponentNumber >= numberOfOpponents || opponentNumber < 0) throw new IndexOutOfBoundsException();

        return opponentNumber;
    }


    /**
     * Prints the List of the cached opponents nicknames.
     */
    private void printOpponentsPersonalBoardList() {
        if (client.getCachedModel().getNumberOfOpponents() == 0) {
            cliUtils.pPCS("No opponent registered yet.", Color.WHITE, 54, 5);
        } else {
            cliUtils.vSpace(2);
            for (int i = 0; i < client.getCachedModel().getNumberOfOpponents(); i++) {
                try {
                    pw.print(cliUtils.hSpace(4) + (i + 1) + " - " + client.getCachedModel().getOpponentCached(i).getObject().getNickname());
                    cliUtils.vSpace(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
