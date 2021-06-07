package it.polimi.ingsw.psp26.network.server.memory;

import it.polimi.ingsw.psp26.application.files.Files;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static it.polimi.ingsw.psp26.configurations.Configurations.GAME_FILES;
import static org.junit.Assert.*;

public class LeaderBoardTest {

    private HashMap<String, Integer> playersExample;

    @Before
    public void setUp() throws Exception {

        playersExample = new HashMap<>();

        playersExample.put("Mario", 59);
        playersExample.put("Luigi", 77);
        playersExample.put("Carlo", 66);
        playersExample.put("Gianni", 36);
        playersExample.put("Fabio", 36);
        playersExample.put("Wario", 22);
    }

    @Test
    public void testSaveLeaderboard() {

        Files.deleteFile(LeaderBoard.LEADERBOARD_FILE);

        for( String nickname : playersExample.keySet()) {

            LeaderBoard.getInstance().addPlayerVictoryPoints(nickname, playersExample.get(nickname));

            assertTrue(LeaderBoard.getInstance().getLeaderboard().containsKey(nickname));
            assertTrue(LeaderBoard.getInstance().getLeaderboard().containsValue(playersExample.get(nickname)));


        }

        assertTrue(Files.getFilesNamesInDirectory(GAME_FILES).contains("nickname-points.json"));

        Files.deleteFile(LeaderBoard.LEADERBOARD_FILE);

    }

}
