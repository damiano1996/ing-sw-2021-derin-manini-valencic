package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;

import java.io.Serializable;

public class LiveUpdateMessage extends SessionMessage implements Serializable {

    /**
     * Constructor of the class.
     *
     * @param sessionToken session token
     * @param payloads     payloads to send
     * @throws InvalidPayloadException if the given payloads are not serializable
     */
    public LiveUpdateMessage(String sessionToken, Object... payloads) throws InvalidPayloadException {
        super(sessionToken, MessageType.LIVE_UPDATE, payloads);
    }
}
