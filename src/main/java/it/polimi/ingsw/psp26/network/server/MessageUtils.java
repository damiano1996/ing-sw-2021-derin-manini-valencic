package it.polimi.ingsw.psp26.network.server;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.NetworkNode;
import it.polimi.ingsw.psp26.network.SpecialToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Method to receive session messages from network node.
     * It listens until a message, different from the one in the black list, is received.
     * Then it returns the first white message received.
     *
     * @param networkNode   Network node from which object data will be received
     * @param blackMessages List of message types that must be avoided
     * @return Session message
     * @throws IOException            If error in IO socket communication
     * @throws ClassNotFoundException If object class not found
     */
    public static SessionMessage filterMessages(NetworkNode networkNode, List<MessageType> blackMessages) throws IOException, ClassNotFoundException {
        SessionMessage message = (SessionMessage) networkNode.receiveData();
        while (blackMessages.contains(message.getMessageType()))
            message = (SessionMessage) networkNode.receiveData();

        return message;
    }

    /**
     * Method to filter heartbeat messages.
     * It listens messages from the network node and returns the first session message different from the heartbeat.
     *
     * @param networkNode Network node from which object data will be received
     * @return Session message
     * @throws IOException            If error in IO socket communication
     * @throws ClassNotFoundException If object class not found
     */
    public static SessionMessage filterHeartbeatMessages(NetworkNode networkNode) throws IOException, ClassNotFoundException {
        return filterMessages(networkNode, new ArrayList<>() {{
            add(MessageType.HEARTBEAT);
        }});
    }

    /**
     * Method to filter all messages looking for the given message type.
     * It returns the first session message, received from the network node, which is of the expected message type.
     *
     * @param networkNode         Network node from which object data will be received
     * @param expectedMessageType Expected message type
     * @return Session message
     * @throws IOException            If error in IO socket communication
     * @throws ClassNotFoundException If object class not found
     */
    public static SessionMessage lookingForMessage(NetworkNode networkNode, MessageType expectedMessageType) throws IOException, ClassNotFoundException {
        SessionMessage message = (SessionMessage) networkNode.receiveData();
        while (!message.getMessageType().equals(expectedMessageType))
            message = (SessionMessage) networkNode.receiveData();

        return message;
    }

}
