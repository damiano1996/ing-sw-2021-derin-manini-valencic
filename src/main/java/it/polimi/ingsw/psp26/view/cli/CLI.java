package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.ResourceSupply;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
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
import it.polimi.ingsw.psp26.network.server.Server;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.getElementsByIndices;

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



        //---ACTION-TOKENS-TEST---//

        List<ActionToken> unusedTokens = new ArrayList<>();

        unusedTokens = metch.drawActionTokens(7);
        for (int i = 0; i < 7; i++) {
            displayActionTokens(unusedTokens);
            in.nextLine();
            unusedTokens.remove(0);
        }


        //--RESOURCE-SUPPLY-TEST---// Press enter 1 time

        displayResourceSupply(resourceSupply);
        in.nextLine();


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
        List<LeaderCard> leaderCardList = new ArrayList<>();
        LeaderCardsInitializer l = new LeaderCardsInitializer();
        leaderCardList = l.getLeaderCards();
        playerCards.add(leaderCardList.get(8));
        playerCards.add(leaderCardList.get(9));
        player.setLeaderCards(playerCards);
        displayPersonalBoard(player);
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
        displayPersonalBoard(player);
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
        displayPersonalBoard(player);
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
        displayPersonalBoard(player);
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
        displayProductionActivation(productions);
        in.nextLine();


        //---PRINT-PLAYER-LEADER-CARDS-TEST---// Press Enter 7 times

        cliUtils.cls();
        displayLeaderCards(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.getLeaderCards().get(0).activate();
        displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        displayLeaderCards(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.getLeaderCards().get(1).activate();
        displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        displayLeaderCards(player.getLeaderCards());
        displayLeaderCardDiscardSelection(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.discardLeaderCard(player.getLeaderCards().get(0)); //Simulate a Leader Card discard
        displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        displayLeaderCards(player.getLeaderCards());
        in.nextLine();
        cliUtils.cls();

        player.discardLeaderCard(player.getLeaderCards().get(0)); //Simulate a Leader Card discard
        displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        displayLeaderCards(player.getLeaderCards());
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
                String nickname = in.nextLine();
                client.setNickname(nickname);
            } else {
                pw.println(cliUtils.hSpace(100) + "Enter Nickname: " + client.getNickname());
                pw.flush();
                cliUtils.vSpace(2);
                pw.print(cliUtils.hSpace(100) + "Enter IP-Address: ");
                pw.flush();
                try {
                    String serverIP = in.nextLine();
                    client.initializeNetworkNode(serverIP);
                    // go to Multi/single player choice
                    client.handleMessage(new Message(
                            client.getSessionToken(),
                            MULTI_OR_SINGLE_PLAYER_MODE, // message type
                            MULTIPLAYER_MODE, SINGLE_PLAYER_MODE // choices
                    ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayLeaderChoice(List<LeaderCard> leaderCards) {
        leaderCardsCli.displayLeaderSelection(leaderCards);
    }

    @Override
    public void displayLeaderCards(List<LeaderCard> leaderCards) {
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

    @Override
    public void displayMarketTray(MarketTray marketTray) {
        marketCli.displayMarketTray(marketTray, 12, 88);
    }

    @Override
    public void displayMarketScreen(MarketTray marketTray) {
        marketCli.displayMarketScreen(marketTray);
    }

    private void displayMarketResourcesSelection(List<Depot> depots, List<Resource> resources) {
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
        personalBoardCli.displayResourceSupply(resourceSupply, 1, 37);
    }

    @Override
    public void displayProductionActivation(List<Production> productions) { //TODO must be completed with controller integration e forse mettere la lista di object
        personalBoardCli.displayProductionActivation(productions);


        cliUtils.vSpace(10);
        pw.print(cliUtils.hSpace(20) + "Select which production you want to activate: ");
        pw.flush();
    }

    private void displayLeaderCardDiscardSelection(List<LeaderCard> leaderCards) { //TODO devi mandare al server cosa scegli
        personalBoardCli.displayPlayerLeaderCards(leaderCards, 1, 1);

        cliUtils.setCursorPosition(35, 21);
        pw.print("Type the LeaderCard number you want to discard: ");
        pw.flush();
    }

    @Override
    public void displayActionTokens(List<ActionToken> unusedTokens) {
        personalBoardCli.displayActionTokens(unusedTokens);
    }

    @Override
    public void displayDevelopmentCardDiscard(DevelopmentGrid developmentGrid, DevelopmentCardType developmentCardType) {
        //To be implemented
    }

    @Override
    public void displayChoices(MessageType messageType, String question, List<Object> choices, int minChoices, int maxChoices) {
        try {

            List<Object> selected = new ArrayList<>();

            cliUtils.cls();
            cliUtils.vSpace(1);
            pw.println(cliUtils.hSpace(3) + question);

            switch (messageType) {
                case MULTI_OR_SINGLE_PLAYER_MODE:
                    // display list of choices
                    displayMultipleMessageTypeChoices(choices);
                    selected = getElementsByIndices(choices, displayInputChoice(choices.size(), minChoices, maxChoices));

                    // send to server response
                    client.sendToServer(
                            new Message(
                                    client.getSessionToken(),
                                    MULTI_OR_SINGLE_PLAYER_MODE,
                                    selected.toArray(new Object[0]) // to array
                            )
                    );

                    break;

                case CHOICE_NORMAL_ACTION:
//                    displayNormalActionsSelection();
                    break;

                case CHOICE_LEADER_ACTION:
//                    displayLeaderActionSelection();
                    break;

                case CHOICE_LEADER_TO_ACTIVATE_OR_DISCARD:


                default:
                    break;
            }

            pw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> displayInputChoice(int nChoices, int minChoices, int maxChoices) {
        Scanner in = new Scanner(System.in);
        List<Integer> choices = new ArrayList<>();

        cliUtils.vSpace(1);
        pw.print(cliUtils.hSpace(3) + "Select at least " + minChoices + " items." + ((maxChoices > minChoices) ? " Up to " + maxChoices + " items." : ""));

        while (choices.size() < maxChoices) {
            pw.print(cliUtils.hSpace(3) + "Enter the number of the corresponding item [" + 1 + ", " + nChoices + "] (type 'q' - to quit): ");

            pw.flush();
            String item = in.nextLine();
            if (item.equals("q") && choices.size() > minChoices) break;
            int itemInt = Integer.parseInt(item) - 1;

            if (!choices.contains(itemInt))
                choices.add(itemInt);
        }

        pw.flush();

        return choices;
    }

    private void displayMultipleMessageTypeChoices(List<Object> choices) {
        for (int i = 0; i < choices.size(); i++) {
            cliUtils.vSpace(1);
            pw.println(cliUtils.hSpace(5) + (i + 1) + " - " + choices.get(i));
        }
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
