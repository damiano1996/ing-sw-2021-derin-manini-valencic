package it.polimi.ingsw.psp26.view.cli.manualtesting;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCardsInitializer;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.view.cli.CLI;
import it.polimi.ingsw.psp26.view.cli.CliUtils;
import it.polimi.ingsw.psp26.view.cli.LeaderCardsCli;
import it.polimi.ingsw.psp26.view.cli.PersonalBoardCli;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestDisplayLeaderCards {

    CLI cli;
    PersonalBoardCli personalBoardCli;
    LeaderCardsCli leaderCardsCli;
    CliUtils cliUtils;
    Scanner in;
    PrintWriter pw;

    public TestDisplayLeaderCards() {
        pw = new PrintWriter(System.out);
        in = new Scanner(System.in);
        cliUtils = new CliUtils(pw);
        leaderCardsCli = new LeaderCardsCli(pw);
        personalBoardCli = new PersonalBoardCli(pw);
        cli = new CLI();
    }


    /**
     * Used to launch tests
     */
    public static void main(String[] args) {
        TestDisplayLeaderCards testDisplayLeaderCards = new TestDisplayLeaderCards();
        testDisplayLeaderCards.testMethod();
    }


    /**
     * Call here the methods to test
     */
    public void testMethod() {
        testLeaderCards();
        testSelectLeaders();
        testPrintPlayerLeaderCards();
    }


    /**
     * Test the ability to show all the Leader Cards
     */
    private void testLeaderCards() {

        //---SHOW-ALL-LEADERS-TEST---// Press enter 4 times

        List<LeaderCard> leaderCards = LeaderCardsInitializer.getInstance().getLeaderCards();
        List<LeaderCard> fourLeaders;

        cliUtils.cls();

        for (int i = 0; i < 4; i++) {
            fourLeaders = new ArrayList<>();
            fourLeaders.add(leaderCards.get(i));
            fourLeaders.add(leaderCards.get(4 + i));
            fourLeaders.add(leaderCards.get(8 + i));
            fourLeaders.add(leaderCards.get(12 + i));

            leaderCardsCli.printMultipleLeaders(fourLeaders, 1);
            in.nextLine();
            cliUtils.cls();
        }
    }


    /**
     * Test the ability to print the selected Leader Cards
     */
    private void testSelectLeaders() {

        //---SELECT-LEADER-SCREEN-TEST---// Press enter 1 time

        List<LeaderCard> leaderCards = LeaderCardsInitializer.getInstance().getLeaderCards();
        List<LeaderCard> fourLeaders = new ArrayList<>();
        for (int i = 0; i < 4; i++) fourLeaders.add(leaderCards.get(i));

        cliUtils.cls();
        leaderCardsCli.printMultipleLeaders(fourLeaders, 1);
        in.nextLine();

        List<LeaderCard> twoLeaders = new ArrayList<>();
        twoLeaders.add(fourLeaders.get(0));
        twoLeaders.add(fourLeaders.get(1));

        cliUtils.cls();
        cli.displayLeaderCardDiscardActivationSelection(twoLeaders);
        in.nextLine();
    }


    /**
     * Test the ability to display the PLayer's Leader Cards
     */
    private void testPrintPlayerLeaderCards() {

        //---PRINT-PLAYER-LEADER-CARDS-TEST---// Press Enter 7 times

        List<LeaderCard> leaderCards = LeaderCardsInitializer.getInstance().getLeaderCards();
        Player player = new Player(new VirtualView(), "nickname", "sessionToken");

        List<LeaderCard> playerLeaders = new ArrayList<>();
        playerLeaders.add(leaderCards.get(0));
        playerLeaders.add(leaderCards.get(1));

        player.setLeaderCards(playerLeaders);

        cliUtils.cls();
        cli.displayLeaderCards(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.getLeaderCards().get(0).activate(player);
        personalBoardCli.displayPersonalBoard(player, false);
        in.nextLine();
        cliUtils.cls();
        cli.displayLeaderCards(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.getLeaderCards().get(1).activate(player);
        personalBoardCli.displayPersonalBoard(player, false);
        in.nextLine();
        cliUtils.cls();
        cli.displayLeaderCards(player.getLeaderCards());
        cli.displayLeaderCardDiscardActivationSelection(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.discardLeaderCard(player.getLeaderCards().get(0)); //Simulate a Leader Card discard
        personalBoardCli.displayPersonalBoard(player, false);
        in.nextLine();
        cliUtils.cls();
        cli.displayLeaderCards(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.discardLeaderCard(player.getLeaderCards().get(0)); //Simulate a Leader Card discard
        personalBoardCli.displayPersonalBoard(player, false);
        in.nextLine();
        cliUtils.cls();
        cli.displayLeaderCards(player.getLeaderCards());
        in.nextLine();
    }

}
