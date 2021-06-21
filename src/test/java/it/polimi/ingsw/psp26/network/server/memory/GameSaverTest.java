package it.polimi.ingsw.psp26.network.server.memory;

import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class GameSaverTest {

    private VirtualView virtualView;

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
        List<Integer> matchIDs = new ArrayList<>();
        Match match = virtualView.getMatchController().getMatch();

        matchIDs.add(match.getId());

        GameSaver.getInstance().backupMatch(match, 1, 1);
        Match restoredMatch = GameSaver.getInstance().loadMatch(match.getId());

        assertEquals(match.getPlayers().get(0).getNickname(), restoredMatch.getPlayers().get(0).getNickname());
        assertEquals(match.getPlayers().get(1).getNickname(), restoredMatch.getPlayers().get(1).getNickname());

        deleteCreatedDirectories(matchIDs);
    }

    @Test
    public void testRestoreMatch_EqualsActionTokens() {
        List<Integer> matchIDs = new ArrayList<>();
        Match match = virtualView.getMatchController().getMatch();

        matchIDs.add(match.getId());

        GameSaver.getInstance().backupMatch(match, 1, 1);
        Match restoredMatch = GameSaver.getInstance().loadMatch(match.getId());

        for (int i = 0; i < match.getActionTokens().size(); i++) {
            // If you don't put a toString() it will cause an error even though the tokens are the same
            assertEquals(match.getActionTokens().get(i).toString(), restoredMatch.getActionTokens().get(i).toString());
        }

        deleteCreatedDirectories(matchIDs);
    }

    @Test
    public void testCreateNewDirectories() {
        List<Integer> matchIDs = new ArrayList<>();
        int savedDirectories = GameSaver.getInstance().getSavedMatchesDirectoriesNames().size();
        int directoriesToCreate = 4;

        for (int i = 0; i < directoriesToCreate; i++) {
            VirtualView virtualView = new VirtualView();
            GameSaver.getInstance().backupMatch(virtualView.getMatchController().getMatch(), 0, 0);
            matchIDs.add(virtualView.getMatchController().getMatch().getId());
        }
        int expectedDirectoriesNumber = GameSaver.getInstance().getSavedMatchesDirectoriesNames().size();

        assertEquals(directoriesToCreate + savedDirectories, expectedDirectoriesNumber);

        deleteCreatedDirectories(matchIDs);
    }

    @Test
    public void testDeleteDirectory() {
        int savedDirectoriesNumber = GameSaver.getInstance().getSavedMatchesDirectoriesNames().size();

        GameSaver.getInstance().backupMatch(virtualView.getMatchController().getMatch(), 0, 0);
        GameSaver.getInstance().deleteDirectoryByName("game_" + String.format("%03d", virtualView.getMatchController().getMatch().getId()));

        assertEquals(savedDirectoriesNumber, GameSaver.getInstance().getSavedMatchesDirectoriesNames().size());
    }


    private void deleteCreatedDirectories(List<Integer> matchIDs) {
        for (Integer id : matchIDs) {
            GameSaver.getInstance().deleteDirectoryByName("game_" + String.format("%03d", id));
        }
    }

}