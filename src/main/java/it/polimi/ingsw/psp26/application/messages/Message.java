package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.application.GsonConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {

    private MessageType messageType;
    private List<String> jsonPayloads;
    private Class<?> payloadClass;

    public Message() {
    }

    public Message(MessageType messageType, Object... payloads) {
        this.messageType = messageType;
        objectToJson(payloads);
    }

    private void objectToJson(Object... payloads) {
        this.payloadClass = payloads[0].getClass();

        this.jsonPayloads = new ArrayList<>();
        for (Object payload : payloads)
            this.jsonPayloads.add(
                    GsonConverter.getInstance().getGson().toJson(payload, this.payloadClass)
            );
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public int getNumberOfPayloads() {
        return jsonPayloads.size();
    }

    public Object getPayload(int index) {
        return GsonConverter.getInstance().getGson().fromJson(jsonPayloads.get(index), payloadClass);
    }

    public Object getPayload() {
        return getPayload(0);
    }

    public List<Object> getListPayloads() {
        List<Object> objectPayloads = new ArrayList<>();
        for (int i = 0; i < jsonPayloads.size(); i++)
            objectPayloads.add(getPayload(i));
        return objectPayloads;
    }

    public Object[] getArrayPayloads() {
        return getListPayloads().toArray();
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType=" + messageType +
                ", jsonPayloads=" + jsonPayloads +
                ", payloadClass=" + payloadClass +
                '}';
    }
}
