package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.SpecialDepotAbility;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
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
        player = new Player(new VirtualView(), nickname, sessionToken);
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
            add(new LeaderCard(new HashMap<>(), new HashMap<>(), 0, new SpecialDepotAbility(Resource.COIN)));
            add(new LeaderCard(new HashMap<>(), new HashMap<>(), 0, new SpecialDepotAbility(Resource.SERVANT)));
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
    public void isLeaderActionPlayable_PlayableLeader() {
        List<LeaderCard> leaderCards = new ArrayList<>() {{
            add(new LeaderCard(new HashMap<>(), new HashMap<>(), 0, new SpecialDepotAbility(Resource.COIN)));
        }};

        // First case: playable leader
        player.setLeaderCards(leaderCards);
        assertTrue(player.isLeaderActionPlayable());
    }

    @Test
    public void isLeaderActionPlayable_AlreadyActive() {
        List<LeaderCard> leaderCards = new ArrayList<>() {{
            add(new LeaderCard(new HashMap<>(), new HashMap<>(), 0, new SpecialDepotAbility(Resource.COIN)));
        }};

        // Second case: leader already active
        player.setLeaderCards(leaderCards);
        player.getLeaderCards().get(0).activate();
        assertFalse(player.isLeaderActionPlayable());
    }

    @Test
    public void isLeaderActionPlayable_NoMoreLeaders() {
        List<LeaderCard> leaderCards = new ArrayList<>() {{
            add(new LeaderCard(new HashMap<>(), new HashMap<>(), 0, new SpecialDepotAbility(Resource.COIN)));
        }};

        // Third case: no more leaders
        player.setLeaderCards(leaderCards);
        player.discardLeaderCard(leaderCards.get(0));

        assertFalse(player.isLeaderActionPlayable());
    }
}