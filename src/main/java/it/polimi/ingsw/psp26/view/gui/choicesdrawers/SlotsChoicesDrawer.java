package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.view.gui.ButtonContainer;

public class SlotsChoicesDrawer implements ChoicesDrawer<String> {
    @Override
    public ButtonContainer<String> decorateButtonContainer(ButtonContainer<String> buttonContainer) {
        buttonContainer.setText(buttonContainer.getContainedObject());
        return buttonContainer;
    }
}
