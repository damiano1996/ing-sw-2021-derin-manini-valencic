package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.exceptions.ResourceSupplySlotOutOfBoundsException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;

import java.io.PrintWriter;
import java.util.List;

/**
 * Class that contains all the methods related to display the PersonalBoard.
 */
public class PersonalBoardCli {

    private final PrintWriter pw;
    private final CliUtils cliUtils;
    private final LeaderCardsCli leaderCardsCli;
    private final DevelopmentCardsCli developmentCardsCli;
    private final FaithTrackCli faithTrackCli;
    private final DepotCli depotCli;

    /**
     * Constructor of the class.
     * It creates a new PrintWriter, a new CliUtils, a new LeaderCardsCli, a new DevelopmentCardsCli,
     * a new FaithTrackCli and a new DepotCli to use.
     *
     * @param pw the PrintWriter to use
     */
    public PersonalBoardCli(PrintWriter pw) {
        this.pw = pw;
        this.cliUtils = new CliUtils(pw);
        this.leaderCardsCli = new LeaderCardsCli(pw);
        this.developmentCardsCli = new DevelopmentCardsCli(pw);
        this.faithTrackCli = new FaithTrackCli(pw);
        this.depotCli = new DepotCli(pw);
    }


    /**
     * Prints the PersonalBoard of a given Player.
     * To display each element, one specific method is called for each figure to print.
     *
     * @param player            the player who's Personal Board is gonna be printed
     * @param isMultiplayerMode tells if the Match is Single or MultiPlayer (used to print the correct FaithTrack)
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
     * Prints the Inkwell.
     *
     * @param isPrintable    tells the method if the Player has the Inkwell
     * @param startingRow    the starting row where the Inkwell is going to be printed
     * @param startingColumn the starting column where the Inkwell is going to be printed
     */
    public void displayInkwell(boolean isPrintable, int startingRow, int startingColumn) {
        if (isPrintable) cliUtils.printFigure("Inkwell", startingRow, startingColumn);
    }


    /**
     * Prints the Player's LeaderCards in the bottom right after the DevelopmentCard Slots.
     *
     * @param leaderCards    the Player's cards
     * @param startingRow    the starting row where the Player's LeaderCards are going to be printed in the PersonalBoard
     * @param startingColumn the starting column where the Player's LeaderCards are going to be printed in the PersonalBoard
     */
    public void printLeaderCardsInPersonalBoard(List<LeaderCard> leaderCards, int startingRow, int startingColumn) {
        for (int i = 0; i < leaderCards.size(); i++) {
            cliUtils.printFigure("LeaderInPersonalBoard", startingRow, startingColumn + (16 * i));
            printLeaderNumber(startingRow, startingColumn + (16 * i), i + 1);
            isLeaderCardActive(leaderCards.get(i), startingRow, startingColumn + (16 * i));
        }
    }


    /**
     * Auxiliary method used to print the LeaderCard number in the PersonalBoard view.
     *
     * @param startingRow    the first row where the LeaderCards will be printed
     * @param startingColumn the first column where the LeaderCards will be printed
     * @param index          the number of the LeaderCard
     */
    private void printLeaderNumber(int startingRow, int startingColumn, int index) {
        cliUtils.setCursorPosition(startingRow + 2, startingColumn);

        pw.print("| Leader " + index + " |");
        pw.flush();
    }


    /**
     * Used to print the String under the Player's LeaderCards.
     *
     * @param leaderCard     the LeaderCard to be controlled
     * @param startingRow    the starting row where the Player's LeaderCards are going to be printed
     * @param startingColumn the starting column where the Player's LeaderCards are going to be printed
     */
    private void isLeaderCardActive(LeaderCard leaderCard, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 5, startingColumn + 1);

        if (leaderCard.isActive()) pw.print("  ACTIVE  ");
        else pw.print(" INACTIVE ");
        pw.flush();
    }


    /**
     * Prints the Player's LeaderCards in a new screen.
     *
     * @param leaderCards    the Player's LeaderCards to print
     * @param startingRow    the starting row where the Player's LeaderCards are going to be printed
     * @param startingColumn the starting column where the Player's LeaderCards are going to be printed
     */
    public void displayPlayerLeaderCards(List<LeaderCard> leaderCards, int startingRow, int startingColumn) {
        cliUtils.clns();
        cliUtils.printFigure("/titles/MyLeaderCardsTitle", startingRow, startingColumn + 20);

        leaderCardsCli.printMultipleLeaders(leaderCards, 15);

        //print the String ACTIVE or INACTIVE under each LeaderCard
        for (int i = 0; i < leaderCards.size(); i++)
            isLeaderCardActive(
                    leaderCards.get(i),
                    32,
                    leaderCardsCli.getPrintMultipleLeadersStartingColumn(leaderCards.size()) + 7 + (45 * i)
            );
    }


    /**
     * Prints the Player's DevelopmentCards.
     *
     * @param developmentSlots DevelopmentCards slots of the Player
     * @param startingRow      the initial row of the slots
     * @param startingColumn   the initial column of the slots
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
     * If the Warehouse has more than 3 depots, prints the corresponding LeaderDepots.
     *
     * @param leaderDepots   LeaderDepots of the Warehouse
     * @param startingRow    the starting row where the Player's LeaderDepots are going to be printed
     * @param startingColumn the starting column where the Player's LeaderDepots are going to be printed
     */
    public void printLeaderDepots(List<LeaderDepot> leaderDepots, int startingRow, int startingColumn) {
        for (int i = 0; i < leaderDepots.size(); i++) {
            cliUtils.printFigure("LeaderDepot", startingRow, startingColumn + (i * 18));
            printLeaderDepotResources(leaderDepots.get(i), startingRow, startingColumn + (i * 18));
            cliUtils.pPCS("Leader Depot " + (i + 1), Color.GREY, startingRow + 5, startingColumn + (i * 18));
        }
    }


    /**
     * Prints the Resource stored in the LeaderDepot.
     *
     * @param leaderDepot    the LeaderDepot to print
     * @param startingRow    the starting row where the Player's LeaderDepots are going to be printed
     * @param startingColumn the starting column where the Player's LeaderDepots are going to be printed
     */
    private void printLeaderDepotResources(LeaderDepot leaderDepot, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 2, startingColumn);

        if (leaderDepot.getResources().size() == 0)
            pw.print("|" + cliUtils.pCS(cliUtils.centerString(12, leaderDepot.getLeaderDepotResource().getName()), Color.GREY));
        else if (leaderDepot.getResources().size() == 1)
            pw.print("|     " + cliUtils.pCS("\u2588\u2588", leaderDepot.getLeaderDepotResource().getColor()) + "     |");
        else
            pw.print("|  " + cliUtils.pCS("\u2588\u2588", leaderDepot.getLeaderDepotResource().getColor()) + "    " +
                    cliUtils.pCS("\u2588\u2588", leaderDepot.getLeaderDepotResource().getColor()) + "  |");

        pw.flush();
    }


    /**
     * Prints the Player's available Productions.
     *
     * @param productions    the Productions available for the Player
     * @param playerResource the Player's Resources
     */
    public void displayProductionActivation(List<Production> productions, List<Resource> playerResource) {
        cliUtils.clns();

        cliUtils.printFigure("/titles/ActivateProductionTitle", 1, 18);
        cliUtils.printPlayerResources(playerResource, 29, 18);
        printProductionBooks(productions);
    }


    /**
     * Prints the Productions in the form of Production books.
     *
     * @param productions the productions to print
     */
    private void printProductionBooks(List<Production> productions) {
        for (int i = 0; i < productions.size(); i++) {
            cliUtils.printFigure("ProductionBook", 15, 20 + (i * 35));
            developmentCardsCli.printProduction(productions.get(i).getProductionCost(), productions.get(i).getProductionReturn(), 10, 21 + (i * 35));
            cliUtils.pPCS("PRODUCTION  " + (i + 1), Color.WHITE, 25, 26 + (i * 35));
        }
    }


    /**
     * Prints the ResourceSupply on screen.
     *
     * @param resourceSupply   the ResourceSupply
     * @param resourcesToPrint the Resources to print in the ResourceSupply
     * @param startingRow      the row where the ResourceSupply will be printed
     * @param startingColumn   the column where the ResourceSupply will be printed
     */
    public void displayResourceSupply(ResourceSupply resourceSupply, List<Resource> resourcesToPrint, int startingRow, int startingColumn) {
        cliUtils.clns();

        cliUtils.printFigure("/titles/ResourceSupplyTitle", startingRow, startingColumn);
        cliUtils.printFigure("ResourceSupply", startingRow + 13, startingColumn + 37);

        try {
            int slotNumber = 1;
            for (int i = 0; i <= 3; i++) {
                Resource resourceSlotType = resourceSupply.grabResources(i, 1).get(0);
                if (resourcesToPrint.contains(resourceSlotType)) {
                    printResourceSupplyResources(resourceSlotType.getColor(), startingRow, startingColumn + (i * 22));
                    cliUtils.pPCS("SLOT " + slotNumber, Color.WHITE, startingRow + 27, startingColumn + 45 + (i * 22));
                    slotNumber++;
                }
            }
        } catch (ResourceSupplySlotOutOfBoundsException ignored) {
        }
    }


    /**
     * Prints the Resources in the ResourceSupply Slots.
     * Please note that these are not a real representation, since the Resources are infinite.
     *
     * @param color          the color of the ResourceSupply Slot
     * @param startingRow    the row where the ResourceSupply Slot Resources will be printed
     * @param startingColumn the column where the ResourceSupply Slot Resources will be printed
     */
    private void printResourceSupplyResources(Color color, int startingRow, int startingColumn) {
        for (int i = 0; i < 3; i++) cliUtils.pPCS("\u25A0", color, startingRow + 16, startingColumn + 41 + (i * 6));
        for (int i = 0; i < 3; i++) cliUtils.pPCS("\u25A0", color, startingRow + 18, startingColumn + 41 + (i * 6));
        for (int i = 0; i < 6; i++) cliUtils.pPCS("\u25A0", color, startingRow + 20, startingColumn + 41 + (i * 3));
        for (int i = 0; i < 4; i++) cliUtils.pPCS("\u25A0", color, startingRow + 22, startingColumn + 41 + (i * 5));
    }


    /**
     * Prints the ActionTokens.
     *
     * @param actionTokens the ActionTokens to print
     */
    public void displayActionTokens(List<ActionToken> actionTokens) {
        cliUtils.clns();

        cliUtils.printFigure("/titles/ActionTokensTitle", 1, 48);
        cliUtils.printFigure("/actiontokens/" + actionTokens.get(0).toString(), 20, 141);
        cliUtils.pPCS("ACTIVATED  TOKEN", Color.WHITE, 34, 148);
        printTokenStack(actionTokens.size());
        cliUtils.vSpace(5);
    }


    /**
     * Prints the stack of covered ActionTokens
     *
     * @param stackSize the number of ActionTokens that are not used yet
     */
    private void printTokenStack(int stackSize) {
        if (stackSize > 1) cliUtils.printFigure("/actiontokenborders/ActionTokenBorder" + (stackSize - 1), 20, 67);
    }

}
