package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.model.personalboard.VaticanReportSection;

import java.io.PrintWriter;

public class FaithTrackCli {

    private final PrintWriter pw;
    private final CliUtils cliUtils;

    public FaithTrackCli(PrintWriter pw) {
        this.pw =pw;
        this.cliUtils = new CliUtils(pw);
    }

    /**
     * Prints the Player's Faith Track. The PopeSpace intervals are printed thicker
     *
     * @param faithTrack     The Faith Track to print
     * @param startingRow    The starting row where the Faith Track is going to be printed
     * @param startingColumn The starting column where the Faith Track is going to be printed
     */
    public void displayFaithTrack(FaithTrack faithTrack, int startingRow, int startingColumn) {
        cliUtils.printFigure("FaithTrack", startingRow, startingColumn);
        setPointsNumbers(startingRow, startingColumn);
        firstLine(startingRow, startingColumn, faithTrack.getMarkerPosition());
        midLine(startingRow, startingColumn, faithTrack.getMarkerPosition(), faithTrack.getVaticanReportSections());
        lastine(startingRow, startingColumn, faithTrack.getMarkerPosition());
    }

    /**
     * Prints the yellow numbers on the FaithTrack
     *
     * @param startingRow    The row where the FaithTrack will be printed
     * @param startingColumn The column where the FaithTrack will be printed
     */
    private void setPointsNumbers(int startingRow, int startingColumn) {
        int[] pointsNumbers = {1, 2, 4, 6, 9, 12, 16, 20};

        for (int pointsNumber : pointsNumbers) printPointsNumber(pointsNumber, startingRow, startingColumn);
    }

    /**
     * Auxiliary method that print the points numbers in the correct position
     *
     * @param pointNumber    The point number to print
     * @param startingRow    The row where the FaithTrack will be printed
     * @param startingColumn The column where the FaithTrack will be printed
     */
    private void printPointsNumber(int pointNumber, int startingRow, int startingColumn) { //TODO se si possono leggere direttamente i codici ansi da file cambia questo metodo
        switch (pointNumber) {
            case 1:
                cliUtils.setCursorPosition(startingRow + 6, startingColumn + 16);
                pw.print(cliUtils.pCS("1", Color.BYELLOW));
                break;

            case 2:
                cliUtils.setCursorPosition(startingRow, startingColumn + 36);
                pw.print(cliUtils.pCS("2", Color.BYELLOW));
                break;

            case 4:
                cliUtils.setCursorPosition(startingRow, startingColumn + 60);
                pw.print(cliUtils.pCS("4", Color.BYELLOW));
                break;

            case 6:
                cliUtils.setCursorPosition(startingRow + 12, startingColumn + 68);
                pw.print(cliUtils.pCS("6", Color.BYELLOW));
                break;

            case 9:
                cliUtils.setCursorPosition(startingRow + 12, startingColumn + 92);
                pw.print(cliUtils.pCS("9", Color.BYELLOW));
                break;

            case 12:
                cliUtils.setCursorPosition(startingRow, startingColumn + 99);
                pw.print(cliUtils.pCS("1", Color.BYELLOW) + "-" + cliUtils.pCS("2", Color.BYELLOW));
                break;

            case 16:
                cliUtils.setCursorPosition(startingRow, startingColumn + 123);
                pw.print(cliUtils.pCS("1", Color.BYELLOW) + "-" + cliUtils.pCS("6", Color.BYELLOW));
                break;

            case 20:
                cliUtils.setCursorPosition(startingRow, startingColumn + 147);
                pw.print(cliUtils.pCS("2", Color.BYELLOW) + "-" + cliUtils.pCS("0", Color.BYELLOW));
                break;

            default:
                break;
        }
        pw.flush();
    }

    /**
     * Return a String representing the Player's cross
     */
    private void printCross() {
        pw.print("  \u001b[41;1m \u2020 \u001b[0m  ");
    }

    /**
     * Auxiliary method used in firstLine(), secondLine() and thirdLine() methods
     *
     * @param start The position of the first cell of the line on the track
     * @param end   The position of the last cell of the line on the track
     * @param fp    (start <= fp <= end) ==> a cross will be printed in fp position
     *              A blank space is printed otherwise
     */
    private void printRow(int start, int end, int fp) {
        for (int i = start; i <= end; i++) {
            if (i != fp) {
                if (i < 10) {
                    if (i == 8)
                        pw.print(cliUtils.pCS(cliUtils.hSpace(3) + "\u256C" + cliUtils.hSpace(3), Color.WHITE)); //PopeSpace symbol
                    else pw.print(cliUtils.pCS(cliUtils.hSpace(3) + i + cliUtils.hSpace(3), Color.GREY));
                } else {
                    if (i == 16 || i == 24)
                        pw.print(cliUtils.pCS(cliUtils.hSpace(3) + "\u256C" + cliUtils.hSpace(3), Color.WHITE)); //PopeSpace symbol
                    else
                        pw.print(cliUtils.pCS(cliUtils.hSpace(2) + (i / 10) + " " + (i % 10) + cliUtils.hSpace(2), Color.GREY));
                }
            } else printCross();
            if (i == 4 || i == 8 || i == 11 || i == 16 || i == 18 || i == 24) pw.print("\u2551");
            else pw.print("|");
        }
    }

    /**
     * Prints the first line of the Track
     *
     * @param fp             Has the same function as before
     * @param startingRow    The starting row where the Faith Track is going to be printed
     * @param startingColumn The starting column where the Faith Track is going to be printed
     */
    private void firstLine(int startingRow, int startingColumn, int fp) {
        cliUtils.setCursorPosition(startingRow + 2, startingColumn);

        pw.print(cliUtils.hSpace(16) + "|");
        printRow(4, 9, fp);
        pw.print(cliUtils.hSpace(31) + "|");
        printRow(18, 24, fp);

        pw.flush();
    }

    /**
     * Prints the second line of the Track, including VaticanReportSections
     *
     * @param fp             Has the same function as before
     * @param startingRow    The starting row where the Faith Track is going to be printed
     * @param startingColumn The starting column where the Faith Track is going to be printed
     */
    private void midLine(int startingRow, int startingColumn, int fp, VaticanReportSection[] vaticanReportSections) {
        cliUtils.setCursorPosition(startingRow + 6, startingColumn);

        pw.print(cliUtils.hSpace(16) + "|");
        printRow(3, 3, fp);
        pw.print(cliUtils.hSpace(11) + printVaticanReportSection(vaticanReportSections[0]));
        pw.print(cliUtils.hSpace(11) + "|");
        printRow(10, 10, fp);
        pw.print(cliUtils.hSpace(11) + printVaticanReportSection(vaticanReportSections[1]));
        pw.print(cliUtils.hSpace(11) + "|");
        printRow(17, 17, fp);
        pw.print(cliUtils.hSpace(19) + printVaticanReportSection(vaticanReportSections[2]));

        pw.flush();
    }

    /**
     * Prints the third line of the Track
     *
     * @param fp             Has the same function as before
     * @param startingRow    The starting row where the Faith Track is going to be printed
     * @param startingColumn The starting column where the Faith Track is going to be printed
     */
    private void lastine(int startingRow, int startingColumn, int fp) {
        cliUtils.setCursorPosition(startingRow + 10, startingColumn);

        pw.print("|");
        printRow(0, 2, fp);
        pw.print(cliUtils.hSpace(31) + "|");
        printRow(11, 16, fp);

        pw.flush();
    }

    /**
     * Prints the Vatican Report Tiles in the Faith Track
     *
     * @param vaticanReportSection The section to print
     * @return X if the Section isn't active, the Section's value if it's active
     */
    private String printVaticanReportSection(VaticanReportSection vaticanReportSection) {
        if (vaticanReportSection.isPopesFavorTileActive())
            return "\u2551   \u001b[38;5;209m" + vaticanReportSection.getValue() + "\u001b[0m   \u2551";
        else return "\u2551   X   \u2551";
    }

}
