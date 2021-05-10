package it.polimi.ingsw.psp26.view.gui;

import javafx.scene.control.CheckBox;

public class CheckBoxContainer<T> extends CheckBox {

    private final T object;

    public CheckBoxContainer(T object) {
        this.object = object;
    }

    public T getContainedObject() {
        return object;
    }
}
