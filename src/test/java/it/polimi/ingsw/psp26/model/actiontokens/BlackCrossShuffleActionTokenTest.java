package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.exceptions.MustShuffleActionTokenStackException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BlackCrossShuffleActionTokenTest {

    DevelopmentGrid developmentGrid;
    FaithTrack faithTrack;
    BlackCrossShuffleActionToken blackCrossShuffleActionToken;

    @Before
    public void setUp() {
        VirtualView virtualView = new VirtualView();

        developmentGrid = new DevelopmentGrid(virtualView);
        blackCrossShuffleActionToken = new BlackCrossShuffleActionToken();
        faithTrack = new FaithTrack(virtualView);
    }

    @Test(expected = MustShuffleActionTokenStackException.class)
    public void testExecute() throws MustShuffleActionTokenStackException {
        blackCrossShuffleActionToken.execute(faithTrack, developmentGrid);
        assertEquals(1, faithTrack.getBlackCrossPosition());
    }

    @Test
    public void testExecute_PositionTest() {
        try {
            blackCrossShuffleActionToken.execute(faithTrack, developmentGrid);
        } catch (MustShuffleActionTokenStackException e) {
            assertEquals(1, faithTrack.getBlackCrossPosition());
        }
    }

}