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
     * When receiving a new Object from the Model, change the current object value with the new one
     *
     * @param object The new Object version
     */
    public synchronized void updateObject(T object) {
        this.object = object;
        obsolete = false;
        notifyAll();
    }


    /**
     * Getter of an updated version of object
     *
     * @return An updated version of object
     * @throws InterruptedException If object is null, calls wait() on the current thread
     */
    public synchronized T getUpdatedObject() throws InterruptedException {
        while (obsolete) wait();
        obsolete = true;
        return object;
    }


    /**
     * Getter of an updated version of object
     * By assumption it will never stop the calling thread
     *
     * @return An obsolete version of object
     * @throws InterruptedException If object is null, calls wait() on the current thread
     */
    public synchronized T getObsoleteObject() throws InterruptedException {
//            if (object == null) return getUpdatedObject();
        return object;
    }

}