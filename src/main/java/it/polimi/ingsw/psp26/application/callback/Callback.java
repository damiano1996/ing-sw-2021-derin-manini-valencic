package it.polimi.ingsw.psp26.application.callback;

/**
 * Interface class for Callback design pattern.
 */
public interface Callback {

    /**
     * Method that must be called by the thread and executes the task in background.
     */
    void callMe();
}
