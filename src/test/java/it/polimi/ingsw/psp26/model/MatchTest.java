package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.exceptions.NegativeNumberOfCardsToDrawException;
import it.polimi.ingsw.psp26.exceptions.NotEnoughCardsToDrawException;
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

    @Test
    public void testGetLeaderDeck() {
        match = new Match(id, multiPlayersList);
        assertNotNull(match.getLeaderDeck());
    }

    @Test(expected = NegativeNumberOfCardsToDrawException.class)
    public void testDrawLeaders() throws NegativeNumberOfCardsToDrawException, NotEnoughCardsToDrawException {
        int[] numbersOfCards = {-1, 0, 4, 1000};

        for (int numberOfCards : numbersOfCards) {
            match = new Match(id, multiPlayersList);
            int initialNumberOfLeaderCards = match.getLeaderDeck().size();

            List<LeaderCard> drawnLeaderCards = match.drawLeaders(numberOfCards);

            int finalNumberOfLeaderCards = match.getLeaderDeck().size();
            assertEquals(initialNumberOfLeaderCards, finalNumberOfLeaderCards + numberOfCards);
            assertNotNull(drawnLeaderCards);
            assertEquals(drawnLeaderCards.size(), numberOfCards);
        }
    }

    @Test
    public void testShuffleLeaderDeck() {
        match = new Match(id, multiPlayersList);
        int initialNumberOfLeaderCardSize = match.getLeaderDeck().size();

        // Shuffle LeaderDeck
        match.shuffleLeaderDeck();

        int finalNumberOfLeaderCardsSize = match.getLeaderDeck().size();
        assertEquals(initialNumberOfLeaderCardSize, finalNumberOfLeaderCardsSize);
        assertNotNull(match.getLeaderDeck());
    }

    @Test
    public void testGetActionTokenStack() {
        match = new Match(id, multiPlayersList);
        assertNotNull(match.getActionTokenStack());
    }

    @Test
    public void testShuffleActionTokenStack() {
        match = new Match(id, multiPlayersList);
        int initialActionTokenStackSize = match.getActionTokenStack().size();

        // Shuffle the tokens
        match.shuffleActionTokenStack();

        int finalActionTokenStackSize = match.getActionTokenStack().size();
        assertEquals(initialActionTokenStackSize, finalActionTokenStackSize);
        assertNotNull(match.getActionTokenStack());
    }
}