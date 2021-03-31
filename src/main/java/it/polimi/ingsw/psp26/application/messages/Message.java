package it.polimi.ingsw.psp26.application.messages;

import java.io.Serializable;

public class Message implements Serializable {

    private final MessageType messageType;
    private final Object payload;

    public Message(MessageType messageType, Object payload) {
        this.messageType = messageType;
        this.payload = payload;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Object getPayload() {
        return payload;
    }
}
