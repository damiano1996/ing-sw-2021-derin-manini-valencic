package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LiveUpdateMessageTest {

    @Test
    public void testMessagePayloadExtraction() throws InvalidPayloadException, EmptyPayloadException {
        String contentMessage = "contentMessage";
        LiveUpdateMessage liveUpdateMessage = new LiveUpdateMessage("sessionToken", MessageType.GENERAL_MESSAGE, contentMessage);

        assertEquals(MessageType.LIVE_UPDATE, liveUpdateMessage.getMessageType());
        assertEquals(MessageType.GENERAL_MESSAGE, liveUpdateMessage.getPayload(0));
        assertEquals(contentMessage, liveUpdateMessage.getPayload(1));
    }
}