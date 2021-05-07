package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;

import java.io.Serializable;

public class ModelUpdateMessage extends SessionMessage implements Serializable {

    /**
     * Class constructor.
     *
     * @param sessionToken session token of the client
     * @param messageType  message type of the payload
     * @param payload      payload to send to client
     * @throws InvalidPayloadException if payload is not serializable
     */
    public ModelUpdateMessage(String sessionToken, MessageType messageType, Object payload) throws InvalidPayloadException {
        super(sessionToken, MessageType.MODEL_UPDATE, messageType, payload);
    }

    public MessageType getPayloadMessageType() throws EmptyPayloadException {
        return (MessageType) getPayload(0);
    }

    public Object getModelPayload() throws EmptyPayloadException {
        return getPayload(1);
    }
}
