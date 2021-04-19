package it.polimi.ingsw.psp26.application.callback;

public abstract class Caller {

    public void register(Callback callback) {
        callback.callMe();
    }
}
