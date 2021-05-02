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
        assertFalse(player.hasInkwell());
        player.giveInkwell();
        assertTrue(player.hasInkwell());
    }

    @Test
    public void isInkwell() {
        assertFalse(player.hasInkwell());
    }

}