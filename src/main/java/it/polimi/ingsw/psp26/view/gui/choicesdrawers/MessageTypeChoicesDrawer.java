package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.view.gui.CheckBoxContainer;
import javafx.scene.control.Button;

import static it.polimi.ingsw.psp26.view.ViewUtils.beautifyMessageType;

public class MessageTypeChoicesDrawer implements ChoicesDrawer<MessageType> {

    @Override
    public Button decorateButton(Button button, MessageType choice) {
        button.setText(beautifyMessageType(choice));
        return button;
    }

    @Override
    public CheckBoxContainer decorateCheckBoxContainer(CheckBoxContainer checkBoxContainer, MessageType choice) {
        checkBoxContainer.setText(beautifyMessageType(choice));
        return checkBoxContainer;
    }
}
