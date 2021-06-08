package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.psp26.configurations.Configurations.RESOURCES_PATH;

public class CliUtils {

    public final PrintWriter pw;

    public CliUtils(PrintWriter pw) {
        this.pw = pw;
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
    public void printFigure(String figureName, int row, int col) {
        Scanner in = null;

        try {
            in = new Scanner(new File(RESOURCES_PATH + "cli/" + figureName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert in != null;
        String[] ascii = new String[Integer.parseInt(in.nextLine())];

        for (int i = 0; i < ascii.length; i++) ascii[i] = in.nextLine();

        printASCII(ascii, row, col);

        in.close();
    }


    /**
     * Auxiliary method that prints the specified ascii figure
     *
     * @param ascii The figure to print
     * @param row   The y coordinate
     * @param col   The x coordinate
     */
    private void printASCII(String[] ascii, int row, int col) {
        for (int i = 0; i < ascii.length; i++) {
            setCursorPosition(row + i, col);
            pw.print(ascii[i]);
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
     * @param s     The String to print
     * @param color The color of the printed String
     */
    public void printAndBackWithColor(String s, Color color) {
        pw.print(pCS(s, color));
        pw.flush();
        moveCursor("lf", s.length());
        moveCursor("dn", 1);
    }


    /**
     * pPCS = print Positioned Colored String
     * Prints a colored String in a specified position
     *
     * @param s              The string to print
     * @param color          The Color of the string
     * @param startingRow    The row where the String will be printed
     * @param startingColumn The column where the String will be printed
     */
    public void pPCS(String s, Color color, int startingRow, int startingColumn) {
        setCursorPosition(startingRow, startingColumn);
        pw.print(pCS(s, color));
        pw.flush();
    }


    /**
     * Centers a String in the given space
     *
     * @param rowSize The size of the row where the String will be printed
     * @param string  The String to print
     * @return A String centered in the rowSize space
     */
    public String centerString(int rowSize, String string) {
        if (rowSize >= string.length()) return hSpace((rowSize - string.length()) / 2) + string;
        else return string.substring(0, rowSize);
    }


    /**
     * Hides the cursor from screen
     */
    public void hideCursor() {
        pw.print("\u001b[?25l");
        pw.flush();
    }


    /**
     * Puts the cursor visibility back on screen
     */
    public void showCursor() {
        pw.print("\u001b[?25h");
        pw.flush();
    }


    /**
     * Clears a line from the current cursor position to end of line
     *
     * @param row    The row of the line to clean
     * @param column The column of the line to clean
     */
    public void clearLine(int row, int column) {
        setCursorPosition(row, column);
        pw.println("\u001b[0K");
        pw.flush();
        setCursorPosition(row, column);
    }


    /**
     * Clears the screen without the notification area
     */
    public void clns() {
        for (int i = 1; i < 30; i++) clearLine(i, 1);
        for (int i = 30; i <= 61; i++) reverseClearLine(i, 200);
        setCursorPosition(1, 1);
    }


    /**
     * Clears a line from the current cursor position to start of line
     *
     * @param row    The row of the line to clean
     * @param column The column of the line to clean
     */
    public void reverseClearLine(int row, int column) {
        setCursorPosition(row, column);
        pw.println("\u001b[1K");
        pw.flush();
        setCursorPosition(row, column);
    }


    /**
     * Checks if the inserted char is a number or a character
     *
     * @param c The char to examine
     * @return False if c is a number, true if it's not a number
     */
    public boolean checkAsciiRange(char c) {
        return (48 > (int) c || (int) c > 57);
    }


    /**
     * Given a List of Resources, it returns the number of a specific ResourceType contained in the List
     *
     * @param resources    The Resource List
     * @param resourceType The Resource you want to know the number of elements contained in the List
     * @return The number of resourceType contained in resources
     */
    public int getResourcesNumber(List<Resource> resources, Resource resourceType) {
        if (!resources.contains(resourceType)) return 0;
        return (int) resources.stream().filter(x -> x.equals(resourceType)).count();
    }


    /**
     * Prints a List of Resources where it is indicated the number of Resources the Player has
     *
     * @param resources      The Player's Resources
     * @param startingRow    The row at which the List is going to be printed
     * @param startingColumn The column at which the List is going to be printed
     */
    public void printPlayerResources(List<Resource> resources, int startingRow, int startingColumn) {
        CliUtils cliUtils = new CliUtils(new PrintWriter(System.out));

        cliUtils.pPCS("Player's Resources:", Color.WHITE, startingRow, startingColumn);

        printResourceAndNumber(Resource.SHIELD, getResourcesNumber(resources, Resource.SHIELD), startingRow + 2, startingColumn);
        printResourceAndNumber(Resource.STONE, getResourcesNumber(resources, Resource.STONE), startingRow + 2, startingColumn + 10);
        printResourceAndNumber(Resource.COIN, getResourcesNumber(resources, Resource.COIN), startingRow + 2, startingColumn + 20);
        printResourceAndNumber(Resource.SERVANT, getResourcesNumber(resources, Resource.SERVANT), startingRow + 2, startingColumn + 30);
    }


    /**
     * Prints a Resource and the number (previously calculated) of this Resource in a List
     *
     * @param resource        The Resource to print
     * @param resourceNumbers The number of this Resource in the List
     * @param startingRow     The row at which the Resource is going to be printed
     * @param startingColumn  The column at which the Resource is going to be printed
     */
    private void printResourceAndNumber(Resource resource, int resourceNumbers, int startingRow, int startingColumn) {
        CliUtils cliUtils = new CliUtils(new PrintWriter(System.out));
        PrintWriter pw = new PrintWriter(System.out);

        cliUtils.setCursorPosition(startingRow, startingColumn);
        pw.print(cliUtils.pCS("\u2588\u2588", resource.getColor()) + "  " + resourceNumbers);
        pw.flush();
    }

}
