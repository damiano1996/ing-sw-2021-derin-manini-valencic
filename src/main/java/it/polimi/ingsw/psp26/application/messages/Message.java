package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.application.messages.serialization.GsonConverter;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to model the messages.
 * A message is defined by a message type and 0 or more payloads.
 * Payloads are of same object type.
 * <p>
 * This class is serializable, therefore a message can be sent over the network.
 * The GsonConverter is used to serialize the payloads.
 */
public class Message implements Serializable {

    private final MessageType messageType;
    private final List<String> jsonPayloads;
    private final List<Class<?>> payloadsClasses;

    /**
     * Constructor of the class.
     *
     * @param messageType message type
     * @param payloads    0 or more payloads
     */
    public Message(MessageType messageType, Object... payloads) throws InvalidPayloadException {
        this.messageType = messageType;
        jsonPayloads = new ArrayList<>();
        payloadsClasses = new ArrayList<>();

        if (payloads.length > 0) objectToJson(payloads);
    }

    /**
     * Method to serialize payloads.
     * It serializes payloads using the GsonConverter singleton.
     * It saves the class type of the object as private variable to allow deserialization.
     *
     * @param payloads 0 or more payloads
     * @throws InvalidPayloadException if payloads are not serializable
     */
    private void objectToJson(Object... payloads) throws InvalidPayloadException {
        try {
            for (Object payload : payloads) {
                payloadsClasses.add(payload.getClass());
                jsonPayloads.add(GsonConverter.getInstance().getGson().toJson(payload, payload.getClass()));
            }
        } catch (Exception exception) {
            throw new InvalidPayloadException();
        }
    }

    /**
     * Getter of the message type.
     *
     * @return message type
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Getter of the payload at the specified index.
     *
     * @param index index of the payload
     * @return object contained ate the specified index
     * @throws EmptyPayloadException if index out of bounds
     */
    public Object getPayload(int index) throws EmptyPayloadException {
        try {
            return GsonConverter.getInstance().getGson().fromJson(jsonPayloads.get(index), payloadsClasses.get(index));
        } catch (Exception e) {
            throw new EmptyPayloadException();
        }
    }

    /**
     * Getter of the payload at index 0.
     *
     * @return object in first position
     * @throws EmptyPayloadException if payload doesn't exist
     */
    public Object getPayload() throws EmptyPayloadException {
        return getPayload(0);
    }

    /**
     * Getter of the list of payloads.
     *
     * @return list containing all the payloads
     */
    public List<Object> getListPayloads() {
        List<Object> objectPayloads = new ArrayList<>();
        for (int i = 0; i < jsonPayloads.size(); i++) {
            try {
                objectPayloads.add(getPayload(i));
            } catch (EmptyPayloadException ignored) {
            }
        }
        return objectPayloads;
    }


    /**
     * Getter of the array of payloads.
     *
     * @return array containing all the payloads
     */
    public Object[] getArrayPayloads() {
        return getListPayloads().toArray();
    }

    /**
     * To string method, to print the message.
     *
     * @return string version of the message
     */
    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", jsonPayloads=" + jsonPayloads +
                ", payloadClass=" + payloadsClasses +
                '}';
    }
}
