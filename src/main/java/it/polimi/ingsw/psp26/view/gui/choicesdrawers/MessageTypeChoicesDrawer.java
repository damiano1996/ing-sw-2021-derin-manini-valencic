package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.view.gui.ButtonContainer;

import static it.polimi.ingsw.psp26.view.ViewUtils.beautifyMessageType;

public class MessageTypeChoicesDrawer implements ChoicesDrawer<MessageType> {

    @Override
    public ButtonContainer<MessageType> decorateButtonContainer(ButtonContainer<MessageType> messageTypeButtonContainer) {
        messageTypeButtonContainer.setText(beautifyMessageType(messageTypeButtonContainer.getContainedObject()));
        return messageTypeButtonContainer;
    }

}
