package it.polimi.ingsw.psp26.network.client.cache;

/**
 * Class to store objects that are frequently requested by the client side.
 *
 * @param <T>
 */
public class CachedObject<T> {
    private T object;
    private boolean obsolete;

    public CachedObject() {
        obsolete = true;
    }


    /**
     * When receiving a new Object from the Model, change the current object value with the new one.
     *
     * @param object The new Object version
     */
    public synchronized void updateObject(T object) {
        this.object = object;
        obsolete = false;
        notifyAll();
    }


    /**
     * Method that stops the calling thread until the contained object will be updated.
     *
     * @throws InterruptedException If unable to wait the calling thread
     */
    public synchronized void lookingForUpdate() throws InterruptedException {
        while (obsolete) wait();
        obsolete = true;
    }


    /**
     * Getter of the contained object.
     *
     * @return Object contained
     */
    public synchronized T getObject() {
        return object;
    }

}