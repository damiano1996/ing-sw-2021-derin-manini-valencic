package it.polimi.ingsw.psp26.view.cli.manualtesting;

import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCardsInitializer;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.view.cli.CLI;
import it.polimi.ingsw.psp26.view.cli.CliUtils;
import it.polimi.ingsw.psp26.view.cli.PersonalBoardCli;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestDisplayPersonalBoard {

    CLI cli;
    PersonalBoardCli personalBoardCli;
    CliUtils cliUtils;
    Scanner in;
    PrintWriter pw;

    public TestDisplayPersonalBoard() {
        pw = new PrintWriter(System.out);
        in = new Scanner(System.in);
        cliUtils = new CliUtils(pw);
        personalBoardCli = new PersonalBoardCli(pw);
        cli = new CLI();
    }


    /**
     * Used to launch tests
     */
    public static void main(String[] args) throws DevelopmentCardSlotOutOfBoundsException, CanNotAddDevelopmentCardToSlotException, CanNotAddResourceToDepotException, ColorDoesNotExistException, NoMoreDevelopmentCardsException, LevelDoesNotExistException, CanNotAddResourceToStrongboxException {
        TestDisplayPersonalBoard testDisplayPersonalBoard = new TestDisplayPersonalBoard();
        testDisplayPersonalBoard.testMethod();
    }


    /**
     * Call here the methods to test
     */
    public void testMethod() throws DevelopmentCardSlotOutOfBoundsException, CanNotAddDevelopmentCardToSlotException, CanNotAddResourceToDepotException, ColorDoesNotExistException, NoMoreDevelopmentCardsException, LevelDoesNotExistException, CanNotAddResourceToStrongboxException {
        testPersonalBoard();
        testResourceSupply();
        testFaithTrackMovement();
        testActionTokens();
        testActivateProduction();
    }


    /**
     * Test the ability to display the entire PersonalBoard elements
     */
    private void testPersonalBoard() throws CanNotAddResourceToDepotException, CanNotAddResourceToStrongboxException, ColorDoesNotExistException, NoMoreDevelopmentCardsException, LevelDoesNotExistException, DevelopmentCardSlotOutOfBoundsException, CanNotAddDevelopmentCardToSlotException {

        //---PERSONAL-BOARD-TEST---// Press Enter 3 times

        VirtualView virtualView = new VirtualView();
        Player player = new Player(virtualView, "nickname", "sessionToken");
        DevelopmentCardsGrid developmentCardsGrid = new DevelopmentCardsGrid(virtualView);

        LeaderDepot coinDepot = new LeaderDepot(virtualView, Resource.COIN);
        LeaderDepot servantDepot = new LeaderDepot(virtualView, Resource.SERVANT);

        List<Resource> strongboxResources = new ArrayList<>();
        List<LeaderCard> playerCards = new ArrayList<>();
        List<LeaderCard> leaderCardList;
        LeaderCardsInitializer leaderCardsInitializer = LeaderCardsInitializer.getInstance();

        player.giveInkwell();

        leaderCardList = leaderCardsInitializer.getLeaderCards();
        playerCards.add(leaderCardList.get(8));
        playerCards.add(leaderCardList.get(9));
        player.setLeaderCards(playerCards);

        cliUtils.cls();
        personalBoardCli.displayPersonalBoard(player, false);
        in.nextLine();
        cliUtils.cls();

        player.getPersonalBoard().getWarehouse().addLeaderDepot(coinDepot);
        player.getPersonalBoard().getWarehouse().addLeaderDepot(servantDepot);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(0, Resource.SHIELD);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(1, Resource.COIN);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(2, Resource.SERVANT);
        strongboxResources.add(Resource.STONE);
        strongboxResources.add(Resource.SERVANT);
        player.getPersonalBoard().addResourcesToStrongbox(strongboxResources);
        player.getPersonalBoard().addDevelopmentCard(0, developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST));
        player.getPersonalBoard().addDevelopmentCard(1, developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST));
        player.getPersonalBoard().addDevelopmentCard(2, developmentCardsGrid.drawCard(Color.YELLOW, Level.FIRST));
        player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[0].activatePopesFavorTile();

        personalBoardCli.displayPersonalBoard(player, false);
        in.nextLine();
        cliUtils.cls();

        player.getPersonalBoard().getWarehouse().addResourceToDepot(3, Resource.COIN);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(4, Resource.SERVANT);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(1, Resource.COIN);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(2, Resource.SERVANT);
        strongboxResources.add(Resource.COIN);
        strongboxResources.add(Resource.STONE);
        strongboxResources.add(Resource.SHIELD);
        player.getPersonalBoard().addResourcesToStrongbox(strongboxResources);
        player.getPersonalBoard().addDevelopmentCard(1, developmentCardsGrid.drawCard(Color.BLUE, Level.SECOND));
        player.getPersonalBoard().addDevelopmentCard(2, developmentCardsGrid.drawCard(Color.PURPLE, Level.SECOND));
        player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[1].activatePopesFavorTile();

        personalBoardCli.displayPersonalBoard(player, false);
        in.nextLine();
        cliUtils.cls();

        player.getPersonalBoard().getWarehouse().addResourceToDepot(3, Resource.COIN);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(4, Resource.SERVANT);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(2, Resource.SERVANT);
        strongboxResources.add(Resource.COIN);
        strongboxResources.add(Resource.SHIELD);
        strongboxResources.add(Resource.SERVANT);
        strongboxResources.add(Resource.COIN);
        player.getPersonalBoard().addResourcesToStrongbox(strongboxResources);
        player.getPersonalBoard().addDevelopmentCard(2, developmentCardsGrid.drawCard(Color.GREEN, Level.THIRD));
        player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[2].activatePopesFavorTile();

        personalBoardCli.displayPersonalBoard(player, false);
        in.nextLine();
    }


    /**
     * Test the ability to display the Resource Supply
     */
    private void testResourceSupply() {

        //--RESOURCE-SUPPLY-TEST---// Press enter 1 time

        ResourceSupply resourceSupply = new ResourceSupply();

        List<Resource> resources = new ArrayList<>();
        resources.add(Resource.COIN);
        resources.add(Resource.STONE);
        resources.add(Resource.SERVANT);

        cli.displayResourceSupply(resourceSupply, resources);
        in.nextLine();
    }


    /**
     * Test the ability to display the movement of the Faith Markers
     */
    private void testFaithTrackMovement() {

        //FAITH-TRACK-MOVEMENT-TEST---// Press Enter 39 times

        PersonalBoard personalBoard = new PersonalBoard(new VirtualView(), "sessionToken");

        cliUtils.cls();
        for (int i = 0; i < 25; i++) {
            if (i == 3 || i == 7) personalBoard.getFaithTrack().moveBlackCrossPosition(1);
            if (i == 14) {
                for (int m = 0; m < 14; m++) {
                    personalBoard.getFaithTrack().moveBlackCrossPosition(1);
                    cli.displayFaithTrack(personalBoard.getFaithTrack(), false);
                    in.nextLine();
                }
            }
            cli.displayFaithTrack(personalBoard.getFaithTrack(), false);
            in.nextLine();
            personalBoard.getFaithTrack().moveMarkerPosition(1);
            cliUtils.cls();
        }
    }


    /**
     * Test the ability to display the Action Tokens
     */
    private void testActionTokens() {

        //---ACTION-TOKENS-TEST---// Press enter 7 times

        Match match = new Match(new VirtualView(), 0);

        List<ActionToken> unusedTokens = match.drawActionTokens(7);
        for (int i = 0; i < 7; i++) {
            personalBoardCli.displayActionTokens(unusedTokens);
            in.nextLine();
            unusedTokens.remove(0);
        }
    }


    /**
     * Test the ability to display the Productions
     */
    private void testActivateProduction() throws ColorDoesNotExistException, NoMoreDevelopmentCardsException, LevelDoesNotExistException {

        //---ACTIVATE-PRODUCTION-TEST---// Press enter 1 time

        DevelopmentCardsGrid developmentCardsGrid = new DevelopmentCardsGrid(new VirtualView());
        List<Production> productions = new ArrayList<>();

        productions.add(developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST).getProduction());
        productions.add(developmentCardsGrid.drawCard(Color.GREEN, Level.SECOND).getProduction());
        productions.add(developmentCardsGrid.drawCard(Color.GREEN, Level.THIRD).getProduction());
        productions.add(developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST).getProduction());
        productions.add(developmentCardsGrid.drawCard(Color.GREEN, Level.SECOND).getProduction());
        productions.add(developmentCardsGrid.drawCard(Color.GREEN, Level.THIRD).getProduction());

        cliUtils.cls();
        personalBoardCli.displayProductionActivation(productions, new ArrayList<>());
        in.nextLine();
    }

}
