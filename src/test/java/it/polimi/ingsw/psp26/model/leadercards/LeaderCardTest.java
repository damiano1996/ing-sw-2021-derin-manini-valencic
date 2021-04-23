package it.polimi.ingsw.psp26.model.leadercards;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.ProductionAbility;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.SpecialAbility;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class LeaderCardTest {

    private HashMap<Resource, Integer> resourcesRequirements;
    private HashMap<DevelopmentCardType, Integer> developmentCardRequirements;
    private int victoryPoints;
    private SpecialAbility specialAbility;

    private LeaderCard leaderCard;

    @Before
    public void setUp() {
        resourcesRequirements = new HashMap<>() {{
            put(Resource.COIN, 1);
        }};

        developmentCardRequirements = new HashMap<>() {{
            put(new DevelopmentCardType(Color.GREEN, Level.FIRST), 2);
        }};

        victoryPoints = 2;
        specialAbility = new ProductionAbility(Resource.COIN);

        leaderCard = new LeaderCard(resourcesRequirements, developmentCardRequirements, victoryPoints, specialAbility);
    }

    @Test
    public void testIsActive_and_activate() {
        assertFalse(leaderCard.isActive());
        leaderCard.activate(new Player(new VirtualView(), "nickname", "sessionToken"));
        assertTrue(leaderCard.isActive());
    }

    @Test
    public void testGetResourcesRequirements() {
        assertEquals(resourcesRequirements, leaderCard.getResourcesRequirements());
    }

    @Test
    public void testGetDevelopmentCardRequirements() {
        assertEquals(developmentCardRequirements, leaderCard.getDevelopmentCardRequirements());
    }

    @Test
    public void testGetVictoryPoints() {
        assertEquals(victoryPoints, leaderCard.getVictoryPoints());
    }


    @Test
    public void testGetAbilityResource() {
        assertEquals(Resource.COIN, leaderCard.getAbilityResource());
    }

    @Test
    public void testEquals_FirstCase() {
        assertEquals(leaderCard, leaderCard);
    }

    @Test
    public void testEquals_SecondCase() {
        LeaderCard secondLeader = new LeaderCard(new HashMap<>() {{
            put(Resource.COIN, 1);
        }}, new HashMap<>() {{
            put(new DevelopmentCardType(Color.GREEN, Level.FIRST), 2);
        }}, 2, new ProductionAbility(Resource.COIN));
        assertEquals(leaderCard, secondLeader);
    }
}