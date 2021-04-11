package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;

import java.io.PrintWriter;
import java.util.List;

public class PersonalBoardCli {

    private final CliUtils cliUtils;
    private final LeaderCardsCli leaderCardsCli;
    private final DevelopmentCardsCli developmentCardsCli;
    private final FaithTrackCli faithTrackCli;
    private final DepotCli depotCli;
    private final PrintWriter pw;

    public PersonalBoardCli() {
        this.pw = new PrintWriter(System.out);
        this.cliUtils = new CliUtils();
        this.faithTrackCli = new FaithTrackCli();
        this.developmentCardsCli = new DevelopmentCardsCli();
        this.leaderCardsCli = new LeaderCardsCli();
        this.depotCli = new DepotCli();
    }

    /**
     * Prints the Personal Board of a given player
     *
     * @param player The player who's Personal Board is gonna be printed
     */
    public void displayPersonalBoard(Player player) {

        faithTrackCli.displayFaithTrack(player.getPersonalBoard().getFaithTrack(), 3, 10);

        displayInkwell(player.isInkwell(), 5, 190);

        depotCli.printWarehouse(player.getPersonalBoard().getWarehouseDepots(), 17, 13);

        depotCli.displayStrongbox(player.getPersonalBoard().getStrongbox(), 30, 3);

        displayDevelopmentCardsSlots(player.getPersonalBoard().getDevelopmentCardsSlots(), 30, 70);

        printLeaderCardsInPersonalBoard(player.getLeaderCards(), 38, 195);

        printLeaderDepots(player.getPersonalBoard().getWarehouseDepots(), 46, 7);
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

        for (int i = 0; i < leaderCards.size(); i++)
            leaderCardsCli.printLeader(leaderCards.get(i), startingRow + 10, startingColumn + 70 + (65 * i));
        for (int i = 0; i < leaderCards.size(); i++) isLeaderCardActive(leaderCards.get(i), 25, 78 + (65 * i));

        cliUtils.vSpace(5);
        cliUtils.pw.println("Press Enter to exit this view.");

        cliUtils.pw.flush();
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
        if (depots.size() > 3)
            for (int i = 3; i < depots.size(); i++) {
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
            pw.print("|" + cliUtils.pCS(leaderDepot.getDepotResource().getName(), Color.GREY) + "|");
        else if (leaderDepot.getResources().size() == 1)
            pw.print("|     " + cliUtils.pCS("\u2588\u2588", leaderDepot.getDepotResource().getColor()) + "     |");
        else
            pw.print("|  " + cliUtils.pCS("\u2588\u2588", leaderDepot.getDepotResource().getColor()) + "    " + cliUtils.pCS("\u2588\u2588", leaderDepot.getDepotResource().getColor()) + "  |");

        pw.flush();
    }

}
