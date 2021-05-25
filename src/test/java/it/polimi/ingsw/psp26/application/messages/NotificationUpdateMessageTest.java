package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NotificationUpdateMessageTest {

    @Test
    public void testMessagePayloadExtraction() throws InvalidPayloadException, EmptyPayloadException {
        String contentMessage = "contentMessage";
        NotificationUpdateMessage liveUpdateMessage = new NotificationUpdateMessage("sessionToken", contentMessage);

        assertEquals(MessageType.NOTIFICATION_UPDATE, liveUpdateMessage.getMessageType());
        assertTrue(((String) liveUpdateMessage.getPayload()).contains(contentMessage));
    }
}