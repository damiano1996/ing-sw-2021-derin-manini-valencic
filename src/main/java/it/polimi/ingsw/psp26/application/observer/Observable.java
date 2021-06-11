package it.polimi.ingsw.psp26.application.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T> {

    private transient List<Observer<T>> observers;

    public Observable() {
        observers = new ArrayList<>();
    }

    /**
     * Method that notifies the observers contained in the observers List
     *
     * @param object The Object that causes the notification
     */
    public void notifyObservers(T object) {
        if (observers != null) {
            for (Observer<T> observer : observers)
                if (observer != null) observer.update(object);
        }
    }

    /**
     * Adds an observer to the observers List
     *
     * @param observer The observer to add
     */
    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    /**
     * Resets the observers List by creating a new one
     */
    public void resetObservers() {
        observers = new ArrayList<>();
    }

}
