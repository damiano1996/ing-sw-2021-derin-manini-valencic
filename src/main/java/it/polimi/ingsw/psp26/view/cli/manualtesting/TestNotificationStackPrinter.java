package it.polimi.ingsw.psp26.view.cli.manualtesting;

import it.polimi.ingsw.psp26.view.cli.CliUtils;
import it.polimi.ingsw.psp26.view.cli.NotificationStackPrinter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class used to test NotificationStackPrinter related methods.
 */
public class TestNotificationStackPrinter {

    CliUtils cliUtils;
    Scanner in;
    PrintWriter pw;

    public TestNotificationStackPrinter() {
        pw = new PrintWriter(System.out);
        in = new Scanner(System.in);
        cliUtils = new CliUtils(pw);
    }


    /**
     * Used to launch tests.
     */
    public static void main(String[] args) {
        TestNotificationStackPrinter testNotificationStackPrinter = new TestNotificationStackPrinter();
        testNotificationStackPrinter.testMethod();
    }


    /**
     * Call here the methods to test.
     */
    public void testMethod() {
        testMessageStackPrinter();
    }


    /**
     * Test the ability to print messages in the notification stack.
     */
    private void testMessageStackPrinter() {

        //---MESSAGE-STACK-PRINTER-TEST---// Press Enter 3 times

        NotificationStackPrinter notificationStackPrinter = new NotificationStackPrinter(new PrintWriter(System.out));
        cliUtils.cls();

        List<String> messages = new ArrayList<>();
        messages.add("efigwqfqieyifqygfqwfqywufgwifqgqfwiefwffewqfwqwfwqweugfyeiufyqiueygfiugwefuiqgwefygfqefwrehqoi");
        messages.add("efigwqfqieyifqygfqwfqywufgwifqgqfwiefwffewqfwqwfwqweugfyeiufyqiueygfiugwefuiqgwefygfqefwrehqoi");
        notificationStackPrinter.printMessageStack(messages);
        in.nextLine();

        messages.add("iufyqiueygfiugwefuiqgwefygfqefwrehqoi");
        messages.add("gfqwfqywufgwifqgqfwiefwffewqfwqwfwqweugfyeiufyqiueygfiugwefuiqgwefygfqefwrehqoiygfqefwrehqoi");
        messages.add("gfyeiufyqiueygfiugwefuiqgwefygfqefwrehqoirehqoi");
        messages.add("gfqwfqywufgwifqgqfwiefwffewqfwqwfwqweugfyeiufyqiueygfiugwefuiqgwefygfqefwrehqoiygfqefwrehqoi");
        notificationStackPrinter.printMessageStack(messages);
        in.nextLine();

        messages.add("eefwgfwggeffeqq");
        messages.add("efigwqfqieyifqygfqwfqywufgwifqgqfwiefwffewqfwqwfwqweugfyeiufyqiueygfiugwefuiqgwefygfqefwrehqoi");
        messages.add("wqfwqwfwqweugfyeiufyqiueygfiugwefuiqgwefygfqefwrehqoi");
        messages.add("ciao");
        messages.add("ciao");
        notificationStackPrinter.printMessageStack(messages);
        in.nextLine();
    }

}
