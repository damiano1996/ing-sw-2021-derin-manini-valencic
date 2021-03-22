package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.exceptions.NegativeNumberOfCardsToDrawException;
import it.polimi.ingsw.psp26.exceptions.PlayerDoesNotExistException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MatchTest {

    private Match match;
    private int id;
    private List<Player> multiPlayersList;
    private List<Player> singlePlayerList;

    @Before
    public void setUp() {
        id = 0;
        multiPlayersList = new ArrayList<>() {{
            add(new Player("nickname0", "sessionToken0"));
            add(new Player("nickname1", "sessionToken1"));
            add(new Player("nickname2", "sessionToken2"));
        }};

        singlePlayerList = new ArrayList<>() {{
            add(new Player("nickname0", "sessionToken0"));
        }};
    }

    @Test
    public void testGetId() {
        match = new Match(id, multiPlayersList);
        assertEquals(id, match.getId());
    }

    @Test
    public void testGetPlayers() {
        match = new Match(id, multiPlayersList);
        assertEquals(multiPlayersList, match.getPlayers());
    }

    @Test
    public void testIsMultiPlayerMode_TrueCase() {
        match = new Match(id, multiPlayersList);
        assertTrue(match.isMultiPlayerMode());
    }

    @Test
    public void testIsMultiPlayerMode_FalseCase() {
        match = new Match(id, singlePlayerList);
        assertFalse(match.isMultiPlayerMode());
    }

    @Test
    public void testGetPlayerByNickname() throws PlayerDoesNotExistException {
        int playerIndex = 0;
        String nickname = multiPlayersList.get(playerIndex).getNickname();

        match = new Match(id, multiPlayersList);
        assertEquals(multiPlayersList.get(playerIndex), match.getPlayerByNickname(nickname));
    }

    @Test(expected = PlayerDoesNotExistException.class)
    public void testGetPlayerByNickname_ExceptionCase() throws PlayerDoesNotExistException {
        match = new Match(id, multiPlayersList);
        match.getPlayerByNickname(null);
    }

    @Test
    public void testGetResourceSupply() {
        match = new Match(id, multiPlayersList);
        assertNotNull(match.getResourceSupply());
    }

    @Test
    public void testGetDevelopmentGrid() {
        match = new Match(id, multiPlayersList);
        assertNotNull(match.getDevelopmentGrid());
    }

    @Test
    public void testGetMarketTray() {
        match = new Match(id, multiPlayersList);
        assertNotNull(match.getMarketTray());
    }

    @Test(expected = NegativeNumberOfCardsToDrawException.class)
    public void testDrawLeaders_NegativeNumberOfCardsToDrawException() throws NegativeNumberOfCardsToDrawException {
        int numberOfCardsToDraw = -1;
        match = new Match(id, multiPlayersList);
        List<LeaderCard> drawnCards = match.drawLeaders(numberOfCardsToDraw);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDrawLeaders_IndexOutOfBoundsException() throws NegativeNumberOfCardsToDrawException, IndexOutOfBoundsException {
        int numberOfCardsToDraw = 1000;
        match = new Match(id, multiPlayersList);
        List<LeaderCard> drawnCards = match.drawLeaders(numberOfCardsToDraw);
    }

    @Test
    public void testDrawLeaders_StandardCase() throws NegativeNumberOfCardsToDrawException {
        int numberOfCardsToDraw = 4;
        match = new Match(id, multiPlayersList);
        List<LeaderCard> drawnCards = match.drawLeaders(numberOfCardsToDraw);
        assertEquals(drawnCards.size(), numberOfCardsToDraw);
    }

    @Test
    public void testGetActionTokenStack() {
        match = new Match(id, multiPlayersList);
        assertNotNull(match.getActionTokenStack());
    }
}