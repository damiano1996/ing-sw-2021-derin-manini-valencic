package it.polimi.ingsw.psp26.view.gui.asynchronousjobs;

import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * Class to execute periodic tasks in background on the main thread used by javaFX.
 * <p>
 * It starts a thread to execute a JobListener operation,
 * then it runs a javaFX task to execute the DelayedJob action
 * (usually an operation that requires the javafx thread, such as graphics operations: drawing panes and so on).
 */
public class AsynchronousDrawer {

    private final JobListener jobListener;
    private final DelayedJob delayedJob;
    private final boolean loop;
    private boolean firstExecution;

    /**
     * Constructor of the class.
     *
     * @param jobListener JobListener object to be executed on the java thread
     * @param delayedJob  DelayedJob object to be executed on the javaFX main thread
     * @param loop        boolean to execute jobs in loop
     */
    public AsynchronousDrawer(JobListener jobListener, DelayedJob delayedJob, boolean loop) {
        this.jobListener = jobListener;
        this.delayedJob = delayedJob;
        this.loop = loop;
        firstExecution = true;
    }

    /**
     * Method that creates the Task that must be executed on the javaFX main thread.
     * It runs the DelayedJob in the Platform.runLater().
     *
     * @return the task that must be executed
     */
    @SuppressWarnings("rawtypes")
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

    /**
     * Method to start the JobListener.
     * When the JobListener terminates, it calls the task that executes the DelayedJob on the javaFX thread.
     * Then, if the loop option was true, it re-execute the JobListener, otherwise it ends.
     */
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
