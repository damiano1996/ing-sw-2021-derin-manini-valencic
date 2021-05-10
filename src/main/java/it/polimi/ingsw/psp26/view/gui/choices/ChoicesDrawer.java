package it.polimi.ingsw.psp26.view.gui.choices;

import it.polimi.ingsw.psp26.view.gui.CheckBoxContainer;
import javafx.scene.control.Button;

public interface ChoicesDrawer<T> {

    Button decorateButton(Button button, T choice);

    CheckBoxContainer decorateCheckBoxContainer(CheckBoxContainer checkBoxContainer, T choice);
}
