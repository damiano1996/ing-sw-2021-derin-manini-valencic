package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.exceptions.PlayerDoesNotExistException;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.actiontokens.BlackCrossActionToken;
import it.polimi.ingsw.psp26.model.actiontokens.BlackCrossShuffleActionToken;
import it.polimi.ingsw.psp26.model.actiontokens.DiscardActionToken;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        match = new Match(virtualView, id);
    }

    @Test
    public void testGetId() {
        assertEquals(id, match.getId());
    }

    @Test
    public void testAddPlayer() {
        match.addPlayer(singlePlayerList.get(0));
        assertEquals(singlePlayerList.get(0), match.getPlayers().get(0));
    }

    private void addPlayers(List<Player> players) {
        for (Player player : players)
            match.addPlayer(player);
    }

    @Test
    public void testGetPlayers() {
        addPlayers(multiPlayersList);
        assertEquals(multiPlayersList, match.getPlayers());
    }

    @Test
    public void testIsMultiPlayerMode_TrueCase() {
        addPlayers(multiPlayersList);
        assertTrue(match.isMultiPlayerMode());
    }

    @Test
    public void testIsMultiPlayerMode_FalseCase() {
        addPlayers(singlePlayerList);
        assertFalse(match.isMultiPlayerMode());
    }

    @Test
    public void testGetPlayerByNickname() throws PlayerDoesNotExistException {
        int playerIndex = 0;
        String nickname = multiPlayersList.get(playerIndex).getNickname();

        addPlayers(multiPlayersList);
        assertEquals(multiPlayersList.get(playerIndex), match.getPlayerByNickname(nickname));
    }

    @Test(expected = PlayerDoesNotExistException.class)
    public void testGetPlayerByNickname_ExceptionCase() throws PlayerDoesNotExistException {
        match.getPlayerByNickname(null);
    }

    @Test
    public void testGetResourceSupply() {
        assertNotNull(match.getResourceSupply());
    }

    @Test
    public void testGetDevelopmentGrid() {
        assertNotNull(match.getDevelopmentGrid());
    }

    @Test
    public void testGetMarketTray() {
        assertNotNull(match.getMarketTray());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDrawLeaders_IndexOutOfBoundsException() throws IndexOutOfBoundsException {
        int numberOfCardsToDraw = 1000;
        List<LeaderCard> drawnCards = match.drawLeaders(numberOfCardsToDraw);
    }

    @Test
    public void testDrawLeaders_StandardCase() {
        int numberOfCardsToDraw = 4;
        List<LeaderCard> drawnCards = match.drawLeaders(numberOfCardsToDraw);
        assertEquals(drawnCards.size(), numberOfCardsToDraw);
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void testDrawActionTokens_IndexOutOfBoundsException() throws IndexOutOfBoundsException {
        int numberOfTokensToDraw = 1000;
        List<ActionToken> drawnTokens = match.drawActionTokens(numberOfTokensToDraw);
    }

    @Test
    public void testDrawActionTokens_StandardCase() {
        int numberOfTokensToDraw = 1;
        List<ActionToken> drawnTokens = match.drawActionTokens(numberOfTokensToDraw);
        assertEquals(drawnTokens.size(), numberOfTokensToDraw);
    }

    @Test
    public void testGetActionTokens() {
        List<ActionToken> actionTokens = new ArrayList<>();
        actionTokens.add(new BlackCrossActionToken());
        actionTokens.add(new BlackCrossActionToken());
        actionTokens.add(new BlackCrossShuffleActionToken());
        actionTokens.add(new DiscardActionToken(Color.GREEN));
        actionTokens.add(new DiscardActionToken(Color.YELLOW));
        actionTokens.add(new DiscardActionToken(Color.BLUE));
        actionTokens.add(new DiscardActionToken(Color.PURPLE));

        List<String> tokenNames = actionTokens.stream().map(ActionToken::toString).collect(Collectors.toList());
        List<String> matchTokenNames = match.getActionTokens().stream().map(ActionToken::toString).collect(Collectors.toList());

        assertTrue(tokenNames.containsAll(matchTokenNames));
    }

}