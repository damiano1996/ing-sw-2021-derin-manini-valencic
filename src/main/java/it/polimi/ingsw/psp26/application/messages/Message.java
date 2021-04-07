package it.polimi.ingsw.psp26.application.messages;

import java.io.Serializable;
import java.util.HashMap;

public class Message implements Serializable {

    private String sessionToken;
    private MessageType messageType;
    private HashMap<String, Object> payload;

    public Message() { // TODO: temporary
    }

    public Message(String sessionToken, MessageType messageType, HashMap<String, Object> payload) {
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

    public HashMap<String, Object> getPayload() {
        return payload;
    }
}
