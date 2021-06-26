package it.polimi.ingsw.psp26.application.observer;

/**
 * * Class implementing the Observer interface of the Observer/Observable design pattern.
 *
 * @param <T> generic type
 */
public interface Observer<T> {

    /**
     * Method to update the observer with the given object.
     *
     * @param object object used to update the observer
     */
    void update(T object);
}
