package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCardsInitializer;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.server.Server;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliTest {

    private final Client client;

    private final PrintWriter pw;
    private final boolean isMultiPlayerMode;

    private final CLI cli;
    private final CliUtils cliUtils;
    private final DepotCli depotCli;
    private final DevelopmentCardsCli developmentCardsCli;
    private final FaithTrackCli faithTrackCli;
    private final LeaderCardsCli leaderCardsCli;
    private final MarketCli marketCli;
    private final PersonalBoardCli personalBoardCli;

    public CliTest(Client client) {
        this.client = client;

        this.pw = new PrintWriter(System.out);
        this.isMultiPlayerMode = true;
        this.cliUtils = new CliUtils(pw);
        this.depotCli = new DepotCli(pw);
        this.developmentCardsCli = new DevelopmentCardsCli(pw);
        this.faithTrackCli = new FaithTrackCli(pw);
        this.leaderCardsCli = new LeaderCardsCli(pw);
        this.marketCli = new MarketCli(pw);
        this.personalBoardCli = new PersonalBoardCli(pw);
        this.cli = new CLI(client);
    }


    //CLI testing. Only press start to test CLI functionalities
    public static void main(String[] args) throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, CanNotAddResourceToDepotException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, DepotOutOfBoundException, CanNotAddResourceToStrongboxException {
        try {
            CliTest cliTest = new CliTest(new Client());
            cliTest.testMethod();

        } catch (IOException | NegativeNumberOfElementsToGrabException e) {
            e.printStackTrace();
        }
    }

    public void testMethod() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, DepotOutOfBoundException, CanNotAddResourceToDepotException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, CanNotAddResourceToStrongboxException, NegativeNumberOfElementsToGrabException {

        //---OBJECTS-DECLARATION---//

        VirtualView virtualView = new VirtualView(new Server());
        Player player = new Player(virtualView, "Player", "000000");
        LeaderCardsInitializer leaderCardsInitializer = new LeaderCardsInitializer();
        List<LeaderCard> leaderCards = leaderCardsInitializer.getLeaderCards();
        PersonalBoard personalBoard = new PersonalBoard(virtualView);
        MarketTray marketTray = new MarketTray(virtualView);
        DevelopmentGrid developmentGrid;
        ResourceSupply resourceSupply = new ResourceSupply();
        Match metch = new Match(virtualView, 0);
        Scanner in = new Scanner(System.in);


        //---MARKET-TEST---// Press Enter 3 times

        cliUtils.cls();
        cli.displayMarketScreen(marketTray);
        in.nextLine();
        marketTray.pushMarbleFromSlideToRow(1);
        cliUtils.cls();
        cli.displayMarketScreen(marketTray);
        in.nextLine();
        marketTray.pushMarbleFromSlideToColumn(3);
        cliUtils.cls();
        cli.displayMarketScreen(marketTray);
        in.nextLine();


        //---ACTION-TOKENS-TEST---//

        List<ActionToken> unusedTokens = metch.drawActionTokens(7);
        for (int i = 0; i < 7; i++) {
            cli.displayActionTokens(unusedTokens);
            in.nextLine();
            unusedTokens.remove(0);
        }


        //--RESOURCE-SUPPLY-TEST---// Press enter 1 time

        cli.displayResourceSupply(resourceSupply);
        in.nextLine();


        //---ERROR-TEST---//

        cli.displayError("ERROR                     TEST");
        in.nextLine();


        //---DEVELOPMENT-GRID-SHOW-ALL-CARDS-TEST---// Press Enter 4 times

        developmentGrid = new DevelopmentGrid(virtualView);

        for (int i = 0; i < 4; i++) {
            cliUtils.cls();
            cli.displayDevelopmentGridCardSelection(developmentGrid);
            in.nextLine();
            developmentGrid.drawCard(Color.PURPLE, Level.FIRST);
            developmentGrid.drawCard(Color.PURPLE, Level.SECOND);
            developmentGrid.drawCard(Color.PURPLE, Level.THIRD);
            developmentGrid.drawCard(Color.BLUE, Level.FIRST);
            developmentGrid.drawCard(Color.BLUE, Level.SECOND);
            developmentGrid.drawCard(Color.BLUE, Level.THIRD);
            developmentGrid.drawCard(Color.YELLOW, Level.FIRST);
            developmentGrid.drawCard(Color.YELLOW, Level.SECOND);
            developmentGrid.drawCard(Color.YELLOW, Level.THIRD);
            developmentGrid.drawCard(Color.GREEN, Level.FIRST);
            developmentGrid.drawCard(Color.GREEN, Level.SECOND);
            developmentGrid.drawCard(Color.GREEN, Level.THIRD);
        }

        //---WAREHOUSE-CONFIGURATION-TEST---// Press Enter 3 times

        PersonalBoard p = new PersonalBoard(virtualView);
        List<Resource> resources = new ArrayList<>();
        resources.add(Resource.STONE);
        resources.add(Resource.STONE);
        resources.add(Resource.COIN);
        resources.add(Resource.SERVANT);
        cli.displayMarketResourcesSelection(p.getWarehouseDepots(), resources);

        p.getWarehouseDepot(2).addResource(Resource.STONE);
        p.getWarehouseDepot(2).addResource(Resource.STONE);
        resources.remove(0);
        resources.remove(0);
        cli.displayMarketResourcesSelection(p.getWarehouseDepots(), resources);

        p.getWarehouseDepot(1).addResource(Resource.COIN);
        resources.remove(0);
        cli.displayMarketResourcesSelection(p.getWarehouseDepots(), resources);

        p.getWarehouseDepot(0).addResource(Resource.SERVANT);
        resources.remove(0);
        cli.displayMarketResourcesSelection(p.getWarehouseDepots(), resources);


        //---TITLE-SCREEN-TEST---// Press Enter and follow terminal instructions

        cli.displayLogIn();


        //---SHOW-ALL-LEADERS-TEST---// Press enter 4 times

        cliUtils.cls();
        List<LeaderCard> fourLeaders = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            fourLeaders = new ArrayList<>();
            fourLeaders.add(leaderCards.get(i));
            fourLeaders.add(leaderCards.get(4 + i));
            fourLeaders.add(leaderCards.get(8 + i));
            fourLeaders.add(leaderCards.get(12 + i));

            leaderCardsCli.printLeaderChoice(fourLeaders, 1);
            in.nextLine();
            cliUtils.cls();
        }


        //---SELECT-LEADER-SCREEN-TEST---// Follow terminal instructions

        cliUtils.cls();
        cli.displayLeaderChoice(fourLeaders);
        in.nextLine();


        //---PERSONAL-BOARD-TEST---// Press Enter 3 times

        cliUtils.cls();
        player.giveInkwell();
        List<Resource> strongbox = new ArrayList<>();
        LeaderDepot coinDepot = new LeaderDepot(virtualView, Resource.COIN);
        LeaderDepot servantDepot = new LeaderDepot(virtualView, Resource.SERVANT);
        List<LeaderCard> playerCards = new ArrayList<>();
        List<LeaderCard> leaderCardList = new ArrayList<>();
        LeaderCardsInitializer l = new LeaderCardsInitializer();
        leaderCardList = l.getLeaderCards();
        playerCards.add(leaderCardList.get(8));
        playerCards.add(leaderCardList.get(9));
        player.setLeaderCards(playerCards);
        cli.displayPersonalBoard(player);
//        displayNormalActionsSelection();
        List<Resource> strResource = new ArrayList<>();
        in.nextLine();
        cliUtils.cls();

        developmentGrid = new DevelopmentGrid(virtualView);

        player.getPersonalBoard().addLeaderDepot(coinDepot);
        player.getPersonalBoard().addLeaderDepot(servantDepot);
        player.getPersonalBoard().getWarehouseDepots().get(0).addResource(Resource.SHIELD);
        player.getPersonalBoard().getWarehouseDepots().get(1).addResource(Resource.COIN);
        player.getPersonalBoard().getWarehouseDepots().get(2).addResource(Resource.SERVANT);
        strResource.add(Resource.STONE);
        strResource.add(Resource.SERVANT);
        player.getPersonalBoard().addResourcesToStrongbox(strResource);
        player.getPersonalBoard().addDevelopmentCard(0, developmentGrid.drawCard(Color.GREEN, Level.FIRST));
        player.getPersonalBoard().addDevelopmentCard(1, developmentGrid.drawCard(Color.GREEN, Level.FIRST));
        player.getPersonalBoard().addDevelopmentCard(2, developmentGrid.drawCard(Color.YELLOW, Level.FIRST));
        player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[0].activatePopesFavorTile();
        cli.displayPersonalBoard(player);
//        displayLeaderActionSelection();
        in.nextLine();
        cliUtils.cls();

        player.getPersonalBoard().getWarehouseDepot(3).addResource(Resource.COIN);
        player.getPersonalBoard().getWarehouseDepot(4).addResource(Resource.SERVANT);
        player.getPersonalBoard().getWarehouseDepots().get(1).addResource(Resource.COIN);
        player.getPersonalBoard().getWarehouseDepots().get(2).addResource(Resource.SERVANT);
        strResource.add(Resource.COIN);
        strResource.add(Resource.STONE);
        strResource.add(Resource.SHIELD);
        player.getPersonalBoard().addResourcesToStrongbox(strResource);
        player.getPersonalBoard().addDevelopmentCard(1, developmentGrid.drawCard(Color.BLUE, Level.SECOND));
        player.getPersonalBoard().addDevelopmentCard(2, developmentGrid.drawCard(Color.PURPLE, Level.SECOND));
        player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[1].activatePopesFavorTile();
        cli.displayPersonalBoard(player);
//        displayNormalActionsSelection();
        in.nextLine();
        cliUtils.cls();

        player.getPersonalBoard().getWarehouseDepot(3).addResource(Resource.COIN);
        player.getPersonalBoard().getWarehouseDepot(4).addResource(Resource.SERVANT);
        player.getPersonalBoard().getWarehouseDepots().get(2).addResource(Resource.SERVANT);
        strResource.add(Resource.COIN);
        strResource.add(Resource.SHIELD);
        strResource.add(Resource.SERVANT);
        strResource.add(Resource.COIN);
        player.getPersonalBoard().addResourcesToStrongbox(strResource);
        player.getPersonalBoard().addDevelopmentCard(2, developmentGrid.drawCard(Color.GREEN, Level.THIRD));
        player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[2].activatePopesFavorTile();
        cli.displayPersonalBoard(player);
//        displayNormalActionsSelection();
        in.nextLine();


        //---ACTIVATE-PRODUCTION-TEST---// Press enter 1 time

        cliUtils.cls();
        developmentGrid = new DevelopmentGrid(virtualView);
        List<Production> productions = new ArrayList<>();
        productions.add(developmentGrid.drawCard(Color.GREEN, Level.FIRST).getProduction());
        productions.add(developmentGrid.drawCard(Color.GREEN, Level.SECOND).getProduction());
        productions.add(developmentGrid.drawCard(Color.GREEN, Level.THIRD).getProduction());
        productions.add(developmentGrid.drawCard(Color.GREEN, Level.FIRST).getProduction());
        productions.add(developmentGrid.drawCard(Color.GREEN, Level.SECOND).getProduction());
        productions.add(developmentGrid.drawCard(Color.GREEN, Level.THIRD).getProduction());
        cli.displayProductionActivation(productions);
        in.nextLine();


        //---PRINT-PLAYER-LEADER-CARDS-TEST---// Press Enter 7 times

        cliUtils.cls();
        cli.displayLeaderCards(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.getLeaderCards().get(0).activate();
        cli.displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        cli.displayLeaderCards(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.getLeaderCards().get(1).activate();
        cli.displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        cli.displayLeaderCards(player.getLeaderCards());
        cli.displayLeaderCardDiscardSelection(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.discardLeaderCard(player.getLeaderCards().get(0)); //Simulate a Leader Card discard
        cli.displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        cli.displayLeaderCards(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.discardLeaderCard(player.getLeaderCards().get(0)); //Simulate a Leader Card discard
        cli.displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        cli.displayLeaderCards(player.getLeaderCards());
        in.nextLine();


        //FAITH-TRACK-MOVEMENT-TEST---// Press Enter 25 times

        cliUtils.cls();
        for (int i = 0; i < 25; i++) {
            cli.displayFaithTrack(personalBoard.getFaithTrack());
            in.nextLine();
            personalBoard.getFaithTrack().moveMarkerPosition(1);
            cliUtils.cls();
        }

    }
}
