package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.exceptions.ResourceSupplySlotOutOfBoundsException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;

import java.io.PrintWriter;
import java.util.List;

import static it.polimi.ingsw.psp26.model.personalboard.Warehouse.NUMBER_OF_DEFAULT_DEPOTS;

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

        faithTrackCli.displayFaithTrack(player.getPersonalBoard().getFaithTrack(), 3, 10, isMultiplayerMode);

        displayInkwell(player.isInkwell(), 5, 190);

        depotCli.printWarehouse(player.getPersonalBoard().getWarehouse(), 17, 13);

        depotCli.displayStrongbox(player.getPersonalBoard().getStrongbox(), 30, 3);

        displayDevelopmentCardsSlots(player.getPersonalBoard().getDevelopmentCardsSlots(), 30, 70);

        printLeaderCardsInPersonalBoard(player.getLeaderCards(), 38, 195);

        printLeaderDepots(player.getPersonalBoard().getWarehouse().getDepots(), 46, 7);
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
        cliUtils.printFigure("MyLeaderCardsTitle", startingRow, startingColumn + 20);

        printLeaders(leaderCards, startingRow, startingColumn);
        for (int i = 0; i < leaderCards.size(); i++) isLeaderCardActive(leaderCards.get(i), 27, 78 + (65 * i));

        cliUtils.setCursorBottomLeft();
        pw.print("Press Enter to exit this view.");

        pw.flush();
    }

    /**
     * Prints the given Leader Cards
     *
     * @param leaderCards    The Leader Cards to print
     * @param startingRow    The first row where the cards are going to be printed
     * @param startingColumn The first column where the cards are going to be printed
     */
    public void printLeaders(List<LeaderCard> leaderCards, int startingRow, int startingColumn) {
        for (int i = 0; i < leaderCards.size(); i++)
            leaderCardsCli.printLeader(leaderCards.get(i), startingRow + 10, startingColumn + 70 + (65 * i));
        for (int i = 0; i < leaderCards.size(); i++)
            cliUtils.pPCS("LEADER  #" + (i + 1), Color.WHITE, 30, 79 + (65 * i));
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
     * @param depots         The total Warehouse Depots
     * @param startingRow    The starting row where the Player's Leader Depots are going to be printed
     * @param startingColumn The starting column where the Player's Leader Depots are going to be printed
     */
    public void printLeaderDepots(List<Depot> depots, int startingRow, int startingColumn) { //forse c'è una soluzione più elegante
        if (depots.size() > NUMBER_OF_DEFAULT_DEPOTS)
            for (int i = NUMBER_OF_DEFAULT_DEPOTS; i < depots.size(); i++) {
                cliUtils.printFigure("LeaderDepot", startingRow, startingColumn + ((i - 3) * 18));
                printLeaderDepotResources((LeaderDepot) depots.get(i), startingRow, startingColumn + ((i - 3) * 18));
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
     * Prints the Player's available production actions
     *
     * @param productions The Productions available for the Player
     */
    public void displayProductionActivation(List<Production> productions) {
        cliUtils.cls();

        cliUtils.printFigure("ActivateProductionTitle", 1, 18);

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
        cliUtils.cls();

        cliUtils.printFigure("ResourceSupplyTitle", startingRow, startingColumn);
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
        cliUtils.cls();

        cliUtils.printFigure("ActionTokensTitle", 1, 48);
        cliUtils.printFigure(actionTokens.get(0).getTokenName(), 20, 141);
        cliUtils.pPCS("ACTIVATED  TOKEN", Color.WHITE, 34, 148);
        printTokenStack(actionTokens.size());
    }

    /**
     * Prints the stack of covered Tokens
     *
     * @param stackSize The number of Tokens that are not used yet
     */
    private void printTokenStack(int stackSize) {
        if (stackSize > 1) cliUtils.printFigure("ActionTokenBorder" + (stackSize - 1), 20, 67);
    }
}
