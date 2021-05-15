package it.polimi.ingsw.psp26.view.gui.asynchronousupdates;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class AsynchronousUpdateDrawer {

    private final LoopListener loopListener;
    private final RunLater runLater;

    private Task task;

    public AsynchronousUpdateDrawer(LoopListener loopListener, RunLater runLater) {
        this.loopListener = loopListener;
        this.runLater = runLater;

    }

    private Task createTask() {
        return new Task() {
            @Override
            protected Object call() {
                Platform.runLater(() -> {
                    try {
                        runLater.execute();
                        System.out.println("TASK HAS BEEN EXECUTED!");
                    } catch (InterruptedException ignored) {
                    }
                });
                return null;
            }
        };
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    loopListener.lookingFor();
                    System.out.println("STARTING TASK");
                    createTask().run();
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }
}
