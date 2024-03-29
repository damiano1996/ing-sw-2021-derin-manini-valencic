package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LorenzoWinException;
import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DiscardActionTokenTest {

    FaithTrack faithTrack;
    DevelopmentCardsGrid developmentCardsGrid;
    DiscardActionToken discardActionTokenGreen;

    @Before
    public void setUp() {
        VirtualView virtualView = new VirtualView();
        faithTrack = new FaithTrack(virtualView, "sessionToken");
        developmentCardsGrid = new DevelopmentCardsGrid(virtualView);
        discardActionTokenGreen = new DiscardActionToken(Color.GREEN);
    }


    //Token takes 2 cards from first level. User draws 2 cards from first level. No exception rised.
    @Test
    public void testExecute_TwoCardsInFirstLevelGreen_NoExceptionThrowing() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, LorenzoWinException {
        discardActionTokenGreen.execute(faithTrack, developmentCardsGrid);
        for (int i = 0; i < 2; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST);
    }

    //Token takes 2 cards from first level. User tries to draw 3 cards from first level. NoMoreDevelopmentCardException rised.
    @Test(expected = NoMoreDevelopmentCardsException.class)
    public void testExecute_TwoCardsInFirstLevelGreen_NoMoreDevelopmentCardsException() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, LorenzoWinException {
        discardActionTokenGreen.execute(faithTrack, developmentCardsGrid);
        for (int i = 0; i < 3; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST);
    }

    //Token takes 1 card from first level and 1 card from second level. User draws 3 cards from second level. No exception rised.
    @Test
    public void testExecute_OneCardFirstLevelOneCardSecondLevelGreen_NoExceptionThrowing() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, LorenzoWinException {
        for (int i = 0; i < 3; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST);
        discardActionTokenGreen.execute(faithTrack, developmentCardsGrid);
        for (int i = 0; i < 3; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.SECOND);
    }

    //Token takes 1 card from first level and 1 card from second level. User tries to draw 4 cards from second level. NoMoreDevelopmentCardsException rised.
    @Test(expected = NoMoreDevelopmentCardsException.class)
    public void testExecute_OneCardFirstLevelOneCardSecondLevelGreen_NoMoreDevelopmentCardsException() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, LorenzoWinException {
        for (int i = 0; i < 3; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST);
        discardActionTokenGreen.execute(faithTrack, developmentCardsGrid);
        for (int i = 0; i < 4; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.SECOND);
    }

    //Token takes 2 card from second level. User draws 2 cards from second level. No exception rised.
    @Test
    public void testExecute_TwoCardsInSecondLevelGreen_NoExceptionThrowing() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, LorenzoWinException {
        for (int i = 0; i < 4; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST);
        discardActionTokenGreen.execute(faithTrack, developmentCardsGrid);
        for (int i = 0; i < 2; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.SECOND);
    }

    //Token takes 2 card from second level. User tries to draw 3 cards from second level. NoMoreDevelopmentCardsException rised.
    @Test(expected = NoMoreDevelopmentCardsException.class)
    public void testExecute_TwoCardsInSecondLevelGreen_NoMoreDevelopmentCardsException() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, LorenzoWinException {
        for (int i = 0; i < 4; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST);
        discardActionTokenGreen.execute(faithTrack, developmentCardsGrid);
        for (int i = 0; i < 3; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.SECOND);
    }

    //Token takes 1 card from second level and 1 card from third level. User draws 3 cards from third level. No exception rised.
    @Test
    public void testExecute_OneCardSecondLevelOneCardThirdLevelGreen_NoExceptionThrowing() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, LorenzoWinException {
        for (int i = 0; i < 4; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST);
        for (int i = 0; i < 3; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.SECOND);
        discardActionTokenGreen.execute(faithTrack, developmentCardsGrid);
        for (int i = 0; i < 3; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.THIRD);
    }

    //Token takes 1 card from second level and 1 card from third level. User tries to draw 4 cards from third level. NoMoreDevelopmentCardsException rised.
    @Test(expected = NoMoreDevelopmentCardsException.class)
    public void testExecute_OneCardSecondLevelOneCardThirdLevelGreen_NoMoreDevelopmentCardsException() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, LorenzoWinException {
        for (int i = 0; i < 4; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST);
        for (int i = 0; i < 3; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.SECOND);
        discardActionTokenGreen.execute(faithTrack, developmentCardsGrid);
        for (int i = 0; i < 4; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.THIRD);
    }

    //Token takes 2 card from third level. User draws 2 cards from third level. No exception rised.
    @Test
    public void testExecute_TwoCardsInThirdLevelGreen_NoExceptionThrowing() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, LorenzoWinException {
        for (int i = 0; i < 4; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST);
        for (int i = 0; i < 4; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.SECOND);
        discardActionTokenGreen.execute(faithTrack, developmentCardsGrid);
        for (int i = 0; i < 2; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.THIRD);
    }

    //Token takes 2 card from third level. User tries to draw 3 cards from third level. NoMoreDevelopmentCardsException rised.
    @Test(expected = NoMoreDevelopmentCardsException.class)
    public void testExecute_TwoCardsInThirdLevelGreen_NoMoreDevelopmentCardsException() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, LorenzoWinException {
        for (int i = 0; i < 4; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST);
        for (int i = 0; i < 4; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.SECOND);
        discardActionTokenGreen.execute(faithTrack, developmentCardsGrid);
        for (int i = 0; i < 3; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.THIRD);
    }

    //Token takes all cards from third level.
    @Test(expected = LorenzoWinException.class)
    public void testExecute_EmptyColumnGreen_LorenzoWinException_OneCardLeft() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, LorenzoWinException {
        for (int i = 0; i < 4; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST);
        for (int i = 0; i < 4; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.SECOND);
        for (int i = 0; i < 3; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.THIRD);
        discardActionTokenGreen.execute(faithTrack, developmentCardsGrid);
    }

    //Token takes all cards from third level.
    @Test(expected = LorenzoWinException.class)
    public void testExecute_EmptyColumnGreen_LorenzoWinException_TwoCardsLeft() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, LorenzoWinException {
        for (int i = 0; i < 4; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST);
        for (int i = 0; i < 4; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.SECOND);
        for (int i = 0; i < 2; i++) developmentCardsGrid.drawCard(Color.GREEN, Level.THIRD);
        discardActionTokenGreen.execute(faithTrack, developmentCardsGrid);
    }

    @Test
    public void testGetTokenName() {
        assertEquals("DiscardActionToken-GREEN", discardActionTokenGreen.toString());
    }

}