package it.polimi.ingsw.psp26.view.gui;

import javafx.scene.control.Button;

/**
 * Class that represent a button containing an object.
 *
 * @param <T> the Object type contained by the ButtonContainer.
 */
public class ButtonContainer<T> extends Button {

    private final T object;
    private boolean clicked;

    /**
     * Constructor of the class.
     * Initializes the object attribute with the passed parameter.
     *
     * @param object the Object to insert into the ButtonContainer
     */
    public ButtonContainer(T object) {
        this.object = object;
    }


    /**
     * Getter of the object attribute.
     *
     * @return the object contained in the ButtonContainer
     */
    public T getContainedObject() {
        return object;
    }


    /**
     * Getter of the clicked attribute.
     *
     * @return the clicked attribute
     */
    public boolean isClicked() {
        return clicked;
    }


    /**
     * Setter of the clicked attribute.
     *
     * @param clicked the value to set in the clicked attribute
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}
