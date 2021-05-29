package it.polimi.ingsw.psp26.network.server.memory;

import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class GameSaverTest {

    VirtualView virtualView;

    @Before
    public void setUp() throws Exception {
        virtualView = new VirtualView();

        Player player1 = new Player(virtualView, "nickname1", "sessionToken1");
        Player player2 = new Player(virtualView, "nickname2", "sessionToken2");

        virtualView.getMatchController().getMatch().addPlayer(player1);
        virtualView.getMatchController().getMatch().addPlayer(player2);
    }

    @Test
    public void testRestoreMatch_EqualsPlayersNicknames() {
        Match match = virtualView.getMatchController().getMatch();

        GameSaver.getInstance().backupMatch(virtualView.getMatchController().getMatch(), 1, 1);
        Match restoredMatch = GameSaver.getInstance().loadMatch(match.getId());

        assertEquals(match.getPlayers().get(0).getNickname(), restoredMatch.getPlayers().get(0).getNickname());
        assertEquals(match.getPlayers().get(1).getNickname(), restoredMatch.getPlayers().get(1).getNickname());
    }

    @Test
    public void testRestoreMatch_EqualsActionTokens() {
        Match match = virtualView.getMatchController().getMatch();

        GameSaver.getInstance().backupMatch(virtualView.getMatchController().getMatch(), 1, 1);
        Match restoredMatch = GameSaver.getInstance().loadMatch(match.getId());

        for (int i = 0; i < match.getActionTokens().size(); i++) {
            // If you don't put a toString() it will cause an error even though the tokens are the same
            assertEquals(match.getActionTokens().get(i).toString(), restoredMatch.getActionTokens().get(i).toString());
        }
    }

    @Test
    public void testCreateNewDirectories() {
        int savedDirectories = GameSaver.getInstance().getSavedMatchesPath().size();
        int directoriesToCreate = 4;

        for (int i = 0; i < directoriesToCreate; i++) {
            VirtualView virtualView = new VirtualView();
            GameSaver.getInstance().backupMatch(virtualView.getMatchController().getMatch(), 0, 0);
        }
        System.out.println(GameSaver.getInstance().getSavedMatchesPath());

        // Clean saved_matches folder before running the test
        assertEquals(directoriesToCreate + savedDirectories, GameSaver.getInstance().getSavedMatchesPath().size());
    }

}