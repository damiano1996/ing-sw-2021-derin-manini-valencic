package it.polimi.ingsw.psp26.view.gui.asynchronousjobs;

/**
 * Interface for a job that must be executed with a delay.
 */
public interface DelayedJob {

    /**
     * Method that must be executed.
     *
     * @throws InterruptedException In case of thread error
     */
    void execute() throws InterruptedException;
}
