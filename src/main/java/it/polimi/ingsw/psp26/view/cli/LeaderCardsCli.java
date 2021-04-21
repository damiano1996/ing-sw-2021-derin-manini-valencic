package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class LeaderCardsCli { //TODO se va bene così il metodo per scegliere i leader elimina le parti commentate sotto

    private static final int CLI_WIDTH = 237;
    private final PrintWriter pw;
    private final CliUtils cliUtils;

    public LeaderCardsCli(PrintWriter pw) {
        this.pw = pw;
        this.cliUtils = new CliUtils(pw);
    }


    /**
     * Prints the Leader selection Screen
     *
     * @param leaderCards The 4 Leader Cards given to the Player choice
     */
//    public void displayLeaderSelection(List<LeaderCard> leaderCards) {
//        cliUtils.cls();
//
//        cliUtils.printFigure("LeaderSelectionTitle", 1, 28);
//        printMultipleLeaders(leaderCards, 10);
//
//        //for (int j = 0; j < leaderCards.size(); j++)
//        //isLeaderSelected(leaderCards.get(j), selectedLeaders, leaderCards, 30, 40 + (j * 45));
//
//        cliUtils.vSpace(5);
//
//    }


    /**
     * If a Leader Cards is selected, mark it with a red SELECTED String
     *
     * @param leaderCard      The card to check if has been selected
     * @param selectedLeaders The Leaders selected by the Player
     * @param leaderCards     The 4 Leader Cards given to the Player's choice
     * @param startingRow     The starting row where the Leader Card is going to be printed
     * @param startingColumn  The starting column where the Leader Card is going to be printed
     */
//    private void isLeaderSelected(LeaderCard leaderCard, List<LeaderCard> selectedLeaders, List<LeaderCard> leaderCards, int startingRow, int startingColumn) {
//        cliUtils.setCursorPosition(startingRow, startingColumn);
//        if (selectedLeaders.contains(leaderCard)) pw.print("\u001b[41m  SELECTED  \u001b[0m");
//        else pw.print("  Leader " + (leaderCards.indexOf(leaderCard) + 1) + "  ");
//        pw.flush();
//    }


    /**
     * Prints the given Leader Cards by correctly aligning them on screen
     *
     * @param leaderCards The Leader Cards to print
     * @param startingRow The row where the cards will be printed
     */
    public void printMultipleLeaders(List<LeaderCard> leaderCards, int startingRow) {
        //print the cards
        for (int i = 0; i < leaderCards.size(); i++)
            printLeader(leaderCards.get(i), startingRow, getPrintMultipleLeadersStartingColumn(leaderCards.size()) + (i * 45));

        //print the leader progressive number
        for (int i = 0; i < leaderCards.size(); i++)
            cliUtils.pPCS("LEADER  #" + (i + 1), Color.WHITE, startingRow + 19, getPrintMultipleLeadersStartingColumn(leaderCards.size()) + 8 + (i * 45));
    }


    /**
     * Calculate the correct position for printing the Leader Cards
     *
     * @param numberOfLeaders The number of Leader Cards to print
     * @return The correct column position to print them
     */
    public int getPrintMultipleLeadersStartingColumn(int numberOfLeaders) {
        return ((CLI_WIDTH - (numberOfLeaders * 27) - ((numberOfLeaders - 1) * 18)) / 2) - 4;
    }


    /**
     * Prints a single LeaderCard
     *
     * @param leaderCard     The Leader Card to print
     * @param startingRow    The row in which the Leader Card will be printed
     * @param startingColumn The column in which the Leader Card will be printed
     */
    private void printLeader(LeaderCard leaderCard, int startingRow, int startingColumn) {
        cliUtils.printFigure(
                "leadercards/" + leaderCard.getAbilityToString().split(":")[0],
                startingRow, startingColumn);
        printLeaderRequirements(leaderCard, startingRow, startingColumn);
        //Prints the colored square for the Resource information
        cliUtils.pPCS("\u25A0", leaderCard.getAbilityResource().getColor(), startingRow + 16, startingColumn + 22);
    }


    /**
     * Prints the requirement of a given Leader Card
     *
     * @param leaderCard     The Leader Card to analyze
     * @param startingRow    The starting row where the Leader Card is going to be printed
     * @param startingColumn The starting column where the Leader Card is going to be printed
     */
    private void printLeaderRequirements(LeaderCard leaderCard, int startingRow, int startingColumn) {
        if (leaderCard.getDevelopmentCardRequirements().keySet().size() == 0)
            printLeaderResourcesRequirements(leaderCard.getResourcesRequirements(), startingRow, startingColumn);
        else
            printLeaderDevelopmentCardRequirements(leaderCard.getDevelopmentCardRequirements(), startingRow, startingColumn);
    }


    /**
     * If the Leader Cards requires Development Cards, this method is invoked in printLeaderRequirements()
     *
     * @param requirements   The requirements of the Leader Card
     * @param startingRow    The starting row where the Leader Card is going to be printed
     * @param startingColumn The starting column where the Leader Card is going to be printed
     */
    private void printLeaderDevelopmentCardRequirements(Map<DevelopmentCardType, Integer> requirements, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 1, startingColumn);

        StringBuilder s = new StringBuilder();
        int remainingSpace = 7;

        if (requirements.keySet().size() == 1) {
            s.append("|  LevelRequired: ");
            for (Map.Entry<DevelopmentCardType, Integer> entry : requirements.entrySet()) {
                s.append(cliUtils.pCS(" \u25CF \u25CF", entry.getKey().getColor()));
                remainingSpace -= 4;
            }
        } else {
            s.append("|  DevCardsCost:  ");
            for (Map.Entry<DevelopmentCardType, Integer> entry : requirements.entrySet()) {
                s.append(entry.getValue()).append(cliUtils.pCS("\u25D8", entry.getKey().getColor())).append(" ");
                remainingSpace -= 3;
            }
        }
        s.append(cliUtils.hSpace(remainingSpace)).append("|");

        pw.print(s);
        pw.flush();
    }


    /**
     * If the Leader Cards requires Resources, this method is invoked in printLeaderRequirements()
     *
     * @param requirements   The requirements of the Leader Card
     * @param startingRow    The starting row where the Leader Card is going to be printed
     * @param startingColumn The starting column where the Leader Card is going to be printed
     */
    private void printLeaderResourcesRequirements(Map<Resource, Integer> requirements, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 1, startingColumn);

        StringBuilder s = new StringBuilder();
        int remainingSpace = 7;

        s.append("|  ResourcesCost: ");
        for (Map.Entry<Resource, Integer> entry : requirements.entrySet()) {
            s.append(entry.getValue()).append(cliUtils.pCS("\u25A0", entry.getKey().getColor())).append(" ");
            remainingSpace -= 3;
        }
        s.append(cliUtils.hSpace(remainingSpace)).append("|");

        pw.print(s);
        pw.flush();
    }

}
