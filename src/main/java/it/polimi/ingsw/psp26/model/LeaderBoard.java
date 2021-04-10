package it.polimi.ingsw.psp26.model;

import java.util.Collections;
import java.util.HashMap;

/**
 * Singleton class to keep a leaderboard among all the matches served by the system.
 */
public class LeaderBoard {

    private static LeaderBoard instance;

    private final HashMap<Player, Integer> leaderboard;

    /**
     * Private constructor.
     */
    private LeaderBoard() {
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
    public HashMap<Player, Integer> getLeaderboard() {
        return (HashMap<Player, Integer>) Collections.unmodifiableMap(leaderboard);
    }

    /**
     * Method to add a new score to the leaderboard.
     *
     * @param player        player that has obtained a new score
     * @param victoryPoints obtained victory points
     */
    public void addPlayerVictoryPoints(Player player, int victoryPoints) {
        leaderboard.put(player, victoryPoints);
    }
}
