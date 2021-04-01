package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.exceptions.NegativeNumberOfElementsToDrawException;
import it.polimi.ingsw.psp26.exceptions.PlayerDoesNotExistException;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MatchTest {

    private Match match;
    private VirtualView virtualView;
    private int id;
    private List<Player> multiPlayersList;
    private List<Player> singlePlayerList;

    @Before
    public void setUp() {
        virtualView = new VirtualView();
        id = 0;
        multiPlayersList = new ArrayList<>() {{
            add(new Player(virtualView, "nickname0", "sessionToken0"));
            add(new Player(virtualView, "nickname1", "sessionToken1"));
            add(new Player(virtualView, "nickname2", "sessionToken2"));
        }};

        singlePlayerList = new ArrayList<>() {{
            add(new Player(virtualView, "nickname0", "sessionToken0"));
        }};
    }

    @Test
    public void testGetId() {
        match = new Match(virtualView, id, multiPlayersList);
        assertEquals(id, match.getId());
    }

    @Test
    public void testGetPlayers() {
        match = new Match(virtualView, id, multiPlayersList);
        assertEquals(multiPlayersList, match.getPlayers());
    }

    @Test
    public void testIsMultiPlayerMode_TrueCase() {
        match = new Match(virtualView, id, multiPlayersList);
        assertTrue(match.isMultiPlayerMode());
    }

    @Test
    public void testIsMultiPlayerMode_FalseCase() {
        match = new Match(virtualView, id, singlePlayerList);
        assertFalse(match.isMultiPlayerMode());
    }

    @Test
    public void testGetPlayerByNickname() throws PlayerDoesNotExistException {
        int playerIndex = 0;
        String nickname = multiPlayersList.get(playerIndex).getNickname();

        match = new Match(virtualView, id, multiPlayersList);
        assertEquals(multiPlayersList.get(playerIndex), match.getPlayerByNickname(nickname));
    }

    @Test(expected = PlayerDoesNotExistException.class)
    public void testGetPlayerByNickname_ExceptionCase() throws PlayerDoesNotExistException {
        match = new Match(virtualView, id, multiPlayersList);
        match.getPlayerByNickname(null);
    }

    @Test
    public void testGetResourceSupply() {
        match = new Match(virtualView, id, multiPlayersList);
        assertNotNull(match.getResourceSupply());
    }

    @Test
    public void testGetDevelopmentGrid() {
        match = new Match(virtualView, id, multiPlayersList);
        assertNotNull(match.getDevelopmentGrid());
    }

    @Test
    public void testGetMarketTray() {
        match = new Match(virtualView, id, multiPlayersList);
        assertNotNull(match.getMarketTray());
    }

    @Test(expected = NegativeNumberOfElementsToDrawException.class)
    public void testDrawLeaders_NegativeNumberOfElementsToDrawException() throws NegativeNumberOfElementsToDrawException {
        int numberOfCardsToDraw = -1;
        match = new Match(virtualView, id, multiPlayersList);
        List<LeaderCard> drawnCards = match.drawLeaders(numberOfCardsToDraw);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDrawLeaders_IndexOutOfBoundsException() throws NegativeNumberOfElementsToDrawException, IndexOutOfBoundsException {
        int numberOfCardsToDraw = 1000;
        match = new Match(virtualView, id, multiPlayersList);
        List<LeaderCard> drawnCards = match.drawLeaders(numberOfCardsToDraw);
    }

    @Test
    public void testDrawLeaders_StandardCase() throws NegativeNumberOfElementsToDrawException {
        int numberOfCardsToDraw = 4;
        match = new Match(virtualView, id, multiPlayersList);
        List<LeaderCard> drawnCards = match.drawLeaders(numberOfCardsToDraw);
        assertEquals(drawnCards.size(), numberOfCardsToDraw);
    }

    @Test(expected = NegativeNumberOfElementsToDrawException.class)
    public void testDrawActionTokens_NegativeNumberOfElementsToDrawException() throws NegativeNumberOfElementsToDrawException {
        int numberOfTokensToDraw = -1;
        match = new Match(virtualView, id, multiPlayersList);
        List<ActionToken> drawnTokens = match.drawActionTokens(numberOfTokensToDraw);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDrawActionTokens_IndexOutOfBoundsException() throws NegativeNumberOfElementsToDrawException, IndexOutOfBoundsException {
        int numberOfTokensToDraw = 1000;
        match = new Match(virtualView, id, multiPlayersList);
        List<ActionToken> drawnTokens = match.drawActionTokens(numberOfTokensToDraw);
    }

    @Test
    public void testDrawActionTokens_StandardCase() throws NegativeNumberOfElementsToDrawException {
        int numberOfTokensToDraw = 1;
        match = new Match(virtualView, id, multiPlayersList);
        List<ActionToken> drawnTokens = match.drawActionTokens(numberOfTokensToDraw);
        assertEquals(drawnTokens.size(), numberOfTokensToDraw);
    }

}