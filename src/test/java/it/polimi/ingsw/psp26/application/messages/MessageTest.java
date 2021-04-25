package it.polimi.ingsw.psp26.application.messages;

import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
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
import static org.junit.Assert.assertEquals;

public class MessageTest {

    private MessageType messageType;
    private Message message;

    @Before
    public void setUp() {
        messageType = MessageType.GENERAL_MESSAGE;
        message = new Message(messageType, COIN, COIN, SHIELD);
    }

    @Test(expected = EmptyPayloadException.class)
    public void testNoPayload() throws EmptyPayloadException {
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

    @Test
    public void testGetPayloads() throws EmptyPayloadException {
        assertEquals(new ArrayList<>() {{
            add(COIN);
            add(COIN);
            add(SHIELD);
        }}, message.getListPayloads());
    }

    @Test
    public void testGetPayloads_ComplexObject() throws EmptyPayloadException {
        Player player = new Player(new VirtualView(), "nickname", "sessionToken");
        message = new Message(MessageType.PERSONAL_BOARD, player);
        assertEquals(player.getNickname(), ((Player) message.getPayload()).getNickname());
    }

    @Test
    public void testGetPayloads_ComplexObjectTwo() throws EmptyPayloadException {
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
}