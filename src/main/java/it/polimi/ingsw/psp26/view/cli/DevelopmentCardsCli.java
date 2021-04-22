package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGridCell;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DevelopmentCardsCli {

    private final PrintWriter pw;
    private final CliUtils cliUtils;

    public DevelopmentCardsCli(PrintWriter pw) {
        this.pw = pw;
        this.cliUtils = new CliUtils(pw);
    }


    /**
     * Prints the Development Card Grid
     *
     * @param developmentGrid The Development Grid to print
     */
    public void displayDevelopmentGrid(DevelopmentGrid developmentGrid) {
        for (int i = 0; i < 3; i++) {
            printDevelopmentGridCard(developmentGrid.getDevelopmentGridCell(i, 0), 2 + (19 * i), 5);
            printDevelopmentGridCard(developmentGrid.getDevelopmentGridCell(i, 1), 2 + (19 * i), 36);
            printDevelopmentGridCard(developmentGrid.getDevelopmentGridCell(i, 2), 2 + (19 * i), 67);
            printDevelopmentGridCard(developmentGrid.getDevelopmentGridCell(i, 3), 2 + (19 * i), 98);
        }

        cliUtils.printFigure("/titles/GridTitle", 10, 135);
    }


    /**
     * Printrint DevelopmentCards.
     *
     * @param developmentCard The desired card to print
     * @param startingRow     The row where the Card will be printed
     * @param startingColumn  The column where the Card will be printed
     * @param cardStackSize   How many cards are on the top of each other
     */
    public void printDevelopmentCard(DevelopmentCard developmentCard, int startingRow, int startingColumn, int cardStackSize) {
        printDevelopmentCardBorder(startingRow, startingColumn, cardStackSize);
        cliUtils.printFigure("DevelopmentCard", startingRow, startingColumn);
        printDevelopmentCardRequirements(developmentCard, startingRow, startingColumn);
        printCardColor(developmentCard, startingRow, startingColumn, true);
        printDevelopmentCardLevel(developmentCard, startingRow, startingColumn);
        printCardColor(developmentCard, startingRow, startingColumn, false);
        printProduction(developmentCard.getProduction().getProductionCost(), developmentCard.getProduction().getProductionReturn(), startingRow, startingColumn);
        printCardVictoryPoints(developmentCard, startingRow, startingColumn);

        pw.flush();
    }


    /**
     * Prints one border for each Development Card under the top one
     *
     * @param startingRow    The row where the Card will be printed
     * @param startingColumn The column where the Card will be printed
     * @param cardStackSize  How many cards are on the top of each othe
     */
    private void printDevelopmentCardBorder(int startingRow, int startingColumn, int cardStackSize) {
        if (cardStackSize > 1)
            cliUtils.printFigure("/developmentcardborders/DevelopmentCardBorder" + (cardStackSize - 1), startingRow, startingColumn);
    }


    /**
     * Prints the Card color on top and on bottom of the level dots
     *
     * @param developmentCard Get the color of this Card
     * @param startingRow     The row where the Card will be printed
     * @param startingColumn  The column where the Card will be printed
     * @param upOrDown        If true prints the upper section, if false print the lower section
     */
    private void printCardColor(DevelopmentCard developmentCard, int startingRow, int startingColumn, boolean upOrDown) {
        if (upOrDown) {
            cliUtils.setCursorPosition(startingRow + 2, startingColumn);
            pw.print("| " + cliUtils.pCS(".-----------------.", developmentCard.getDevelopmentCardType().getColor()) + " |");
        } else {
            cliUtils.setCursorPosition(startingRow + 4, startingColumn);
            pw.print("| " + cliUtils.pCS("`-----------------'", developmentCard.getDevelopmentCardType().getColor()) + " |");
        }

        pw.flush();
    }


    /**
     * Prints the Development Card requirements line
     *
     * @param developmentCard The Card to print
     * @param startingRow     The starting row where the Development Card is going to be printed
     * @param startingColumn  The starting column where the Development Card is going to be printed
     */
    private void printDevelopmentCardRequirements(DevelopmentCard developmentCard, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 1, startingColumn);

        StringBuilder s = new StringBuilder("| ");
        int remainingSpace = 20;

        for (Map.Entry<Resource, Integer> entry : developmentCard.getCost().entrySet()) {
            s.append(entry.getValue()).append(cliUtils.pCS("\u25A0", entry.getKey().getColor())).append("  ");
            remainingSpace -= 4;
        }
        s.append(cliUtils.hSpace(remainingSpace)).append("|");

        pw.print(s);
        pw.flush();
    }


    /**
     * Prints the Development Card level line in the correct color
     *
     * @param developmentCard The Card to print
     * @param startingRow     The starting row where the Development Card is going to be printed
     * @param startingColumn  The starting column where the Development Card is going to be printed
     */
    private void printDevelopmentCardLevel(DevelopmentCard developmentCard, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 3, startingColumn);

        String s = "| ";

        s = s + printLevelDots(developmentCard.getDevelopmentCardType().getLevel().getLevelNumber(), developmentCard.getDevelopmentCardType().getColor());
        s = s + cliUtils.hSpace(8 - (2 * developmentCard.getDevelopmentCardType().getLevel().getLevelNumber())) + "lvl   " + cliUtils.hSpace(6 - (2 * developmentCard.getDevelopmentCardType().getLevel().getLevelNumber()));
        s = s + printLevelDots(developmentCard.getDevelopmentCardType().getLevel().getLevelNumber(), developmentCard.getDevelopmentCardType().getColor());
        s = s + "|";

        pw.print(s);
        pw.flush();
    }


    /**
     * Prints a Card level dots in the correct Color
     *
     * @param level The level number of the Card
     * @param color The Color of the Card
     * @return A formatted String of the Level dots
     */
    private String printLevelDots(int level, Color color) {
        return cliUtils.pCS("\u25CF ", color).repeat(Math.max(0, level));
    }


    /**
     * Generic method that prints a Production. May be used with every Production Object
     *
     * @param productionCost   The cost of the Production
     * @param productionReturn The Resources returned by the Production
     * @param startingRow      The row where the Production is gonna be printed
     * @param startingColumn   The column where the Production is gonna be printed
     */
    public void printProduction(Map<Resource, Integer> productionCost, Map<Resource, Integer> productionReturn, int startingRow, int startingColumn) {
        for (int i = 0; i < 3; i++)
            printProductionLines(productionCost, productionReturn, i, startingRow, startingColumn);
    }


    /**
     * Used to print the Production section of a DevelopmentCard
     * It calls three auxiliary methods in order to get a clean print of the Resources
     *
     * @param productionCost   The cost of the Production
     * @param productionReturn The Resources returned by the Production
     * @param line             Print the first, second or third line of the Production section
     * @param startingRow      The starting row where the Development Card is going to be printed
     * @param startingColumn   The starting column where the Development Card is going to be printed
     */
    private void printProductionLines(Map<Resource, Integer> productionCost, Map<Resource, Integer> productionReturn, int line, int startingRow, int startingColumn) {
        List<Resource> requiredResources = new ArrayList<>();
        List<Resource> producedResources = new ArrayList<>();
        List<Integer> numberOfRequiredResources = new ArrayList<>();
        List<Integer> numberOfProducedResources = new ArrayList<>();

        for (Map.Entry<Resource, Integer> entry : productionCost.entrySet()) { //TODO magari una gestione miglore delle HashMap
            requiredResources.add(entry.getKey());
            numberOfRequiredResources.add(entry.getValue());
        }

        for (Map.Entry<Resource, Integer> entry : productionReturn.entrySet()) {
            producedResources.add(entry.getKey());
            numberOfProducedResources.add(entry.getValue());
        }

        if (line == 0)
            printFirstProductionRow(requiredResources, numberOfRequiredResources, producedResources, numberOfProducedResources, startingRow, startingColumn);
        else if (line == 1)
            printSecondProductionRow(requiredResources, numberOfRequiredResources, producedResources, numberOfProducedResources, startingRow, startingColumn);
        else
            printThirdProductionRow(requiredResources, numberOfRequiredResources, producedResources, numberOfProducedResources, startingRow, startingColumn);
    }


    /**
     * Prints the first line of the Development Card production section
     *
     * @param requiredResources         Resources type required to perform the production action
     * @param numberOfRequiredResources Resources number required to perform the production action
     * @param producedResources         Resources type produced by the production action
     * @param numberOfProducedResources Resources number produced by the production action
     * @param startingRow               The starting row where the Development Card is going to be printed
     * @param startingColumn            The starting column where the Development Card is going to be printed
     */
    private void printFirstProductionRow(List<Resource> requiredResources, List<Integer> numberOfRequiredResources, List<Resource> producedResources, List<Integer> numberOfProducedResources, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 7, startingColumn + 5);

        String s = "";

        if (requiredResources.size() == 1) s = s + cliUtils.hSpace(10);
        else
            s = s + numberOfRequiredResources.get(0) + cliUtils.pCS(" \u25A0", requiredResources.get(0).getColor()) + cliUtils.hSpace(7);

        if (producedResources.size() == 1) s = s + cliUtils.hSpace(5);
        else
            s = s + numberOfProducedResources.get(0) + cliUtils.pCS(" \u25A0  ", producedResources.get(0).getColor());

        pw.print(s);
        pw.flush();
    }


    /**
     * Prints the second line of the Development Card production section
     *
     * @param requiredResources         Resources type required to perform the production action
     * @param numberOfRequiredResources Resources number required to perform the production action
     * @param producedResources         Resources type produced by the production action
     * @param numberOfProducedResources Resources number produced by the production action
     * @param startingRow               The starting row where the Development Card is going to be printed
     * @param startingColumn            The starting column where the Development Card is going to be printed
     */
    private void printSecondProductionRow(List<Resource> requiredResources, List<Integer> numberOfRequiredResources, List<Resource> producedResources, List<Integer> numberOfProducedResources, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 8, startingColumn + 5);

        String s = "";

        if (requiredResources.size() == 1)
            s = s + numberOfRequiredResources.get(0) + cliUtils.pCS(" \u25A0", requiredResources.get(0).getColor()) + "  -->  ";
        else s = s + "     -->  ";

        if (producedResources.size() == 1)
            s = s + numberOfProducedResources.get(0) + cliUtils.pCS(" \u25A0  ", producedResources.get(0).getColor());
        else if (producedResources.size() == 3)
            s = s + numberOfProducedResources.get(1) + cliUtils.pCS(" \u25A0  ", producedResources.get(1).getColor());

        pw.print(s);
        pw.flush();
    }


    /**
     * Prints the third line of the Development Card production section
     *
     * @param requiredResources         Resources type required to perform the production action
     * @param numberOfRequiredResources Resources number required to perform the production action
     * @param producedResources         Resources type produced by the production action
     * @param numberOfProducedResources Resources number produced by the production action
     * @param startingRow               The starting row where the Development Card is going to be printed
     * @param startingColumn            The starting column where the Development Card is going to be printed
     */
    private void printThirdProductionRow(List<Resource> requiredResources, List<Integer> numberOfRequiredResources, List<Resource> producedResources, List<Integer> numberOfProducedResources, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 9, startingColumn + 5);

        String s = "";

        if (requiredResources.size() == 2)
            s = s + numberOfRequiredResources.get(1) + cliUtils.pCS(" \u25A0", requiredResources.get(1).getColor()) + cliUtils.hSpace(7);
        else s = s + cliUtils.hSpace(10);

        if (producedResources.size() == 2)
            s = s + numberOfProducedResources.get(1) + cliUtils.pCS(" \u25A0  ", producedResources.get(1).getColor());
        else if (producedResources.size() == 3)
            s = s + numberOfProducedResources.get(2) + cliUtils.pCS(" \u25A0  ", producedResources.get(2).getColor());

        pw.print(s);
        pw.flush();
    }


    /**
     * Print the Development Card's Victory Points
     *
     * @param developmentCard The Card where to get the VictoryPoints
     * @param startingRow     The starting row where the Development Card is going to be printed
     * @param startingColumn  The starting column where the Development Card is going to be printed
     */
    private void printCardVictoryPoints(DevelopmentCard developmentCard, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 12, startingColumn);

        String s = "";
        s = s + "|" + cliUtils.hSpace(10) + developmentCard.getVictoryPoints() + cliUtils.hSpace(10 - (developmentCard.getVictoryPoints() / 10)) + "|";

        pw.print(s);
        pw.flush();
    }


    /**
     * Used to print the Development Cards in the Development Grid
     *
     * @param developmentGridCell If the cell contains one or more cards, print the one on the top. Otherwise, leave a blank space
     * @param startingRow         The starting row where the Development Card is going to be printed
     * @param startingColumn      The starting column where the Development Card is going to be printed
     */
    private void printDevelopmentGridCard(DevelopmentGridCell developmentGridCell, int startingRow, int startingColumn) {
        if (!developmentGridCell.isEmpty())
            printDevelopmentCard(developmentGridCell.getFirstCard(), startingRow, startingColumn, developmentGridCell.getDevelopmentCardsSize());
    }

}
