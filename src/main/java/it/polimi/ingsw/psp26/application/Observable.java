package it.polimi.ingsw.psp26.application;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T> {

    private transient final List<Observer<T>> observers;

    public Observable() {
        observers = new ArrayList<>();
    }

    public void notifyObservers(T object) {
        for (Observer<T> observer : observers)
            observer.update(object);
    }

    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }
}
