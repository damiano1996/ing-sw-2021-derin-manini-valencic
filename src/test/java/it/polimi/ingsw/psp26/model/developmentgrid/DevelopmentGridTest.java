package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.network.server.Server;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DevelopmentGridTest {

    private DevelopmentGrid developmentGrid;

    @Before
    public void setUp() {
        developmentGrid = new DevelopmentGrid(new VirtualView(new Server()));
    }

    @Test
    public void testDrawCard_StandardCase() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException {
        Color color = Color.GREEN;
        Level level = Level.THIRD;

        DevelopmentCard developmentCard = developmentGrid.drawCard(color, level);
        assertEquals(color, developmentCard.getDevelopmentCardType().getColor());
        assertEquals(level, developmentCard.getDevelopmentCardType().getLevel());
    }

    @Test(expected = NoMoreDevelopmentCardsException.class)
    public void testDrawCard_NoMoreDevelopmentCardsException() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException {
        Color color = Color.GREEN;
        Level level = Level.THIRD;

        // drawing all the cards
        while (developmentGrid.isAvailable(color, level))
            developmentGrid.drawCard(color, level);

        // drawing one more to cause exception
        developmentGrid.drawCard(color, level);
    }

    @Test
    public void testIsAvailable() throws LevelDoesNotExistException, ColorDoesNotExistException {
        Color color = Color.GREEN;
        Level level = Level.THIRD;

        assertTrue(developmentGrid.isAvailable(color, level));

        int numberOfCardsToDraw = 1000;
        try {

            for (int i = 0; i < numberOfCardsToDraw; i++)
                developmentGrid.drawCard(color, level);

        } catch (NoMoreDevelopmentCardsException e) {
            assertFalse(developmentGrid.isAvailable(color, level));
        }
    }
}