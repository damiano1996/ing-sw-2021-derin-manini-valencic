package it.polimi.ingsw.psp26.network.server.memory;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.psp26.application.messages.serialization.GsonConverter;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.psp26.application.files.Files.*;
import static it.polimi.ingsw.psp26.configurations.Configurations.GAME_FILES;

/**
 * Singleton class to keep a leaderboard among all the matches served by the system.
 */
public class LeaderBoard {

    public static final String LEADERBOARD_FILE = GAME_FILES + "nickname-points.json";

    private static LeaderBoard instance;

    private HashMap<String, Integer> leaderboard;


    /**
     * Constructor of the class.
     */
    private LeaderBoard() {

        createNewDirectory(GAME_FILES);

        leaderboard = new HashMap<>();

        try {
            Type type = new TypeToken<HashMap<String, Integer>>() {
            }.getType();

            leaderboard = GsonConverter.getInstance().getGson().fromJson(readFromFile(LEADERBOARD_FILE), type);

        } catch (FileNotFoundException ignored) {
        }
    }

    /**
     * Getter of the instance of the singleton.
     *
     * @return the LeaderBoard object
     */
    public synchronized static LeaderBoard getInstance() {
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
        writeToFile(LEADERBOARD_FILE, GsonConverter.getInstance().getGson().toJson(leaderboard));
    }
}
