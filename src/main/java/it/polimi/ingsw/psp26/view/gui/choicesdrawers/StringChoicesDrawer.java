package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.view.gui.ButtonContainer;

public class StringChoicesDrawer implements ChoicesDrawer<String> {
    @Override
    public ButtonContainer<String> decorateButtonContainer(ButtonContainer<String> stringButtonContainer) {
        stringButtonContainer.setText(stringButtonContainer.getContainedObject());
        return stringButtonContainer;
    }
}
