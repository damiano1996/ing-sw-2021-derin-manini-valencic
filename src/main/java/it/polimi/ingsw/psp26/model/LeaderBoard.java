package it.polimi.ingsw.psp26.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class to keep a leaderboard among all the matches served by the system.
 */
public class LeaderBoard {

    private static LeaderBoard instance;

    private final HashMap<String, Integer> leaderboard;

    /**
     * Constructor of the class.
     */
    public LeaderBoard() {
        leaderboard = new HashMap<>();
    }

    /**
     * Getter of the instance of the singleton.
     *
     * @return the LeaderBoard object
     */
    public static LeaderBoard getInstance() {
        if (instance == null)
            instance = new LeaderBoard();

        return instance;
    }

    /**
     * Getter of the leaderboard
     *
     * @return leaderboard (hashmap) containing scores in the following format <player, victory points>
     */
    public Map<String, Integer> getLeaderboard() {
        return Collections.unmodifiableMap(leaderboard);
    }

    /**
     * Method to add a new score to the leaderboard.
     *
     * @param player        player that has obtained a new score
     * @param victoryPoints obtained victory points
     */
    public void addPlayerVictoryPoints(String player, int victoryPoints) {
        leaderboard.put(player, victoryPoints);
    }
}
