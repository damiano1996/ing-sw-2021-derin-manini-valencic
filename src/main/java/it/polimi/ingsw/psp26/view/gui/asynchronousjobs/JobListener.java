package it.polimi.ingsw.psp26.view.gui.asynchronousjobs;

/**
 * Interface for a job that must be executed and that the termination can trigger another job.
 */
public interface JobListener {

    /**
     * Method used to execute the job.
     *
     * @throws InterruptedException If thread error
     */
    void lookingFor() throws InterruptedException;
}
