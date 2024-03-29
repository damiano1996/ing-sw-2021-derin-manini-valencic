package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.ProductionAbility;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.SpecialAbility;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.psp26.model.enums.Resource.COIN;
import static it.polimi.ingsw.psp26.model.enums.Resource.SHIELD;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MessageTest {

    private MessageType messageType;
    private Message message;

    @Before
    public void setUp() throws InvalidPayloadException {
        messageType = MessageType.GENERAL_MESSAGE;
        message = new Message(messageType, COIN, COIN, SHIELD);
    }

    @Test(expected = EmptyPayloadException.class)
    public void testNoPayload() throws EmptyPayloadException, InvalidPayloadException {
        Message message = new Message(MessageType.GENERAL_MESSAGE);
        message.getPayload();
    }

    @Test
    public void testGetMessageType() {
        assertEquals(messageType, message.getMessageType());
    }

    @Test
    public void testGetPayload() throws EmptyPayloadException {
        assertEquals(COIN, message.getPayload());
    }

    @Test(expected = EmptyPayloadException.class)
    public void testEmptyPayload() throws EmptyPayloadException, InvalidPayloadException {
        message = new Message(messageType);
        message.getPayload();
    }

    @Test
    public void testEmptyPayloadList() throws InvalidPayloadException {
        message = new Message(MessageType.GENERAL_MESSAGE);
        assertEquals(new ArrayList<>(), message.getListPayloads());
    }

    @Test
    public void testEmptyPayloadArray() throws InvalidPayloadException {
        message = new Message(MessageType.GENERAL_MESSAGE);
        assertArrayEquals(new Object[]{}, message.getArrayPayloads());
    }

    @Test
    public void testGetPayloads() {
        assertEquals(new ArrayList<>() {{
            add(COIN);
            add(COIN);
            add(SHIELD);
        }}, message.getListPayloads());
    }

    @Test
    public void testGetPayloads_ComplexObject() throws EmptyPayloadException, InvalidPayloadException {
        Player player = new Player(new VirtualView(), "nickname", "sessionToken");
        message = new Message(MessageType.PLAYER_MODEL, player);
        assertEquals(player.getNickname(), ((Player) message.getPayload()).getNickname());
    }

    @Test
    public void testGetPayloads_ComplexObjectTwo() throws EmptyPayloadException, InvalidPayloadException {
        Map<Resource, Integer> resourcesRequirements = new HashMap<>() {{
            put(Resource.COIN, 1);
        }};

        Map<DevelopmentCardType, Integer> developmentCardRequirements = new HashMap<>() {{
            put(new DevelopmentCardType(Color.GREEN, Level.FIRST), 2);
        }};

        int victoryPoints = 2;
        SpecialAbility specialAbility = new ProductionAbility(Resource.COIN);

        LeaderCard leaderCard = new LeaderCard(resourcesRequirements, developmentCardRequirements, victoryPoints, specialAbility);

        message = new Message(MessageType.CHOICE_LEADERS, (Object[]) new LeaderCard[]{leaderCard, leaderCard});
        assertEquals(leaderCard, message.getPayload());
    }

    @Test(expected = InvalidPayloadException.class)
    public void testDifferentPayloadsClassType_ExceptionCase() throws InvalidPayloadException {
        message = new Message(MessageType.GENERAL_MESSAGE, 3, COIN, new VirtualView());
    }

    @Test
    public void testDifferentPayloadsClassType() throws EmptyPayloadException, InvalidPayloadException {
        message = new Message(MessageType.GENERAL_MESSAGE, 3, COIN);
        assertEquals(3, message.getPayload(0));
        assertEquals(COIN, message.getPayload(1));
    }
}