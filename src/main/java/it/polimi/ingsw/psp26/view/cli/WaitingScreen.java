package it.polimi.ingsw.psp26.view.cli;

import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * Assuming there will be only one waiting screen active in a client per time
 */
public class WaitingScreen implements Runnable {
    //TODO facendolo stoppare e ripartire in tempi molto ravvicinati 2 volte è successo che stampasse in posti dove non dovrebbe non so perchè
    // magari chiedere alla review se succede ancora

    //Setting this to true will stop the waiting screen
    private boolean stopWaiting;
    private final CliUtils cliUtils;

    
    public WaitingScreen() {
        this.stopWaiting = false;
        this.cliUtils = new CliUtils(new PrintWriter(System.out));
    }

    /**
     * The thread cycles the different Frames reproducing a waiting icon
     * TimeUnit.MILLISECONDS.sleep() is used to give a better viewing experience
     */
    @Override
    public void run() {
        
        int frameCounter = 1;

        while (!stopWaiting) {

            cliUtils.printFigure("waitingscreenframes/WaitingScreenFrame" + frameCounter, 10, 101);

            try {
                //Using TimeUnit will not show a sleep warning in the IDE
                TimeUnit.MILLISECONDS.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            frameCounter++;
            
            //10 is the total amount of WaitingScreen's Frames
            if (frameCounter > 10) frameCounter = 1;

        }

    }

    /**
     * Starts a new thread showing the waiting wheel
     */
    public void startWaiting() {
        Thread waitingScreenThread = new Thread(this);
        waitingScreenThread.start();
    }

    /**
     * Call this method to end the thread by letting it finish it's run() method
     */
    public void stopWaiting() {
        if (!stopWaiting) stopWaiting = true;
    }

}
