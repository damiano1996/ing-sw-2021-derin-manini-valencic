package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;

public class MessageUtils {

    /**
     * Generates a message to notify the VirtualView of the change of the model
     * By assumption it will never return null
     */
    public static SessionMessage updateModelMessage(String sessionToken, MessageType messageType) {
        System.out.println("Message Utils - sending model update message");
        try {
            return new SessionMessage(
                    sessionToken,
                    messageType
            );
        } catch (InvalidPayloadException ignored) {
        }
        return null;
    }

}
