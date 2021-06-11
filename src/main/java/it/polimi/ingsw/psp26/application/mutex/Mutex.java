package it.polimi.ingsw.psp26.application.mutex;

/**
 * Class that resemble a mutex to stop Threads
 */
public class Mutex {

    private boolean semaphore;

    public Mutex() {
        semaphore = true;
    }


    /**
     * Locks the mutex
     */
    public synchronized void lock() {
        while (!semaphore) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        semaphore = false;
    }


    /**
     * Unlocks the mutex
     */
    public synchronized void unlock() {
        semaphore = true;
        notifyAll();
    }

}
