package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.model.specialleaderabilities.ProductionAbility;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player player;
    private String nickname;
    private String sessionToken;

    @Before
    public void setUp() {
        nickname = "nickname";
        sessionToken = "sessionToken";
        player = new Player(nickname, sessionToken);
    }

    @Test
    public void getNickname() {
        assertEquals(nickname, player.getNickname());
    }

    @Test
    public void getSessionToken() {
        assertEquals(sessionToken, player.getSessionToken());
    }

    @Test
    public void getPersonalBoard() {
        assertNotNull(player.getPersonalBoard());
    }

    @Test
    public void getLeaderCards() {
        List<LeaderCard> leaderCards = new ArrayList<>() {{
            add(new LeaderCard(new ArrayList<>(), new ArrayList<>(), 0, new ProductionAbility()));
            add(new LeaderCard(new ArrayList<>(), new ArrayList<>(), 0, new ProductionAbility()));
        }};

        player.setLeaderCards(leaderCards);
        assertEquals(leaderCards, player.getLeaderCards());
    }

    @Test
    public void giveInkwell() {
        assertFalse(player.isInkwell());
        player.giveInkwell();
        assertTrue(player.isInkwell());
    }

    @Test
    public void isInkwell() {
        assertFalse(player.isInkwell());
    }

    @Test
    public void isLeaderActionPlayable() {
        List<LeaderCard> leaderCards = new ArrayList<>() {{
            add(new LeaderCard(new ArrayList<>(), new ArrayList<>(), 0, new ProductionAbility()));
        }};

        // First case: playable leader
        player.setLeaderCards(leaderCards);
        assertTrue(player.isLeaderActionPlayable());

        // Second case: leader already active
        player.getLeaderCards().get(0).activate();
        assertFalse(player.isLeaderActionPlayable());

        // Third case: no more leaders
        player.getLeaderCards().clear();
        assertFalse(player.isLeaderActionPlayable());
    }
}