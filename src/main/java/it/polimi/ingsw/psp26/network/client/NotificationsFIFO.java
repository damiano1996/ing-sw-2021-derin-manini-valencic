package it.polimi.ingsw.psp26.network.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationsFIFO {

    private static NotificationsFIFO instance;
    private final int maxFIFOSize;
    private final List<String> notifications;
    private boolean updated;

    private NotificationsFIFO(int maxFIFOSize) {
        this.maxFIFOSize = maxFIFOSize;
        notifications = new ArrayList<>();
        updated = true;
    }

    public synchronized static NotificationsFIFO getInstance() {
        if (instance == null) instance = new NotificationsFIFO(10);
        return instance;
    }

    public synchronized void pushNotification(String notification) {
        notifications.add(notification);
        if (notifications.size() > maxFIFOSize) notifications.remove(0);
        updated = true;
        notifyAll();
    }

    public synchronized List<String> getNotifications() {
        while (!updated) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        updated = false;
        return Collections.unmodifiableList(notifications);
    }

    public synchronized void resetFIFO() {
        instance = new NotificationsFIFO(10);
    }

}
