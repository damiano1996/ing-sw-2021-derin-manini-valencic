package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.view.gui.ButtonContainer;

import static it.polimi.ingsw.psp26.view.ViewUtils.beautifyMessageType;

/**
 * Class to decorate button container containing a message type.
 */
public class MessageTypeChoicesDrawer implements ChoicesDrawer<MessageType> {

    /**
     * Method to decorate the button with the text associated to the message type.
     *
     * @param messageTypeButtonContainer button container with message type inside
     * @return decorated button
     */
    @Override
    public ButtonContainer<MessageType> decorateButtonContainer(ButtonContainer<MessageType> messageTypeButtonContainer) {
        messageTypeButtonContainer.setText(beautifyMessageType(messageTypeButtonContainer.getContainedObject()));
        return messageTypeButtonContainer;
    }

}
