package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.model.enums.Color;

import java.io.PrintWriter;

/**
 * Use this class to start a new WaitingScreen
 */
public class WaitingScreenStarter {

    private static WaitingScreenStarter instance;
    private final CliUtils cliUtils;
    private WaitingScreen waitingScreen;

    //Control the ability to start or stop the Waiting Screen
    private boolean isWaiting;

    public WaitingScreenStarter() {
        this.cliUtils = new CliUtils(new PrintWriter(System.out));
        this.waitingScreen = null;
        isWaiting = false;
    }


    /**
     * Singleton instance
     *
     * @return The WaitingScreenStarter instance
     */
    public static WaitingScreenStarter getInstance() {
        if (instance == null) instance = new WaitingScreenStarter();
        return instance;
    }

    /**
     * Starts a Waiting Screen by creating a new Waiting Screen
     *
     * @param message A Message containing a String to display on screen
     */
    public void startWaiting(Message message) {
        if (!isWaiting) {
            cliUtils.cls();
            cliUtils.hideCursor();
            cliUtils.pPCS((String) message.getPayload(), Color.WHITE, 25, 90);

            waitingScreen = new WaitingScreen();
            isWaiting = true;
            waitingScreen.startWaiting();
        }
    }

    /**
     * Stops the executing Waiting Screen
     */
    public void stopWaiting() {
        if (isWaiting) {
            cliUtils.showCursor();

            waitingScreen.stopWaiting();
            isWaiting = false;
        }
    }
}
