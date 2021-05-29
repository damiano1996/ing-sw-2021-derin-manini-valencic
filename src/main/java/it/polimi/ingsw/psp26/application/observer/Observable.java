package it.polimi.ingsw.psp26.application.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<T> {

    private transient List<Observer<T>> observers;

    public Observable() {
        observers = new ArrayList<>();
    }

    public void notifyObservers(T object) {
        if (observers != null) {
            for (Observer<T> observer : observers)
                if (observer != null) observer.update(object);
        }
    }

    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    public void resetObservers() {
        observers = new ArrayList<>();
    }

}
