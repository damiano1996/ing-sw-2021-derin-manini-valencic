package it.polimi.ingsw.psp26.view.gui.asynchronousjobs;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class AsynchronousDrawer {

    private final JobListener jobListener;
    private final DelayedJob delayedJob;
    private final boolean loop;
    private boolean firstExecution;

    public AsynchronousDrawer(JobListener jobListener, DelayedJob delayedJob, boolean loop) {
        this.jobListener = jobListener;
        this.delayedJob = delayedJob;
        this.loop = loop;
        firstExecution = true;
    }

    private Task createTask() {
        return new Task() {
            @Override
            protected Object call() {
                Platform.runLater(() -> {
                    // System.out.println("AsynchronousDrawer - RUN LATER THREAD ID: " + Thread.currentThread().getId());
                    try {
                        System.out.println("AsynchronousDrawer - STARTING TASK!");
                        delayedJob.execute();
                        System.out.println("AsynchronousDrawer - TASK HAS BEEN EXECUTED!");
                    } catch (InterruptedException ignored) {
                    }
                });
                return null;
            }
        };
    }

    public void start() {
        new Thread(() -> {
            while (loop || firstExecution) {
                firstExecution = false;
                try {
                    // System.out.println("AsynchronousDrawer - LOOKING FOR THREAD ID: " + Thread.currentThread().getId());
                    System.out.println("AsynchronousDrawer - LOOKING FOR UPDATES!");
                    jobListener.lookingFor();
                    System.out.println("AsynchronousDrawer - UPDATE RECEIVED!");
                    createTask().run();
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }
}
