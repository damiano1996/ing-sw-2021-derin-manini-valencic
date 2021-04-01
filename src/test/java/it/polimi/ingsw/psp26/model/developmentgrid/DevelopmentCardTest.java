package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DevelopmentCardTest {

    private HashMap<Resource, Integer> cost;
    private DevelopmentCardType developmentCardType;
    private HashMap<Resource, Integer> productionCost;
    private HashMap<Resource, Integer> productionReturn;
    private int victoryPoints;

    private DevelopmentCard developmentCard;

    @Before
    public void setUp() {
        cost = new HashMap<>() {{
            put(Resource.SHIELD, 7);
        }};
        developmentCardType = new DevelopmentCardType(Color.GREEN, Level.THIRD);
        productionCost = new HashMap<>() {{
            put(Resource.SERVANT, 1);
        }};
        productionReturn = new HashMap<>() {{
            put(Resource.COIN, 1);
            put(Resource.FAITH_MARKER, 3);
        }};
        victoryPoints = 11;

        developmentCard = new DevelopmentCard(cost, developmentCardType, productionCost, productionReturn, victoryPoints);
    }

    @Test
    public void testGetCost() {
        assertEquals(cost, developmentCard.getCost());
    }

    @Test
    public void testGetDevelopmentCardType() {
        assertEquals(developmentCardType, developmentCard.getDevelopmentCardType());
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
        DevelopmentCard anotherDevelopmentCard = new DevelopmentCard(cost, developmentCardType, productionCost, productionReturn, victoryPoints);
        assertEquals(developmentCard, anotherDevelopmentCard);
    }

    @Test
    public void testEquals_FalseCase() {
        DevelopmentCard anotherDevelopmentCard = new DevelopmentCard(cost, new DevelopmentCardType(Color.BLUE, Level.FIRST), productionCost, productionReturn, victoryPoints);
        assertNotEquals(developmentCard, anotherDevelopmentCard);
    }
}