package it.polimi.ingsw.psp26.application.callback;

/**
 * Abstract class for the Callback design pattern.
 */
public abstract class Caller {

    /**
     * Method to register a callback.
     * It executes the callMe() method of the registered callback.
     *
     * @param callback to execute in background
     */
    public void register(Callback callback) {
        callback.callMe();
    }
}
