package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.SpecialToken;

public class MessageUtils {


    public static SessionMessage getPlayerModelUpdateMessage(String sessionToken) {
        try {
            return new SessionMessage(sessionToken, MessageType.PLAYER_MODEL);
        } catch (InvalidPayloadException e) {
            return null;
        }
    }

    public static SessionMessage getMarketTrayModelUpdateMessage() {
        return getBroadcastSessionMessage(MessageType.MARKET_TRAY_MODEL);
    }

    public static SessionMessage getDevelopmentGridModelUpdateMessage() {
        return getBroadcastSessionMessage(MessageType.DEVELOPMENT_GRID_MODEL);
    }

    private static SessionMessage getBroadcastSessionMessage(MessageType messageType) {
        try {
            return new SessionMessage(SpecialToken.BROADCAST.getToken(), messageType);
        } catch (InvalidPayloadException e) {
            return null;
        }
    }
}
