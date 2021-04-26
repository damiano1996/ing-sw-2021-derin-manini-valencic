package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SessionMessageTest {

    private String sessionToken;

    private SessionMessage sessionMessage;

    @Before
    public void setUp() throws InvalidPayloadException {
        sessionToken = "sessionToken";
        sessionMessage = new SessionMessage(sessionToken, MessageType.GENERAL_MESSAGE);
    }

    @Test
    public void getSessionToken() {
        assertEquals(sessionToken, sessionMessage.getSessionToken());
    }

    @Test
    public void setSessionToken() {
        String newSessionToken = "NewSessionToken";
        sessionMessage.setSessionToken(newSessionToken);
        assertEquals(newSessionToken, sessionMessage.getSessionToken());
    }
}