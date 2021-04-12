package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCardsInitializer;
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;

public class CLI implements ViewInterface { //TODO SISTEMA STA CLASSE DOPO IL TEST METTI NELLE ALTRE CLASSI I METODI CHE NON SERVONO PRIVATI E LA VIRTUALVIEW

    private final Client client;

    private final PrintWriter pw;
    private final boolean isMultiPlayerMode;

    private final CliUtils cliUtils;
    private final DepotCli depotCli;
    private final DevelopmentCardsCli developmentCardsCli;
    private final FaithTrackCli faithTrackCli;
    private final LeaderCardsCli leaderCardsCli;
    private final MarketCli marketCli;
    private final PersonalBoardCli personalBoardCli;


    public CLI(Client client) { //TODO devi passare al costruttore un match e ricavare isMultiPlayerMode da quello
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
    }

    //CLI testing. Only press start to test CLI functionalities
    public static void main(String[] args) throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, CanNotAddResourceToDepotException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, DepotOutOfBoundException, CanNotAddResourceToStrongboxException {
        try {
            CLI cli = new CLI(new Client());
            cli.testMethod();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testMethod() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, DepotOutOfBoundException, CanNotAddResourceToDepotException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, CanNotAddResourceToStrongboxException {
        //---OBJECTS-DECLARATION---//

        VirtualView virtualView = new VirtualView();
        Player player = new Player(virtualView, "Player", "000000");
        LeaderCardsInitializer leaderCardsInitializer = new LeaderCardsInitializer();
        List<LeaderCard> leaderCards = leaderCardsInitializer.getLeaderCards();
        PersonalBoard personalBoard = new PersonalBoard(virtualView);
        MarketTray marketTray = new MarketTray(virtualView);
        DevelopmentGrid developmentGrid;
        Scanner in = new Scanner(System.in);


        //---ERROR-TEST---//

        displayError("ERROR                     TEST");
        in.nextLine();


        //---DEVELOPMENT-GRID-SHOW-ALL-CARDS-TEST---// Press Enter 4 times

        developmentGrid = new DevelopmentGrid(virtualView);

        for (int i = 0; i < 4; i++) {
            cliUtils.cls();
            displayDevelopmentGridCardSelection(developmentGrid);
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
        displayMarketResourcesSelection(p.getWarehouseDepots(), resources);

        p.getWarehouseDepot(2).addResource(Resource.STONE);
        p.getWarehouseDepot(2).addResource(Resource.STONE);
        resources.remove(0);
        resources.remove(0);
        displayMarketResourcesSelection(p.getWarehouseDepots(), resources);

        p.getWarehouseDepot(1).addResource(Resource.COIN);
        resources.remove(0);
        displayMarketResourcesSelection(p.getWarehouseDepots(), resources);

        p.getWarehouseDepot(0).addResource(Resource.SERVANT);
        resources.remove(0);
        displayMarketResourcesSelection(p.getWarehouseDepots(), resources);


        //---TITLE-SCREEN-TEST---// Press Enter and follow terminal instructions

        displayLogIn();


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
        displayLeaderChoice(fourLeaders);
        in.nextLine();


        //---PERSONAL-BOARD-TEST---// Press Enter 3 times

        cliUtils.cls();
        player.giveInkwell();
        List<Resource> strongbox = new ArrayList<>();
        LeaderDepot coinDepot = new LeaderDepot(virtualView, Resource.COIN);
        LeaderDepot servantDepot = new LeaderDepot(virtualView, Resource.SERVANT);
        List<LeaderCard> playerCards = new ArrayList<>();
        playerCards.add(fourLeaders.get(0));
        playerCards.add(fourLeaders.get(1));
        player.setLeaderCards(playerCards);
        displayPersonalBoard(player);
        displayNormalActionsSelection();
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
        displayPersonalBoard(player);
        displayLeaderActionSelection();
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
        displayPersonalBoard(player);
        displayNormalActionsSelection();
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
        displayPersonalBoard(player);
        displayNormalActionsSelection();
        in.nextLine();


        //---PRINT-PLAYER-LEADER-CARDS-TEST---// Press Enter 7 times

        cliUtils.cls();
        displayLeaderCardsDrawn(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.getLeaderCards().get(0).activate();
        displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        displayLeaderCardsDrawn(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.getLeaderCards().get(1).activate();
        displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        displayLeaderCardsDrawn(player.getLeaderCards());
        displayLeaderCardDiscardSelection(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.discardLeaderCard(player.getLeaderCards().get(0)); //Simulate a Leader Card discard
        displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        displayLeaderCardsDrawn(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.discardLeaderCard(player.getLeaderCards().get(0)); //Simulate a Leader Card discard
        displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        displayLeaderCardsDrawn(player.getLeaderCards());
        in.nextLine();


        //FAITH-TRACK-MOVEMENT-TEST---// Press Enter 25 times

        cliUtils.cls();
        for (int i = 0; i < 25; i++) {
            displayFaithTrack(personalBoard.getFaithTrack());
            in.nextLine();
            personalBoard.getFaithTrack().moveMarkerPosition(1);
            cliUtils.cls();
        }


        //---MARKET-TEST---// Press Enter 3 times

        cliUtils.cls();
        displayMarketScreen(marketTray);
        in.nextLine();
        marketTray.pushMarbleFromSlideToRow(1);
        cliUtils.cls();
        displayMarketScreen(marketTray);
        in.nextLine();
        marketTray.pushMarbleFromSlideToColumn(3);
        cliUtils.cls();
        displayMarketScreen(marketTray);
        in.nextLine();

    }


    /**
     * Prints the Title Screen when the program is launched
     * Used in printTitle() method
     */
    private void printTitle() {
        cliUtils.cls();

        pw.print(Color.GREEN.setColor());
        pw.flush();
        cliUtils.printFigure("MainTitle", 1, 10);
        pw.print(Color.RESET.setColor());
        pw.flush();
    }


    //------------------------------------------//
    //          VIEW INTERFACE METHODS          //
    //------------------------------------------//


    /**
     * Used to ask the Player's credentials
     */
    public void displayLogIn() {
        Scanner in = new Scanner(System.in);

        printTitle();
        cliUtils.printFigure("PressEnterTitle", 20, 76);

        in.nextLine();

        for (int i = 0; i < 2; i++) {
            printTitle();
            cliUtils.vSpace(4);
            if (i == 0) {
                pw.print(cliUtils.hSpace(100) + "Enter Nickname: ");
                pw.flush();
                client.setNickname(in.nextLine());
            } else {
                pw.println(cliUtils.hSpace(100) + "Enter Nickname: " + client.getNickname());
                pw.flush();
                cliUtils.vSpace(2);
                pw.print(cliUtils.hSpace(100) + "Enter IP-Address: ");
                pw.flush();
                try {
                    client.initializeNetworkNode(in.nextLine());
                    // go to Multi/single player choice
                    displayChoices(MULTI_OR_SINGLE_PLAYER_MODE,
                            "Do you want to play in multi or single player mode?",
                            new ArrayList<>() {{
                                add(MULTIPLAYER_MODE);
                                add(SINGLE_PLAYER_MODE);
                            }}, 1, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void displayMatchSelection() {
        //To be completed
    }

    @Override
    public void displayLeaderChoice(List<LeaderCard> leaderCards) {
        leaderCardsCli.displayLeaderSelection(leaderCards);
    }

    @Override
    public void displayLeaderCardsDrawn(List<LeaderCard> leaderCards) {
        personalBoardCli.displayPlayerLeaderCards(leaderCards, 1, 1);
    }

    @Override
    public void displayInkwell(boolean isPrintable) {
        personalBoardCli.displayInkwell(isPrintable, 5, 190);
    }

    @Override
    public void displayInitialResources(List<Resource> resources) {
        //To be completed
    }

    @Override
    public void displayPersonalBoard(Player player) {
        personalBoardCli.displayPersonalBoard(player);
    }

    @Override
    public void displayWarehouseDepots(List<Depot> warehouseDepot) {
        depotCli.printWarehouse(warehouseDepot, 17, 13);
    }

    @Override
    public void displayStrongbox(List<Resource> strongbox) {
        depotCli.displayStrongbox(strongbox, 30, 3);
    }

    @Override
    public void displayFaithTrack(FaithTrack faithTrack) {
        faithTrackCli.displayFaithTrack(faithTrack, 3, 10);
    }

    @Override
    public void displayDevelopmentCardsSlots(List<List<DevelopmentCard>> developmentCardsSlots) {
        personalBoardCli.displayDevelopmentCardsSlots(developmentCardsSlots, 30, 70);
    }

    /**
     * Displays the normal action selection
     */
    public void displayNormalActionsSelection() { // TODO Final implementation may be different
        Scanner in = new Scanner(System.in);
        int action;

        cliUtils.vSpace(5);
        pw.println(cliUtils.hSpace(5) + "\u001b[4mSELECT A NORMAL ACTION" + Color.RESET);
        cliUtils.vSpace(1);
        pw.println(cliUtils.hSpace(3) + "1 - Get Resources from the Market");
        pw.println(cliUtils.hSpace(3) + "2 - Activate production");
        pw.println(cliUtils.hSpace(3) + "3 - Buy a Development Card");
        cliUtils.vSpace(2);
        pw.print(cliUtils.hSpace(3) + "Enter the number of the corresponding action: ");
        //action = in.nextInt();
        //in.nextLine();
        pw.flush();
    }

    @Override
    public void displayMarketTray(MarketTray marketTray) {
        marketCli.displayMarketTray(marketTray, 12, 88);
    }

    @Override
    public void displayMarketScreen(MarketTray marketTray) {
        marketCli.displayMarketScreen(marketTray);
    }

    @Override
    public void displayMarketResourcesSelection(List<Depot> depots, List<Resource> resources) {
        depotCli.displayMarketResourcesSelection(depots, resources);
    }

    @Override
    public void displayDevelopmentGrid(DevelopmentGrid developmentGrid) {
        developmentCardsCli.displayDevelopmentGrid(developmentGrid);
    }

    @Override
    public void displayDevelopmentGridCardSelection(DevelopmentGrid developmentGrid) { //TODO must be completed with controller integration
        displayDevelopmentGrid(developmentGrid);

        cliUtils.setCursorPosition(33, 135);
        pw.print("Select a Card by typing the desired LEVEL and COLOR: ");
        pw.flush();
    }

    @Override
    public void displayResourceSupply(ResourceSupply resourceSupply) {
        //To be completed
    }

    @Override
    public void displayProductionActivation(PersonalBoard personalBoard) {
        //To be completed
    }

    /**
     * Displays the leader action selection
     */
    public void displayLeaderActionSelection() { // TODO Final implementation may be different
        Scanner in = new Scanner(System.in);
        int action;

        cliUtils.vSpace(5);
        pw.println(cliUtils.hSpace(3) + "\u001b[4mSELECT A LEADER ACTION" + Color.RESET);
        pw.println(cliUtils.hSpace(3) + "1 - Discard a Leader");
        pw.println(cliUtils.hSpace(3) + "2 - Activate a Leader");
        cliUtils.vSpace(1);
        pw.print(cliUtils.hSpace(3) + "Enter the number of the corresponding action: ");
        //action = in.nextInt();
        //in.nextLine();

        pw.flush();
    }

    @Override
    public void displayLeaderCardDiscardSelection(List<LeaderCard> leaderCards) { //TODO devi mandare al server cosa scegli
        personalBoardCli.displayPlayerLeaderCards(leaderCards, 1, 1);

        cliUtils.setCursorPosition(35, 21);
        pw.print("Type the LeaderCard number you want to discard: ");
        pw.flush();
    }

    @Override
    public void displayActionTokens(ActionToken actionToken) {
        //To be implemented
    }

    @Override
    public void displayDevelopmentCardDiscard(DevelopmentGrid developmentGrid, DevelopmentCardType developmentCardType) {
        //To be implemented
    }

    @Override
    public void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices) {

        cliUtils.cls();
        pw.println(question);

        switch (messageType) {
            case MULTI_OR_SINGLE_PLAYER_MODE:
                // TODO: to be completed
                break;

            case CHOICE_NORMAL_ACTION:
                displayNormalActionsSelection();
                break;

            case CHOICE_LEADER_ACTION:
                displayLeaderActionSelection();
                break;

            case LEADER_ACTIVATED:
                displayLeaderCardsDrawn((List<LeaderCard>) (Object) choices);
                break;

            default:
                break;
        }

        pw.flush();
    }

    @Override
    public void displayText(String text) {
        //To be implemented
    }

    @Override
    public void displayEndGame(HashMap<String, Integer> playersVictoryPoints) {
        //To be implemented
    }

    @Override
    public void displayError(String error) {
        cliUtils.cls();
        cliUtils.setCursorPosition(20, 81);
        for (int i = 0; i < error.length() + 8; i++) pw.print(cliUtils.pCS("=", Color.RED));
        cliUtils.setCursorPosition(21, 1);
        for (int i = 0; i < 2; i++)
            pw.println(cliUtils.hSpace(80) + cliUtils.pCS("║   ", Color.RED) + cliUtils.hSpace(error.length()) + cliUtils.pCS("   ║", Color.RED));
        pw.println(cliUtils.hSpace(80) + cliUtils.pCS("║   ", Color.RED) + cliUtils.pCS(error, Color.RED) + cliUtils.pCS("   ║", Color.RED));
        for (int i = 0; i < 2; i++)
            pw.println(cliUtils.hSpace(80) + cliUtils.pCS("║   ", Color.RED) + cliUtils.hSpace(error.length()) + cliUtils.pCS("   ║", Color.RED));
        cliUtils.setCursorPosition(26, 81);
        for (int i = 0; i < error.length() + 8; i++) pw.print(cliUtils.pCS("=", Color.RED));
        pw.flush();
    }

}
