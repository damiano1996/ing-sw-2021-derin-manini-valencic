package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;

public class MessageUtils {


    /**
     * Generates a message to notify the VirtualView of the change of the model
     */
    public static SessionMessage updatePlayerMessage(String sessionToken) {
        try {
            return new SessionMessage(
                    sessionToken,
                    MessageType.PLAYER_MODEL
            );
        } catch (InvalidPayloadException ignored) {
        }
        return null;
    }

}
