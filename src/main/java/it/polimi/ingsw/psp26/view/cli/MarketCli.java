package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.io.PrintWriter;

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
        cliUtils.printFigure("MarketTitle", 1, 59);
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

}
