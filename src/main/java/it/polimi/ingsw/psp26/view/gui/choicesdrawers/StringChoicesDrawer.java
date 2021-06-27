package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.view.gui.ButtonContainer;

/**
 * Class to decorate button containers containing a string.
 */
public class StringChoicesDrawer implements ChoicesDrawer<String> {

    /**
     * Method to decorate a button container setting as text the string contained in the button.
     *
     * @param stringButtonContainer button container with string inside
     * @return decorated button
     */
    @Override
    public ButtonContainer<String> decorateButtonContainer(ButtonContainer<String> stringButtonContainer) {
        stringButtonContainer.setText(stringButtonContainer.getContainedObject());
        return stringButtonContainer;
    }
}
