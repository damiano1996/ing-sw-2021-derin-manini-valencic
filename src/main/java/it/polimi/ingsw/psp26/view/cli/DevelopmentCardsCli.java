package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.UndoOptionSelectedException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGridCell;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.io.PrintWriter;
import java.util.*;

public class DevelopmentCardsCli {

    private final PrintWriter pw;
    private final CliUtils cliUtils;
    private final Scanner in;

    public DevelopmentCardsCli(PrintWriter pw) {
        this.pw = pw;
        this.cliUtils = new CliUtils(pw);
        this.in = new Scanner(System.in);
    }


    /**
     * Prints the Development Card Grid
     *
     * @param developmentCardsGrid The Development Grid to print
     */
    public void displayDevelopmentGrid(DevelopmentCardsGrid developmentCardsGrid) {
        for (int i = 0; i < 3; i++) {
            printDevelopmentGridCard(developmentCardsGrid.getDevelopmentGridCell(i, 0), 2 + (19 * i), 5);
            printDevelopmentGridCard(developmentCardsGrid.getDevelopmentGridCell(i, 1), 2 + (19 * i), 36);
            printDevelopmentGridCard(developmentCardsGrid.getDevelopmentGridCell(i, 2), 2 + (19 * i), 67);
            printDevelopmentGridCard(developmentCardsGrid.getDevelopmentGridCell(i, 3), 2 + (19 * i), 98);
        }

        cliUtils.printFigure("/titles/GridTitle", 10, 135);
    }


    /**
     * Prints a DevelopmentCard
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
     * @param cardStackSize  How many cards are on the top of each other
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

        for (Map.Entry<Resource, Integer> entry : productionCost.entrySet()) {
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


    /**
     * Display the Grid selection screen
     *
     * @param developmentCardsGrid The Development Grid to display
     * @param playerResources      The Player's Resources
     * @return The chosen Development Card
     * @throws UndoOptionSelectedException The Player decided to quit from the DevelopmentGrid selection screen
     */
    public DevelopmentCard displayDevelopmentCardSelection(DevelopmentCardsGrid developmentCardsGrid, List<Resource> playerResources) throws UndoOptionSelectedException {
        String cardLevel = "";
        String cardColor = "";
        boolean isCardChosen = false;
        boolean printErrorString = false;

        cliUtils.cls();

        while (!isCardChosen) {

            displayDevelopmentGrid(developmentCardsGrid);
            cliUtils.printPlayerResources(playerResources, 39, 135);

            if (printErrorString)
                cliUtils.pPCS("THE DESIRED CARD IS NOT AVAILABLE! Please try again", Color.RED, 30, 135);
            printErrorString = false;

            cardLevel = askForLevelAndColor("Enter the card Level [first - second - third]: ", true);
            cardColor = askForLevelAndColor("Enter the card Color [green - blue - yellow - purple]: ", false);

            isCardChosen = isCardAvailable(developmentCardsGrid, cardLevel, cardColor);
            if (!isCardChosen) {
                printErrorString = true;
                cliUtils.clearLine(33, 198);
            }

        }

        return getSelectedDevelopmentCard(developmentCardsGrid, cardLevel, cardColor);
    }


    /**
     * Asks the Player the Level and the Color of the desired Development Card
     *
     * @param stringToDisplay A message to inform the Player the String to enter
     * @param levelOrColor    True if a Level is entered, false if a Color is entered
     * @return The Level/Color chosen by the Player
     * @throws UndoOptionSelectedException The Player decided to quit from the DevelopmentGrid selection screen
     */
    private String askForLevelAndColor(String stringToDisplay, boolean levelOrColor) throws UndoOptionSelectedException {
        boolean correctInputInserted = false;
        String input;

        do {
            cliUtils.pPCS("Enter 'u' (undo) if you want to exit from DevelopmentGrid screen.", Color.WHITE, 32, 135);
            cliUtils.pPCS(stringToDisplay, Color.WHITE, 33, 135);
            input = in.nextLine();

            if (input.equals("u")) throw new UndoOptionSelectedException();

            if (isInputCorrect(input, levelOrColor)) correctInputInserted = true;
            else {
                cliUtils.pPCS("WRONG INPUT INSERTED! Please try again", Color.RED, 30, 135);
                cliUtils.clearLine(33, 198);
            }
        } while (!correctInputInserted);

        cliUtils.clearLine(30, 135);
        cliUtils.setCursorPosition(33, 198);

        return input;
    }


    /**
     * Checks if the input inserted by the Player is correct in terms of Levels or Colors
     *
     * @param input        The String entered by the Player
     * @param levelOrColor True if Level is checked, false if Color is checked
     * @return True if the input is correct, false otherwise
     */
    private boolean isInputCorrect(String input, boolean levelOrColor) {
        if (levelOrColor)
            return (input.equalsIgnoreCase("FIRST") || input.equalsIgnoreCase("SECOND") || input.equalsIgnoreCase("THIRD"));
        else
            return (input.equalsIgnoreCase("GREEN") || input.equalsIgnoreCase("BLUE") || input.equalsIgnoreCase("YELLOW") || input.equalsIgnoreCase("PURPLE"));
    }


    /**
     * Checks if the desired Card is available on the Grid
     * Warnings are suppressed since previous controls ensure that the value returned by the get() methods is always not null
     *
     * @param developmentCardsGrid The DevelopmentGrid
     * @param level                The Level of the desired Card
     * @param color                The Color of the desired Card
     * @return True if the card is available, false otherwise
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private boolean isCardAvailable(DevelopmentCardsGrid developmentCardsGrid, String level, String color) {
        Color cardColor = Arrays.stream(Color.values()).filter(x -> x.getName().equalsIgnoreCase(color)).findFirst().get();
        Level cardLevel = Arrays.stream(Level.values()).filter(x -> x.getLevelName().equalsIgnoreCase(level)).findFirst().get();

        try {
            return developmentCardsGrid.isAvailable(cardColor, cardLevel);
        } catch (LevelDoesNotExistException | ColorDoesNotExistException e) {
            return false;
        }
    }


    /**
     * Returns the desired Development Card without drawing it from the Development Grid
     * Warnings are suppressed since previous controls ensure that the value returned by the get() method is always not null
     *
     * @param developmentCardsGrid The Grid from where to get the Development Card
     * @param cardLevel            The desired Development Card Level
     * @param cardColor            The desired Development Card Color
     * @return The desired Development Card
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private DevelopmentCard getSelectedDevelopmentCard(DevelopmentCardsGrid developmentCardsGrid, String cardLevel, String cardColor) {
        return developmentCardsGrid.getAllVisibleCards().stream().filter(x -> x.getDevelopmentCardType().getColor().getName().equalsIgnoreCase(cardColor))
                .filter(x -> x.getDevelopmentCardType().getLevel().getLevelName().equalsIgnoreCase(cardLevel)).findFirst().get();
    }

}
