package it.polimi.ingsw.psp26.network.client;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class NotificationsFIFOTest {

    @Test
    public void testPushAndGetNotification() {
        int maxSize = 10;
        
        for (int i = 0; i < maxSize + 1; i++) {
            String notification = "notification_" + i;
            NotificationsFIFO.getInstance().pushNotification(notification);
            List<String> notifications = NotificationsFIFO.getInstance().getNotifications();
            assertEquals(Math.min(i + 1, maxSize), notifications.size());
            assertEquals(notification, notifications.get(Math.min(i, maxSize - 1)));
        }
    }

}