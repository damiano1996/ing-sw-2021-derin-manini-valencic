package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGridCell;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DevelopmentGridCellTest {

    private int quantity;

    private Color color;
    private Level level;

    private DevelopmentCard developmentCard;

    private DevelopmentGridCell developmentGridCell;

    @Before
    public void setUp() {
        quantity = 3;
        HashMap<Resource, Integer> cost = new HashMap<>() {{
            put(Resource.SHIELD, 7);
        }};
        color = Color.GREEN;
        level = Level.THIRD;
        HashMap<Resource, Integer> productionCost = new HashMap<>() {{
            put(Resource.SERVANT, 1);
        }};
        HashMap<Resource, Integer> productionReturn = new HashMap<>() {{
            put(Resource.COIN, 1);
            put(Resource.FAITH_MARKERS, 3);
        }};
        int victoryPoints = 11;

        developmentCard = new DevelopmentCard(cost, color, level, productionCost, productionReturn, victoryPoints);
        developmentGridCell = new DevelopmentGridCell(color, level, quantity, cost, productionCost, productionReturn, victoryPoints);
    }

    @Test
    public void testGetColor() {
        assertEquals(color, developmentGridCell.getColor());
    }

    @Test
    public void testGetLevel() {
        assertEquals(level, developmentGridCell.getLevel());
    }

    @Test
    public void testDrawCard_StandardCase() throws NoMoreDevelopmentCardsException {
        DevelopmentCard drawnDevelopmentCard = developmentGridCell.drawCard();
        assertTrue(drawnDevelopmentCard.equals(developmentCard));
    }

    @Test(expected = NoMoreDevelopmentCardsException.class)
    public void testDrawCard_NoMoreDevelopmentCardsException() throws NoMoreDevelopmentCardsException {
        for (int i = 0; i < quantity + 1; i++)
            developmentGridCell.drawCard();
    }

    @Test
    public void testIsEmpty() throws NoMoreDevelopmentCardsException {
        for (int i = 0; i < quantity; i++)
            developmentGridCell.drawCard();
        assertTrue(developmentGridCell.isEmpty());
    }
}