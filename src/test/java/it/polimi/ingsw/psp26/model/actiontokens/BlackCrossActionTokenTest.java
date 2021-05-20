package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BlackCrossActionTokenTest {

    DevelopmentCardsGrid developmentCardsGrid;
    FaithTrack faithTrack;
    BlackCrossActionToken blackCrossActionToken;

    @Before
    public void setUp() {
        VirtualView virtualView = new VirtualView();
        Player player = new Player(virtualView, "nickname", "sessionToken");
        virtualView.getMatchController().getMatch().addPlayer(player);
        developmentCardsGrid = new DevelopmentCardsGrid(virtualView);
        blackCrossActionToken = new BlackCrossActionToken();
        faithTrack = new FaithTrack(virtualView, "sessionToken");
    }

    @Test
    public void testExecute() {
        blackCrossActionToken.execute(faithTrack, developmentCardsGrid);
        blackCrossActionToken.execute(faithTrack, developmentCardsGrid);
        assertEquals(4, faithTrack.getBlackCrossPosition());
    }

    @Test
    public void testGetTokenName() {
        assertEquals("BlackCrossActionToken", blackCrossActionToken.toString());
    }

}