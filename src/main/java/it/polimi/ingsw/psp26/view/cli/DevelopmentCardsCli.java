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

/**
 * Class that contains all the methods related to display DevelopmentCards.
 */
public class DevelopmentCardsCli {

    private final PrintWriter pw;
    private final CliUtils cliUtils;
    private final Scanner in;

    /**
     * Constructor of the class.
     * It creates a new PrintWriter, a new CliUtils and a new Scanner to use.
     *
     * @param pw the PrintWriter to use
     */
    public DevelopmentCardsCli(PrintWriter pw) {
        this.pw = pw;
        this.cliUtils = new CliUtils(pw);
        this.in = new Scanner(System.in);
    }


    /**
     * Prints the DevelopmentCardGrid.
     * The DevelopmentCardGrid is printed a row at a time, and each DevelopmentGridCell is printed individually.
     *
     * @param developmentCardsGrid the DevelopmentCardGrid to print
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
     * Prints a DevelopmentCard.
     * First, the base DevelopmentCard Border is printed.
     * Then, the property of the DevelopmentCard are added:
     * - The Development Card requirements;
     * - The DevelopmentCard Color;
     * - The DevelopmentCard Level;
     * - The DevelopmentCard Production power;
     * - The DevelopmentCard victory points;
     * It also prints additional borders based on the number of the other DevelopmentCards placed under the one is being printed.
     *
     * @param developmentCard the desired DevelopmentCard to print
     * @param startingRow     the row where the DevelopmentCard will be printed
     * @param startingColumn  the column where the DevelopmentCard will be printed
     * @param cardStackSize   how many DevelopmentCards are on the top of each other
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
     * Prints one border for each DevelopmentCard under the top one.
     *
     * @param startingRow    the row where the DevelopmentCard will be printed
     * @param startingColumn the column where the DevelopmentCard will be printed
     * @param cardStackSize  how many DevelopmentCards are on the top of each other
     */
    private void printDevelopmentCardBorder(int startingRow, int startingColumn, int cardStackSize) {
        if (cardStackSize > 1)
            cliUtils.printFigure("/developmentcardborders/DevelopmentCardBorder" + (cardStackSize - 1), startingRow, startingColumn);
    }


    /**
     * Prints the DevelopmentCard Color on top and on bottom of the level dots.
     *
     * @param developmentCard get the color of this DevelopmentCard
     * @param startingRow     the row where the DevelopmentCard will be printed
     * @param startingColumn  the column where the DevelopmentCard will be printed
     * @param upOrDown        if true prints the upper section, if false print the lower section
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
     * Prints the DevelopmentCard requirements line.
     *
     * @param developmentCard the DevelopmentCard to print
     * @param startingRow     the starting row where the DevelopmentCard is going to be printed
     * @param startingColumn  the starting column where the DevelopmentCard is going to be printed
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
     * Prints the DevelopmentCard Level line in the correct Color.
     *
     * @param developmentCard the DevelopmentCard to print
     * @param startingRow     the starting row where the DevelopmentCard is going to be printed
     * @param startingColumn  the starting column where the DevelopmentCard is going to be printed
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
     * Prints a Card level dots in the correct Color.
     *
     * @param level the level number of the DevelopmentCard
     * @param color the Color of the DevelopmentCard
     * @return a formatted String of the Level dots
     */
    private String printLevelDots(int level, Color color) {
        return cliUtils.pCS("\u25CF ", color).repeat(Math.max(0, level));
    }


    /**
     * Generic method that prints a Production. May be used with every Production Object.
     *
     * @param productionCost   the cost of the Production
     * @param productionReturn the Resources returned by the Production
     * @param startingRow      the row where the Production is gonna be printed
     * @param startingColumn   the column where the Production is gonna be printed
     */
    public void printProduction(Map<Resource, Integer> productionCost, Map<Resource, Integer> productionReturn, int startingRow, int startingColumn) {
        for (int i = 0; i < 3; i++)
            printProductionLines(productionCost, productionReturn, i, startingRow, startingColumn);
    }


    /**
     * Used to print the Production section of a DevelopmentCard.
     * It calls three auxiliary methods in order to get a clean print of the Resources (centered in the DevelopmentCard).
     *
     * @param productionCost   the cost of the Production
     * @param productionReturn the Resources returned by the Production
     * @param line             print the first, second or third line of the Production section
     * @param startingRow      the starting row where the DevelopmentCard is going to be printed
     * @param startingColumn   the starting column where the DevelopmentCard is going to be printed
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
     * Prints the first line of the DevelopmentCard production section.
     *
     * @param requiredResources         resources type required to perform the production action
     * @param numberOfRequiredResources resources number required to perform the production action
     * @param producedResources         resources type produced by the production action
     * @param numberOfProducedResources resources number produced by the production action
     * @param startingRow               the starting row where the DevelopmentCard is going to be printed
     * @param startingColumn            the starting column where the DevelopmentCard is going to be printed
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
     * Prints the second line of the DevelopmentCard production section.
     *
     * @param requiredResources         resources type required to perform the production action
     * @param numberOfRequiredResources resources number required to perform the production action
     * @param producedResources         resources type produced by the production action
     * @param numberOfProducedResources resources number produced by the production action
     * @param startingRow               the starting row where the DevelopmentCard is going to be printed
     * @param startingColumn            the starting column where the DevelopmentCard is going to be printed
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
     * Prints the third line of the DevelopmentCard production section.
     *
     * @param requiredResources         resources type required to perform the production action
     * @param numberOfRequiredResources resources number required to perform the production action
     * @param producedResources         resources type produced by the production action
     * @param numberOfProducedResources resources number produced by the production action
     * @param startingRow               the starting row where the DevelopmentCard is going to be printed
     * @param startingColumn            the starting column where the DevelopmentCard is going to be printed
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
     * Print the DevelopmentCard's Victory Points.
     *
     * @param developmentCard the DevelopmentCard where to get the VictoryPoints
     * @param startingRow     the starting row where the DevelopmentCard is going to be printed
     * @param startingColumn  the starting column where the DevelopmentCard is going to be printed
     */
    private void printCardVictoryPoints(DevelopmentCard developmentCard, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 12, startingColumn);

        String s = "";
        s = s + "|" + cliUtils.hSpace(10) + developmentCard.getVictoryPoints() + cliUtils.hSpace(10 - (developmentCard.getVictoryPoints() / 10)) + "|";

        pw.print(s);
        pw.flush();
    }


    /**
     * Used to print the DevelopmentCards in the DevelopmentGrid.
     *
     * @param developmentGridCell if the cell contains one or more DevelopmentCards, print the one on the top. Otherwise, leave a blank space
     * @param startingRow         the starting row where the DevelopmentCard is going to be printed
     * @param startingColumn      the starting column where the DevelopmentCard is going to be printed
     */
    private void printDevelopmentGridCard(DevelopmentGridCell developmentGridCell, int startingRow, int startingColumn) {
        if (!developmentGridCell.isEmpty())
            printDevelopmentCard(developmentGridCell.getFirstCard(), startingRow, startingColumn, developmentGridCell.getDevelopmentCardsSize());
    }


    /**
     * Display the DevelopmentCardGrid selection screen.
     * The method check the input entered by the Player to ensure he selects a valid DevelopmentCard.
     * It also habilitate the undo option if the Player decides to do it.
     * If the entered input is not correct, or the DevelopmentCard not available, error strings will be printed.
     *
     * @param developmentCardsGrid the DevelopmentCardGrid to display
     * @param playerResources      the Player's Resources
     * @return the chosen DevelopmentCard
     * @throws UndoOptionSelectedException the Player decided to quit from the DevelopmentCardGrid selection screen
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
     * Asks the Player the Level and the Color of the desired DevelopmentCard.
     * Each input is checked to see if it matches the correct requirements (Level or Color) of the desired DevelopmentCard.
     *
     * @param stringToDisplay a message to inform the Player the String to enter
     * @param levelOrColor    true if a Level is entered, false if a Color is entered
     * @return the Level/Color chosen by the Player
     * @throws UndoOptionSelectedException the Player decided to quit from the DevelopmentCardGrid selection screen
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
     * Checks if the input inserted by the Player is correct in terms of Levels or Colors.
     *
     * @param input        the String entered by the Player
     * @param levelOrColor true if Level is checked, false if Color is checked
     * @return true if the input is correct, false otherwise
     */
    private boolean isInputCorrect(String input, boolean levelOrColor) {
        if (levelOrColor)
            return (input.equalsIgnoreCase("FIRST") || input.equalsIgnoreCase("SECOND") || input.equalsIgnoreCase("THIRD"));
        else
            return (input.equalsIgnoreCase("GREEN") || input.equalsIgnoreCase("BLUE") || input.equalsIgnoreCase("YELLOW") || input.equalsIgnoreCase("PURPLE"));
    }


    /**
     * Checks if the desired DevelopmentCard is available on the DevelopmentCardGrid.
     * Warnings are suppressed since previous controls ensure that the value returned by the get() methods is always not null.
     *
     * @param developmentCardsGrid the DevelopmentCardGrid
     * @param level                the Level of the desired DevelopmentCard
     * @param color                the Color of the desired DevelopmentCard
     * @return true if the DevelopmentCard is available, false otherwise
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
     * Returns the desired DevelopmentCard without drawing it from the DevelopmentCardGrid.
     * Warnings are suppressed since previous controls ensure that the value returned by the get() method is always not null.
     *
     * @param developmentCardsGrid the Grid from where to get the DevelopmentCard
     * @param cardLevel            the desired DevelopmentCard Level
     * @param cardColor            the desired DevelopmentCard Color
     * @return the desired DevelopmentCard
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private DevelopmentCard getSelectedDevelopmentCard(DevelopmentCardsGrid developmentCardsGrid, String cardLevel, String cardColor) {
        return developmentCardsGrid.getAllVisibleCards().stream().filter(x -> x.getDevelopmentCardType().getColor().getName().equalsIgnoreCase(cardColor))
                .filter(x -> x.getDevelopmentCardType().getLevel().getLevelName().equalsIgnoreCase(cardLevel)).findFirst().get();
    }

}
