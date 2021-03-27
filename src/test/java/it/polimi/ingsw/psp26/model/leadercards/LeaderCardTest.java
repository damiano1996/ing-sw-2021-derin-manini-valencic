package it.polimi.ingsw.psp26.model.leadercards;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.ProductionAbility;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.SpecialAbility;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class LeaderCardTest {

    private HashMap<Resource, Integer> resourcesRequirements;
    private List<DevelopmentCard> developmentCardRequirements;
    private int victoryPoints;
    private SpecialAbility specialAbility;

    private LeaderCard leaderCard;

    @Before
    public void setUp() {
        resourcesRequirements = new HashMap<>() {{
            put(Resource.COIN, 1);
        }};

        HashMap<Resource, Integer> cost = new HashMap<>() {{
            put(Resource.SHIELD, 7);
        }};
        Color color = Color.GREEN;
        Level level = Level.THIRD;
        HashMap<Resource, Integer> productionCost = new HashMap<>() {{
            put(Resource.SERVANT, 1);
        }};
        HashMap<Resource, Integer> productionReturn = new HashMap<>() {{
            put(Resource.COIN, 1);
            put(Resource.FAITH_MARKER, 3);
        }};

        DevelopmentCard developmentCard = new DevelopmentCard(cost, color, level, productionCost, productionReturn, 11);
        developmentCardRequirements = new ArrayList<>() {{
            add(developmentCard);
        }};

        victoryPoints = 2;
        specialAbility = new ProductionAbility();

        leaderCard = new LeaderCard(resourcesRequirements, developmentCardRequirements, victoryPoints, specialAbility);
    }

    @Test
    public void testIsActive_and_activate() {
        assertFalse(leaderCard.isActive());
        leaderCard.activate();
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
    public void testGetSpecialAbility() {
        assertEquals(specialAbility, leaderCard.getSpecialAbility());
    }

    @Test
    public void testGetVictoryPoints() {
        assertEquals(victoryPoints, leaderCard.getVictoryPoints());
    }
}