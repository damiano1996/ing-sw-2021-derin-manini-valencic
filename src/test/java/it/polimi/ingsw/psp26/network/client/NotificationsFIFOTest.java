package it.polimi.ingsw.psp26.network.client;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NotificationsFIFOTest {

    private int maxSize;
    private NotificationsFIFO notificationsFIFO;

    @Before
    public void setUp() throws Exception {
        maxSize = 5;
        notificationsFIFO = new NotificationsFIFO(maxSize);
    }

    @Test
    public void testPushAndGetNotification() {
        for (int i = 0; i < maxSize + 1; i++) {
            String notification = "notification_" + i;
            notificationsFIFO.pushNotification(notification);
            assertEquals(Math.min(i + 1, maxSize), notificationsFIFO.getNotifications().size());
            assertEquals(notification, notificationsFIFO.getNotifications().get(Math.min(i, maxSize - 1)));
        }
    }

}