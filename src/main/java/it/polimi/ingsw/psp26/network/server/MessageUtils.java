package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.SpecialToken;

public class MessageUtils {

    /**
     * Used to create a ModelUpdateMessage for the Player in order to notify the CachedModel
     *
     * @param sessionToken The Player's SessionToken
     * @return A new Player ModelUpdateMessage
     */
    public static SessionMessage getPlayerModelUpdateMessage(String sessionToken) {
        try {
            return new SessionMessage(sessionToken, MessageType.PLAYER_MODEL);
        } catch (InvalidPayloadException e) {
            return null;
        }
    }


    /**
     * Used to create a ModelUpdateMessage for the MarketTray in order to notify the CachedModel
     *
     * @return A new MarketTray ModelUpdateMessage
     */
    public static SessionMessage getMarketTrayModelUpdateMessage() {
        return getBroadcastSessionMessage(MessageType.MARKET_TRAY_MODEL);
    }


    /**
     * Used to create a ModelUpdateMessage for the DevelopmentCardGrid in order to notify the CachedModel
     *
     * @return A new DevelopmentCardGrid ModelUpdateMessage
     */
    public static SessionMessage getDevelopmentGridModelUpdateMessage() {
        return getBroadcastSessionMessage(MessageType.DEVELOPMENT_GRID_MODEL);
    }


    /**
     * Used to create a broadcast Message
     *
     * @param messageType The MessageType to insert in the broadcast
     * @return The new broadcast SessionMessage
     */
    private static SessionMessage getBroadcastSessionMessage(MessageType messageType) {
        try {
            return new SessionMessage(SpecialToken.BROADCAST.getToken(), messageType);
        } catch (InvalidPayloadException e) {
            return null;
        }
    }
    
}
