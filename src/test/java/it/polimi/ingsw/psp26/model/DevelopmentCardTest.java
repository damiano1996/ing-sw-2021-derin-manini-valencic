package it.polimi.ingsw.psp26.model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class DevelopmentCardTest {

    private HashMap<Resource, Integer> cost;
    private Color color;
    private Level level;
    private HashMap<Resource, Integer> productionCost;
    private HashMap<Resource, Integer> productionReturn;
    private int victoryPoints;

    private DevelopmentCard developmentCard;

    @Before
    public void setUp() {
        cost = new HashMap<>() {{
            put(Resource.SHIELD, 7);
        }};
        color = Color.GREEN;
        level = Level.THIRD;
        productionCost = new HashMap<>() {{
            put(Resource.SERVANT, 1);
        }};
        productionReturn = new HashMap<>() {{
            put(Resource.COIN, 1);
            put(Resource.FAITH_MARKERS, 3);
        }};
        victoryPoints = 11;

        developmentCard = new DevelopmentCard(cost, color, level, productionCost, productionReturn, victoryPoints);
    }

    @Test
    public void testGetCost() {
        assertEquals(cost, developmentCard.getCost());
    }

    @Test
    public void testGetColor() {
        assertEquals(color, developmentCard.getColor());
    }

    @Test
    public void testGetLevel() {
        assertEquals(level, developmentCard.getLevel());
    }

    @Test
    public void testGetProductionCost() {
        assertEquals(productionCost, developmentCard.getProductionCost());
    }

    @Test
    public void testGetProductionReturn() {
        assertEquals(productionReturn, developmentCard.getProductionReturn());
    }

    @Test
    public void testGetVictoryPoints() {
        assertEquals(victoryPoints, developmentCard.getVictoryPoints());
    }

    @Test
    public void testEquals_TrueCase() {
        DevelopmentCard AnotherDevelopmentCard = new DevelopmentCard(cost, color, level, productionCost, productionReturn, victoryPoints);
        assertTrue(developmentCard.equals(AnotherDevelopmentCard));
    }

    @Test
    public void testEquals_FalseCase() {
        Color differentColor = Color.RED;
        DevelopmentCard AnotherDevelopmentCard = new DevelopmentCard(cost, differentColor, level, productionCost, productionReturn, victoryPoints);
        assertFalse(developmentCard.equals(AnotherDevelopmentCard));
    }
}