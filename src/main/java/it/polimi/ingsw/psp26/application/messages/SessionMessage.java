package it.polimi.ingsw.psp26.application.messages;

import java.io.Serializable;

public class SessionMessage extends Message implements Serializable {

    private String sessionToken;

    public SessionMessage(String sessionToken, MessageType messageType, Object... payloads) {
        super(messageType, payloads);
        this.sessionToken = sessionToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

}
