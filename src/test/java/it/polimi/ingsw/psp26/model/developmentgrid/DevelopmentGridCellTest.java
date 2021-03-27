package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DevelopmentGridCellTest {

    private Color color;
    private Level level;

    private int cardsInCell;

    private DevelopmentGridCell developmentGridCell;

    @Before
    public void setUp() {
        color = Color.GREEN;
        level = Level.THIRD;

        cardsInCell = 4; // by rules

        developmentGridCell = new DevelopmentGridCell(color, level);
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
        assertNotNull(drawnDevelopmentCard);
        assertEquals(color, drawnDevelopmentCard.getColor());
        assertEquals(level, drawnDevelopmentCard.getLevel());
    }

    @Test(expected = NoMoreDevelopmentCardsException.class)
    public void testDrawCard_NoMoreDevelopmentCardsException() throws NoMoreDevelopmentCardsException {
        for (int i = 0; i < cardsInCell + 1; i++)
            developmentGridCell.drawCard();
    }

    @Test
    public void testIsEmpty() throws NoMoreDevelopmentCardsException {
        for (int i = 0; i < cardsInCell; i++)
            developmentGridCell.drawCard();
        assertTrue(developmentGridCell.isEmpty());
    }
}