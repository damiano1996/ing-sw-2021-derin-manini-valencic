package it.polimi.ingsw.psp26.network.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationsFIFO {

    private final int maxFIFOSize;
    private final List<String> notifications;

    public NotificationsFIFO(int maxFIFOSize) {
        this.maxFIFOSize = maxFIFOSize;
        notifications = new ArrayList<>();
    }

    public void pushNotification(String notification) {
        notifications.add(notification);
        if (notifications.size() > maxFIFOSize)
            notifications.remove(0);
    }

    public List<String> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }
}
