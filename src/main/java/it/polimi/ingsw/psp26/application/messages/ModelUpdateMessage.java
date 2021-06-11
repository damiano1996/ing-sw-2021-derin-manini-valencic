package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;

import java.io.Serializable;

/**
 * Class used to send updates of the Model to the Clients
 */
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

    /**
     * Getter of the Message Type of the payload of the ModelUpdateMessage
     * 
     * @return The Message Type of the payload
     * @throws EmptyPayloadException The required payload is empty
     */
    public MessageType getPayloadMessageType() throws EmptyPayloadException {
        return (MessageType) getPayload(0);
    }

    /**
     * Getter of the payload that contains the update the Client receives
     * 
     * @return The Object containing the update
     * @throws EmptyPayloadException The required payload is empty
     */
    public Object getModelPayload() throws EmptyPayloadException {
        return getPayload(1);
    }
}
