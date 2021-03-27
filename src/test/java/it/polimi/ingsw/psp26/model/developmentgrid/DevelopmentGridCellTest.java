package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DevelopmentGridCellTest {

    private DevelopmentCardType developmentCardType;

    private int cardsInCell;

    private DevelopmentGridCell developmentGridCell;

    @Before
    public void setUp() {
        developmentCardType = new DevelopmentCardType(Color.GREEN, Level.THIRD);

        cardsInCell = 4; // by rules

        developmentGridCell = new DevelopmentGridCell(developmentCardType);
    }

    @Test
    public void testGetColor() {
        assertEquals(developmentCardType, developmentGridCell.getDevelopmentCardType());
    }

    @Test
    public void testGetLevel() {
        assertEquals(developmentCardType, developmentGridCell.getDevelopmentCardType());
    }

    @Test
    public void testDrawCard_StandardCase() throws NoMoreDevelopmentCardsException {
        DevelopmentCard drawnDevelopmentCard = developmentGridCell.drawCard();
        assertNotNull(drawnDevelopmentCard);
        assertEquals(developmentCardType, drawnDevelopmentCard.getDevelopmentCardType());
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