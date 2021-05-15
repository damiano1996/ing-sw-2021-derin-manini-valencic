package it.polimi.ingsw.psp26.view.gui;

import javafx.scene.control.Button;

public class ButtonContainer<T> extends Button {

    private final T object;
    private boolean clicked;

    public ButtonContainer(T object) {
        this.object = object;
    }

    public T getContainedObject() {
        return object;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}
