package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.psp26.configurations.Configurations.CLI_WIDTH;

/**
 * Class that contains all the methods related to display the LeaderCards.
 */
public class LeaderCardsCli {

    private final PrintWriter pw;
    private final CliUtils cliUtils;

    /**
     * Constructor of the class.
     * It creates a new PrintWriter and a new CliUtils to use.
     *
     * @param pw the PrintWriter to use
     */
    public LeaderCardsCli(PrintWriter pw) {
        this.pw = pw;
        this.cliUtils = new CliUtils(pw);
    }


    /**
     * Prints the given LeaderCards by correctly aligning them in the center of the screen.
     *
     * @param leaderCards the LeaderCards to print
     * @param startingRow the row where the cards will be printed
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
     * Calculates the correct position for printing the LeaderCards.
     *
     * @param numberOfLeaders the number of LeaderCards to print
     * @return the correct column position to print them
     */
    public int getPrintMultipleLeadersStartingColumn(int numberOfLeaders) {
        return ((CLI_WIDTH - (numberOfLeaders * 27) - ((numberOfLeaders - 1) * 18)) / 2) - 4;
    }


    /**
     * Prints a single LeaderCard.
     * First, the base LeaderCard figure is printed.
     * Then, adds the LeaderCard requirements and the LeaderCard characteristic Resource.
     *
     * @param leaderCard     the LeaderCard to print
     * @param startingRow    the row in which the LeaderCard will be printed
     * @param startingColumn the column in which the LeaderCard will be printed
     */
    private void printLeader(LeaderCard leaderCard, int startingRow, int startingColumn) {
        cliUtils.printFigure(
                "leadercards/" + leaderCard.getAbilityToString().split("-")[0],
                startingRow, startingColumn);
        printLeaderRequirements(leaderCard, startingRow, startingColumn);
        //Prints the colored square for the Resource information
        cliUtils.pPCS("\u25A0", leaderCard.getAbilityResource().getColor(), startingRow + 16, startingColumn + 22);
    }


    /**
     * Prints the requirement of a given LeaderCard.
     *
     * @param leaderCard     the LeaderCard to analyze
     * @param startingRow    the starting row where the LeaderCard is going to be printed
     * @param startingColumn tThe starting column where the LeaderCard is going to be printed
     */
    private void printLeaderRequirements(LeaderCard leaderCard, int startingRow, int startingColumn) {
        if (leaderCard.getDevelopmentCardRequirements().keySet().size() == 0)
            printLeaderResourcesRequirements(leaderCard.getResourcesRequirements(), startingRow, startingColumn);
        else
            printLeaderDevelopmentCardRequirements(leaderCard.getDevelopmentCardRequirements(), startingRow, startingColumn);
    }


    /**
     * If the LeaderCards requires DevelopmentCards, this method is invoked in printLeaderRequirements().
     *
     * @param requirements   the requirements of the LeaderCard
     * @param startingRow    the starting row where the LeaderCard is going to be printed
     * @param startingColumn the starting column where the LeaderCard is going to be printed
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
     * If the LeaderCards requires Resources, this method is invoked in printLeaderRequirements().
     *
     * @param requirements   the requirements of the LeaderCard
     * @param startingRow    the starting row where the LeaderCard is going to be printed
     * @param startingColumn the starting column where the LeaderCard is going to be printed
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
