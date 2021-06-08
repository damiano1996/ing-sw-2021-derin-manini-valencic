package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.exceptions.UndoOptionSelectedException;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class MarketCli {

    private final PrintWriter pw;
    private final CliUtils cliUtils;

    public MarketCli(PrintWriter pw) {
        this.pw = pw;
        this.cliUtils = new CliUtils(pw);
    }


    /**
     * Prints the Market interaction screen
     *
     * @param marketTray The market
     */
    public void displayMarketScreen(MarketTray marketTray) {
        cliUtils.printFigure("/titles/MarketTitle", 1, 59);
        displayMarketTray(marketTray, 13, 18);
    }


    /**
     * Prints the Market Tray
     *
     * @param marketTray     The Market Tray to print
     * @param startingRow    The starting row where the Market is going to be printed
     * @param startingColumn The starting column where the Market is going to be printed
     */
    public void displayMarketTray(MarketTray marketTray, int startingRow, int startingColumn) {
        cliUtils.printFigure("Market", startingRow, startingColumn + 17);
        cliUtils.printFigure("MarketLegend", startingRow, startingColumn + 102);
        printMarbleOnSlide(marketTray, startingRow, startingColumn + 17);
        printMarketMarbleRows(marketTray, startingRow, startingColumn + 17);
    }


    /**
     * Prints the rows of the Market Tray
     *
     * @param marketTray     The Market Tray to print
     * @param startingRow    The starting row where the Market is going to be printed
     * @param startingColumn The starting column where the Market is going to be printed
     */
    private void printMarketMarbleRows(MarketTray marketTray, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 8, startingColumn + 18);

        for (int i = 0; i < marketTray.getMarblesOnColumn(0).length; i++) {
            Resource[] resources = marketTray.getMarblesOnRow(i);

            for (Resource resource : resources) pw.print(cliUtils.pCS(".d88b.  ", resource.getColor()));
            pw.flush();
            cliUtils.setCursorPosition(startingRow + 9 + (4 * i), startingColumn + 18);
            for (Resource resource : resources) pw.print(cliUtils.pCS("8b88d8  ", resource.getColor()));
            pw.flush();
            cliUtils.setCursorPosition(startingRow + 10 + (4 * i), startingColumn + 18);
            for (Resource resource : resources) pw.print(cliUtils.pCS("`Y88P'  ", resource.getColor()));
            pw.flush();
            cliUtils.setCursorPosition(startingRow + 12 + (4 * i), startingColumn + 18);
        }
    }


    /**
     * Prints the marble on the Market Tray slide
     *
     * @param marketTray     The Market Tray to print
     * @param startingRow    The starting row where the Market is going to be printed
     * @param startingColumn The starting column where the Market is going to be printed
     */
    private void printMarbleOnSlide(MarketTray marketTray, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 2, startingColumn + 40);

        cliUtils.printAndBackWithColor(".d88b.", marketTray.getMarbleOnSlide().getColor());
        cliUtils.printAndBackWithColor("8b88d8", marketTray.getMarbleOnSlide().getColor());
        cliUtils.printAndBackWithColor("`Y88P'", marketTray.getMarbleOnSlide().getColor());
    }


    /**
     * Displays the Market row/column choice
     *
     * @param marketTray      The actual state of the Market
     * @param playerResources The Player Resources
     * @return The selected row or column
     * @throws UndoOptionSelectedException The Player decided to quit from the Market selection screen
     */
    public int displayMarketSelection(MarketTray marketTray, List<Resource> playerResources) throws UndoOptionSelectedException {
        cliUtils.cls();
        displayMarketScreen(marketTray);
        cliUtils.printPlayerResources(playerResources, 38, 145);
        cliUtils.pPCS("Enter 'u' (undo) if you want to exit from Market screen.", Color.WHITE, 45, 21);
        cliUtils.pPCS("Choose a number between 1 to 7 where 1-3 refers to rows and 4-7 refers to columns: ", Color.WHITE, 46, 21);

        int marketIndex = 0;
        boolean indexInserted = false;

        while (!indexInserted) {
            try {
                marketIndex = getMarketIndex();
                indexInserted = true;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                cliUtils.pPCS("WRONG INDEX INSERTED! Please try again", Color.RED, 43, 21);
                cliUtils.clearLine(46, 104);
            }
        }

        return marketIndex;
    }


    /**
     * Asks the desired index for Market row/column
     *
     * @return An integer between 0 and 6
     * @throws IndexOutOfBoundsException   If the index inserted is not correct
     * @throws UndoOptionSelectedException The Player decided to quit from the Market selection screen
     */
    private int getMarketIndex() throws IndexOutOfBoundsException, UndoOptionSelectedException {
        Scanner in = new Scanner(System.in);
        String marketString = in.nextLine();
        int marketIndex;

        if (marketString.isEmpty()) throw new IndexOutOfBoundsException();
        if (marketString.equals("u")) throw new UndoOptionSelectedException();
        if (cliUtils.checkAsciiRange(marketString.charAt(0))) throw new IndexOutOfBoundsException();

        marketIndex = Integer.parseInt(marketString) - 1;
        if (marketIndex < 0 || marketIndex > 6) throw new IndexOutOfBoundsException();

        return marketIndex;
    }

}
