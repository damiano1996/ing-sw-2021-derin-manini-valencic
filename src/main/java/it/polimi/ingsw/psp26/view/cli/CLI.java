package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.*;
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
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.client.NotificationsFIFO;
import it.polimi.ingsw.psp26.utils.ViewUtils;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.utils.CollectionsUtils.castElements;
import static it.polimi.ingsw.psp26.utils.CollectionsUtils.getElementsByIndices;
import static it.polimi.ingsw.psp26.view.ViewUtils.toTitleStyle;

public class CLI implements ViewInterface {

    private final Scanner in;
    private final PrintWriter pw;
    private final CliUtils cliUtils;
    private final DevelopmentCardsCli developmentCardsCli;
    private final FaithTrackCli faithTrackCli;
    private final LeaderCardsCli leaderCardsCli;
    private final MarketCli marketCli;
    private final PersonalBoardCli personalBoardCli;
    private final DisplayWarehousePlacer displayWarehousePlacer;
    private final NotificationStackPrinter notificationStackPrinter;
    private final CommonScreensCli commonScreensCli;
    private Client client;

    // If true, the Thread printing notifications will wait until other cli's elements are printed
    private volatile boolean executingTask;

    public CLI() {
        this.in = new Scanner(System.in);
        this.pw = new PrintWriter(System.out);
        this.cliUtils = new CliUtils(pw);
        this.developmentCardsCli = new DevelopmentCardsCli(pw);
        this.faithTrackCli = new FaithTrackCli(pw);
        this.leaderCardsCli = new LeaderCardsCli(pw);
        this.marketCli = new MarketCli(pw);
        this.personalBoardCli = new PersonalBoardCli(pw);
        this.displayWarehousePlacer = new DisplayWarehousePlacer(pw);
        this.notificationStackPrinter = new NotificationStackPrinter(pw);
        this.commonScreensCli = new CommonScreensCli(pw);
    }


    //--------------------------------------//
    //          START GAME METHODS          //
    //--------------------------------------//

    /**
     * Starts a new Client and displays the login screen
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
     * Prints the Title Screen when the program is launched
     * Used in printTitle() method
     */
    private void printTitle() {
        cliUtils.cls();

        pw.print(Color.GREEN.setColor());
        pw.flush();
        cliUtils.printFigure("/titles/MainTitle", 1, 10);
        pw.print(Color.RESET.setColor());
        pw.flush();
    }


    /**
     * Used to ask the Player's credentials
     */
    public void displayLogIn() {
        printTitle();
        cliUtils.printFigure("/titles/PressEnterTitle", 20, 76);
        in.nextLine();

        String nickname = "";
        String password = "";

        for (int i = 0; i < 3; i++) {
            printTitle();
            cliUtils.vSpace(4);

            if (i == 0) {
                pw.print(cliUtils.hSpace(100) + "Enter Nickname: ");
                pw.flush();
                nickname = in.nextLine();
            }

            if (i == 1) {
                pw.println(cliUtils.hSpace(100) + "Enter Nickname: " + nickname);
                pw.flush();
                cliUtils.vSpace(2);
                pw.print(cliUtils.hSpace(100) + "Enter Password: ");
                pw.flush();
                password = in.nextLine();
            }

            if (i == 2) {
                pw.println(cliUtils.hSpace(100) + "Enter Nickname: " + nickname);
                pw.flush();
                cliUtils.vSpace(2);
                pw.print(cliUtils.hSpace(100) + "Enter Password: " + password);
                pw.flush();
                cliUtils.vSpace(3);
                pw.print(cliUtils.hSpace(100) + "Enter IP-Address: ");
                pw.flush();
                String serverIP = in.nextLine();

                // Starting the Thread to print notifications
                startLiveUpdate();

                client.initializeNetworkHandler(nickname, password, serverIP);
                client.viewNext();
            }
        }
    }

    @Override
    public void displayGlobalLeaderboard() {
        // to implement;
    }

    //------------------------------------------//
    //          WAITING SCREEN METHODS          //
    //------------------------------------------//

    /**
     * Starts a waiting screen
     *
     * @param message A String to display during the waiting screen
     */
    public void displayWaitingScreen(Message message) {
        try {
            WaitingScreenStarter.getInstance().startWaiting(message);
        } catch (EmptyPayloadException ignored) {
        }
        client.viewNext();
    }


    /**
     * Stops the waiting screen
     */
    public void stopDisplayingWaitingScreen() {
        WaitingScreenStarter.getInstance().stopWaiting();
        client.viewNext();
    }


    //---------------------------------------//
    //          LIVE UPDATE METHODS          //
    //---------------------------------------//

    /**
     * Creates a new Thread that checks if there are new notifications
     */
    private void startLiveUpdate() {
        new Thread(() -> {
            while (true) {
                // Only one call to the get method, otherwise it will freeze
                List<String> notifications = NotificationsFIFO.getInstance().getNotifications();

                // If you don't give time the cursor may be in the wrong position when printing
                try {
                    TimeUnit.MILLISECONDS.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                displayNotifications(notifications);
            }
        }).start();
    }


    /**
     * Used to print notifications in the Notification Stack printer
     *
     * @param notifications The Strings to print in the Notification Stack
     */
    @Override
    public synchronized void displayNotifications(List<String> notifications) {
        while (executingTask) {
            // Using onSpinWait() in order to not call notifyAll() from other cli's methods
            Thread.onSpinWait();
        }
        notificationStackPrinter.printMessageStack(notifications);
    }


    //-----------------------------------------//
    //          MISC ELEMENTS METHODS          //
    //-----------------------------------------//

    /**
     * Displays the given Leader Cards
     *
     * @param leaderCards The Leader Cards to display
     */
    @Override
    public void displayLeaderCards(List<LeaderCard> leaderCards) { // never used
        executingTask = true;
        personalBoardCli.displayPlayerLeaderCards(leaderCards, 1, 1);
        executingTask = false;
    }


    /**
     * Displays the Player's Personal Board
     *
     * @param player            The Player from where to get the Personal Board
     * @param isMultiplayerMode Used to print Lorenzo's Faith Marker if set to false
     */
    @Override
    public void displayPersonalBoard(Player player, boolean isMultiplayerMode) { // never used
        executingTask = true;

        notificationStackPrinter.restoreStackView();
        personalBoardCli.displayPersonalBoard(player, isMultiplayerMode);
        cliUtils.setCursorPosition(47, 1);

        executingTask = false;
        client.viewNext();
    }


    /**
     * Displays a Faith Track
     *
     * @param faithTrack        The Faith Track to display
     * @param isMultiPlayerMode Used to print Lorenzo's Faith Marker if set to false
     */
    @Override
    public void displayFaithTrack(FaithTrack faithTrack, boolean isMultiPlayerMode) { // never used
        executingTask = true;
        faithTrackCli.displayFaithTrack(faithTrack, 3, 10, isMultiPlayerMode);
        executingTask = false;
    }


    /**
     * Displays the List of Development Cards as Development Card Slots
     *
     * @param developmentCardsSlots The Cards to display
     */
    @Override
    public void displayDevelopmentCardsSlots(List<List<DevelopmentCard>> developmentCardsSlots) {
        cliUtils.pPCS("Enter the number of the slot in which you want to put the drawn card", Color.WHITE, 10, 4);
        personalBoardCli.displayDevelopmentCardsSlots(developmentCardsSlots, 30, 70);
        cliUtils.pPCS("Slot  1", Color.GREY, 47, 85);
        cliUtils.pPCS("Slot  2", Color.GREY, 47, 121);
        cliUtils.pPCS("Slot  3", Color.GREY, 47, 157);
        cliUtils.vSpace(3);
    }


    /**
     * Displays the Market screen
     *
     * @param marketTray The Market Tray to display
     */
    @Override
    public void displayMarketScreen(MarketTray marketTray) { // never used
        executingTask = true;
        marketCli.displayMarketScreen(marketTray);
        executingTask = false;
    }


    /**
     * Displays the Development Card Grid
     *
     * @param developmentCardsGrid The Development Card Grid to display
     */
    @Override
    public void displayDevelopmentGrid(DevelopmentCardsGrid developmentCardsGrid) { // never used
        executingTask = true;
        developmentCardsCli.displayDevelopmentGrid(developmentCardsGrid);
        executingTask = false;
    }


    /**
     * Displays the Resource Supply
     *
     * @param resourceSupply The Resource Supply to display
     * @param resourcesTypes The Resource types contained in the Resource Supply
     */
    @Override
    public void displayResourceSupply(ResourceSupply resourceSupply, List<Resource> resourcesTypes) {
        executingTask = true;
        personalBoardCli.displayResourceSupply(resourceSupply, resourcesTypes, 1, 37);
        executingTask = false;
    }


    /**
     * Displays the Action Tokens screen
     *
     * @param unusedTokens The Action Tokens to display
     */
    @Override
    public void displayActionTokens(List<ActionToken> unusedTokens) {
        executingTask = true;

        notificationStackPrinter.restoreStackView();
        personalBoardCli.displayActionTokens(unusedTokens);

        executingTask = false;
        displayNext();
    }


    //----------------------------------------//
    //          MAIN ACTIONS METHODS          //
    //----------------------------------------//

    /**
     * Displays the Market action screen
     *
     * @param marketTray      The Market Tray to display
     * @param playerResources The Player's current Resources
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
     * Displays the Development Card buy action screen
     *
     * @param developmentCardsGrid The Development Grid to display
     * @param playerResources      The Player's current Resources
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
     * Displays the screen where the Player can modify the Depots
     *
     * @param warehouse      The Warehouse of the Player
     * @param resourcesToAdd The Resources the Player can add to the Warehouse
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
     *
     * @param messageType   The Message Type used to get the correct representation of the screen
     * @param question      The question to ask
     * @param choices       The choices the Player has
     * @param minChoices    The minimum number of choices the Player can make
     * @param maxChoices    The maximum number of choices the Player can make
     * @param hasUndoOption True if the Player can exit from the choice screen
     */
    @Override
    public void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices, boolean hasUndoOption) {
        // Starts blocking the notifications printing
        executingTask = true;

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
            case CHOICE_LEADER_ACTION:
                choiceNormalLeaderActionExecute(choices);
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
                displayResourceSupply(new ResourceSupply(), castElements(Resource.class, choices));
                cliUtils.vSpace(10);
                cliUtils.pPCS("Please insert the number of the Resource slot you want.", Color.WHITE, 38, 4);
                break;

            default:
                break;
        }

        // Stops blocking the notification printing
        executingTask = false;

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
     * Displays the Leader Cards activation/discard selection screen
     *
     * @param leaderCards The Leader Cards to display
     */
    public void displayLeaderCardDiscardActivationSelection(List<LeaderCard> leaderCards) {
        cliUtils.clns();
        leaderCardsCli.printMultipleLeaders(leaderCards, 3);
        cliUtils.vSpace(3);
    }


    /**
     * Executes the corresponding switch case in displayChoices()
     *
     * @param choices The choices to display
     */
    private void choicePositionExecute(List<Object> choices) {
        cliUtils.cls();
        cliUtils.printFigure("/titles/DevelopmentCardSlotSelection", 1, 11);
        cliUtils.vSpace(10);
        cliUtils.pPCS("Slots numbering convention:", Color.WHITE, 12, 4);
        cliUtils.vSpace(1);
        displayMultipleStringChoices(choices);
        displayDevelopmentCardsSlots(client.getCachedModel().getMyPlayerCached().getObject().getPersonalBoard().getDevelopmentCardsSlots());
    }


    /**
     * Executes the corresponding switch case in displayChoices()
     *
     * @param choices The choices to display
     */
    private void choiceNormalLeaderActionExecute(List<Object> choices) {
        notificationStackPrinter.restoreStackView();

        personalBoardCli.displayPersonalBoard(client.getCachedModel().getMyPlayerCached().getObject(), client.isMultiplayerMode());
        cliUtils.setCursorPosition(47, 1);
        displayMultipleStringChoices(choices);
    }


    /**
     * Executes the corresponding switch case in displayChoices()
     *
     * @param choices The choices to display
     */
    private void choiceResourceFromWarehouseExecute(List<Object> choices) {
        cliUtils.clns();
        cliUtils.printFigure("/titles/ChooseResourceFromWarehouse", 1, 8);
        cliUtils.vSpace(3);
        pw.println(cliUtils.hSpace(3) + "Please type the number of the corresponding Resource you want to give");
        cliUtils.vSpace(3);
        pw.flush();
        displayMultipleStringChoices(choices);
        cliUtils.vSpace(10);
    }


    /**
     * Displays the Production Activation screen
     *
     * @param productions The Productions to display
     */
    public void displayProductionSelection(List<Production> productions) {
        List<Resource> playerResources = client.getCachedModel().getMyPlayerCached().getObject().getPersonalBoard().getAllAvailableResources();
        personalBoardCli.displayProductionActivation(productions, playerResources);
        cliUtils.moveCursor("dn", 10);
    }


    /**
     * Used as an auxiliary method in displayChoices() to display the available choices
     *
     * @param nChoices      The number of choices the Player can make
     * @param minChoices    The minimum number of choices the Player can make
     * @param maxChoices    The maximum number of choices the Player can make
     * @param hasUndoOption True if the Player can exit from the choice screen
     * @return A List containing the Player's choices
     * @throws UndoOptionSelectedException The Player chooses to exit from the current choice screen
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
     * Used as an auxiliary method in displayChoices() to get the correct input from the Player
     *
     * @param nChoices      The number of choices the Player can make
     * @param minChoices    The minimum number of choices the Player can make
     * @param hasUndoOption True if the Player can exit from the choice screen
     * @return A correct Integer selected by the Player
     * @throws IndexOutOfBoundsException   The index is greater/less than the maximum/minimum range of possible choices
     * @throws ConfirmationException       The Player confirms the current choices
     * @throws UndoOptionSelectedException The Player decides to exit from the current display choices screen
     */
    private int getCorrectChoice(int numOfChoices, int minChoices, int nChoices, boolean hasUndoOption) throws IndexOutOfBoundsException, ConfirmationException, UndoOptionSelectedException {
        if (hasUndoOption) pw.print("\n" + cliUtils.hSpace(3) + "Type 'u' to undo operation.");
        pw.print("\n" + cliUtils.hSpace(3) + "Enter the number of the corresponding item [" + 1 + ", " + nChoices + "] (type 'c' - to confirm selections): ");
        pw.flush();

        String item = in.nextLine();

        if (hasUndoOption)
            if (item.equals("u")) throw new UndoOptionSelectedException();

        if (item.equals("c") && numOfChoices >= minChoices) throw new ConfirmationException();
        if (item.isEmpty() || ViewUtils.checkAsciiRange(item.charAt(0))) throw new IndexOutOfBoundsException();

        int itemInt = Integer.parseInt(item) - 1;
        if (itemInt < 0 || itemInt >= nChoices) throw new IndexOutOfBoundsException();

        return itemInt;
    }


    /**
     * Used as an auxiliary method in displayChoices() to correctly print the choices
     *
     * @param choices The choices to print
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
     * Displays the screen that appears when the Match stops
     *
     * @param leaderboard   It contains the Players nicknames and the points they achieved during the Match
     * @param winningPlayer The nickname of the winning Player
     */
    @Override
    public void displayEndGame(Map<String, Integer> leaderboard, String winningPlayer) {
        executingTask = true;

        commonScreensCli.displayFinalScreen(leaderboard);

        if (client.getNickname().equals(winningPlayer)) {
            pw.print(Color.GREEN.setColor());
            pw.flush();
            cliUtils.printFigure("/titles/YouWonTitle", 37, 89);
        } else {
            pw.print(Color.RED.setColor());
            pw.flush();
            cliUtils.printFigure("/titles/YouLostTitle", 37, 85);
        }
        pw.print(Color.RESET.setColor());
        pw.flush();

        cliUtils.pPCS("Press Enter to go back to the main screen", Color.WHITE, 50, 4);

        executingTask = false;

        in.nextLine();
        client.viewNext();
    }


    //-------------------------------------------//
    //          GENERAL DISPLAY METHODS          //
    //-------------------------------------------//

    /**
     * Displays the given String on screen
     *
     * @param text The String to display
     */
    @Override
    public void displayText(String text) {
        executingTask = true;

        cliUtils.clns();
        cliUtils.vSpace(1);
        pw.println(cliUtils.hSpace(3) + text);

        executingTask = false;
        displayNext();
    }

    /**
     * Displays an error popup
     *
     * @param error The error String to print on screen
     */
    @Override
    public void displayError(String error) {
        executingTask = true;

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

        executingTask = false;
        displayNext();
    }


    /**
     * Used to continue between screens and to get the next Message from MessageSynchronizedFIFO
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
     * Method used to do some actions while opponents are playing
     */
    @Override
    public void waitForYourTurn() {
        // A dummy Player used when there are no Players contained in the CachedModel
        if (client.getCachedModel().getMyPlayerCached().getObject() == null)
            client.getCachedModel().getMyPlayerCached().updateObject(new Player(null, "", ""));

        while (true) {
            // At each iteration the current Player Personal Board will be printed
            personalBoardCli.displayPersonalBoard(client.getCachedModel().getMyPlayerCached().getObject(), client.isMultiplayerMode());

            executingTask = true;
            printOpponentViewScreenInformation();
            printOpponentsPersonalBoardList();
            executingTask = false;

            // If there are opponents to display, the Player can choose which one to see the Personal Board
            if (client.getCachedModel().getNumberOfOpponents() > 0) {
                try {
                    displayOpponentPersonalBoard();
                    in.nextLine();
                } catch (FinishedViewingOpponentsException e) {
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IndexOutOfBoundsException | NumberFormatException ignored) {
                }
            } else {
                cliUtils.vSpace(2);
                pw.print(cliUtils.hSpace(4) + "Press Enter to continue with your turn.");
                pw.flush();
                in.nextLine();
                break;
            }
        }

        displayNext();
    }


    /**
     * Prints the instructions of the opponent viewing screen
     */
    private void printOpponentViewScreenInformation() {
        cliUtils.pPCS("OPPONENTS TURN!", Color.WHITE, 48, 5);
        cliUtils.pPCS("Select the number of the opponents Board you want to see.", Color.WHITE, 49, 5);
        cliUtils.pPCS("Enter 'done' when you want to continue with your turn.", Color.WHITE, 50, 5);
        cliUtils.pPCS("After entering 'done' if nothing happens don't worry: the opponents are still playing their turn!", Color.WHITE, 51, 5);
    }


    /**
     * Displays the chosen opponent Personal Board
     *
     * @throws FinishedViewingOpponentsException The Player enters 'done' and continues with it's turn
     * @throws InterruptedException              The thread is interrupted
     */
    private void displayOpponentPersonalBoard() throws FinishedViewingOpponentsException, InterruptedException {
        cliUtils.vSpace(2);
        pw.print(cliUtils.hSpace(4) + "Your choice: ");
        pw.flush();

        int opponentNumber = getCorrectOpponentNumber(client.getCachedModel().getNumberOfOpponents());
        Player opponent = client.getCachedModel().getOpponentCached(opponentNumber).getObject();

        executingTask = true;
        personalBoardCli.displayPersonalBoard(opponent, client.isMultiplayerMode());
        cliUtils.pPCS("Viewing " + opponent.getNickname() + " Personal Board", Color.WHITE, 50, 5);
        cliUtils.pPCS("Press Enter to go back to your Personal Board view.", Color.WHITE, 52, 5);
        executingTask = false;
    }


    /**
     * Used to get the correct opponent number for displaying the opponent Personal Board
     *
     * @param numberOfOpponents The total amount of opponents from which choose the Personal Board
     * @return The opponent number
     * @throws FinishedViewingOpponentsException The Player enters 'done' and continues with it's turn
     * @throws IndexOutOfBoundsException         The opponent's number chosen exceeds the admissible bounds
     */
    private int getCorrectOpponentNumber(int numberOfOpponents) throws FinishedViewingOpponentsException, IndexOutOfBoundsException {
        String input;

        input = in.nextLine();
        if (input.equalsIgnoreCase("done")) throw new FinishedViewingOpponentsException();

        int opponentNumber = Integer.parseInt(input) - 1;
        if (opponentNumber >= numberOfOpponents || opponentNumber < 0) throw new IndexOutOfBoundsException();

        return opponentNumber;
    }


    /**
     * Prints the List of the cached opponents nicknames
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
