package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LeaderCardsCli {

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
    public void displayLeaderSelection(List<LeaderCard> leaderCards) { //TODO migliora la gestione della scelta
        List<LeaderCard> selectedLeaders = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        int index;

        for (int i = 0; i < 2; i++) {
            cliUtils.cls();

            cliUtils.printFigure("LeaderSelectionTitle", 1, 28);

            printLeaderChoice(leaderCards, 11);

            for (int j = 0; j < leaderCards.size(); j++)
                isLeaderSelected(leaderCards.get(j), selectedLeaders, leaderCards, 30, 40 + (j * 45));

            cliUtils.vSpace(5);

            pw.print(cliUtils.hSpace(27) + "Please type the number of the ");
            if (i == 0) pw.print("first ");
            else pw.print("second ");
            pw.print("Leader of your choice: ");
            pw.flush();

            //TEMPORARY SOLUTION
            do {
                do {
                    index = in.nextInt() - 1;
                    if (index < 0 || index > 3) {
                        pw.print(cliUtils.hSpace(27) + "Index is out of bounds! Please try again: ");
                        pw.flush();
                    }
                } while (index > 3 || index < 0);
                if (!selectedLeaders.contains(leaderCards.get(index))) {
                    selectedLeaders.add(leaderCards.get(index));
                    break;
                } else {
                    pw.print(cliUtils.hSpace(27) + "Leader already selected! Please try again: ");
                    pw.flush();
                }
            } while (selectedLeaders.contains(leaderCards.get(index)));
        }

        in.nextLine();

        /*Debug only*/
        pw.println("You selected Leader " + (leaderCards.indexOf(selectedLeaders.get(0)) + 1) + " and Leader " + (leaderCards.indexOf(selectedLeaders.get(1)) + 1));
    }

    /**
     * If a Leader Cards is selected, mark it with a red SELECTED String
     *
     * @param leaderCard      The card to check if has been selected
     * @param selectedLeaders The Leaders selected by the Player
     * @param leaderCards     The 4 Leader Cards given to the Player's choice
     * @param startingRow     The starting row where the Leader Card is going to be printed
     * @param startingColumn  The starting column where the Leader Card is going to be printed
     */
    private void isLeaderSelected(LeaderCard leaderCard, List<LeaderCard> selectedLeaders, List<LeaderCard> leaderCards, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow, startingColumn);
        if (selectedLeaders.contains(leaderCard)) pw.print("\u001b[41m  SELECTED  \u001b[0m");
        else pw.print("  Leader " + (leaderCards.indexOf(leaderCard) + 1) + "  ");
        pw.flush();
    }

    /**
     * Prints the 4 Leader cards one next to the others. Used in selectLeaders() method
     *
     * @param leaderCards The cards to print
     * @param startingRow The starting row where the Leader Card is going to be printed
     */
    public void printLeaderChoice(List<LeaderCard> leaderCards, int startingRow) {
        for (int i = 0; i < leaderCards.size(); i++) printLeader(leaderCards.get(i), startingRow, 33 + (i * 45));
    }

    /**
     * Prints a LeaderCard line by line
     *
     * @param leaderCard     The Leader Card to print
     * @param startingRow    The row in which the Leader Card will be printed
     * @param startingColumn The column in which the Leader Card will be printed
     */
    public void printLeader(LeaderCard leaderCard, int startingRow, int startingColumn) {
        cliUtils.printFigure("LeaderCard", startingRow, startingColumn);
        printLeaderRequirements(leaderCard, startingRow, startingColumn);
        printLeaderAbility(leaderCard, startingRow, startingColumn);
        printLeaderDescription(leaderCard, startingRow, startingColumn);
        printLeaderVictoryPoints(leaderCard, startingRow, startingColumn);
        printLeaderResourceInformation(leaderCard, startingRow, startingColumn);
    }

    /**
     * Prints the Leader Card Resource information
     *
     * @param leaderCard     The Leader Card to print
     * @param startingRow    The row in which the Leader Card will be printed
     * @param startingColumn The column in which the Leader Card will be printed
     */
    private void printLeaderResourceInformation(LeaderCard leaderCard, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 16, startingColumn);
//        pw.print("| " + leaderCard.getSpecialAbility().getResourceInformation() + " |"); // TODO: to be fixed
        pw.flush();
    }

    /**
     * Prints the Leader Card Victory Points
     *
     * @param leaderCard     The Leader Card to print
     * @param startingRow    The row in which the Leader Card will be printed
     * @param startingColumn The column in which the Leader Card will be printed
     */
    private void printLeaderVictoryPoints(LeaderCard leaderCard, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 13, startingColumn);
        pw.print("| |        VP " + leaderCard.getVictoryPoints() + "        | |");
        pw.flush();
    }

    /**
     * Prints the Leader Card description
     *
     * @param leaderCard     The Leader Card to print
     * @param startingRow    The row in which the Leader Card will be printed
     * @param startingColumn The column in which the Leader Card will be printed
     */
    private void printLeaderDescription(LeaderCard leaderCard, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 8, startingColumn);

        for (int i = 0; i < 4; i++) {
            cliUtils.saveCursorPosition();
//            pw.print("| " + leaderCard.getSpecialAbility().getPowerDescription(i) + " |"); // TODO: to be fixed
            pw.flush();
            cliUtils.restoreCursorPosition();
            cliUtils.moveCursor("dn", 1);
        }
    }

    /**
     * Prints the Leader Card ability name
     *
     * @param leaderCard     The Leader Card to print
     * @param startingRow    The row in which the Leader Card will be printed
     * @param startingColumn The column in which the Leader Card will be printed
     */
    private void printLeaderAbility(LeaderCard leaderCard, int startingRow, int startingColumn) {
        cliUtils.setCursorPosition(startingRow + 3, startingColumn);
//        pw.print("| | " + leaderCard.getSpecialAbility().getAbilityType() + " | |"); // TODO: to be fixed
        pw.flush();
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
