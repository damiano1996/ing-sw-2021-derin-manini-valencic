package it.polimi.ingsw.psp26.network.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that represent a FIFO containing the notifications send by the Server to the Client.
 */
public class NotificationsFIFO {

    private static NotificationsFIFO instance;
    private final int maxFIFOSize;
    private List<String> notifications;
    private boolean updated;

    /**
     * Constructor of the class.
     * It sets the maxFIFOSize and set the updated attribute to true.
     * It also create a new notifications List.
     *
     * @param maxFIFOSize the maximum number of notifications that can be contained in the NotificationsFIFO
     */
    private NotificationsFIFO(int maxFIFOSize) {
        this.maxFIFOSize = maxFIFOSize;
        notifications = new ArrayList<>();
        updated = true;
    }


    /**
     * Getter of the NotificationsFIFO instance.
     *
     * @return the NotificationsFIFO instance
     */
    public synchronized static NotificationsFIFO getInstance() {
        if (instance == null) instance = new NotificationsFIFO(10);
        return instance;
    }


    /**
     * Push a new notification in the FIFO.
     * If the size of the FIFO grows over the maxFIFOSize, removes the first element of the FIFO.
     *
     * @param notification the notification to insert
     */
    public synchronized void pushNotification(String notification) {
        notifications.add(notification);
        if (notifications.size() > maxFIFOSize) notifications.remove(0);
        updated = true;
        notifyAll();
    }


    /**
     * Getter of all the notifications contained in the notificationFIFO.
     *
     * @return an unmodifiable List of all the contained notifications
     */
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


    /**
     * Creates a new List of notifications.
     */
    public synchronized void resetFIFO() {
        notifications = new ArrayList<>();
    }

}
