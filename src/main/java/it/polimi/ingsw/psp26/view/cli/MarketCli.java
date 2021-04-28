package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.utils.ViewUtils;

import java.io.PrintWriter;
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

        for (int i = 0; i <= 2; i++) {
            Resource[] resources = marketTray.getMarblesOnRow(2 - i);

            for (int k = 3; k >= 0; k--) pw.print(cliUtils.pCS(".d88b.  ", resources[k].getColor()));
            pw.flush();
            cliUtils.setCursorPosition(startingRow + 9 + (4 * i), startingColumn + 18);
            for (int l = 3; l >= 0; l--) pw.print(cliUtils.pCS("8b88d8  ", resources[l].getColor()));
            pw.flush();
            cliUtils.setCursorPosition(startingRow + 10 + (4 * i), startingColumn + 18);
            for (int m = 3; m >= 0; m--) pw.print(cliUtils.pCS("`Y88P'  ", resources[m].getColor()));
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
     * @param marketTray The actual state of the Market
     * @return The selected row or column
     */
    public int displayMarketSelection(MarketTray marketTray) {
        cliUtils.cls();
        displayMarketScreen(marketTray);
        cliUtils.pPCS("Choose a number between 0 to 6 where 0-2 refers to rows and 3-6 refers to columns: ", Color.WHITE, 45, 21);

        int marketIndex = 0;
        boolean indexInserted = false;

        while (!indexInserted) {
            try {
                marketIndex = getMarketIndex();
                indexInserted = true;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                cliUtils.pPCS("WRONG INDEX INSERTED! Please try again", Color.RED, 43, 21);
                cliUtils.clearLine(45, 104);
            }
        }

        return adjustMarketIndex(marketIndex);
    }


    /**
     * Asks the desired index for Market row/column
     *
     * @return An integer between 0 and 6
     * @throws IndexOutOfBoundsException If the index inserted is not correct
     */
    private int getMarketIndex() throws IndexOutOfBoundsException {
        Scanner in = new Scanner(System.in);
        String marketString = in.nextLine();
        int marketIndex;

        if (marketString.isEmpty()) throw new IndexOutOfBoundsException();
        if (ViewUtils.checkAsciiRange(marketString.charAt(0))) throw new IndexOutOfBoundsException();

        marketIndex = Integer.parseInt(marketString);
        if (marketIndex < 0 || marketIndex > 6) throw new IndexOutOfBoundsException();

        return marketIndex;
    }


    /**
     * Method to get the correct index to send to Server (indexes are inverted in controller)
     *
     * @param marketIndex The index to adjust
     * @return The correct index
     */
    private int adjustMarketIndex(int marketIndex) {
        if (marketIndex <= 2) return (2 - marketIndex);
        else return (9 - marketIndex);
    }

}
