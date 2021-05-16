package it.polimi.ingsw.psp26.view.gui.asynchronousjobs;

public interface DelayedJob {

    void execute() throws InterruptedException;
}
