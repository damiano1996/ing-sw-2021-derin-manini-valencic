package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.model.personalboard.VaticanReportSection;

import java.io.PrintWriter;

public class FaithTrackCli {

    private final PrintWriter pw;
    private final CliUtils cliUtils;

    public FaithTrackCli(PrintWriter pw) {
        this.pw = pw;
        this.cliUtils = new CliUtils(pw);
    }

    /**
     * Prints the Player's Faith Track. The PopeSpace intervals are printed thicker
     *
     * @param faithTrack        The Faith Track to print
     * @param startingRow       The starting row where the Faith Track is going to be printed
     * @param startingColumn    The starting column where the Faith Track is going to be printed
     * @param isMultiPlayerMode If the Match is SinglePlayer, prints the Lorenzo's cross too
     */
    public void displayFaithTrack(FaithTrack faithTrack, int startingRow, int startingColumn, boolean isMultiPlayerMode) {
        cliUtils.printFigure("FaithTrack", startingRow, startingColumn);
        printVaticanReportSections(faithTrack.getVaticanReportSections(), startingRow, startingColumn);

        setMarkerCursorPosition(startingRow, startingColumn, faithTrack.getMarkerPosition());
        if (isMultiPlayerMode) printCross();
        else {
            if (faithTrack.getMarkerPosition() == faithTrack.getBlackCrossPosition()) printTwoCrosses();
            else {
                printCross();
                setMarkerCursorPosition(startingRow, startingColumn, faithTrack.getBlackCrossPosition());
                printBlackCross();
            }
        }
    }

    /**
     * Return a String representing the Player's cross
     */
    private void printCross() {
        pw.print("  \u001b[41;1m \u2020 \u001b[0m  ");
        pw.flush();
    }

    /**
     * Return a String representing Lorenzo's cross
     */
    private void printBlackCross() {
        pw.print("  \u001b[47m \u001b[30m\u2020 \u001b[0m  ");
        pw.flush();
    }

    /**
     * Return a String representing the Player's cross and Lorenzo's cross both in the same cell
     */
    private void printTwoCrosses() {
        pw.print(" \u001b[41;1m\u2020\u001b[0m   \u001b[47m\u001b[30m\u2020\u001b[0m ");
        pw.flush();
    }

    /**
     * If the Marker is in the first line of the Track, set the cursor to the correct position for printing it
     *
     * @param fp             The total amount of Marker's Faith Points
     * @param startingRow    The starting row where the Faith Track is going to be printed
     * @param startingColumn The starting column where the Faith Track is going to be printed
     */
    private void firstLineMarkerPosition(int startingRow, int startingColumn, int fp) {
        if (4 <= fp && fp <= 9) cliUtils.setCursorPosition(startingRow + 2, startingColumn + 17 + (8 * (fp - 4)));
        else if (18 <= fp && fp <= 24)
            cliUtils.setCursorPosition(startingRow + 2, startingColumn + 97 + (8 * (fp - 18)));
    }

    /**
     * If the Marker is in the mid line of the Track, set the cursor to the correct position for printing it
     *
     * @param fp             The total amount of Marker's Faith Points
     * @param startingRow    The starting row where the Faith Track is going to be printed
     * @param startingColumn The starting column where the Faith Track is going to be printed
     */
    private void midLineMarkerPosition(int startingRow, int startingColumn, int fp) {
        if (fp == 3) cliUtils.setCursorPosition(startingRow + 6, startingColumn + 17);
        else if (fp == 10) cliUtils.setCursorPosition(startingRow + 6, startingColumn + 57);
        else if (fp == 17) cliUtils.setCursorPosition(startingRow + 6, startingColumn + 97);
    }

    /**
     * If the Marker is in the last line of the Track, set the cursor to the correct position for printing it
     *
     * @param fp             The total amount of Marker's Faith Points
     * @param startingRow    The starting row where the Faith Track is going to be printed
     * @param startingColumn The starting column where the Faith Track is going to be printed
     */
    private void lastLineMarkerPosition(int startingRow, int startingColumn, int fp) {
        if (0 <= fp && fp <= 2) cliUtils.setCursorPosition(startingRow + 10, startingColumn + 1 + (8 * fp));
        else if (11 <= fp && fp <= 16)
            cliUtils.setCursorPosition(startingRow + 10, startingColumn + 57 + (8 * (fp - 11)));
    }

    /**
     * Set the cursor in the correct position for printing the Marker
     *
     * @param startingRow    The starting row where the Faith Track is going to be printed
     * @param startingColumn The starting column where the Faith Track is going to be printed
     * @param fp             The total amount of Marker's Faith Points
     */
    private void setMarkerCursorPosition(int startingRow, int startingColumn, int fp) {
        firstLineMarkerPosition(startingRow, startingColumn, fp);
        midLineMarkerPosition(startingRow, startingColumn, fp);
        lastLineMarkerPosition(startingRow, startingColumn, fp);
    }

    /**
     * Prints the Vatican Report Sections on the Track
     *
     * @param vaticanReportSections The sections to print
     * @param startingRow           The starting row where the Faith Track is going to be printed
     * @param startingColumn        The starting column where the Faith Track is going to be printed
     */
    private void printVaticanReportSections(VaticanReportSection[] vaticanReportSections, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 6, startingColumn + 36);
        pw.print(isVaticanReportSectionActive(vaticanReportSections[0]));
        cliUtils.moveCursor("rg", 31);
        pw.print(isVaticanReportSectionActive(vaticanReportSections[1]));
        cliUtils.moveCursor("rg", 39);
        pw.print(isVaticanReportSectionActive(vaticanReportSections[2]));
    }

    /**
     * If the Vatican Report Tile is active returns it's value
     *
     * @param vaticanReportSection The section to print
     * @return X if the Section isn't active, the Section's value if it's active
     */
    private String isVaticanReportSectionActive(VaticanReportSection vaticanReportSection) {
        if (vaticanReportSection.isPopesFavorTileActive())
            return "\u2551   \u001b[38;5;209m" + vaticanReportSection.getValue() + "\u001b[0m   \u2551";
        else return "\u2551   X   \u2551";
    }

}
