package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;

import java.io.Serializable;

/**
 * Class that extends the Message class adding the session token.
 * Useful for network communications,
 */
public class SessionMessage extends Message implements Serializable {

    private String sessionToken;

    /**
     * Constructor of the class.
     *
     * @param sessionToken session token
     * @param messageType  message type
     * @param payloads     payloads to send
     * @throws InvalidPayloadException if the given payloads are not serializable
     */
    public SessionMessage(String sessionToken, MessageType messageType, Object... payloads) throws InvalidPayloadException {
        super(messageType, payloads);
        this.sessionToken = sessionToken;
    }

    /**
     * Getter of the session token
     *
     * @return session token
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * Setter of the session token.
     *
     * @param sessionToken session token
     */
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    /**
     * To string method, to print the message.
     *
     * @return string version of the message
     */
    @Override
    public String toString() {
        return "SessionMessage{" +
                "sessionToken='" + sessionToken + '\'' +
                "} " + super.toString();
    }
}
