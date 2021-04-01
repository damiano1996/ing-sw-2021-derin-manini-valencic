package it.polimi.ingsw.psp26.model;

import java.util.HashMap;

public class LeaderBoard {

    private static LeaderBoard instance;

    private final HashMap<Player, Integer> leaderboard;

    private LeaderBoard() {
        leaderboard = new HashMap<>();
    }

    public static LeaderBoard getInstance() {
        if (instance == null)
            instance = new LeaderBoard();

        return instance;
    }

    public HashMap<Player, Integer> getLeaderboard() {
        return leaderboard;
    }

    public void addPlayerVictoryPoints(Player player, int victoryPoints) {
        leaderboard.put(player, victoryPoints);
    }
}
