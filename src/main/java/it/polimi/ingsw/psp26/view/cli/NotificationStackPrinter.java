package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.enums.Color;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that handle a live NotificationStack.
 */
public class NotificationStackPrinter {

    private static final int STARTING_ROW = 30;
    private static final int STARTING_COLUMN = 201;
    private static final int STACK_HEIGHT = 30;
    private static final int MAX_NUMBER_OF_STRINGS = 10;
    private static final int STACK_WIDTH = 30;

    private final CliUtils cliUtils;
    private List<String> notificationCopy;
    private boolean isStackPrintable;

    /**
     * Constructor of the class.
     * It creates a new CliUtils to use.
     * It also initialize the notificationCopy List and sets the isStackPrintable attribute to false.
     *
     * @param pw the PrintWriter to use
     */
    public NotificationStackPrinter(PrintWriter pw) {
        this.cliUtils = new CliUtils(pw);
        notificationCopy = new ArrayList<>();
        isStackPrintable = true;
    }


    /**
     * Prints the given List of notifications in a stack of fixed size.
     * No more of MAX_NUMBER_OF_STRINGS Strings will be printed in the stack.
     *
     * @param notifications the Strings to print
     */
    public void printMessageStack(List<String> notifications) {
        notificationCopy = notifications;

        if (isStackPrintable) {
            cliUtils.saveCursorPosition();
            int delta = 0;

            cliUtils.printFigure("NotificationStackBorder", STARTING_ROW, STARTING_COLUMN);

            // Inserts in the stack a maxNumberOfStrings number of Strings in the stack 
            List<List<String>> parsedMessages = stringParser(notifications.size() >= MAX_NUMBER_OF_STRINGS ? notifications.subList(0, MAX_NUMBER_OF_STRINGS) : notifications);

            for (List<String> parsedMessage : parsedMessages) {
                for (String string : parsedMessage) {
                    cliUtils.pPCS(string, Color.WHITE, STARTING_ROW + delta + 5, STARTING_COLUMN + 3);
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
     * For each String given, splits it and put it in a List of substrings.
     *
     * @param strings the Strings to parse
     * @return a List os substrings
     */
    private List<List<String>> stringParser(List<String> strings) {
        List<List<String>> parsedStrings = new ArrayList<>();

        for (int i = 0; i < strings.size(); i++)
            parsedStrings.add(splitString(strings.get(strings.size() - 1 - i)));

        return parsedStrings;
    }


    /**
     * Splits a String into multiple substrings of stackWidth length.
     *
     * @param string the String to split
     * @return a List of the substrings
     */
    private List<String> splitString(String string) {
        List<String> stringList = new ArrayList<>();

        String noEscapeString = string.replaceAll("\n", " ");
        for (int i = 0; i < noEscapeString.length(); i += STACK_WIDTH)
            stringList.add(noEscapeString.substring(i, Math.min(i + STACK_WIDTH, noEscapeString.length())));

        return stringList;
    }


    /**
     * Used to set isStackPrintable attribute to false.
     */
    public void hideNotifications() {
        isStackPrintable = false;
    }


    /**
     * Sets isStackPrintable to true and prints the notification stack.
     */
    public void restoreStackView() {
        if (!isStackPrintable) {
            isStackPrintable = true;
            printMessageStack(notificationCopy);
        }
    }

}
