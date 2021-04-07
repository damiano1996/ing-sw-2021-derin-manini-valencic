package it.polimi.ingsw.psp26.application.messages;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class MessageTest {
    private String sessionToken;
    private MessageType messageType;
    private Message message;
    private HashMap<String, Object> object;

    @Before
    public void setUp() {
        sessionToken = "sessionToken";
        messageType = MessageType.GENERAL_MESSAGE;
        object = new HashMap<>();
        message = new Message(sessionToken, messageType, object);
    }

    @Test
    public void testGetSessionToken() {
        assertEquals(sessionToken, message.getSessionToken());
    }

    @Test
    public void testGetMessageType() {
        assertEquals(messageType, message.getMessageType());
    }

    @Test
    public void testGetPayload() {
        assertEquals(object, message.getPayload());
    }
}