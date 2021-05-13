package it.polimi.ingsw.psp26.view;

import it.polimi.ingsw.psp26.application.messages.MessageType;

import java.util.Locale;

public class ViewUtils {

    public static String beautifyMessageType(MessageType messageType) {
        String message = messageType.name();
        message = message.replace('_', ' ');
        return toTitleStyle(message);
    }

    public static String toTitleStyle(String string) {
        StringBuilder newString = new StringBuilder();

        boolean space = true;
        for (Character character : string.toLowerCase(Locale.ROOT).toCharArray()) {
            newString.append((space) ? Character.toUpperCase(character) : character);
            space = character == ' ';
        }
        return newString.toString();
    }
}
