package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.enums.Color;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.psp26.view.ViewUtils.getOrderedPlayersList;

public class CommonScreensCli {

    private final CliUtils cliUtils;

    public CommonScreensCli(PrintWriter pw) {
        this.cliUtils = new CliUtils(pw);
    }


    //-----------------------------------//
    //          END GAME SCREEN          //
    //-----------------------------------//    

    /**
     * Displays the end game screen
     *
     * @param leaderboard It contains the Players nicknames and the points they achieved during the Match
     */
    public void displayFinalScreen(Map<String, Integer> leaderboard) {
        cliUtils.cls();
        cliUtils.printFigure("/titles/LeaderboardTitle", 1, 53);
        cliUtils.printFigure("FinalLeaderboard", 15, 89);
        printLeaderboardContent(leaderboard, getOrderedPlayersList(leaderboard));
    }


    /**
     * Auxiliary method used to print the leaderboard content
     *
     * @param leaderboard        The Map containing the Players nicknames and their points
     * @param orderedPlayersList A List containing the Players in the order the have to be printed
     */
    private void printLeaderboardContent(Map<String, Integer> leaderboard, List<String> orderedPlayersList) {
        int verticalPadding = 0;
        for (String playerNickname : orderedPlayersList) {
            cliUtils.pPCS(cliUtils.centerString(30, playerNickname), Color.WHITE, 19 + verticalPadding, 89);
            cliUtils.pPCS(cliUtils.centerString(30, Integer.toString(leaderboard.get(playerNickname))), Color.WHITE, 19 + verticalPadding, 120);
            verticalPadding += 3;
        }
    }

}
