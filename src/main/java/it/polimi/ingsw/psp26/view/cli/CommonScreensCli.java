package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.enums.Color;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.psp26.view.ViewUtils.getOrderedPlayersList;

/**
 * Class used to contain methods to display screens not related to a Match.
 */
public class CommonScreensCli {

    private final CliUtils cliUtils;

    /**
     * Constructor of the class.
     * Initialize a cliUtils attribute in order to use its methods.
     *
     * @param pw the PrintWriter to use
     */
    public CommonScreensCli(PrintWriter pw) {
        this.cliUtils = new CliUtils(pw);
    }


    //-----------------------------------//
    //          END GAME SCREEN          //
    //-----------------------------------//    

    /**
     * Displays the end game screen by printing the Match Leaderboard.
     *
     * @param leaderboard     it contains the Players nicknames and the points they achieved during the Match
     * @param numberOfPlayers the number of players that are displayed
     */
    public void displayFinalScreen(Map<String, Integer> leaderboard, int numberOfPlayers) {
        cliUtils.cls();
        cliUtils.printFigure("titles/LeaderboardTitle", 1, 53);
        cliUtils.printFigure("FinalLeaderboard", 15, 89);
        printLeaderboardContent(leaderboard, getOrderedPlayersList(leaderboard), numberOfPlayers);
    }


    /**
     * Auxiliary method used to print the leaderboard content.
     * A verticalPadding variable is used to set the correct space between the Leaderboard rows.
     *
     * @param leaderboard        the Map containing the Players nicknames and their points
     * @param orderedPlayersList a List containing the Players in the order the have to be printed
     * @param numberOfPlayers    the number of players that are printed
     */
    private void printLeaderboardContent(Map<String, Integer> leaderboard, List<String> orderedPlayersList, int numberOfPlayers) {
        int verticalPadding = 0;

        List<String> maximumPlayersToDisplay = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) maximumPlayersToDisplay.add(orderedPlayersList.get(i));

        for (String playerNickname : maximumPlayersToDisplay) {
            cliUtils.pPCS(cliUtils.centerString(30, playerNickname), Color.WHITE, 19 + verticalPadding, 89);
            cliUtils.pPCS(cliUtils.centerString(30, Integer.toString(leaderboard.get(playerNickname))), Color.WHITE, 19 + verticalPadding, 120);
            verticalPadding += 3;
        }
    }

}
