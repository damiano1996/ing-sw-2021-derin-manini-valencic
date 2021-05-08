package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.ConfirmationException;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.exceptions.UndoOptionSelectedException;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.utils.ViewUtils;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.getElementsByIndices;

public class CLI implements ViewInterface {

    private final PrintWriter pw;
    private final CliUtils cliUtils;
    private final DepotCli depotCli;
    private final DevelopmentCardsCli developmentCardsCli;
    private final FaithTrackCli faithTrackCli;
    private final LeaderCardsCli leaderCardsCli;
    private final MarketCli marketCli;
    private final PersonalBoardCli personalBoardCli;
    private final DisplayWarehousePlacer displayWarehousePlacer;
    private final NotificationStackPrinter notificationStackPrinter;
    private Client client;

    public CLI() {
        this.pw = new PrintWriter(System.out);
        this.cliUtils = new CliUtils(pw);
        this.depotCli = new DepotCli(pw);
        this.developmentCardsCli = new DevelopmentCardsCli(pw);
        this.faithTrackCli = new FaithTrackCli(pw);
        this.leaderCardsCli = new LeaderCardsCli(pw);
        this.marketCli = new MarketCli(pw);
        this.personalBoardCli = new PersonalBoardCli(pw);
        this.displayWarehousePlacer = new DisplayWarehousePlacer(pw);
        this.notificationStackPrinter = new NotificationStackPrinter(pw);
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


    //------------------------------------------//
    //          VIEW INTERFACE METHODS          //
    //------------------------------------------//


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
     * Used to ask the Player's credentials
     */
    public void displayLogIn() {
        Scanner in = new Scanner(System.in);

        printTitle();
        cliUtils.printFigure("/titles/PressEnterTitle", 20, 76);

        in.nextLine();

        for (int i = 0; i < 2; i++) {
            printTitle();
            cliUtils.vSpace(4);
            if (i == 0) {
                pw.print(cliUtils.hSpace(100) + "Enter Nickname: ");
                pw.flush();
                String nickname = in.nextLine();
                client.setNickname(nickname);
            } else {
                pw.println(cliUtils.hSpace(100) + "Enter Nickname: " + client.getNickname());
                pw.flush();
                cliUtils.vSpace(2);
                pw.print(cliUtils.hSpace(100) + "Enter IP-Address: ");
                pw.flush();
                String serverIP = in.nextLine();
                client.initializeNetworkHandler(serverIP);
                // go to Multi/single player choice
                displayChoices(
                        MULTI_OR_SINGLE_PLAYER_MODE,
                        "Choose the playing mode:",
                        Arrays.asList(new MessageType[]{SINGLE_PLAYER_MODE, TWO_PLAYERS_MODE, THREE_PLAYERS_MODE, FOUR_PLAYERS_MODE}),
                        1, 1,
                        false
                );
            }
        }
    }


    @Override
    public void displayLeaderCards(List<LeaderCard> leaderCards) {
        personalBoardCli.displayPlayerLeaderCards(leaderCards, 1, 1);
    }

    @Override
    public void displayNotifications(List<String> notifications) {
        notificationStackPrinter.printMessageStack(notifications);
    }


    @Override
    public void displayInkwell(boolean isPrintable) {
        personalBoardCli.displayInkwell(isPrintable, 5, 190);
    }


    @Override
    public void displayPersonalBoard(Player player, boolean isMultiplayerMode) {
        notificationStackPrinter.restoreStackView();
        personalBoardCli.displayPersonalBoard(player, isMultiplayerMode);
        cliUtils.setCursorPosition(47, 1);
        client.viewNext();
    }


    @Override
    public void displayWarehouseDepots(Warehouse warehouse) {
        depotCli.printWarehouse(warehouse, 17, 13);
    }


    @Override
    public void displayWarehouseNewResourcesAssignment(Warehouse warehouse, List<Resource> resourceToAdd) {
        notificationStackPrinter.hideNotifications();
        List<Resource> resources = displayWarehousePlacer.displayMarketResourcesSelection(warehouse, resourceToAdd);
        try {
            client.notifyObservers(new Message(PLACE_IN_WAREHOUSE, resources.toArray(new Object[0])));
        } catch (InvalidPayloadException ignored) {
        }
        displayNext();
    }


    @Override
    public void displayStrongbox(List<Resource> strongbox) {
        depotCli.displayStrongbox(strongbox, 30, 3);
    }


    @Override
    public void displayFaithTrack(FaithTrack faithTrack, boolean isMultiPlayerMode) {
        faithTrackCli.displayFaithTrack(faithTrack, 3, 10, isMultiPlayerMode);
    }


    @Override
    public void displayDevelopmentCardsSlots(List<List<DevelopmentCard>> developmentCardsSlots) {
        cliUtils.pPCS("Enter the number of the slot in which you want to put the drawn card", Color.WHITE, 10, 4);
        personalBoardCli.displayDevelopmentCardsSlots(developmentCardsSlots, 30, 70);
        cliUtils.pPCS("Slot  1", Color.GREY, 47, 85);
        cliUtils.pPCS("Slot  2", Color.GREY, 47, 121);
        cliUtils.pPCS("Slot  3", Color.GREY, 47, 157);
        cliUtils.vSpace(3);
    }


    @Override
    public void displayMarketTray(MarketTray marketTray) {
        marketCli.displayMarketTray(marketTray, 12, 88);
    }


    @Override
    public void displayMarketScreen(MarketTray marketTray) {
        marketCli.displayMarketScreen(marketTray);
    }


    @Override
    public void displayDevelopmentGrid(DevelopmentGrid developmentGrid) {
        developmentCardsCli.displayDevelopmentGrid(developmentGrid);
    }


    @Override
    public void displayResourceSupply(ResourceSupply resourceSupply) {
        personalBoardCli.displayResourceSupply(resourceSupply, 1, 37);
    }


    @Override
    public void displayProductionActivation(List<Production> productions, List<Resource> playerResources) {
        notificationStackPrinter.hideNotifications();
        try {
            List<Production> choices = new ArrayList<>(personalBoardCli.displayProductionActivation(productions, playerResources));
            client.notifyObservers(new Message(CHOICE_CARDS_TO_ACTIVATE, choices.toArray(new Object[0])));
            cliUtils.vSpace(3);
            displayNext();
        } catch (UndoOptionSelectedException e) {
            client.sendUndoMessage();
            client.viewNext();
        } catch (InvalidPayloadException ignored) {
        }
    }


    public void displayLeaderCardDiscardActivationSelection(List<LeaderCard> leaderCards) {
        cliUtils.clns();
        leaderCardsCli.printMultipleLeaders(leaderCards, 3);
        cliUtils.vSpace(3);
    }


    @Override
    public void displayActionTokens(List<ActionToken> unusedTokens) {
        personalBoardCli.displayActionTokens(unusedTokens);
        displayNext();
    }


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


    @Override
    public void displayDevelopmentCardBuyAction(DevelopmentGrid developmentGrid, List<Resource> playerResources) {
        notificationStackPrinter.hideNotifications();
        List<DevelopmentCard> choice = new ArrayList<>();
        try {
            choice.add(developmentCardsCli.displayDevelopmentCardSelection(developmentGrid, playerResources));
            client.notifyObservers(new Message(CHOICE_CARD_TO_BUY, choice.toArray(new Object[0])));
            cliUtils.setCursorBottomLeft();
            displayNext();
        } catch (UndoOptionSelectedException e) {
            client.sendUndoMessage();
            client.viewNext();
        } catch (InvalidPayloadException ignored) {
        }
    }


    @Override
    public void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices, boolean hasUndoOption) {

        cliUtils.vSpace(1);
        pw.println(cliUtils.hSpace(3) + question);

        switch (messageType) {

            case MULTI_OR_SINGLE_PLAYER_MODE:
                cliUtils.clns();
                displayMultipleStringChoices(choices);
                break;

            case CHOICE_POSITION:
                cliUtils.cls();
                displayMultipleStringChoices(choices);
                displayDevelopmentCardsSlots(client.getPersonalBoardCopy().getDevelopmentCardsSlots());
                break;

            case CHOICE_NORMAL_ACTION:
            case CHOICE_LEADER_ACTION:
                displayMultipleStringChoices(choices);
                break;

            case CHOICE_LEADERS:
                cliUtils.clns();
                displayLeaderCardDiscardActivationSelection(castElements(LeaderCard.class, choices));
                break;

            case CHOICE_RESOURCE_FROM_WAREHOUSE: //TODO migliora la grafica di questa schermata
            case CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY:
                cliUtils.clns();
                //ResourceSupply is the unique element of choices List
                displayResourceSupply(new ResourceSupply());
                cliUtils.vSpace(10);
                break;

            default:
                break;
        }

        // send to server response
        try {
            cliUtils.vSpace(1);
            List<Object> selected = getElementsByIndices(choices, displayInputChoice(choices.size(), minChoices, maxChoices, hasUndoOption));
            client.notifyObservers(new Message(messageType, selected.toArray(new Object[0])));

            if (messageType.equals(MULTI_OR_SINGLE_PLAYER_MODE)) {
                client.setMatchModeType((MessageType) selected.get(0));
                try {
                    client.notifyObservers(new Message(ADD_PLAYER, client.getNickname()));
                } catch (InvalidPayloadException e) {
                    e.printStackTrace();
                }
            }
            client.viewNext();

        } catch (InvalidPayloadException e) {
            e.printStackTrace();
        } catch (UndoOptionSelectedException e) { //TODO da fare con i leader
            client.sendUndoMessage();
            client.viewNext();
        }

    }


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


    private int getCorrectChoice(int numOfChoices, int minChoices, int nChoices, boolean hasUndoOption) throws IndexOutOfBoundsException, ConfirmationException, UndoOptionSelectedException {
        if (hasUndoOption) pw.print("\n" + cliUtils.hSpace(3) + "Type 'u' to undo operation.");
        pw.print("\n" + cliUtils.hSpace(3) + "Enter the number of the corresponding item [" + 1 + ", " + nChoices + "] (type 'c' - to confirm selections): ");
        pw.flush();

        Scanner in = new Scanner(System.in);
        String item = in.nextLine();

        if (hasUndoOption)
            if (item.equals("u")) throw new UndoOptionSelectedException();

        if (item.equals("c") && numOfChoices >= minChoices) throw new ConfirmationException();
        if (item.isEmpty() || ViewUtils.checkAsciiRange(item.charAt(0))) throw new IndexOutOfBoundsException();

        int itemInt = Integer.parseInt(item) - 1;
        if (itemInt < 0 || itemInt >= nChoices) throw new IndexOutOfBoundsException();

        return itemInt;
    }


    private void displayMultipleStringChoices(List<Object> choices) {
        for (int i = 0; i < choices.size(); i++) {
            cliUtils.vSpace(1);
            pw.println(cliUtils.hSpace(5) + (i + 1) + " - " + choices.get(i));
        }
    }


    @Override
    public void displayText(String text) {
        cliUtils.clns();
        cliUtils.vSpace(1);
        pw.println(cliUtils.hSpace(3) + text);

        displayNext();
    }


    @Override
    public void displayEndGame(HashMap<String, Integer> playersVictoryPoints) {
        //To be implemented
    }


    @Override
    public void displayError(String error) {
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

        displayNext();
    }


    private void displayNext() {
        Scanner in = new Scanner(System.in);
        cliUtils.vSpace(1);
        pw.print(cliUtils.hSpace(3) + "Press ENTER to confirm.");
        pw.flush();
        in.nextLine();
        client.viewNext();
    }


    public void displayWaitingScreen(Message message) {
        try {
            WaitingScreenStarter.getInstance().startWaiting(message);
        } catch (EmptyPayloadException ignored) {
        }
        client.viewNext();
    }


    public void stopDisplayingWaitingScreen() {
        WaitingScreenStarter.getInstance().stopWaiting();
        client.viewNext();
    }

}
