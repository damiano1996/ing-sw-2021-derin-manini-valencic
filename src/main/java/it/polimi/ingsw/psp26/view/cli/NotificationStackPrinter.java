package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.enums.Color;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationStackPrinter {

    private final CliUtils cliUtils;
    private List<String> notificationCopy;
    private boolean isStackPrintable;

    private static final int STARTING_ROW = 30;
    private static final int STARTING_COLUMN = 201;
    private static final int STACK_HEIGHT = 30;
    private static final int MAX_NUMBER_OF_STRINGS = 10;
    private static final int STACK_WIDTH = 30;

    public NotificationStackPrinter(PrintWriter pw) {
        this.cliUtils = new CliUtils(pw);
        notificationCopy = new ArrayList<>();
        isStackPrintable = true;
    }


    /**
     * Prints the given List of notifications in a stack of fixed size
     *
     * @param notifications The Strings to print
     */
    public void printMessageStack(List<String> notifications) {
        notificationCopy = notifications;
        
        if (isStackPrintable) {
            cliUtils.saveCursorPosition();
            int delta = 0;
            Color stringColor;

            cliUtils.printFigure("NotificationStackBorder", STARTING_ROW, STARTING_COLUMN);

            // Inserts in the stack a maxNumberOfStrings number of Strings in the stack 
            List<List<String>> parsedMessages = stringParser(notifications.size() >= MAX_NUMBER_OF_STRINGS ? notifications.subList(0, MAX_NUMBER_OF_STRINGS) : notifications);

            for (List<String> parsedMessage : parsedMessages) {
                stringColor = randomColorPicker();

                for (String string : parsedMessage) {
                    cliUtils.pPCS(string, stringColor, STARTING_ROW + delta + 5, STARTING_COLUMN + 3);
                    delta++;
                    if (delta > STACK_HEIGHT - 9) break;
                }

                delta++;
                if (delta > STACK_HEIGHT - 9) break;
            }

            cliUtils.restoreCursorPosition();
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

        for (int i = 0; i < string.length(); i += STACK_WIDTH)
            stringList.add(string.substring(i, Math.min(i + STACK_WIDTH, string.length())));

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


    /**
     * Used to set isStackPrintable attribute to false
     */
    public void hideNotifications() {
        isStackPrintable = false;
    }


    /**
     * Sets isStackPrintable to true and prints the notification stack
     */
    public void restoreStackView() {
        if (!isStackPrintable) {
            isStackPrintable = true;
            printMessageStack(notificationCopy);
        }
    }

}
