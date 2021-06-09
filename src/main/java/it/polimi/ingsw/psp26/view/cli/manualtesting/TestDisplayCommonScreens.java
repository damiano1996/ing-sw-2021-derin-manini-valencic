package it.polimi.ingsw.psp26.view.cli.manualtesting;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.view.cli.CLI;
import it.polimi.ingsw.psp26.view.cli.CliUtils;
import it.polimi.ingsw.psp26.view.cli.CommonScreensCli;
import it.polimi.ingsw.psp26.view.cli.WaitingScreenStarter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TestDisplayCommonScreens {

    CLI cli;
    CommonScreensCli commonScreensCli;
    CliUtils cliUtils;
    Scanner in;
    PrintWriter pw;

    public TestDisplayCommonScreens() {
        pw = new PrintWriter(System.out);
        in = new Scanner(System.in);
        cliUtils = new CliUtils(pw);
        commonScreensCli = new CommonScreensCli(pw);
        cli = new CLI();
    }


    /**
     * Used to launch tests
     */
    public static void main(String[] args) throws EmptyPayloadException, InvalidPayloadException {
        TestDisplayCommonScreens testDisplayCommonScreens = new TestDisplayCommonScreens();
        testDisplayCommonScreens.testMethod();
    }


    /**
     * Call here the methods to test
     */
    public void testMethod() throws EmptyPayloadException, InvalidPayloadException {
        testError();
        testWaitingScreen();
        testFinalScreen();
    }


    /**
     * Test the ability to display error messages
     */
    private void testError() {

        //---ERROR-TEST---// Press enter 1 time

        cliUtils.cls();

        try {
            cli.displayError("ERROR                     TEST");
        } catch (Exception ignored) {
        }
        in.nextLine();
    }


    /**
     * Test the ability to start and stop the waiting screen
     */
    private void testWaitingScreen() throws EmptyPayloadException, InvalidPayloadException {

        //---WAITING-SCREEN-TEST---// press Enter 3 times

        WaitingScreenStarter waitingScreenStarter = WaitingScreenStarter.getInstance();
        Message message = new Message(MessageType.GENERAL_MESSAGE, "Please wait the other players to come");

        waitingScreenStarter.startWaiting(message);
        in.nextLine();
        waitingScreenStarter.stopWaiting();

        message = new Message(MessageType.GENERAL_MESSAGE, "Please wait the other players to choose the resources");
        waitingScreenStarter.startWaiting(message);
        in.nextLine();
        waitingScreenStarter.stopWaiting();

        message = new Message(MessageType.GENERAL_MESSAGE, "Press Enter to exit");
        waitingScreenStarter.startWaiting(message);
        in.nextLine();
        waitingScreenStarter.stopWaiting();
    }


    /**
     * Test the ability to show the final screen
     */
    private void testFinalScreen() {

        //---FINAL-SCREEN-TEST---// Press Enter 2 times

        Map<String, Integer> leaderboard = new HashMap<>();

        leaderboard.put("Damiano", 10);
        leaderboard.put("Jas", 30);
        leaderboard.put("Player", 0);
        leaderboard.put("Andrea", 20);

        String myName = "Jas";

        try {
            cli.displayEndGame(leaderboard, myName);
        } catch (Exception ignored) {
        }
        pw.print(Color.GREEN.setColor());
        pw.flush();
        cliUtils.printFigure("/titles/YouWonTitle", 37, 89);
        pw.print(Color.RESET.setColor());
        pw.flush();

        in.nextLine();
        cliUtils.cls();

        try {
            cli.displayEndGame(leaderboard, myName);
        } catch (Exception ignored) {
        }
        pw.print(Color.RED.setColor());
        pw.flush();
        cliUtils.printFigure("/titles/YouLostTitle", 37, 85);
        pw.print(Color.RESET.setColor());
        pw.flush();

        cliUtils.pPCS("Press Enter to go back to the main screen", Color.WHITE, 50, 4);

        in.nextLine();
    }

}
