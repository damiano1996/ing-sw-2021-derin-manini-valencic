package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.exceptions.MustShuffleActionTokenStackException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BlackCrossShuffleActionTokenTest {

    DevelopmentCardsGrid developmentCardsGrid;
    FaithTrack faithTrack;
    BlackCrossShuffleActionToken blackCrossShuffleActionToken;

    @Before
    public void setUp() {
        VirtualView virtualView = new VirtualView();
        Player player = new Player(virtualView, "nickname", "sessionToken");
        virtualView.getMatchController().getMatch().addPlayer(player);

        developmentCardsGrid = new DevelopmentCardsGrid(virtualView);
        blackCrossShuffleActionToken = new BlackCrossShuffleActionToken();
        faithTrack = new FaithTrack(virtualView, "sessionToken");
    }

    @Test(expected = MustShuffleActionTokenStackException.class)
    public void testExecute() throws MustShuffleActionTokenStackException {
        blackCrossShuffleActionToken.execute(faithTrack, developmentCardsGrid);
        assertEquals(1, faithTrack.getBlackCrossPosition());
    }

    @Test
    public void testExecute_PositionTest() {
        try {
            blackCrossShuffleActionToken.execute(faithTrack, developmentCardsGrid);
        } catch (MustShuffleActionTokenStackException e) {
            assertEquals(1, faithTrack.getBlackCrossPosition());
        }
    }

    @Test
    public void testGetTokenName() {
        assertEquals("BlackCrossShuffleActionToken", blackCrossShuffleActionToken.toString());
    }

}