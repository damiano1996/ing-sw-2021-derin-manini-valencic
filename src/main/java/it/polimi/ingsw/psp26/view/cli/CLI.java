package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCardsInitializer;
import it.polimi.ingsw.psp26.model.personalboard.*;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.view.ViewInterface;

import java.io.PrintWriter;
import java.util.*;

public class CLI { //TODO SISTEMA STA CLASSE DOPO IL TEST METTI NELLE ALTRE CLASSI I METODI CHE NON SERVONO PRIVATI E LA VIEW INTERFACE E LA VIRTUALVIEW

    private final PrintWriter pw;
    private final boolean isMultiPlayerMode;

    private final CliUtils cliUtils;
    private final DevelopmentCardsCli developmentCardsCli;
    private final FaithTrackCli faithTrackCli;
    private final LeaderCardsCli leaderCardsCli;
    private final MarketCli marketCli;
    private final PersonalBoardCli personalBoardCli;
    private final DepotCli depotCli;


    public CLI() { //TODO devi passare al costruttore un match e ricavare isMultiPlayerMode da quello
        this.pw = new PrintWriter(System.out);
        this.isMultiPlayerMode = true;
        this.depotCli = new DepotCli();
        this.cliUtils = new CliUtils();
        this.developmentCardsCli = new DevelopmentCardsCli();
        this.faithTrackCli = new FaithTrackCli();
        this.leaderCardsCli = new LeaderCardsCli();
        this.marketCli = new MarketCli();
        this.personalBoardCli = new PersonalBoardCli();
    }

    //CLI testing. Only press start to test CLI functionalities
    public static void main(String[] args) throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, CanNotAddResourceToDepotException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, DepotOutOfBoundException, CanNotAddResourceToStrongboxException {
        CLI cli = new CLI();

        cli.testMethod();
    }

    public void testMethod() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, DepotOutOfBoundException, CanNotAddResourceToDepotException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, CanNotAddResourceToStrongboxException {
        //---OBJECTS-DECLARATION---//

        CLI cli = new CLI();
        VirtualView virtualView = new VirtualView();
        Player player = new Player(virtualView, "Player", "000000");
        LeaderCardsInitializer leaderCardsInitializer = new LeaderCardsInitializer();
        List<LeaderCard> leaderCards = leaderCardsInitializer.getLeaderCards();
        PersonalBoard personalBoard = new PersonalBoard(virtualView);
        MarketTray marketTray = new MarketTray(virtualView);
        DevelopmentGrid developmentGrid;
        Scanner in = new Scanner(System.in);

        //---DEVELOPMENT-GRID-SHOW-ALL-CARDS-TEST---// Press Enter 4 times

        developmentGrid = new DevelopmentGrid(virtualView);

        for (int i = 0; i < 4; i++) {
            cliUtils.cls();
            developmentCardsCli.displayDevelopmentGrid(developmentGrid);
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
        depotCli.displayWarehouseConfiguration(p.getWarehouseDepots(), resources);

        p.getWarehouseDepot(2).addResource(Resource.STONE);
        p.getWarehouseDepot(2).addResource(Resource.STONE);
        resources.remove(0);
        resources.remove(0);
        depotCli.displayWarehouseConfiguration(p.getWarehouseDepots(), resources);

        p.getWarehouseDepot(1).addResource(Resource.COIN);
        resources.remove(0);
        depotCli.displayWarehouseConfiguration(p.getWarehouseDepots(), resources);

        p.getWarehouseDepot(0).addResource(Resource.SERVANT);
        resources.remove(0);
        depotCli.displayWarehouseConfiguration(p.getWarehouseDepots(), resources);


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
        leaderCardsCli.displayLeaderSelection(fourLeaders);
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
        personalBoardCli.displayPersonalBoard(player);
        cli.displayNormalActionsSelection();
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
        personalBoardCli.displayPersonalBoard(player);
        cli.displayLeaderActionSelection();
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
        personalBoardCli.displayPersonalBoard(player);
        cli.displayNormalActionsSelection();
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
        personalBoardCli.displayPersonalBoard(player);
        cli.displayNormalActionsSelection();
        in.nextLine();


        //---PRINT-PLAYER-LEADER-CARDS-TEST---// Press Enter 7 times

        cliUtils.cls();
        personalBoardCli.displayPlayerLeaderCards(player.getLeaderCards(), 1, 1);
        in.nextLine();
        cliUtils.cls();

        player.getLeaderCards().get(0).activate();
        personalBoardCli.displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        personalBoardCli.displayPlayerLeaderCards(player.getLeaderCards(), 1, 1);
        in.nextLine();
        cliUtils.cls();

        player.getLeaderCards().get(1).activate();
        personalBoardCli.displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        personalBoardCli.displayPlayerLeaderCards(player.getLeaderCards(), 1, 1);
        in.nextLine();
        cliUtils.cls();

        player.discardLeaderCard(player.getLeaderCards().get(0)); //Simulate a Leader Card discard
        personalBoardCli.displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        personalBoardCli.displayPlayerLeaderCards(player.getLeaderCards(), 1, 1);
        in.nextLine();
        cliUtils.cls();

        player.discardLeaderCard(player.getLeaderCards().get(0)); //Simulate a Leader Card discard
        personalBoardCli.displayPersonalBoard(player);
        in.nextLine();
        cliUtils.cls();
        personalBoardCli.displayPlayerLeaderCards(player.getLeaderCards(), 1, 1);
        in.nextLine();


        //FAITH-TRACK-MOVEMENT-TEST---// Press Enter 25 times

        cliUtils.cls();
        for (int i = 0; i < 25; i++) {
            faithTrackCli.printFaithTrack(personalBoard.getFaithTrack(), 5, 5);
            in.nextLine();
            personalBoard.getFaithTrack().moveMarkerPosition(1);
            cliUtils.cls();
        }


        //---MARKET-TEST---// Press Enter 3 times

        cliUtils.cls();
        marketCli.displayMarketTray(marketTray, 10, 10);
        in.nextLine();
        marketTray.pushMarbleFromSlideToRow(1);
        cliUtils.cls();
        marketCli.displayMarketTray(marketTray, 10, 10);
        in.nextLine();
        marketTray.pushMarbleFromSlideToColumn(3);
        cliUtils.cls();
        marketCli.displayMarketTray(marketTray, 10, 10);
        in.nextLine();

    }


    //-------------------------//
    //          TITLE          //
    //-------------------------//

    /**
     * Prints the Title Screen when the program is launched
     * Used in printTitle() method
     */
    private void printTitle() {
        cliUtils.cls();

        pw.print(Color.GREEN.setColor());
        pw.println(cliUtils.hSpace(10) + "#####   ##    ##                                                                            /##           ##### /##                                                                                                      ");
        pw.println(cliUtils.hSpace(10) + "  ######  /#### #####                                                                          #/ ###       ######  / ##            #                                                                                    ");
        pw.println(cliUtils.hSpace(10) + " /#   /  /  ##### #####                        #                                              ##   ###     /#   /  /  ##           ###                                                                                   ");
        pw.println(cliUtils.hSpace(10) + "/    /  /   # ##  # ##                        ##                                              ##          /    /  /   ##            #                                                                                    ");
        pw.println(cliUtils.hSpace(10) + "    /  /    #     #                           ##                                              ##              /  /    /                                                                                                  ");
        pw.println(cliUtils.hSpace(10) + "   ## ##    #     #      /###      /###     ######## /##  ###  /###     /###          /###    ######         ## ##   /       /##  ###   ###  /###     /###      /###     /###      /###   ###  /###     /###      /##    ");
        pw.println(cliUtils.hSpace(10) + "   ## ##    #     #     / ###  /  / #### / ######## / ###  ###/ #### / / #### /      / ###  / #####          ## ##  /       / ###  ###   ###/ #### / / ###  /  / #### / / #### /  / ###  / ###/ #### / / ###  /  / ###   ");
        pw.println(cliUtils.hSpace(10) + "   ## ##    #     #    /   ###/  ##  ###/     ##   /   ###  ##   ###/ ##  ###/      /   ###/  ##             ## ###/       /   ###  ##    ##   ###/ /   ###/  ##  ###/ ##  ###/  /   ###/   ##   ###/ /   ###/  /   ###  ");
        pw.println(cliUtils.hSpace(10) + "   ## ##    #     #   ##    ##  ####          ##  ##    ### ##       ####          ##    ##   ##             ## ##  ###   ##    ### ##    ##    ## ##    ##  ####     ####      ##    ##    ##    ## ##        ##    ### ");
        pw.println(cliUtils.hSpace(10) + "   ## ##    #     ##  ##    ##    ###         ##  ########  ##         ###         ##    ##   ##             ## ##    ##  ########  ##    ##    ## ##    ##    ###      ###     ##    ##    ##    ## ##        ########  ");
        pw.println(cliUtils.hSpace(10) + "   #  ##    #     ##  ##    ##      ###       ##  #######   ##           ###       ##    ##   ##             #  ##    ##  #######   ##    ##    ## ##    ##      ###      ###   ##    ##    ##    ## ##        #######   ");
        pw.println(cliUtils.hSpace(10) + "      /     #      ## ##    ##        ###     ##  ##        ##             ###     ##    ##   ##                /     ##  ##        ##    ##    ## ##    ##        ###      ### ##    ##    ##    ## ##        ##        ");
        pw.println(cliUtils.hSpace(10) + "  /##/      #      ## ##    /#   /###  ##     ##  ####    / ##        /###  ##     ##    ##   ##            /##/      ### ####    / ##    ##    ## ##    /#   /###  ## /###  ## ##    /#    ##    ## ###     / ####    / ");
        pw.println(cliUtils.hSpace(10) + " /  #####           ## ####/ ## / #### /      ##   ######/  ###      / #### /       ######    ##           /  ####    ##   ######/  ### / ###   ### ####/ ## / #### / / #### /   ####/ ##   ###   ### ######/   ######/  ");
        pw.println(cliUtils.hSpace(10) + "/     ##                ###   ##   ###/        ##   #####    ###        ###/         ####      ##         /    ##     #     #####    ##/   ###   ### ###   ##   ###/     ###/     ###   ##   ###   ### #####     #####   ");
        pw.println(cliUtils.hSpace(10) + "#                                                                                                         #                                                                                                              ");
        pw.println(cliUtils.hSpace(10) + " ##                                                                                                        ##                                                                                                            ");
        pw.print(Color.RESET.setColor());

        cliUtils.vSpace(4);
        pw.println(cliUtils.hSpace(10) + "                                                                   _____   ______ _______ _______ _______      _______ __   _ _______ _______  ______");
        pw.println(cliUtils.hSpace(10) + "                                                                  |_____] |_____/ |______ |______ |______      |______ | \\  |    |    |______ |_____/");
        pw.println(cliUtils.hSpace(10) + "                                                                  |       |    \\_ |______ ______| ______|      |______ |  \\_|    |    |______ |    \\_");
    }

    //--------------------------------//
    //          GAME ACTIONS          //
    //--------------------------------//

    /**
     * Used to ask the Player's credentials
     */
    public void displayLogIn() { //MUST BE COMPLETED WITH CONTROLLER INTEGRATION
        String nickname = "";
        String ipAddress = "";
        Scanner in = new Scanner(System.in);

        printTitle();
        pw.flush();
        in.nextLine();

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                printTitle();
                cliUtils.vSpace(3);
                pw.print(cliUtils.hSpace(100) + "Enter Nickname: ");
                pw.flush();
                nickname = in.nextLine();
            } else {
                printTitle();
                cliUtils.vSpace(3);
                pw.println(cliUtils.hSpace(100) + "Enter Nickname: " + nickname);
                cliUtils.vSpace(1);
                pw.print(cliUtils.hSpace(100) + "Enter IP-Address: ");
                pw.flush();
                in.nextLine();
                //ipAddress = in.nextLine(); COMMENTATO SOLO PER EVITARE IL WARNING NEL COMMIT, NELLA VERSIONE FINALE VA CONTROLLATO L'INSERIMENTO DELL'IP ADDRESS
            }
        }
    }

    public void displayNormalActionsSelection() { // TODO Final implementation may be different
        Scanner in = new Scanner(System.in);
        int action;

        cliUtils.vSpace(2);
        pw.println("What action do you want to do?");
        pw.println("1 - Get Resources from the Market");
        pw.println("2 - Activate production");
        pw.println("3 - Buy a Development Card");
        cliUtils.vSpace(1);
        pw.print("Enter the number of the corresponding action: ");
        //action = in.nextInt();
        //in.nextLine();

        pw.flush();
    }

    public void displayLeaderActionSelection() { // TODO Final implementation may be different
        Scanner in = new Scanner(System.in);
        int action;

        cliUtils.vSpace(2);
        pw.println("What action do you want to do?");
        pw.println("1 - Discard a Leader");
        pw.println("2 - Activate a Leader");
        cliUtils.vSpace(1);
        pw.print("Enter the number of the corresponding action: ");
        //action = in.nextInt();
        //in.nextLine();

        pw.flush();
    }

}
