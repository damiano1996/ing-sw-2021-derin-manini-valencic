package it.polimi.ingsw.psp26.application.messages;

import java.io.Serializable;

public class Message implements Serializable {

    private String sessionToken;
    private MessageType messageType;
    private Object payload;

    public Message() {
    }

    public Message(String sessionToken, MessageType messageType, Object payload) {
        this.sessionToken = sessionToken;
        this.messageType = messageType;
        this.payload = payload;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Object getPayload() {
        return payload;
    }
}
