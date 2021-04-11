package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.io.PrintWriter;

public class MarketCli {

    private final CliUtils cliUtils;
    private final PrintWriter pw;

    public MarketCli() {
        this.cliUtils = new CliUtils();
        pw = new PrintWriter(System.out);
    }

    /**
     * Prints the Market Tray
     *
     * @param marketTray     The Market Tray to print
     * @param startingRow    The starting row where the Market is going to be printed
     * @param startingColumn The starting column where the Market is going to be printed
     */
    public void displayMarketTray(MarketTray marketTray, int startingRow, int startingColumn) {
        cliUtils.printFigure("Market", startingRow, startingColumn + 6);
        printMarbleOnSlide(marketTray, startingRow, startingColumn + 6);
        printMarketMarbleRows(marketTray, startingRow, startingColumn + 6);
        printMarketLegend(startingColumn);
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
     * Prints the Market legend
     *
     * @param startingColumn The starting column where the Market is going to be printed
     */
    private void printMarketLegend(int startingColumn) {
        cliUtils.vSpace(7);

        pw.println(cliUtils.hSpace(startingColumn) + "+-----------------------------------------------------------+");
        pw.println(cliUtils.hSpace(startingColumn) + "|                         Legend                            |");
        pw.println(cliUtils.hSpace(startingColumn) + "|                                                           |");
        pw.println(cliUtils.hSpace(startingColumn) + "|  " + cliUtils.pCS(".d88b.", Color.GREY) + "     " + cliUtils.pCS("__", Color.GREY) + "         " + cliUtils.pCS(".d88b.", Color.PURPLE) + "     " + cliUtils.pCS("__", Color.PURPLE) + "        " + cliUtils.pCS(".d88b.", Color.RED) + "   " + cliUtils.pCS("_|_", Color.RED) + "  |");
        pw.println(cliUtils.hSpace(startingColumn) + "|  " + cliUtils.pCS("8b88d8", Color.GREY) + " _  " + cliUtils.pCS("/  \\_", Color.GREY) + "       " + cliUtils.pCS("8b88d8", Color.PURPLE) + " _  " + cliUtils.pCS("(  )", Color.PURPLE) + "       " + cliUtils.pCS("8b88d8", Color.RED) + " _  " + cliUtils.pCS("|", Color.RED) + "   |");
        pw.println(cliUtils.hSpace(startingColumn) + "|  " + cliUtils.pCS("`Y88P'", Color.GREY) + "   " + cliUtils.pCS("/_____|", Color.GREY) + "      " + cliUtils.pCS("`Y88P'", Color.PURPLE) + "   " + cliUtils.pCS("/____\\", Color.PURPLE) + "      " + cliUtils.pCS("`Y88P'", Color.RED) + "    " + cliUtils.pCS("|", Color.RED) + "   |");
        pw.println(cliUtils.hSpace(startingColumn) + "|                                                           |");
        pw.println(cliUtils.hSpace(startingColumn) + "|  " + cliUtils.pCS(".d88b.", Color.BLUE) + "   " + cliUtils.pCS("_____", Color.BLUE) + "        " + cliUtils.pCS(".d88b.", Color.YELLOW) + "    " + cliUtils.pCS("____", Color.YELLOW) + "       " + cliUtils.pCS(".d88b.", Color.WHITE) + "    " + cliUtils.pCS("_", Color.WHITE) + "   |");
        pw.println(cliUtils.hSpace(startingColumn) + "|  " + cliUtils.pCS("8b88d8", Color.BLUE) + " _ " + cliUtils.pCS("\\   /", Color.BLUE) + "        " + cliUtils.pCS("8b88d8", Color.YELLOW) + " _ " + cliUtils.pCS("/ __ \\", Color.YELLOW) + "      " + cliUtils.pCS("8b88d8", Color.WHITE) + " _ " + cliUtils.pCS("| |", Color.WHITE) + "  |");
        pw.println(cliUtils.hSpace(startingColumn) + "|  " + cliUtils.pCS("`Y88P'", Color.BLUE) + "    " + cliUtils.pCS("\\_/", Color.BLUE) + "         " + cliUtils.pCS("`Y88P'", Color.YELLOW) + "   " + cliUtils.pCS("\\____/", Color.YELLOW) + "      " + cliUtils.pCS("`Y88P'", Color.WHITE) + "   " + cliUtils.pCS("|_|", Color.WHITE) + "  |");
        pw.println(cliUtils.hSpace(startingColumn) + "|                                                           |");
        pw.println(cliUtils.hSpace(startingColumn) + "+-----------------------------------------------------------+");

        pw.flush();
    }

}
