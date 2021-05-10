package it.polimi.ingsw.psp26.view.gui.choices;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.view.gui.CheckBoxContainer;
import javafx.scene.control.Button;

public class MessageTypeChoicesDrawer implements ChoicesDrawer<MessageType> {

    @Override
    public Button decorateButton(Button button, MessageType choice) {
        button.setText(choice.name());
        return button;
    }

    @Override
    public CheckBoxContainer decorateCheckBoxContainer(CheckBoxContainer checkBoxContainer, MessageType choice) {
        checkBoxContainer.setText(choice.name());
        return checkBoxContainer;
    }
}
