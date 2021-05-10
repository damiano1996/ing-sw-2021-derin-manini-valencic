package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.exceptions.ConfirmationException;
import it.polimi.ingsw.psp26.exceptions.ResourceSupplySlotOutOfBoundsException;
import it.polimi.ingsw.psp26.exceptions.UndoOptionSelectedException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;
import it.polimi.ingsw.psp26.utils.ViewUtils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.psp26.utils.ViewUtils.printPlayerResources;

public class PersonalBoardCli {

    private final PrintWriter pw;
    private final CliUtils cliUtils;
    private final LeaderCardsCli leaderCardsCli;
    private final DevelopmentCardsCli developmentCardsCli;
    private final FaithTrackCli faithTrackCli;
    private final DepotCli depotCli;

    public PersonalBoardCli(PrintWriter pw) {
        this.pw = pw;
        this.cliUtils = new CliUtils(pw);
        this.leaderCardsCli = new LeaderCardsCli(pw);
        this.developmentCardsCli = new DevelopmentCardsCli(pw);
        this.faithTrackCli = new FaithTrackCli(pw);
        this.depotCli = new DepotCli(pw);
    }


    /**
     * Prints the Personal Board of a given player
     *
     * @param player            The player who's Personal Board is gonna be printed
     * @param isMultiplayerMode Tells if the Match is Single or MultiPlayer (used to print the correct FaithTrack)
     */
    public void displayPersonalBoard(Player player, boolean isMultiplayerMode) {
        cliUtils.clns();

        faithTrackCli.displayFaithTrack(player.getPersonalBoard().getFaithTrack(), 3, 10, isMultiplayerMode);

        displayInkwell(player.hasInkwell(), 5, 199);

        depotCli.printWarehouse(player.getPersonalBoard().getWarehouse(), 17, 13);

        depotCli.displayStrongbox(player.getPersonalBoard().getStrongbox(), 30, 3);

        displayDevelopmentCardsSlots(player.getPersonalBoard().getDevelopmentCardsSlots(), 30, 70);

        printLeaderCardsInPersonalBoard(player.getLeaderCards(), 18, 195);

        printLeaderDepots(player.getPersonalBoard().getWarehouse().getLeaderDepots(), 20, 50);
    }


    /**
     * Prints the Inkwell
     *
     * @param isPrintable    Tells the method if the Player has the Inkwell
     * @param startingRow    The starting row where the Inkwell is going to be printed
     * @param startingColumn The starting column where the Inkwell is going to be printed
     */
    public void displayInkwell(boolean isPrintable, int startingRow, int startingColumn) {
        if (isPrintable) cliUtils.printFigure("Inkwell", startingRow, startingColumn);
    }


    /**
     * Prints the Player's Leader Cards in the bottom right after the Development Card Slots
     *
     * @param leaderCards    The Player's cards
     * @param startingRow    The starting row where the Player's Leader Cards are going to be printed in the Personal Board
     * @param startingColumn The starting column where the Player's Leader Cards are going to be printed in the Personal Board
     */
    public void printLeaderCardsInPersonalBoard(List<LeaderCard> leaderCards, int startingRow, int startingColumn) {
        for (int i = 0; i < leaderCards.size(); i++) {
            cliUtils.printFigure("LeaderInPersonalBoard", startingRow, startingColumn + (16 * i));
            printLeaderNumber(startingRow, startingColumn + (16 * i), i + 1);
            isLeaderCardActive(leaderCards.get(i), startingRow, startingColumn + (16 * i));
        }
    }


    /**
     * Auxiliary method used to print the Leader Card number in the Personal Board view
     *
     * @param startingRow    The first row where the Cards will be printed
     * @param startingColumn The first column where the Cards will be printed
     * @param index          The number of the Card
     */
    private void printLeaderNumber(int startingRow, int startingColumn, int index) {
        cliUtils.setCursorPosition(startingRow + 2, startingColumn);

        pw.print("| Leader " + index + " |");
        pw.flush();
    }


    /**
     * Used to print the String under the Player's Leader Cards
     *
     * @param leaderCard     The card to be controlled
     * @param startingRow    The starting row where the Player's Leader Cards are going to be printed
     * @param startingColumn The starting column where the Player's Leader Cards are going to be printed
     */
    private void isLeaderCardActive(LeaderCard leaderCard, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 5, startingColumn + 1);

        if (leaderCard.isActive()) pw.print("  ACTIVE  ");
        else pw.print(" INACTIVE ");
        pw.flush();
    }


    /**
     * Prints the Player's Leader Cards in a new screen
     *
     * @param leaderCards    The Player's Leader Cards to print
     * @param startingRow    The starting row where the Player's Leader Cards are going to be printed
     * @param startingColumn The starting column where the Player's Leader Cards are going to be printed
     */
    public void displayPlayerLeaderCards(List<LeaderCard> leaderCards, int startingRow, int startingColumn) {
        cliUtils.printFigure("/titles/MyLeaderCardsTitle", startingRow, startingColumn + 20);

        leaderCardsCli.printMultipleLeaders(leaderCards, 15);

        //print the String ACTIVE or INACTIVE under each LeaderCard
        for (int i = 0; i < leaderCards.size(); i++)
            isLeaderCardActive(leaderCards.get(i), 32, leaderCardsCli.getPrintMultipleLeadersStartingColumn(leaderCards.size()) + 7 + (45 * i));

        cliUtils.pPCS("Press Enter to exit this view.", Color.WHITE, 40, startingColumn + 20);
    }


    /**
     * Prints the Player's Development Cards
     *
     * @param developmentSlots Development Cards slots of the Player
     * @param startingRow      The initial row of the slots
     * @param startingColumn   The initial column of the slots
     */
    public void displayDevelopmentCardsSlots(List<List<DevelopmentCard>> developmentSlots, int startingRow, int startingColumn) {
        for (int i = 0; i < 3; i++) {
            cliUtils.printFigure("DevelopmentCardSeparator", startingRow, startingColumn + (i * 36));
            if (developmentSlots.get(i).size() > 0)
                developmentCardsCli.printDevelopmentCard(developmentSlots.get(i).get(developmentSlots.get(i).size() - 1), startingRow, startingColumn + 6 + (35 * i) + i, developmentSlots.get(i).size());
        }
        cliUtils.printFigure("DevelopmentCardSeparator", startingRow, startingColumn + 108);
    }


    /**
     * If the Warehouse has more than 3 depots, prints the corresponding Leader Depots
     *
     * @param leaderDepots   Leader Depots of the warehouse
     * @param startingRow    The starting row where the Player's Leader Depots are going to be printed
     * @param startingColumn The starting column where the Player's Leader Depots are going to be printed
     */
    public void printLeaderDepots(List<LeaderDepot> leaderDepots, int startingRow, int startingColumn) {
        for (int i = 0; i < leaderDepots.size(); i++) {
            cliUtils.printFigure("LeaderDepot", startingRow, startingColumn + (i * 18));
            printLeaderDepotResources(leaderDepots.get(i), startingRow, startingColumn + (i * 18));
            cliUtils.pPCS("Leader Depot " + (i + 1), Color.GREY, startingRow + 5, startingColumn + (i * 18));
        }
    }


    /**
     * Prints the Resource stored in the Leader Depot
     *
     * @param leaderDepot    The Leader Depot to print
     * @param startingRow    The starting row where the Player's Leader Depots are going to be printed
     * @param startingColumn The starting column where the Player's Leader Depots are going to be printed
     */
    private void printLeaderDepotResources(LeaderDepot leaderDepot, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 2, startingColumn);

        if (leaderDepot.getResources().size() == 0)
            pw.print("|" + cliUtils.pCS(leaderDepot.getLeaderDepotResource().getName(), Color.GREY) + "|");
        else if (leaderDepot.getResources().size() == 1)
            pw.print("|     " + cliUtils.pCS("\u2588\u2588", leaderDepot.getLeaderDepotResource().getColor()) + "     |");
        else
            pw.print("|  " + cliUtils.pCS("\u2588\u2588", leaderDepot.getLeaderDepotResource().getColor()) + "    " +
                    cliUtils.pCS("\u2588\u2588", leaderDepot.getLeaderDepotResource().getColor()) + "  |");

        pw.flush();
    }


    /**
     * Prints the Player's available Productions
     *
     * @param productions    The Productions available for the Player
     * @param playerResource The Player's Resources
     * @return The selected Productions
     * @throws UndoOptionSelectedException The Player decides to quit from Production screen
     */
    public List<Production> displayProductionActivation(List<Production> productions, List<Resource> playerResource) throws UndoOptionSelectedException {
        cliUtils.cls();
        List<Integer> indexChoices = new ArrayList<>();

        cliUtils.printFigure("/titles/ActivateProductionTitle", 1, 18);
        printPlayerResources(playerResource, 32, 18);
        printProductionBooks(productions);

        do {
            printProductionQuestion(indexChoices.size() >= 1);

            try {
                int choice = getCorrectChoice(productions.size(), indexChoices.size() >= 1);
                if (!indexChoices.contains(choice)) indexChoices.add(choice);
                cliUtils.pPCS("Player's choices: " + indexChoices, Color.WHITE, 44, 18);
                cliUtils.clearLine(38, 18);
                cliUtils.clearLine(42, 75);
            } catch (ConfirmationException e) {
                break;
            } catch (UndoOptionSelectedException e) {
                throw new UndoOptionSelectedException();
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                cliUtils.pPCS("INCORRECT INDEX INSERTED!", Color.RED, 38, 18);
                cliUtils.clearLine(42, 75);
            }

        } while (indexChoices.size() < productions.size());

        return getSelectedProductions(productions, indexChoices);
    }


    /**
     * Creates a List of Productions selected by the Player
     *
     * @param productions The total Productions received from the Server
     * @param selections  The Production indexes selected by the Player
     * @return The List of selected Productions
     */
    private List<Production> getSelectedProductions(List<Production> productions, List<Integer> selections) {
        List<Production> productionsToReturn = new ArrayList<>();
        for (Integer i : selections) productionsToReturn.add(productions.get(i - 1));
        return productionsToReturn;
    }


    /**
     * Prints the questions to the Player
     *
     * @param printConfirm The Player can confirm the current selections
     */
    private void printProductionQuestion(boolean printConfirm) {
        cliUtils.pPCS("Enter 'u' if you want to exit from the Production selection screen.", Color.WHITE, 40, 18);
        if (printConfirm)
            cliUtils.pPCS("Enter 'c' to confirm selections.", Color.WHITE, 41, 18);
        cliUtils.pPCS("Enter the number of the Production you want to activate: ", Color.WHITE, 42, 18);
    }


    /**
     * Gets and parse correctly the input of the Player
     *
     * @param maxNumberOfChoices The maximum range of the index permitted
     * @param canConfirm         If there is 1 ore more choice the Player can confirm the actual choices
     * @return The index selected by the Player
     * @throws UndoOptionSelectedException The PLayer exits from the Production screen
     * @throws ConfirmationException       The Player confirm the current List of choices
     * @throws IndexOutOfBoundsException   The index selected is not correct
     */
    private int getCorrectChoice(int maxNumberOfChoices, boolean canConfirm) throws UndoOptionSelectedException, ConfirmationException, IndexOutOfBoundsException {
        Scanner in = new Scanner(System.in);
        String choice;
        choice = in.nextLine();

        if (choice.isEmpty()) throw new IndexOutOfBoundsException();
        if (choice.equals("u")) throw new UndoOptionSelectedException();
        if (canConfirm)
            if (choice.equals("c")) throw new ConfirmationException();
        if (ViewUtils.checkAsciiRange(choice.charAt(0))) throw new IndexOutOfBoundsException();

        int chosenIndex = Integer.parseInt(choice);
        if (chosenIndex <= 0 || chosenIndex > maxNumberOfChoices) throw new IndexOutOfBoundsException();

        return chosenIndex;
    }


    /**
     * Prints the Productions in the form of Production books
     *
     * @param productions The productions to print
     */
    private void printProductionBooks(List<Production> productions) {
        for (int i = 0; i < productions.size(); i++) {
            cliUtils.printFigure("ProductionBook", 15, 20 + (i * 35));
            developmentCardsCli.printProduction(productions.get(i).getProductionCost(), productions.get(i).getProductionReturn(), 10, 21 + (i * 35));
            cliUtils.pPCS("PRODUCTION  " + (i + 1), Color.WHITE, 25, 26 + (i * 35));
        }
    }


    /**
     * Prints the Resource Supply on screen
     *
     * @param resourceSupply The Resource Supply
     * @param startingRow    The row where the Resource Supply will be printed
     * @param startingColumn The column where the Resource Supply will be printed
     */
    public void displayResourceSupply(ResourceSupply resourceSupply, int startingRow, int startingColumn) {
        cliUtils.clns();

        cliUtils.printFigure("/titles/ResourceSupplyTitle", startingRow, startingColumn);
        cliUtils.printFigure("ResourceSupply", startingRow + 19, startingColumn + 37);
        try {
            for (int i = 0; i <= 3; i++)
                printResourceSupplyResources(resourceSupply.grabResources(i, 1).get(0).getColor(), startingRow, startingColumn + (i * 22));
        } catch (ResourceSupplySlotOutOfBoundsException e) {
            e.printStackTrace();
        }
    }


    /**
     * Prints the Resources in the Resource Supply Slots
     * Please note that these are not a real representation, since the Resources are infinite
     *
     * @param color          The color of the Resource Supply Slot
     * @param startingRow    The row where the Resource Supply Slot Resources will be printed
     * @param startingColumn The column where the Resource Supply Slot Resources will be printed
     */
    private void printResourceSupplyResources(Color color, int startingRow, int startingColumn) {
        for (int i = 0; i < 3; i++) cliUtils.pPCS("\u25A0", color, startingRow + 22, startingColumn + 41 + (i * 6));
        for (int i = 0; i < 3; i++) cliUtils.pPCS("\u25A0", color, startingRow + 24, startingColumn + 41 + (i * 6));
        for (int i = 0; i < 6; i++) cliUtils.pPCS("\u25A0", color, startingRow + 26, startingColumn + 41 + (i * 3));
        for (int i = 0; i < 4; i++) cliUtils.pPCS("\u25A0", color, startingRow + 28, startingColumn + 41 + (i * 5));
    }


    /**
     * Prints the Action Tokens
     *
     * @param actionTokens The action Tokens to print
     */
    public void displayActionTokens(List<ActionToken> actionTokens) {
        cliUtils.clns();

        cliUtils.printFigure("/titles/ActionTokensTitle", 1, 48);
        cliUtils.printFigure("/actiontokens/" + actionTokens.get(0).toString(), 20, 141);
        cliUtils.pPCS("ACTIVATED  TOKEN", Color.WHITE, 34, 148);
        printTokenStack(actionTokens.size());
    }


    /**
     * Prints the stack of covered Tokens
     *
     * @param stackSize The number of Tokens that are not used yet
     */
    private void printTokenStack(int stackSize) {
        if (stackSize > 1) cliUtils.printFigure("/actiontokenborders/ActionTokenBorder" + (stackSize - 1), 20, 67);
    }
}
