package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.enums.Color;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationStackPrinter {

    private final CliUtils cliUtils;

    public NotificationStackPrinter(PrintWriter pw) {
        this.cliUtils = new CliUtils(pw);
    }


    /**
     * Prints the given List of notifications in a stack of fixed size
     *
     * @param notifications  The Strings to print
     * @param startingRow    The row where the notifications are going to be printed
     * @param startingColumn The column where the notifications are going to be printed
     */
    public void printMessageStack(List<String> notifications, int startingRow, int startingColumn) {
        int delta = 0;
        int stackHeight = 30;
        int maxNumberOfStrings = 7;
        Color stringColor;

        cliUtils.printFigure("NotificationStackBorder", startingRow, startingColumn);

        // Inserts in the stack a maxNumberOfStrings number of Strings in the stack 
        List<List<String>> parsedMessages = stringParser(notifications.size() >= maxNumberOfStrings ? notifications.subList(0, maxNumberOfStrings) : notifications);

        for (List<String> parsedMessage : parsedMessages) {
            stringColor = randomColorPicker();

            for (String string : parsedMessage) {
                cliUtils.pPCS(string, stringColor, startingRow + delta + 5, startingColumn + 3);
                delta++;
                if (delta > stackHeight - 9) break;
            }

            delta++;
            if (delta > stackHeight - 9) break;
        }
    }


    /**
     * For each String given, splits it and put it in a List of substrings
     *
     * @param strings The Strings to parse
     * @return A List os substrings
     */
    private List<List<String>> stringParser(List<String> strings) {
        List<List<String>> parsedStrings = new ArrayList<>();

        for (String string : strings)
            parsedStrings.add(splitString(string));

        return parsedStrings;
    }


    /**
     * Splits a String into multiple substrings of stackWidth length
     *
     * @param string The String to split
     * @return A List of the substrings
     */
    private List<String> splitString(String string) {
        List<String> stringList = new ArrayList<>();
        int stackWidth = 30;

        for (int i = 0; i < string.length(); i += stackWidth)
            stringList.add(string.substring(i, Math.min(i + stackWidth, string.length())));

        return stringList;
    }


    /**
     * Picks a random Color
     *
     * @return A random Color
     */
    private Color randomColorPicker() {
        List<Color> colors = new ArrayList<>();

        colors.add(Color.GREEN);
        colors.add(Color.GREY);
        colors.add(Color.PURPLE);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);

        Collections.shuffle(colors);
        return colors.get(0);
    }

}
