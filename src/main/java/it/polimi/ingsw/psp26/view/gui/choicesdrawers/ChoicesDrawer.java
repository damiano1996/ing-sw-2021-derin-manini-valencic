package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.view.gui.ButtonContainer;

public interface ChoicesDrawer<T> {

    ButtonContainer<T> decorateButtonContainer(ButtonContainer<T> tButtonContainer);
}
