package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.view.gui.ButtonContainer;

/**
 * Interface that collects methods used to decorate javaFX components.
 *
 * @param <T> generic type
 */
public interface ChoicesDrawer<T> {

    /**
     * Method to decorate a button container.
     * It takes as input a button container and returns a button container enriched with
     * new properties.
     *
     * @param tButtonContainer button container containing a generic type
     * @return button container with new properties
     */
    ButtonContainer<T> decorateButtonContainer(ButtonContainer<T> tButtonContainer);
}
