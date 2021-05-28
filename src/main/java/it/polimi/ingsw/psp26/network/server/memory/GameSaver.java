package it.polimi.ingsw.psp26.network.server.memory;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.psp26.application.messages.serialization.GsonConverter;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;

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

    private static final String FORMAT_ID = "%03d";

    public static GameSaver getInstance() {
        if (instance == null) instance = new GameSaver();
        return instance;
    }


    //-------------------------------------------------------------------

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            VirtualView virtualView = new VirtualView();
            GameSaver.getInstance().backupMatch(virtualView.getMatchController().getMatch(), 0, 0);
        }
        System.out.println(getInstance().getSavedMatchesPath());
    }

    //-------------------------------------------------------------------


    //--------------------------//
    //          BACKUP          //
    //--------------------------//

    /**
     * Used to backup a VirtualView in a new directory (if needed)
     * At first, the method checks if a folder with the Match already exists: if not, creates one
     * Then it calls several auxiliary methods for backing up things
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

    
    public Match loadMatch(String gamePath) {
        return loadMatch(getID(gamePath));
    }
    
    
    public int loadTurnPlayerIndex(String gamePath) throws FileNotFoundException {
        return loadMatchControllersData(gamePath).get(0);
    }
    
    
    public int loadTurnNumber(String gamePath) throws FileNotFoundException {
        return loadMatchControllersData(gamePath).get(1);
    }

    
    private List<Integer> loadMatchControllersData(String gamePath) throws FileNotFoundException {
        Type type = new TypeToken<List<Integer>>() {
        }.getType();

        return GsonConverter.getInstance().getGson().fromJson(readFromFile(gamePath), type);
    }

    /**
     * Load a Match from file
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


    public List<String> getSavedMatchesPath() {
        try {
            File directory = new File("saved_matches");
            return Arrays.stream(Objects.requireNonNull(directory.list())).sorted().collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    public int getLastId() {
        List<String> directoryList = getSavedMatchesPath();
        if (directoryList.size() == 0) return 0;
        return getID(directoryList.get(directoryList.size() - 1));
    }
    
    
    public int getID(String gamePath) {
        String[] splitDirectoryName = gamePath.split("_");
        return Integer.parseInt(splitDirectoryName[splitDirectoryName.length - 1]);
    }

}
