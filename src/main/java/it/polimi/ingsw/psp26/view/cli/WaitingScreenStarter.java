package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.model.enums.Color;

import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * Use this class to start a new waiting screen.
 */
public class WaitingScreenStarter {

    private static WaitingScreenStarter instance;
    private final CliUtils cliUtils;
    private Thread waitingScreenThread;
    private boolean isWaiting;

    /**
     * Constructor of the class.
     * It creates a new CliUtils to use and sets the isWaiting attribute to false.
     */
    private WaitingScreenStarter() {
        this.cliUtils = new CliUtils(new PrintWriter(System.out));
        isWaiting = false;
    }


    /**
     * Getter of the singleton instance.
     *
     * @return the WaitingScreenStarter instance
     */
    public static WaitingScreenStarter getInstance() {
        if (instance == null) instance = new WaitingScreenStarter();
        return instance;
    }


    /**
     * Starts a waiting screen by creating a new Thread.
     * The waiting screen will then show the five Resources moving simulating a waiting icon.
     *
     * @param message a Message containing a String to display on screen
     */
    public void startWaiting(Message message) throws EmptyPayloadException {
        if (!isWaiting) {
            cliUtils.clns();
            cliUtils.hideCursor();
            cliUtils.pPCS((String) message.getPayload(), Color.WHITE, 25, 90);
            isWaiting = true;

            //The thread cycles the different Frames reproducing a waiting icon
            waitingScreenThread = new Thread(() -> {

                //This wait will display correctly the starting screen
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int frameCounter = 1;

                while (isWaiting) {

                    cliUtils.printFigure("waitingscreenframes/WaitingScreenFrame" + frameCounter, 10, 101);

                    try {
                        //TimeUnit.MILLISECONDS.sleep() is used to give a better viewing experience
                        TimeUnit.MILLISECONDS.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    frameCounter++;

                    //10 is the total amount of WaitingScreen's Frames
                    if (frameCounter > 10) frameCounter = 1;
                }

            });
            waitingScreenThread.start();
        }
    }


    /**
     * Stops the executing waitingScreenThread.
     */
    public void stopWaiting() {
        if (isWaiting) {
            isWaiting = false;
            cliUtils.clns();
            cliUtils.showCursor();

            try {
                waitingScreenThread.join();
                cliUtils.setCursorPosition(1, 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}