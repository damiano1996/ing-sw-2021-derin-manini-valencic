package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Class used to send notifications to the Clients
 */
public class NotificationUpdateMessage extends SessionMessage implements Serializable {

    /**
     * Constructor of the class.
     *
     * @param sessionToken session token
     * @param notification notification to send
     * @throws InvalidPayloadException if the given payloads are not serializable
     */
    public NotificationUpdateMessage(String sessionToken, String notification) throws InvalidPayloadException {
        super(sessionToken, MessageType.NOTIFICATION_UPDATE, decorateWithTime(notification));
    }

    /**
     * Adds the current time in the Notification Update Message
     *
     * @param notification The notification where to add the time decoration
     * @return The decorated String containing the notification
     */
    private static String decorateWithTime(String notification) {
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        String timeString = "<" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + ">\n";
        return timeString + notification;
    }
}
