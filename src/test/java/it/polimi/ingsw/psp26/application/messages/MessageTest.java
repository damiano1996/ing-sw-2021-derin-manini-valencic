package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static it.polimi.ingsw.psp26.model.enums.Resource.COIN;
import static it.polimi.ingsw.psp26.model.enums.Resource.SHIELD;
import static org.junit.Assert.assertEquals;

public class MessageTest {
    private String sessionToken;
    private MessageType messageType;
    private Message message;
    private Map<String, Object> payload;

    @Before
    public void setUp() {
        sessionToken = "sessionToken";
        messageType = MessageType.GENERAL_MESSAGE;
        message = new Message(sessionToken, messageType, COIN, COIN, SHIELD);
    }

    @Test
    public void testGetSessionToken() {
        assertEquals(sessionToken, message.getSessionToken());
    }

    @Test
    public void testGetMessageType() {
        assertEquals(messageType, message.getMessageType());
    }

    @Test
    public void testGetPayload() {
        assertEquals(COIN, message.getPayload());
    }

    @Test
    public void testGetPayloads() {
        assertEquals(new ArrayList<>() {{
            add(COIN);
            add(COIN);
            add(SHIELD);
        }}, message.getPayloads());
    }

    @Test
    public void testGetPayloads_ComplexObject() {
        Player player = new Player(new VirtualView(), "nickname", "sessionToken");
        message = new Message(player.getSessionToken(), MessageType.PERSONAL_BOARD, player);
        assertEquals(player.getNickname(), ((Player) message.getPayload()).getNickname());
    }
}