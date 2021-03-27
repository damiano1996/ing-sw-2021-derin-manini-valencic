package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BlackCrossActionTokenTest {

    DevelopmentGrid developmentGrid;
    FaithTrack faithTrack;
    BlackCrossActionToken blackCrossActionToken;

    @Before
    public void setUp() {
        developmentGrid = new DevelopmentGrid();
        blackCrossActionToken = new BlackCrossActionToken();
        faithTrack = new FaithTrack();
    }

    @Test
    public void testExecute() {
        blackCrossActionToken.execute(faithTrack, developmentGrid);
        blackCrossActionToken.execute(faithTrack, developmentGrid);
        assertEquals(4, faithTrack.getBlackCrossPosition());
    }

}