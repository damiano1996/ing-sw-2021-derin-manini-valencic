package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.enums.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CliUtils {

    public final PrintWriter pw;

    public CliUtils() {
        this.pw = new PrintWriter(System.out);
    }

    /**
     * Inserts blank lines
     *
     * @param spaces The number of the lines
     */
    public void vSpace(int spaces) {
        for (int i = 0; i < spaces; i++) pw.println();
        pw.flush();
    }

    /**
     * Inserts blank spaces in a String
     *
     * @param spaces The number of the spaces
     * @return A blank String which length is == spaces
     */
    public String hSpace(int spaces) {
        return " ".repeat(Math.max(0, spaces));
    }

    /**
     * Clears the terminal screen
     */
    public void cls() {
        pw.print("\033[H\033[2J");
        pw.flush();
    }

    /**
     * pCS = Print Colored String. Returns a colored String
     *
     * @param string The String to be colored
     * @param color  The Color
     * @return The colored String
     */
    public String pCS(String string, Color color) {
        return color.toString() + string + Color.RESET;
    }

    /**
     * Moves the cursor in the specified direction and by the specified numbers
     *
     * @param direction Up(up), Down(dn), Left(lf) or Right(rg)
     * @param spaces    The number of spaces the cursor will move
     */
    public void moveCursor(String direction, int spaces) {
        switch (direction) {
            case ("up"):
                pw.print("\u001b[" + spaces + "A");
                break;

            case ("dn"):
                pw.print("\u001b[" + spaces + "B");
                break;

            case ("rg"):
                pw.print("\u001b[" + spaces + "C");
                break;

            case ("lf"):
                pw.print("\u001b[" + spaces + "D");
                break;

            default:
                break;
        }
        pw.flush();
    }

    /**
     * Saves the cursor position
     */
    public void saveCursorPosition() {
        pw.print("\u001b[s");
        pw.flush();
    }

    /**
     * Restore the cursor position from the previous saved one
     */
    public void restoreCursorPosition() {
        pw.print("\u001b[u");
        pw.flush();
    }

    /**
     * Set the cursor position at the specified row and column
     *
     * @param row    The row where the cursor will appear
     * @param column The column where the cursor will appear
     */
    public void setCursorPosition(int row, int column) {
        pw.print("\u001b[" + row + ";" + column + "H");
        pw.flush();
    }

    /**
     * Prints an ASCII figure on the screen th the specified coordinates (x,y)
     *
     * @param figureName The name of the figure file you want to print
     * @param row        The y coordinate
     * @param col        The x coordinate
     */
    public void printFigure(String figureName, int row, int col) { //PATHS MAY DIFFER IN THE FINAL SOLUTION
        String windowsPath = "src\\main\\resources\\" + figureName;
        String ubuntuPath = "/mnt/c/Users/andre/Desktop/POLI/ProgettoIngSW/project/src/main/resources/" + figureName;

        Scanner in = null;

        try {
            in = new Scanner(new File(windowsPath));
        } catch (FileNotFoundException e) {
            try {
                in = new Scanner(new File(ubuntuPath));
            } catch (FileNotFoundException f) {
                e.printStackTrace();
            }
        }

        assert in != null;
        String[] ascii = new String[Integer.parseInt(in.nextLine())];

        for (int i = 0; i < ascii.length; i++) ascii[i] = in.nextLine();

        printASCII(ascii, row, col);

        in.close();
    }

    /**
     * Auxiliary method that prints the specidied ascii figure
     *
     * @param ascii The figure to print
     * @param row   The y coordinate
     * @param col   The x coordinate
     */
    private void printASCII(String[] ascii, int row, int col) {
        setCursorPosition(row, col);

        for (String s : ascii) {
            saveCursorPosition();
            pw.print(s);
            restoreCursorPosition();
            moveCursor("dn", 1);
        }

        pw.flush();
    }

    /**
     * Set the cursor in the bottom left position of the screen
     */
    public void setCursorBottomLeft() {
        setCursorPosition(58, 1);
    }

    /**
     * Prints a colored string and set the cursor one line under the first column
     *
     * @param s     The Strinf to print
     * @param color The color of the printed String
     */
    public void printAndBackWithColor(String s, Color color) {
        saveCursorPosition();
        pw.print(pCS(s, color));
        pw.flush();
        restoreCursorPosition();
        moveCursor("dn", 1);
    }

}
