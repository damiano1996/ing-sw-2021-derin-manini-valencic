package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.*;
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
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliTest {

    private final CLI cli;
    private final CliUtils cliUtils;
    private final DepotCli depotCli;
    private final DevelopmentCardsCli developmentCardsCli;
    private final FaithTrackCli faithTrackCli;
    private final LeaderCardsCli leaderCardsCli;
    private final MarketCli marketCli;
    private final PersonalBoardCli personalBoardCli;

    private final VirtualView virtualView;
    private final Player player;
    private final List<LeaderCard> leaderCards;
    private final PersonalBoard personalBoard;
    private final MarketTray marketTray;
    private DevelopmentGrid developmentGrid;
    private final ResourceSupply resourceSupply;
    private final Match match;
    private final Scanner in;

    public CliTest(Client client) {

        PrintWriter pw = new PrintWriter(System.out);
        boolean isMultiPlayerMode = true;
        this.cliUtils = new CliUtils(pw);
        this.depotCli = new DepotCli(pw);
        this.developmentCardsCli = new DevelopmentCardsCli(pw);
        this.faithTrackCli = new FaithTrackCli(pw);
        this.leaderCardsCli = new LeaderCardsCli(pw);
        this.marketCli = new MarketCli(pw);
        this.personalBoardCli = new PersonalBoardCli(pw);
        this.cli = new CLI(client);

        virtualView = new VirtualView();
        player = new Player(virtualView, "Player", "000000");
        LeaderCardsInitializer leaderCardsInitializer = LeaderCardsInitializer.getInstance();
        leaderCards = leaderCardsInitializer.getLeaderCards();
        personalBoard = new PersonalBoard(virtualView, player);
        marketTray = new MarketTray(virtualView);
        resourceSupply = new ResourceSupply();
        match = new Match(virtualView, 0);
        in = new Scanner(System.in);
    }


    //CLI testing. Only press start to test CLI functionalities
    public static void main(String[] args) throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, CanNotAddResourceToDepotException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, CanNotAddResourceToStrongboxException {
        try {
            CliTest cliTest = new CliTest(new Client());
            cliTest.testMethod();
        } catch (IOException | NegativeNumberOfElementsToGrabException | EmptyPayloadException | CanNotAddResourceToWarehouse e) {
            e.printStackTrace();
        }
    }

    
    public void testMethod() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, CanNotAddResourceToDepotException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, CanNotAddResourceToStrongboxException, NegativeNumberOfElementsToGrabException, EmptyPayloadException, CanNotAddResourceToWarehouse {

        //Insert here the name of the methods tou want to test

        //testWarehouseConfiguration();
        
        testMessages();
        
        testFaithTrackMovement();
        testMarket();
        testActionTokens();
        testResourceSupply();
        testError();
        testDevelopmentGrid();
        testLeaderCards();
        testSelectLeaders();
        testPersonalBoard();
        testActivateProduction();
        testPrintPlayerLeaderCards();
        testWaitingScreen();
    }


    private void testWarehouseConfiguration() throws CanNotAddResourceToDepotException {

        //---WAREHOUSE-CONFIGURATION-TEST---// Press Enter 3 times

        PersonalBoard p = new PersonalBoard(virtualView, player);
        List<Resource> resources = new ArrayList<>();

        p.getWarehouse().addLeaderDepot(new LeaderDepot(virtualView, Resource.COIN));
        p.getWarehouse().addLeaderDepot(new LeaderDepot(virtualView, Resource.SHIELD));

        p.getWarehouse().addResourceToDepot(1, Resource.STONE);
        p.getWarehouse().addResourceToDepot(2, Resource.SHIELD);
        p.getWarehouse().addResourceToDepot(2, Resource.SHIELD);

        resources.add(Resource.STONE);
        resources.add(Resource.STONE);
        resources.add(Resource.COIN);
        resources.add(Resource.SHIELD);
        resources.add(Resource.COIN);
        resources.add(Resource.COIN);
        resources.add(Resource.SERVANT);

        cli.displayWarehouseNewResourcesAssignment(p.getWarehouse(), resources);
        in.nextLine();
    }


    private void testFaithTrackMovement() {

        //FAITH-TRACK-MOVEMENT-TEST---// Press Enter 25 times

        cliUtils.cls();
        for (int i = 0; i < 25; i++) {
            if (i == 3 || i == 7) personalBoard.getFaithTrack().moveBlackCrossPosition(1);
            if (i == 14) {
                for (int m = 0; m < 14; m++) {
                    personalBoard.getFaithTrack().moveBlackCrossPosition(1);
                    cli.displayFaithTrack(personalBoard.getFaithTrack());
                    in.nextLine();
                }
            }
            cli.displayFaithTrack(personalBoard.getFaithTrack());
            in.nextLine();
            personalBoard.getFaithTrack().moveMarkerPosition(1);
            cliUtils.cls();
        }
    }


    private void testMarket() {

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
    }


    private void testActionTokens() {

        //---ACTION-TOKENS-TEST---// Press enter 7 times

        List<ActionToken> unusedTokens = match.drawActionTokens(7);
        for (int i = 0; i < 7; i++) {
            cli.displayActionTokens(unusedTokens);
            in.nextLine();
            unusedTokens.remove(0);
        }
    }


    private void testResourceSupply() {

        //--RESOURCE-SUPPLY-TEST---// Press enter 1 time

        cli.displayResourceSupply(resourceSupply);
        in.nextLine();
    }


    private void testError() {

        //---ERROR-TEST---// Press enter 1 time

        cli.displayError("ERROR                     TEST");
        in.nextLine();
    }


    private void testDevelopmentGrid() throws ColorDoesNotExistException, NoMoreDevelopmentCardsException, LevelDoesNotExistException {

        //---DEVELOPMENT-GRID-SHOW-ALL-CARDS-TEST---// Press Enter 4 times

        developmentGrid = new DevelopmentGrid(virtualView);

        for (int i = 0; i < 4; i++) {
            cliUtils.cls();
            cli.displayDevelopmentGrid(developmentGrid);
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
    }


    private void testLeaderCards() {

        //---SHOW-ALL-LEADERS-TEST---// Press enter 4 times

        cliUtils.cls();
        List<LeaderCard> fourLeaders;

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


    private void testSelectLeaders() {

        //---SELECT-LEADER-SCREEN-TEST---// Follow terminal instructions

        List<LeaderCard> fourLeaders = new ArrayList<>();
        for (int i = 0; i < 4; i++) fourLeaders.add(leaderCards.get(i));

        cliUtils.cls();
        leaderCardsCli.printMultipleLeaders(fourLeaders, 1);
        in.nextLine();
        List<LeaderCard> twoleaders = new ArrayList<>();
        twoleaders.add(fourLeaders.get(0));
        twoleaders.add(fourLeaders.get(1));
        cliUtils.cls();
        cli.displayLeaderCardDiscardActivationSelection(twoleaders);
        in.nextLine();
    }


    private void testPersonalBoard() throws CanNotAddResourceToDepotException, CanNotAddResourceToStrongboxException, ColorDoesNotExistException, NoMoreDevelopmentCardsException, LevelDoesNotExistException, DevelopmentCardSlotOutOfBoundsException, CanNotAddDevelopmentCardToSlotException {

        //---PERSONAL-BOARD-TEST---// Press Enter 3 times

        cliUtils.cls();
        player.giveInkwell();
        LeaderDepot coinDepot = new LeaderDepot(virtualView, Resource.COIN);
        LeaderDepot servantDepot = new LeaderDepot(virtualView, Resource.SERVANT);
        List<LeaderCard> playerCards = new ArrayList<>();
        List<LeaderCard> leaderCardList;
        LeaderCardsInitializer l = LeaderCardsInitializer.getInstance();

        leaderCardList = l.getLeaderCards();
        playerCards.add(leaderCardList.get(8));
        playerCards.add(leaderCardList.get(9));
        player.setLeaderCards(playerCards);
        cli.displayPersonalBoard(player);
        List<Resource> strResource = new ArrayList<>();
        in.nextLine();
        cliUtils.cls();

        developmentGrid = new DevelopmentGrid(virtualView);

        player.getPersonalBoard().getWarehouse().addLeaderDepot(coinDepot);
        player.getPersonalBoard().getWarehouse().addLeaderDepot(servantDepot);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(0, Resource.SHIELD);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(1, Resource.COIN);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(2, Resource.SERVANT);
        strResource.add(Resource.STONE);
        strResource.add(Resource.SERVANT);
        player.getPersonalBoard().addResourcesToStrongbox(strResource);
        player.getPersonalBoard().addDevelopmentCard(0, developmentGrid.drawCard(Color.GREEN, Level.FIRST));
        player.getPersonalBoard().addDevelopmentCard(1, developmentGrid.drawCard(Color.GREEN, Level.FIRST));
        player.getPersonalBoard().addDevelopmentCard(2, developmentGrid.drawCard(Color.YELLOW, Level.FIRST));
        player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[0].activatePopesFavorTile();
        cli.displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();

        player.getPersonalBoard().getWarehouse().addResourceToDepot(3, Resource.COIN);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(4, Resource.SERVANT);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(1, Resource.COIN);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(2, Resource.SERVANT);
        strResource.add(Resource.COIN);
        strResource.add(Resource.STONE);
        strResource.add(Resource.SHIELD);
        player.getPersonalBoard().addResourcesToStrongbox(strResource);
        player.getPersonalBoard().addDevelopmentCard(1, developmentGrid.drawCard(Color.BLUE, Level.SECOND));
        player.getPersonalBoard().addDevelopmentCard(2, developmentGrid.drawCard(Color.PURPLE, Level.SECOND));
        player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[1].activatePopesFavorTile();
        cli.displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();

        player.getPersonalBoard().getWarehouse().addResourceToDepot(3, Resource.COIN);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(4, Resource.SERVANT);
        player.getPersonalBoard().getWarehouse().addResourceToDepot(2, Resource.SERVANT);
        strResource.add(Resource.COIN);
        strResource.add(Resource.SHIELD);
        strResource.add(Resource.SERVANT);
        strResource.add(Resource.COIN);
        player.getPersonalBoard().addResourcesToStrongbox(strResource);
        player.getPersonalBoard().addDevelopmentCard(2, developmentGrid.drawCard(Color.GREEN, Level.THIRD));
        player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[2].activatePopesFavorTile();
        cli.displayPersonalBoard(player);
        in.nextLine();
    }


    private void testActivateProduction() throws ColorDoesNotExistException, NoMoreDevelopmentCardsException, LevelDoesNotExistException {

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
    }


    private void testPrintPlayerLeaderCards() {

        //---PRINT-PLAYER-LEADER-CARDS-TEST---// Press Enter 7 times

        cliUtils.cls();
        cli.displayLeaderCards(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.getLeaderCards().get(0).activate(player);
        cli.displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        cli.displayLeaderCards(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.getLeaderCards().get(1).activate(player);
        cli.displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        cli.displayLeaderCards(player.getLeaderCards());
        cli.displayLeaderCardDiscardActivationSelection(player.getLeaderCards());
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
    }


    private void testWaitingScreen() throws EmptyPayloadException {

        //---WAIING-SCREEN-TEST---// press Enter 3 times

        WaitingScreenStarter waitingScreenStarter = WaitingScreenStarter.getInstance();
        Scanner tast = new Scanner(System.in);
        Message message = new Message(MessageType.GENERAL_MESSAGE, "Please wait the other players to come");

        waitingScreenStarter.startWaiting(message);
        tast.nextLine();
        waitingScreenStarter.stopWaiting();

        message = new Message(MessageType.GENERAL_MESSAGE, "Please wait the other players to choose the resources");
        waitingScreenStarter.startWaiting(message);
        tast.nextLine();
        waitingScreenStarter.stopWaiting();

        message = new Message(MessageType.GENERAL_MESSAGE, "Press Enter to exit");
        waitingScreenStarter.startWaiting(message);
        tast.nextLine();
        waitingScreenStarter.stopWaiting();
    }


    private void testMessages() {

        //---MESSAGES-TEST---// Uncomment the message you want to try

        List<Object> choices;

        /*INITIAL_RESOURCE_ASSIGNMENT
        choices = new ArrayList<>();
        choices.add(resourceSupply);
        cli.displayChoices(MessageType.INITIAL_RESOURCE_ASSIGNMENT, "Select the Resource you want by typing the slot number", choices, 1, 1);   */

        //CHOICE_ROW_COLUMN
        choices = new ArrayList<>();
        choices.add(marketTray);
        cli.displayChoices(MessageType.CHOICE_ROW_COLUMN, "Insert a value for row or column", choices, 1, 1);   //*/

        /*CHOICE_NORMAL_ACTION
        choices = new ArrayList<>();
        choices.add("1 - Go to market");
        choices.add("2 - Buy a card");
        choices.add("3 - Activate production");
        cli.displayChoices(MessageType.CHOICE_NORMAL_ACTION, "Select a normal action", choices, 1, 1);   //*/

        /*CHOICE_LEADER_ACTION
        choices = new ArrayList<>();
        choices.add("1 - Play Leader");
        choices.add("2 - Discard Leader");
        cli.displayChoices(MessageType.CHOICE_LEADER_ACTION, "Select a leader action", choices, 1, 1);   //*/

        /*ACTIVATE_LEADER
        choices = new ArrayList<>();
        choices.add(leaderCardsInitializer.getLeaderCards().get(0));
        choices.add(leaderCardsInitializer.getLeaderCards().get(1));
        cli.displayChoices(MessageType.ACTIVATE_LEADER, "Select a leader to activate", choices, 1, 1);   //*/

        /*DISCARD_LEADER
        choices = new ArrayList<>();
        choices.add(leaderCardsInitializer.getLeaderCards().get(0));
        choices.add(leaderCardsInitializer.getLeaderCards().get(1));
        cli.displayChoices(MessageType.ACTIVATE_LEADER, "Select a leader to discard", choices, 1, 1);   //*/

        /*ACTIVATE_PRODUCTION
        choices = new ArrayList<>();
        DevelopmentGrid developmentGrid1 = new DevelopmentGrid(virtualView);
        choices.add(developmentGrid1.drawCard(Color.GREEN, Level.FIRST).getProduction());
        choices.add(developmentGrid1.drawCard(Color.GREEN, Level.SECOND).getProduction());
        choices.add(developmentGrid1.drawCard(Color.GREEN, Level.THIRD).getProduction());
        choices.add(developmentGrid1.drawCard(Color.GREEN, Level.FIRST).getProduction());
        choices.add(developmentGrid1.drawCard(Color.GREEN, Level.SECOND).getProduction());
        choices.add(developmentGrid1.drawCard(Color.GREEN, Level.THIRD).getProduction());
        cli.displayChoices(MessageType.ACTIVATE_PRODUCTION, "Select the productions you want to activate", choices, 1, choices.size());   //*/

        /*BUY_CARD
        choices = new ArrayList<>();
        DevelopmentGrid developmentGrid1 = new DevelopmentGrid(virtualView);
        choices.add(developmentGrid1);
        cli.displayChoices(MessageType.BUY_CARD, "Enter the Level/Color (potrà anche essere fatto con le coordinate della cella)", choices, 1, 1);   //*/
    }

}
