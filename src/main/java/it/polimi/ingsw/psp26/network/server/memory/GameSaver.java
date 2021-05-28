package it.polimi.ingsw.psp26.network.server.memory;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.psp26.application.messages.serialization.GsonConverter;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;


import static it.polimi.ingsw.psp26.application.files.Files.*;
import static it.polimi.ingsw.psp26.configurations.Configurations.GAME_FILES;


public class GameSaver {

    private static GameSaver instance;

    // Used to create new directories. Remember to add the matchID after the parameter
    private static final String DIRECTORY_PATH = GAME_FILES + "saved_matches/game_";

    // Used to store backups. Remember to add the matchID after the parameter
    private static final String BACKUP_PATH = "saved_matches/game_";

    // Used to get the correct numbering for directories and files
    private static final String FORMAT_ID = "%03d";

    public static GameSaver getInstance() {
        if (instance == null) instance = new GameSaver();
        return instance;
    }


    //--------------------------//
    //          BACKUP          //
    //--------------------------//

    /**
     * Used to backup a VirtualView in a new directory (if needed)
     * At first, the method checks if a folder with the Match already exists: if not, creates one
     * Then it calls several auxiliary methods for backing up things
     * <p>
     * Naming convention
     * Each directory/file created has the MatchID number at the end of the name
     * This id is properly formatted and is incremental
     * The MatchID is assigned to Matches in the VirtualView class
     *
     * @param match The Match to backup
     */
    public void backupMatch(Match match, int turnPlayerIndex, int turnNumber) {
        System.out.println("Directory created: " + createNewDirectory(DIRECTORY_PATH + String.format(FORMAT_ID, match.getId())));

        storeMatch(match);
        storeMatchControllerData(turnPlayerIndex, turnNumber, match.getId());
    }


    /**
     * Writes on a file the given Match
     *
     * @param match The Match to backup
     */
    private void storeMatch(Match match) {
        String formattedMatchId = String.format(FORMAT_ID, match.getId());
        writeToFile(
                BACKUP_PATH + formattedMatchId + "/match_" + formattedMatchId + ".json",
                GsonConverter.getInstance().getGson().toJson(match)
        );
    }


    /**
     * Writes on a file the given turnPlayerIndex and turnNumber
     * These elements must be saved separately in order to correctly reconstruct the VirtualView when loading a saved Match
     * They are saved in a List for an easier saving process; then the whole List is written to file
     *
     * @param turnPlayerIndex The index of the Player that was playing when the Server shut down
     * @param turnNumber      The number of the turn when the Server shut down
     * @param matchID         The id of the Match to backup
     */
    private void storeMatchControllerData(int turnPlayerIndex, int turnNumber, int matchID) {
        String formattedMatchId = String.format(FORMAT_ID, matchID);
        List<Integer> matchControllerData = new ArrayList<>();

        matchControllerData.add(turnPlayerIndex);
        matchControllerData.add(turnNumber);

        writeToFile(
                BACKUP_PATH + formattedMatchId + "/matchcontrollerdata_" + formattedMatchId + ".json",
                GsonConverter.getInstance().getGson().toJson(matchControllerData)
        );
    }


    //------------------------//
    //          LOAD          //
    //------------------------//

    /**
     * Load an existing Match by retrieving it from the given matchID
     * It calls several auxiliary methods to correctly reconstruct the Match
     * Before returning the Match, the method sets the Player's sessionTokens
     *
     * @param matchID The Match to load from file
     * @return The loaded Match
     */
    public Match loadMatch(int matchID) {
        String formattedMatchId = String.format(FORMAT_ID, matchID);
        Match restoredMatch = null;

        try {
            restoredMatch = restoreMatch("saved_matches/game_" + formattedMatchId + "/match_" + formattedMatchId + ".json");
            for (Player player : restoredMatch.getPlayers())
                player.setSessionToken(Users.getInstance().getNicknameSessionTokens().get(player.getNickname()));
        } catch (FileNotFoundException e) {
            System.out.println("FILE DOESN'T EXIST!");
        }

        return restoredMatch;
    }


    /**
     * Load an existing Match by retrieving it from the given matchID
     *
     * @param gamePath The name of the directory in which the Match is stored (e.g. game_001)
     * @return The desired Match
     */
    public Match loadMatch(String gamePath) {
        return loadMatch(getID(gamePath));
    }


    /**
     * Load the turnPlayerIndex using the given gamePath
     * turnPlayerIndex is the first element of the matchControllersData List
     *
     * @param gamePath The name of the directory in which the Match is stored (e.g. game_001)
     * @return The turnPlayerIndex related to the Match stored in the gamePath
     * @throws FileNotFoundException Thrown if the fle doesn't exists
     */
    public int loadTurnPlayerIndex(String gamePath) throws FileNotFoundException {
        return loadMatchControllersData(gamePath).get(0);
    }


    /**
     * Load the turnNumber using the given gamePath
     * turnNumber is the second element of the matchControllersData List
     *
     * @param gamePath The name of the directory in which the Match is stored (e.g. game_001)
     * @return The turnNumber related to the Match stored in the gamePath
     * @throws FileNotFoundException Thrown if the fle doesn't exists
     */
    public int loadTurnNumber(String gamePath) throws FileNotFoundException {
        return loadMatchControllersData(gamePath).get(1);
    }


    /**
     * Loads the matchControllerData List
     *
     * @param gamePath The name of the directory in which the Match is stored (e.g. game_001)
     * @return The matchControllerData List
     * @throws FileNotFoundException Thrown if the fle doesn't exists
     */
    private List<Integer> loadMatchControllersData(String gamePath) throws FileNotFoundException {
        Type type = new TypeToken<List<Integer>>() {
        }.getType();

        int id = getID(gamePath);
        String formattedId = String.format(FORMAT_ID, id);
        String loadPath = "saved_matches/game_" + formattedId + "/matchcontrollerdata_" + formattedId + ".json";

        return GsonConverter.getInstance().getGson().fromJson(readFromFile(loadPath), type);
    }

    /**
     * Loads a Match from file
     *
     * @param matchPath The path where to find the Match
     * @return The loaded Match
     * @throws FileNotFoundException Thrown if the Match file doesn't exist
     */
    private Match restoreMatch(String matchPath) throws FileNotFoundException {
        return GsonConverter.getInstance().getGson().fromJson(readFromFile(matchPath), Match.class);
    }


    //---------------------------//
    //          UTILITY          //
    //---------------------------//

    /**
     * Creates a new directory in the given directoryPath if it doesn't already exist
     *
     * @param directoryPath The path where to create the directory (if not already present)
     * @return True if a new directory is created, false if a new directory is not created
     */
    private boolean createNewDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        return directory.mkdir();
    }


    /**
     * Creates a List of all the directories names contained in the saved_match folder
     *
     * @return The List of the directories names
     */
    public List<String> getSavedMatchesPath() {
        try {
            File directory = new File(GAME_FILES + "saved_matches");
            return Arrays.stream(Objects.requireNonNull(directory.list())).sorted().collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    /**
     * @return The last directory id contained in the saved_match folder. If no directory is present, returns 0
     */
    public int getLastId() {
        List<String> directoryList = getSavedMatchesPath();
        if (directoryList.size() == 0) return 0;
        return getID(directoryList.get(directoryList.size() - 1));
    }


    /**
     * Returns the last numbers of a game directory name
     *
     * @param gamePath The name of the directory in which the Match is stored (e.g. game_001)
     * @return The last numbers of the game directory name
     */
    public int getID(String gamePath) {
        String[] splitDirectoryName = gamePath.split("_");
        return Integer.parseInt(splitDirectoryName[splitDirectoryName.length - 1]);
    }

}
