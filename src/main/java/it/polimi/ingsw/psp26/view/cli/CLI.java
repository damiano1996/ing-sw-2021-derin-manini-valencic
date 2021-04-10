package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGridCell;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCardsInitializer;
import it.polimi.ingsw.psp26.model.personalboard.*;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CLI {

    //CLI testing. Only press start to test CLI functionalities
    public static void main(String[] args) throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, CanNotAddResourceToDepotException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, DepotOutOfBoundException {

        //---OBJECTS-DECLARATION---//

        CLI cli = new CLI();
        VirtualView virtualView = new VirtualView();
        Player player = new Player(virtualView, "Player", "000000");
        LeaderCardsInitializer leaderCardsInitializer = new LeaderCardsInitializer();
        List<LeaderCard> leaderCards = leaderCardsInitializer.getLeaderCards();
        PersonalBoard personalBoard = new PersonalBoard(virtualView);
        MarketTray marketTray = new MarketTray(virtualView);
        DevelopmentGrid developmentGrid = new DevelopmentGrid(virtualView);
        Scanner in = new Scanner(System.in);


        //---WAREHOUSE-CONFIGURATION-TEST---// Press Enter 3 times

        PersonalBoard p = new PersonalBoard(virtualView);
        List<Resource> resources = new ArrayList<>();
        resources.add(Resource.STONE);
        resources.add(Resource.STONE);
        resources.add(Resource.COIN);
        resources.add(Resource.FAITH_MARKER);
        cli.printWarehouseConfiguration(p.getWarehouseDepots(), resources);

        p.getWarehouseDepot(2).addResource(Resource.STONE);
        p.getWarehouseDepot(2).addResource(Resource.STONE);
        resources.remove(0);
        resources.remove(0);
        cli.printWarehouseConfiguration(p.getWarehouseDepots(), resources);

        p.getWarehouseDepot(1).addResource(Resource.COIN);
        resources.remove(0);
        cli.printWarehouseConfiguration(p.getWarehouseDepots(), resources);

        p.getWarehouseDepot(0).addResource(Resource.FAITH_MARKER);
        resources.remove(0);
        cli.printWarehouseConfiguration(p.getWarehouseDepots(), resources);


        //---TITLE-SCREEN-TEST---// Press Enter and follow terminal instructions

        cli.askForCredentials();


        //---SHOW-ALL-LEADERS-TEST---// Press enter 4 times

        cli.cls();
        List<LeaderCard> fourLeaders = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            fourLeaders = new ArrayList<>();
            fourLeaders.add(leaderCards.get(i));
            fourLeaders.add(leaderCards.get(4 + i));
            fourLeaders.add(leaderCards.get(8 + i));
            fourLeaders.add(leaderCards.get(12 + i));

            cli.printLeaderChoice(fourLeaders);
            in.nextLine();
            cli.cls();
        }


        //---SELECT-LEADER-SCREEN-TEST---// Follow terminal instructions

        cli.cls();
        player.setLeaderCards(cli.selectLeaders(fourLeaders));
        in.nextLine();


        //---PERSONAL-BOARD-TEST---// Press Enter 3 times

        cli.cls();
        player.giveInkwell();
        List<Resource> strongbox = new ArrayList<>();
        LeaderDepot coinDepot = new LeaderDepot(virtualView, Resource.COIN);
        LeaderDepot servantDepot = new LeaderDepot(virtualView, Resource.SERVANT);
        cli.printPersonalBoard(player);
        in.nextLine();
        cli.cls();

        player.getPersonalBoard().addLeaderDepot(coinDepot);
        player.getPersonalBoard().addLeaderDepot(servantDepot);
        player.getPersonalBoard().getWarehouseDepots().get(0).addResource(Resource.SHIELD);
        player.getPersonalBoard().getWarehouseDepots().get(1).addResource(Resource.COIN);
        player.getPersonalBoard().getWarehouseDepots().get(2).addResource(Resource.SERVANT);
        player.getPersonalBoard().getStrongbox().add(Resource.STONE);
        player.getPersonalBoard().getStrongbox().add(Resource.SERVANT);
        player.getPersonalBoard().addResourcesToStrongbox(strongbox);
        player.getPersonalBoard().addDevelopmentCard(0, developmentGrid.drawCard(Color.GREEN, Level.FIRST));
        player.getPersonalBoard().addDevelopmentCard(1, developmentGrid.drawCard(Color.GREEN, Level.FIRST));
        player.getPersonalBoard().addDevelopmentCard(2, developmentGrid.drawCard(Color.YELLOW, Level.FIRST));
        player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[0].activatePopesFavorTile();
        cli.printPersonalBoard(player);
        in.nextLine();
        cli.cls();

        player.getPersonalBoard().getWarehouseDepot(3).addResource(Resource.COIN);
        player.getPersonalBoard().getWarehouseDepot(4).addResource(Resource.SERVANT);
        player.getPersonalBoard().getWarehouseDepots().get(1).addResource(Resource.COIN);
        player.getPersonalBoard().getWarehouseDepots().get(2).addResource(Resource.SERVANT);
        player.getPersonalBoard().getStrongbox().add(Resource.COIN);
        player.getPersonalBoard().getStrongbox().add(Resource.STONE);
        player.getPersonalBoard().getStrongbox().add(Resource.SHIELD);
        player.getPersonalBoard().addResourcesToStrongbox(strongbox);
        player.getPersonalBoard().addDevelopmentCard(1, developmentGrid.drawCard(Color.BLUE, Level.SECOND));
        player.getPersonalBoard().addDevelopmentCard(2, developmentGrid.drawCard(Color.PURPLE, Level.SECOND));
        player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[1].activatePopesFavorTile();
        cli.printPersonalBoard(player);
        in.nextLine();
        cli.cls();

        player.getPersonalBoard().getWarehouseDepot(3).addResource(Resource.COIN);
        player.getPersonalBoard().getWarehouseDepot(4).addResource(Resource.SERVANT);
        player.getPersonalBoard().getWarehouseDepots().get(2).addResource(Resource.SERVANT);
        player.getPersonalBoard().getStrongbox().add(Resource.COIN);
        player.getPersonalBoard().getStrongbox().add(Resource.SHIELD);
        player.getPersonalBoard().getStrongbox().add(Resource.SERVANT);
        player.getPersonalBoard().getStrongbox().add(Resource.COIN);
        player.getPersonalBoard().addResourcesToStrongbox(strongbox);
        player.getPersonalBoard().addDevelopmentCard(2, developmentGrid.drawCard(Color.GREEN, Level.THIRD));
        player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[2].activatePopesFavorTile();
        cli.printPersonalBoard(player);
        in.nextLine();


        //---PRINT-PLAYER-LEADER-CARDS-TEST---// Press Enter 7 times

        cli.cls();
        cli.printPlayerLeaderCards(player.getLeaderCards());
        in.nextLine();
        cli.cls();

        player.getLeaderCards().get(0).activate();
        cli.printPersonalBoard(player);
        in.nextLine();
        cli.cls();
        cli.printPlayerLeaderCards(player.getLeaderCards());
        in.nextLine();
        cli.cls();

        player.getLeaderCards().get(1).activate();
        cli.printPersonalBoard(player);
        in.nextLine();
        cli.cls();
        cli.printPlayerLeaderCards(player.getLeaderCards());
        in.nextLine();
        cli.cls();

        player.getLeaderCards().remove(0); //Simulate a Leader Card discard
        cli.printPersonalBoard(player);
        in.nextLine();
        cli.cls();
        cli.printPlayerLeaderCards(player.getLeaderCards());
        in.nextLine();
        cli.cls();

        player.getLeaderCards().remove(0); //Simulate a Leader Card discard
        cli.printPersonalBoard(player);
        in.nextLine();
        cli.cls();
        cli.printPlayerLeaderCards(player.getLeaderCards());
        in.nextLine();


        //---DEVELOPMENT-GRID-SHOW-ALL-CARDS-TEST---// Press Enter 4 times

        developmentGrid = new DevelopmentGrid(virtualView);

        for (int i = 0; i < 4; i++) {
            cli.cls();
            cli.printDevelopmentGrid(developmentGrid);
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


        //FAITH-TRACK-MOVEMENT-TEST---// Press Enter 25 times

        cli.cls();
        for (int i = 0; i < 25; i++) {
            cli.printFaithTrack(personalBoard.getFaithTrack(), player.isInkwell());
            in.nextLine();
            personalBoard.getFaithTrack().moveMarkerPosition(1);
            cli.cls();
        }


        //---MARKET-TEST---// Press Enter 3 times

        cli.cls();
        cli.printMarket(marketTray);
        in.nextLine();
        marketTray.pushMarbleFromSlideToRow(1);
        cli.cls();
        cli.printMarket(marketTray);
        in.nextLine();
        marketTray.pushMarbleFromSlideToColumn(3);
        cli.cls();
        cli.printMarket(marketTray);
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
        cls();
        Color.GREEN.setColor();
        System.out.println(
                hSpace(10) + "#####   ##    ##                                                                            /##           ##### /##                                                                                 \n" +
                        hSpace(10) + "  ######  /#### #####                                                                          #/ ###       ######  / ##            #                                                                                    \n" +
                        hSpace(10) + " /#   /  /  ##### #####                        #                                              ##   ###     /#   /  /  ##           ###                                                                                   \n" +
                        hSpace(10) + "/    /  /   # ##  # ##                        ##                                              ##          /    /  /   ##            #                                                                                    \n" +
                        hSpace(10) + "    /  /    #     #                           ##                                              ##              /  /    /                                                                                                  \n" +
                        hSpace(10) + "   ## ##    #     #      /###      /###     ######## /##  ###  /###     /###          /###    ######         ## ##   /       /##  ###   ###  /###     /###      /###     /###      /###   ###  /###     /###      /##    \n" +
                        hSpace(10) + "   ## ##    #     #     / ###  /  / #### / ######## / ###  ###/ #### / / #### /      / ###  / #####          ## ##  /       / ###  ###   ###/ #### / / ###  /  / #### / / #### /  / ###  / ###/ #### / / ###  /  / ###   \n" +
                        hSpace(10) + "   ## ##    #     #    /   ###/  ##  ###/     ##   /   ###  ##   ###/ ##  ###/      /   ###/  ##             ## ###/       /   ###  ##    ##   ###/ /   ###/  ##  ###/ ##  ###/  /   ###/   ##   ###/ /   ###/  /   ###  \n" +
                        hSpace(10) + "   ## ##    #     #   ##    ##  ####          ##  ##    ### ##       ####          ##    ##   ##             ## ##  ###   ##    ### ##    ##    ## ##    ##  ####     ####      ##    ##    ##    ## ##        ##    ### \n" +
                        hSpace(10) + "   ## ##    #     ##  ##    ##    ###         ##  ########  ##         ###         ##    ##   ##             ## ##    ##  ########  ##    ##    ## ##    ##    ###      ###     ##    ##    ##    ## ##        ########  \n" +
                        hSpace(10) + "   #  ##    #     ##  ##    ##      ###       ##  #######   ##           ###       ##    ##   ##             #  ##    ##  #######   ##    ##    ## ##    ##      ###      ###   ##    ##    ##    ## ##        #######   \n" +
                        hSpace(10) + "      /     #      ## ##    ##        ###     ##  ##        ##             ###     ##    ##   ##                /     ##  ##        ##    ##    ## ##    ##        ###      ### ##    ##    ##    ## ##        ##        \n" +
                        hSpace(10) + "  /##/      #      ## ##    /#   /###  ##     ##  ####    / ##        /###  ##     ##    ##   ##            /##/      ### ####    / ##    ##    ## ##    /#   /###  ## /###  ## ##    /#    ##    ## ###     / ####    / \n" +
                        hSpace(10) + " /  #####           ## ####/ ## / #### /      ##   ######/  ###      / #### /       ######    ##           /  ####    ##   ######/  ### / ###   ### ####/ ## / #### / / #### /   ####/ ##   ###   ### ######/   ######/  \n" +
                        hSpace(10) + "/     ##                ###   ##   ###/        ##   #####    ###        ###/         ####      ##         /    ##     #     #####    ##/   ###   ### ###   ##   ###/     ###/     ###   ##   ###   ### #####     #####   \n" +
                        hSpace(10) + "#                                                                                                         #                                                                                                              \n" +
                        hSpace(10) + " ##                                                                                                        ##                                                                                                            ");
        Color.RESET.setColor();
        vSpace(4);
        System.out.println(
                hSpace(10) + "                                                                   _____   ______ _______ _______ _______      _______ __   _ _______ _______  ______\n" +
                        hSpace(10) + "                                                                  |_____] |_____/ |______ |______ |______      |______ | \\  |    |    |______ |_____/\n" +
                        hSpace(10) + "                                                                  |       |    \\_ |______ ______| ______|      |______ |  \\_|    |    |______ |    \\_");
    }

    /**
     * Used to ask the Player's credentials
     */
    public void askForCredentials() { //MUST BE COMPLETED WITH CONTROLLER INTEGRATION
        String nickname = "";
        String ipAddress = "";
        Scanner in = new Scanner(System.in);

        printTitle();
        in.nextLine();

        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                printTitle();
                vSpace(3);
                System.out.print(hSpace(100) + "Enter Nickname: ");
                nickname = in.nextLine();
            } else {
                printTitle();
                vSpace(3);
                System.out.println(hSpace(100) + "Enter Nickname: " + nickname);
                vSpace(1);
                System.out.print(hSpace(100) + "Enter IP-Address: ");
                in.nextLine();
                //ipAddress = in.nextLine(); COMMENTATO SOLO PER EVITARE IL WARNING NEL COMMIT, NELLA VERSIONE FINALE VA CONTROLLATO L'INSERIMENTO DELL'IP ADDRESS
            }
        }
    }


    //----------------------------------//
    //          PERSONAL BOARD          //
    //----------------------------------//

    /**
     * Prints the Personal Board of a given player
     *
     * @param player The player who's Personal Board is gonna be printed
     */
    public void printPersonalBoard(Player player) {

        printFaithTrack(player.getPersonalBoard().getFaithTrack(), player.isInkwell());

        vSpace(2);

        printWarehouse(player.getPersonalBoard().getWarehouseDepots(), 0);

        vSpace(1);

        printDevelopmentCardsStrongboxAndLeaders(player.getPersonalBoard().getDevelopmentCardsSlots(), player.getPersonalBoard().getStrongbox(), player.getLeaderCards());

        vSpace(1);

        printLeaderDepots(player.getPersonalBoard().getWarehouseDepots());
    }

    /**
     * Prints the Inkwell line by line
     *
     * @param isPrintable Tells the method if the Player has the Inkwell
     * @param lineToPrint The current line to print
     * @return The corresponding line to be printed
     */
    private String printInkwell(boolean isPrintable, int lineToPrint) {
        String s = "";

        if (isPrintable) {
            switch (lineToPrint) {
                case 0:
                    s = "      .----.";
                    break;

                case 1:
                    s = "     (______)";
                    break;

                case 2:
                    s = "    _--|  |--_";
                    break;

                case 3:
                    s = "   /\\  |  |   \\";
                    break;

                case 4:
                    s = "  /  \\____----'\\";
                    break;

                case 5:
                    s = " /   /    __    \\";
                    break;

                case 6:
                    s = "/   /    /  \\    \\";
                    break;

                case 7:
                    s = "\\  /      ''      \\";
                    break;

                case 8:
                    s = " \\/______-----'''''";
                    break;

                default:
                    break;
            }
        }
        return s;
    }

    /**
     * Prints the Player's Leader Cards in the bottom right after the Development Card Slots
     *
     * @param leaderCards The Player's cards
     */
    private String printLeaderCardsInPersonalBoard(List<LeaderCard> leaderCards, int lineToPrint) {
        StringBuilder s = new StringBuilder();

        if (lineToPrint > 7) {
            s.append(hSpace(20));

            switch (lineToPrint) {
                case 8:
                    for (int i = 0; i < leaderCards.size(); i++) s.append(",----------.").append(hSpace(5));
                    break;

                case 9:
                case 11:
                case 12:
                case 14:
                    for (int i = 0; i < leaderCards.size(); i++) s.append("|          |").append(hSpace(5));
                    break;

                case 10:
                    for (int i = 0; i < leaderCards.size(); i++)
                        s.append("| Leader ").append(i + 1).append(" |").append(hSpace(5));
                    break;

                case 13:
                    for (LeaderCard leaderCard : leaderCards)
                        s.append("| ").append(isLeaderCardActive(leaderCard)).append(" |").append(hSpace(5));
                    break;

                case 15:
                    for (int i = 0; i < leaderCards.size(); i++) s.append("'----------'").append(hSpace(5));
                    break;

                default:
                    break;
            }
        }
        return s.toString();
    }

    /**
     * Used to print the String under the Player's Leader Cards
     *
     * @param leaderCard The card to be controlled
     * @return ACTIVE if the Leader Card has been activated, INACTIVE in the other cases
     */
    private String isLeaderCardActive(LeaderCard leaderCard) {
        if (leaderCard.isActive()) return " ACTIVE ";
        else return "INACTIVE";
    }

    /**
     * Prints the Player's Leader Cards in a new screen
     *
     * @param leaderCards The Player's Leader Cards to print
     */
    public void printPlayerLeaderCards(List<LeaderCard> leaderCards) {
        System.out.println(hSpace(20) + "ooo        ooooo oooooo   oooo      ooooo        oooooooooooo       .o.       oooooooooo.   oooooooooooo ooooooooo.          .oooooo.         .o.       ooooooooo.   oooooooooo.    .oooooo..o \n" +
                hSpace(20) + "`88.       .888'  `888.   .8'       `888'        `888'     `8      .888.      `888'   `Y8b  `888'     `8 `888   `Y88.       d8P'  `Y8b       .888.      `888   `Y88. `888'   `Y8b  d8P'    `Y8 \n" +
                hSpace(20) + " 888b     d'888    `888. .8'         888          888             .8\"888.      888      888  888          888   .d88'      888              .8\"888.      888   .d88'  888      888 Y88bo.      \n" +
                hSpace(20) + " 8 Y88. .P  888     `888.8'          888          888oooo8       .8' `888.     888      888  888oooo8     888ooo88P'       888             .8' `888.     888ooo88P'   888      888  `\"Y8888o.  \n" +
                hSpace(20) + " 8  `888'   888      `888'           888          888    \"      .88ooo8888.    888      888  888    \"     888`88b.         888            .88ooo8888.    888`88b.     888      888      `\"Y88b \n" +
                hSpace(20) + " 8    Y     888       888            888       o  888       o  .8'     `888.   888     d88'  888       o  888  `88b.       `88b    ooo   .8'     `888.   888  `88b.   888     d88' oo     .d8P \n" +
                hSpace(20) + "o8o        o888o     o888o          o888ooooood8 o888ooooood8 o88o     o8888o o888bood8P'   o888ooooood8 o888o  o888o       `Y8bood8P'  o88o     o8888o o888o  o888o o888bood8P'   8\"\"88888P'\n" +
                hSpace(20) + "______________________________________________________________________________________________________________________________________________________________________________________________");

        vSpace(4);

        for (int i = 0; i < 18; i++) {
            System.out.print(hSpace(80));
            for (LeaderCard leaderCard : leaderCards) System.out.print(printLeader(leaderCard, i) + hSpace(20));
            vSpace(1);
        }

        vSpace(1);
        System.out.print(hSpace(89));
        for (LeaderCard leaderCard : leaderCards) {
            System.out.print(isLeaderCardActive(leaderCard) + hSpace(38));
        }
        vSpace(5);

        System.out.println("Press Enter to exit this view."); //FORSE VA SISTEMATO IN UNA FUTURA INTERAZIONE COL CONTROLLER
    }

    /**
     * Prints the Development Card Slots, Strongbox and the Leader Cards section of the Player's Personal Board
     *
     * @param developmentSlots The Development Card Slots to print
     * @param strongbox        The Strongbox to print
     */
    private void printDevelopmentCardsStrongboxAndLeaders(List<List<DevelopmentCard>> developmentSlots, List<Resource> strongbox, List<LeaderCard> leaderCards) {
        for (int j = 0; j <= 15; j++)
            System.out.println(
                    printStrongbox(j, strongbox) + hSpace(20) + "\u2502" + hSpace(8) +
                            printDevelopmentSlotCard(developmentSlots.get(0), j) + hSpace(5) + "\u2502" + hSpace(8) +
                            printDevelopmentSlotCard(developmentSlots.get(1), j) + hSpace(5) + "\u2502" + hSpace(8) +
                            printDevelopmentSlotCard(developmentSlots.get(2), j) + hSpace(5) + "\u2502" +
                            printLeaderCardsInPersonalBoard(leaderCards, j));
    }

    /**
     * If the Warehouse has more than 3 depots, prints the corresponding Leader Depots
     *
     * @param depots The total Warehouse Depots
     */
    private void printLeaderDepots(List<Depot> depots) { //forse c'è una soluzione più elegante
        if (depots.size() > 3) {
            for (int i = 3; i < depots.size(); i++) System.out.print("   ____________" + hSpace(6));
            vSpace(1);
            for (int i = 3; i < depots.size(); i++) System.out.print("  |            |" + hSpace(5));
            vSpace(1);
            for (int i = 3; i < depots.size(); i++)
                System.out.print(printLeaderDepotResources((LeaderDepot) depots.get(i)) + hSpace(5));
            vSpace(1);
            for (int i = 3; i < depots.size(); i++) System.out.print("  |____________|" + hSpace(5));
        }
    }

    /**
     * Prints the Resource stored in the Leader Depot
     *
     * @param leaderDepot The Leader Depot to print
     * @return A formatted String of the Resources stored in the Leader Depot
     */
    private String printLeaderDepotResources(LeaderDepot leaderDepot) {
        if (leaderDepot.getResources().size() == 0)
            return "  |" + pCS(leaderDepot.getDepotResource().getName(), Color.GREY) + "|";
        else if (leaderDepot.getResources().size() == 1)
            return "  |     " + pCS("\u2588\u2588", leaderDepot.getDepotResource().getColor()) + "     |";
        else
            return "  |  " + pCS("\u2588\u2588", leaderDepot.getDepotResource().getColor()) + "    " + pCS("\u2588\u2588", leaderDepot.getDepotResource().getColor()) + "  |";
    }

    //-----------------------------//
    //          STRONGBOX          //
    //-----------------------------//

    /**
     * Used to get the number of the resources stored in the strongbox
     *
     * @param resources    The resources in the strongbox
     * @param resourceType The desired type of resource
     * @return The number of Resources of resourceType stored in the strongbox
     */
    private int getStrongboxResourcesNumber(List<Resource> resources, Resource resourceType) {
        if (!resources.contains(resourceType)) return 0;
        Map<Resource, Long> numberOfResources = resources.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        return numberOfResources.get(resourceType).intValue();
    }

    /**
     * Used to print the Strongbox. Each line is individually printed due to the fact that Development Cards are also printed on the same lines
     *
     * @param lineToPrint The current line to be printed
     * @param resources   The Strongbox's Resources
     * @return The corresponding line to print
     */
    private String printStrongbox(int lineToPrint, List<Resource> resources) {
        String s = "";

        switch (lineToPrint) {
            case 0:
                s = "       ____...------------...____        ";
                break;

            case 1:
                s = "  _.-\"` /o/__ ____ __ __  __ \\o\\_`\"-._   ";
                break;

            case 2:
                s = ".'     / /                    \\ \\     '. ";
                break;

            case 3:
                s = "|=====/o/======================\\o\\=====| ";
                break;

            case 4:
                s = "|____/_/________..____..________\\_\\____| ";
                break;

            case 5:
                s = "/   _/ \\_     <_o#\\__/#o_>     _/ \\_   \\ ";
                break;

            case 6:
                s = "\\       _________\\####/_________       / ";
                break;

            case 7:
                s = " |===\\!/========================\\!/===|  ";
                break;

            case 8:
            case 10:
            case 12:
                s = " |   |o|                        |o|   |  ";
                break;

            case 9:
                s = " |   |o|   " + pCS("\u2588\u2588", Color.YELLOW) + " " + getStrongboxResourcesNumber(resources, Resource.COIN) + "          " + pCS("\u2588\u2588", Color.BLUE) + " " + getStrongboxResourcesNumber(resources, Resource.SHIELD) + "   |o|   |  ";
                break;

            case 11:
                s = " |   |o|   " + pCS("\u2588\u2588", Color.PURPLE) + " " + getStrongboxResourcesNumber(resources, Resource.SERVANT) + "     " +
                        "     " + pCS("\u2588\u2588", Color.GREY) + " " + getStrongboxResourcesNumber(resources, Resource.STONE) + "   |o|   |  ";
                break;

            case 13:
                s = " | __/ \\========================/ \\__ |  ";
                break;

            case 14:
                s = " |  _\\o/   __  {.' __  '.} _   _\\o/  _|  ";
                break;

            case 15:
                s = " `\"\"\"\"-\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"-\"\"\"\"`  ";

            default:
                break;
        }
        return s;
    }


    //------------------------------------------//
    //          DEVELOPMENT CARD SLOTS          //
    //------------------------------------------//

    /**
     * Return one line of the Development Card on the Player Board
     *
     * @param developmentSlot The slot to be printed
     * @param cardLineToPrint Each row of the card is printed individually to provide maximum control over variations of the grid structure
     * @return One line of the slots
     */
    private String printDevelopmentSlotCard(List<DevelopmentCard> developmentSlot, int cardLineToPrint) {
        if (developmentSlot.size() > 0)
            return printDevelopmentCard(developmentSlot.get(developmentSlot.size() - 1), cardLineToPrint, developmentSlot.size());
        return printEmptyDevelopmentCardSpace();
    }


    //-----------------------------//
    //          WAREHOUSE          //
    //-----------------------------//

    /**
     * Prints the Warehouse and the Resources in it
     *
     * @param warehouseDepots          A list of Depots that will be printed
     * @param rearrangeWarehouseHSpace The number of spaces from margin if the Warehouse is in rearrangement visualization
     *                                 Put this param to 0 in the other cases
     */
    private void printWarehouse(List<Depot> warehouseDepots, int rearrangeWarehouseHSpace) {
        System.out.println(hSpace(8 + rearrangeWarehouseHSpace) + "        ________\n" + hSpace(10 + rearrangeWarehouseHSpace) +
                "  ,=='        `==.");
        System.out.println(hSpace(8 + rearrangeWarehouseHSpace) + "  ╭/ " + printDepot(warehouseDepots.get(0)) + " \\╮" + hSpace(3) + pCS("Depot 1", Color.GREY));
        System.out.println(hSpace(8 + rearrangeWarehouseHSpace) + "  || ______________ ||\n" + hSpace(10 + rearrangeWarehouseHSpace) +
                "|)                ||");
        System.out.println(hSpace(8 + rearrangeWarehouseHSpace) + "  || " + printDepot(warehouseDepots.get(1)) + " ||" + hSpace(3) + pCS("Depot 2", Color.GREY));
        System.out.println(hSpace(8 + rearrangeWarehouseHSpace) + "  || ______________ (|\n" + hSpace(10 + rearrangeWarehouseHSpace) +
                "||                ||");
        System.out.println(hSpace(8 + rearrangeWarehouseHSpace) + "  )| " + printDepot(warehouseDepots.get(2)) + " ||" + hSpace(3) + pCS("Depot 3", Color.GREY));
        System.out.println(hSpace(8 + rearrangeWarehouseHSpace) + "  || ______________ ||\n" + hSpace(10 + rearrangeWarehouseHSpace) +
                "||                ||\n" + hSpace(10 + rearrangeWarehouseHSpace) +
                "||___.-__;----.___||");
    }

    /**
     * Used in printWarehouse() to get the correct representation of Resources
     *
     * @param depot The current Depot that will be printed
     * @return A formatted representation of the Resources in this Depot
     */
    private String printDepot(Depot depot) {
        StringBuilder s = new StringBuilder();
        List<Resource> resources = new ArrayList<>(depot.getResources());

        if (resources.size() == 0) return hSpace(14);

        s.append(hSpace(6 - (2 * resources.size())));
        for (Resource resource : resources) s.append(pCS("  \u2588\u2588", resource.getColor()));
        s.append(hSpace(8 - (2 * resources.size())));
        return s.toString();
    }


    /**
     * Show the screen that appears after getting Resources from the Market
     *
     * @param warehouseDepots The Warehouse to print
     * @param resources       The resources get prom the Market to insert into the Warehouse
     */
    public void printWarehouseConfiguration(List<Depot> warehouseDepots, List<Resource> resources) {
        Scanner in = new Scanner(System.in); //temporary solution
        cls();

        System.out.println(hSpace(20) + "____ ____ ____ ____ ____ ____ _  _ ____ ____    _   _ ____ _  _ ____    _ _ _ ____ ____ ____ _  _ ____ _  _ ____ ____    ___  ____ ____ ____ ____ ____    ____ ____ _  _ ___ _ _  _ _  _ _ _  _ ____ \n" +
                hSpace(20) + "|__/ |___ |__| |__/ |__/ |__| |\\ | | __ |___     \\_/  |  | |  | |__/    | | | |__| |__/ |___ |__| |  | |  | [__  |___    |__] |___ |___ |  | |__/ |___    |    |  | |\\ |  |  | |\\ | |  | | |\\ | | __ \n" +
                hSpace(20) + "|  \\ |___ |  | |  \\ |  \\ |  | | \\| |__] |___      |   |__| |__| |  \\    |_|_| |  | |  \\ |___ |  | |__| |__| ___] |___    |__] |___ |    |__| |  \\ |___    |___ |__| | \\|  |  | | \\| |__| | | \\| |__] \n" +
                hSpace(20) + "____________________________________________________________________________________________________________________________________________________________________________________________________");

        vSpace(3);

        printWarehouse(warehouseDepots, 73);

        vSpace(3);

        System.out.print(hSpace(83) + "Resources left to insert:");
        for (Resource resource : resources) {
            System.out.print(hSpace(8) + pCS("\u2588\u2588  ", resource.getColor()));
        }
        vSpace(5);

        System.out.println(hSpace(20) + "Please type the ResourceType and the number of the Depot in which you want to store it.");
        System.out.println(hSpace(20) + "The Resources that can't be added to the Warehouse will be discarded.");

        //Possibile soluzione soggetta a futuri cambiamenti
        vSpace(1);
        System.out.println(hSpace(20) + pCS("[RESOURCETYPE - DEPOTNUMBER]", Color.GREY));
        System.out.print("\u001b[20C"); //moving cursor right of 20 spaces
        in.nextLine(); //temporary solution
    }


    //-------------------------------//
    //          FAITH TRACK          //
    //-------------------------------//

    /**
     * Prints the Player's Faith Track and the Inkwell if the Player has it
     * The PopeSpace intervals are printed thicker
     *
     * @param faithTrack The Faith Track to print
     */
    private void printFaithTrack(FaithTrack faithTrack, boolean isInkwell) {
        System.out.println(
                "                                    +-------+=======+===" + pCS("2", Color.BYELLOW) + "===+=======+=======+---" + pCS("4", Color.BYELLOW) + "---+                               +--" + pCS("1", Color.BYELLOW) + "-" + pCS("2", Color.BYELLOW) + "--+=======+=======+==" + pCS("1", Color.BYELLOW) + "-" + pCS("6", Color.BYELLOW) + "==+=======+=======+==" + pCS("2", Color.BYELLOW) + "-" + pCS("0", Color.BYELLOW) + "==+\n" +
                        "                                    |       \u2551       |       |       |       \u2551       |                               |       \u2551       |       |       |       |       |       \u2551");
        firstLine(isInkwell, faithTrack.getFaithPoints());
        System.out.println(
                "                                    |       \u2551       |       |       |       \u2551       |                               |       \u2551       |       |       |       |       |       \u2551" + hSpace(31) + printInkwell(isInkwell, 1) + "\n" +
                        "                                    +-------+=======+=======+=======+=======+-------+           +=======+           +-------+=======+=======+=======+=======+=======+=======+" + hSpace(31) + printInkwell(isInkwell, 2) + "\n" +
                        "                                    |       |           \u2551       \u2551           |       |           \u2551       \u2551           |       |                   \u2551       \u2551" + hSpace(51) + printInkwell(isInkwell, 3));
        midLine(isInkwell, faithTrack.getFaithPoints(), faithTrack.getVaticanReportSections());
        System.out.println(
                "                                    |       |           \u2551       \u2551           |       |           \u2551       \u2551           |       |                   \u2551       \u2551" + hSpace(51) + printInkwell(isInkwell, 5) + "\n" +
                        "                    +-------+-------+-------+           +=======+           +-------+=======+=======+=======+=======+=======+                   +=======+" + hSpace(51) + printInkwell(isInkwell, 6) + "\n" +
                        "                    |       |       |       |                               |       \u2551       |       |       |       |       \u2551" + hSpace(79) + printInkwell(isInkwell, 7));
        lastine(isInkwell, faithTrack.getFaithPoints());
        System.out.println(
                "                    |       |       |       |                               |       \u2551       |       |       |       |       \u2551\n" +
                        "                    +-------+-------+-------+                               +-------+===" + pCS("6", Color.BYELLOW) + "===+=======+=======+===" + pCS("9", Color.BYELLOW) + "===+=======+");
    }

    /**
     * Return a String representing the Player's cross
     */
    private void printCross() {
        System.out.print("  \u001b[41;1m \u2020 \u001b[0m  ");
    }

    /**
     * Auxiliary method used in firstLine(), secondLine() and thirdLine() methods
     *
     * @param start The position of the first cell of the line on the track
     * @param end   The position of the last cell of the line on the track
     * @param fp    (start <= fp <= end) ==> a cross will be printed in fp position
     *              A blank space is printed otherwise
     */
    private void printRow(int start, int end, int fp) {
        for (int i = start; i <= end; i++) {
            if (i != fp) {
                if (i < 10) {
                    if (i == 8) System.out.print(pCS(hSpace(3) + "\u256C" + hSpace(3), Color.WHITE)); //PopeSpace symbol
                    else System.out.print(pCS(hSpace(3) + i + hSpace(3), Color.GREY));
                } else {
                    if (i == 16 || i == 24)
                        System.out.print(pCS(hSpace(3) + "\u256C" + hSpace(3), Color.WHITE)); //PopeSpace symbol
                    else System.out.print(pCS(hSpace(2) + (i / 10) + " " + (i % 10) + hSpace(2), Color.GREY));
                }
            } else printCross();
            if (i == 4 || i == 8 || i == 11 || i == 16 || i == 18 || i == 24) System.out.print("\u2551");
            else System.out.print("|");
        }
    }

    /**
     * Prints the first line of the Track and one line of the Inkwell
     *
     * @param isInkwell If the Player has the inkwell, prints it
     * @param fp        Has the same function as before
     */
    private void firstLine(boolean isInkwell, int fp) {
        System.out.print(hSpace(36) + pCS("1", Color.BYELLOW));
        printRow(4, 9, fp);
        System.out.print(hSpace(31) + "|");
        printRow(18, 24, fp);
        System.out.print(hSpace(31) + printInkwell(isInkwell, 0)); //Inkwell line
        vSpace(1);
    }

    /**
     * Prints the second line of the Track, including VaticanReportSections and one line of the Inkwell
     *
     * @param isInkwell If the Player has the inkwell, prints it
     * @param fp        Has the same function as before
     */
    private void midLine(boolean isInkwell, int fp, VaticanReportSection[] vaticanReportSections) {
        System.out.print(hSpace(36) + "|");
        printRow(3, 3, fp);
        System.out.print(hSpace(11) + printVaticanReportSection(vaticanReportSections[0]));
        System.out.print(hSpace(11) + "|");
        printRow(10, 10, fp);
        System.out.print(hSpace(11) + printVaticanReportSection(vaticanReportSections[1]));
        System.out.print(hSpace(11) + "|");
        printRow(17, 17, fp);
        System.out.print(hSpace(19) + printVaticanReportSection(vaticanReportSections[2]));
        System.out.print(hSpace(51) + printInkwell(isInkwell, 4)); //Inkwell line
        vSpace(1);
    }

    /**
     * Prints the Vatican Report Tiles in the Faith Track
     *
     * @param vaticanReportSection The section to print
     * @return X if the Section isn't active, the Section's value if it's active
     */
    private String printVaticanReportSection(VaticanReportSection vaticanReportSection) {
        if (vaticanReportSection.isPopesFavorTileActive())
            return "\u2551   \u001b[38;5;209m" + vaticanReportSection.getValue() + "\u001b[0m   \u2551";
        else return "\u2551   X   \u2551";
    }

    /**
     * Prints the third line of the Track and one line of the Inkwell
     *
     * @param isInkwell If the Player has the inkwell, prints it
     * @param fp        Has the same function as before
     */
    private void lastine(boolean isInkwell, int fp) {
        System.out.print(hSpace(20) + "|");
        printRow(0, 2, fp);
        System.out.print(hSpace(31) + "|");
        printRow(11, 16, fp);
        System.out.print(hSpace(79) + printInkwell(isInkwell, 8)); //Inkwell line
        vSpace(1);
    }


    //--------------------------//
    //          MARKET          //
    //--------------------------//

    //   .d88b.
    //   8b88d8    marble protothype
    //   `Y88P'

    /**
     * Prints the Market Tray
     *
     * @param marketTray The Market Tray to print
     */
    public void printMarket(MarketTray marketTray) {
        System.out.println(
                "             ,----------------------------------+");
        printMarbleOnSlide(marketTray);
        System.out.println(
                "         (                                      |\n" +
                        "         |           +--------------------------|");
        printMarketMarbleRows(marketTray);
        System.out.println(
                "         |           |                          |\n" +
                        "         +-----------+--------------------------+\n" +
                        "                           \u028C    \u028C    \u028C    \u028C\n" +
                        "                           |    |    |    |\n" +
                        "                           |    |    |    |");
        vSpace(2);
        printMarketLegend();
    }

    /**
     * Prints the rows of the Market Tray
     *
     * @param marketTray The Market Tray to print
     */
    private void printMarketMarbleRows(MarketTray marketTray) {
        for (int i = 2; i >= 0; i--) {
            Resource[] resources = marketTray.getMarblesOnRow(i);
            System.out.println(
                    "         |           |    " + pCS("___", resources[3].getColor()) + "  " + pCS("___", resources[2].getColor()) + "  " + pCS("___", resources[1].getColor()) + "  " + pCS("___", resources[0].getColor()) + "    |\n" +
                            "         |           |   " + pCS("/   \\", resources[3].getColor()) + pCS("/   \\", resources[2].getColor()) + pCS("/   \\", resources[1].getColor()) + pCS("/   \\", resources[0].getColor()) + "   | <-----\n" +
                            "         |           |   " + pCS("\\___/", resources[3].getColor()) + pCS("\\___/", resources[2].getColor()) + pCS("\\___/", resources[1].getColor()) + pCS("\\___/", resources[0].getColor()) + "   |");
        }
    }

    /**
     * Prints the marble on the Market Tray slide
     *
     * @param marketTray The Market Tray to print
     */
    private void printMarbleOnSlide(MarketTray marketTray) {
        Color marbleOnSlideColor = marketTray.getMarbleOnSlide().getColor();
        System.out.println(
                "            /                             " + pCS("___", marbleOnSlideColor) + "   |\n" +
                        "           /                             " + pCS("/   \\", marbleOnSlideColor) + "  |\n" +
                        "          /                              " + pCS("\\___/", marbleOnSlideColor) + "  |");
    }

    /**
     * Prints the Market legend
     */
    private void printMarketLegend() {
        System.out.println(
                "+--------------------------------------------------------+\n" +
                        "|                         Legend                         |\n" +
                        "|   " + pCS("___", Color.GREY) + "      " + pCS("__", Color.GREY) + "          " + pCS("___", Color.PURPLE) + "      " + pCS("__", Color.PURPLE) + "         " + pCS("___", Color.RED) + "    " + pCS("_|_", Color.RED) + "  |\n" +
                        "|  " + pCS("/   \\", Color.GREY) + " _  " + pCS("/  \\_", Color.GREY) + "       " + pCS("/   \\", Color.PURPLE) + " _  " + pCS("(  )", Color.PURPLE) + "       " + pCS("/   \\", Color.RED) + " _  " + pCS("|", Color.RED) + "   |\n" +
                        "|  " + pCS("\\___/", Color.GREY) + "   " + pCS("/_____|", Color.GREY) + "      " + pCS("\\___/", Color.PURPLE) + "   " + pCS("/____\\", Color.PURPLE) + "      " + pCS("\\___/", Color.RED) + "    " + pCS("|", Color.RED) + "   |\n" +
                        "|   " + pCS("___", Color.BLUE) + "    " + pCS("_____", Color.BLUE) + "         " + pCS("___", Color.YELLOW) + "     " + pCS("____", Color.YELLOW) + "        " + pCS("___", Color.WHITE) + "     " + pCS("_", Color.WHITE) + "   |\n" +
                        "|  " + pCS("/   \\", Color.BLUE) + " _ " + pCS("\\   /", Color.BLUE) + "        " + pCS("/   \\", Color.YELLOW) + " _ " + pCS("/ __ \\", Color.YELLOW) + "      " + pCS("/   \\", Color.WHITE) + " _ " + pCS("| |", Color.WHITE) + "  |\n" +
                        "|  " + pCS("\\___/", Color.BLUE) + "    " + pCS("\\_/", Color.BLUE) + "         " + pCS("\\___/", Color.YELLOW) + "   " + pCS("\\____/", Color.YELLOW) + "      " + pCS("\\___/", Color.WHITE) + "   " + pCS("|_|", Color.WHITE) + "  |\n" +
                        "|                                                        |\n" +
                        "+--------------------------------------------------------+");
    }


    //-----------------------------------------//
    //          DEVELOPMENT CARD GRID          //
    //-----------------------------------------//

    /**
     * Prints the Development Card Grid
     *
     * @param developmentGrid The Development Grid to print
     */
    public void printDevelopmentGrid(DevelopmentGrid developmentGrid) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j <= 16; j++)
                System.out.println(
                        hSpace(12) + printDevelopmentGridCard(developmentGrid.getDevelopmentGridCell(i, 0), j) +
                                hSpace(12) + printDevelopmentGridCard(developmentGrid.getDevelopmentGridCell(i, 1), j) +
                                hSpace(12) + printDevelopmentGridCard(developmentGrid.getDevelopmentGridCell(i, 2), j) +
                                hSpace(12) + printDevelopmentGridCard(developmentGrid.getDevelopmentGridCell(i, 3), j));
            vSpace(1);
        }
    }

    /**
     * Used to print the Development Cards in the Development Grid
     *
     * @param developmentGridCell If the cell contains one or more cards, print the one on the top. Otherwise, leave a blank space
     * @param cardLineToPrint     Each row of the card is printed individually to provide maximum control over variations of the grid structure
     * @return One line of the grid
     */
    private String printDevelopmentGridCard(DevelopmentGridCell developmentGridCell, int cardLineToPrint) {
        if (!developmentGridCell.isEmpty())
            return printDevelopmentCard(developmentGridCell.getFirstCard(), cardLineToPrint, developmentGridCell.getDevelopmentCardsSize());
        return printEmptyDevelopmentCardSpace();
    }


    //-------------------------------------//
    //          DEVELOPMENT CARDS          //
    //-------------------------------------//

    /**
     * Used to print DevelopmentCards.
     *
     * @param developmentCard The desired card to print
     * @param cardLineToPrint Each row of the card is printed individually to provide maximum control
     * @param cardStackSize   How many cards are on the top of each other
     * @return One line of the card
     */
    private String printDevelopmentCard(DevelopmentCard developmentCard, int cardLineToPrint, int cardStackSize) {
        String s = "";

        //In each switch cases, the code near break command is used to build the outline of the cards under the top one
        switch (cardLineToPrint) {
            case 0:
                s = ",---------------------.";

                s = s + hSpace(3);
                break;

            case 1:
                s = printDevelopmentCardRequirements(developmentCard);

                if (cardStackSize > 1) s = s + "." + hSpace(2);
                else s = s + hSpace(3);
                break;

            case 2:
                s = "| " + pCS(".-----------------.", developmentCard.getDevelopmentCardType().getColor()) + " |";

                if (cardStackSize == 1) s = s + hSpace(3);
                else if (cardStackSize == 2) s = s + "\u2502" + hSpace(2);
                else s = s + "\u2502." + hSpace(1);
                break;

            case 3:
                s = printDevelopmentCardLevel(developmentCard);

                if (cardStackSize == 1) s = s + hSpace(3);
                else if (cardStackSize == 2) s = s + "\u2502" + hSpace(2);
                else if (cardStackSize == 3) s = s + "\u2502\u2502" + hSpace(1);
                else s = s + "\u2502\u2502.";
                break;

            case 4:
                s = "| " + pCS("`-----------------'", developmentCard.getDevelopmentCardType().getColor()) + " |";

                s = s + printHorizontalBorder(cardStackSize);
                break;

            case 5:
            case 11:
                s = "|                     |";

                s = s + printHorizontalBorder(cardStackSize);
                break;

            case 6:
                s = "| .-----------------. |";

                s = s + printHorizontalBorder(cardStackSize);
                break;

            case 7:
                s = printProductionLines(developmentCard, 1);

                s = s + printHorizontalBorder(cardStackSize);
                break;

            case 8:
                s = printProductionLines(developmentCard, 2);

                s = s + printHorizontalBorder(cardStackSize);
                break;

            case 9:
                s = printProductionLines(developmentCard, 3);

                s = s + printHorizontalBorder(cardStackSize);
                break;

            case 10:
                s = "| |_________________| |";

                s = s + printHorizontalBorder(cardStackSize);
                break;

            case 12:
                s = printCardVictoryPoints(developmentCard);

                s = s + printHorizontalBorder(cardStackSize);
                break;

            case 13:
                s = "`---------------------'";

                s = s + printHorizontalBorder(cardStackSize);
                break;

            case 14:
                if (cardStackSize == 2) s = " `---------------------'" + hSpace(2);
                else if (cardStackSize == 3)
                    s = " `---------------------'\u2502" + hSpace(1);
                else if (cardStackSize == 4)
                    s = " `---------------------'\u2502\u2502";
                else s = hSpace(26);
                break;

            case 15:
                if (cardStackSize == 3) s = "  `---------------------'" + hSpace(1);
                else if (cardStackSize == 4) s = "  `---------------------'\u2502";
                else s = hSpace(26);
                break;

            case 16:
                if (cardStackSize == 4) s = "   `---------------------'";
                else s = hSpace(26);
                break;

            default:
                break;
        }
        return s;
    }

    /**
     * If there are no Development Cards, leave a blank space
     *
     * @return The blank space to print
     */
    private String printEmptyDevelopmentCardSpace() {
        return hSpace(26);
    }

    /**
     * In case of a Development Card stack, prints the border of the cards under the top one
     *
     * @param numberOfCards How many cards there are in the stack
     * @return The correct border representation of the stack
     */
    private String printHorizontalBorder(int numberOfCards) {
        return "\u2502".repeat(Math.max(0, numberOfCards - 1)) + hSpace(4 - numberOfCards);
    }

    /**
     * Prints the Development Card requirements line
     *
     * @param developmentCard The Card to print
     * @return The requirements of the Card
     */
    private String printDevelopmentCardRequirements(DevelopmentCard developmentCard) {
        StringBuilder s = new StringBuilder("| ");
        int remainingSpace = 20;

        for (Map.Entry<Resource, Integer> entry : developmentCard.getCost().entrySet()) {
            s.append(entry.getValue()).append(pCS("\u25A0", entry.getKey().getColor())).append("  ");
            remainingSpace -= 4;
        }
        s.append(hSpace(remainingSpace)).append("|");

        return s.toString();
    }

    /**
     * Prints the Development Card level lines in the correct color
     *
     * @param developmentCard The Card to print
     * @return The level of the Card
     */
    private String printDevelopmentCardLevel(DevelopmentCard developmentCard) {
        String s = "| ";

        s = s + printLevelDots(developmentCard.getDevelopmentCardType().getLevel().getLevelNumber(), developmentCard.getDevelopmentCardType().getColor());
        s = s + hSpace(8 - (2 * developmentCard.getDevelopmentCardType().getLevel().getLevelNumber())) + "lvl   " + hSpace(6 - (2 * developmentCard.getDevelopmentCardType().getLevel().getLevelNumber()));
        s = s + printLevelDots(developmentCard.getDevelopmentCardType().getLevel().getLevelNumber(), developmentCard.getDevelopmentCardType().getColor());
        s = s + "|";
        return s;
    }

    /**
     * Prints a Card level dots in the correct Color
     *
     * @param level The level number of the Card
     * @param color The Color of the Card
     * @return A formatted String of the Level dots
     */
    private String printLevelDots(int level, Color color) {
        return pCS("\u25CF ", color).repeat(Math.max(0, level));
    }

    /**
     * Used to print the Production section of a DevelopmentCard
     * It calls three auxiliary methods in order to get a clean print of the Resources
     *
     * @param developmentCard The card that is gonna to be printed
     * @param line            Print the first, second or third line of the Production section
     * @return The line that will be printed
     */
    private String printProductionLines(DevelopmentCard developmentCard, int line) {
        List<Resource> requiredResources = new ArrayList<>();
        List<Resource> producedResources = new ArrayList<>();
        List<Integer> numberOfRequiredResources = new ArrayList<>();
        List<Integer> numberOfProducedResources = new ArrayList<>();

        for (Map.Entry<Resource, Integer> entry : developmentCard.getProductionCost().entrySet()) {
            requiredResources.add(entry.getKey());
            numberOfRequiredResources.add(entry.getValue());
        }

        for (Map.Entry<Resource, Integer> entry : developmentCard.getProductionReturn().entrySet()) {
            producedResources.add(entry.getKey());
            numberOfProducedResources.add(entry.getValue());
        }

        if (line == 1)
            return printFirstProductionRow(requiredResources, numberOfRequiredResources, producedResources, numberOfProducedResources);
        else if (line == 2)
            return printSecondProductionRow(requiredResources, numberOfRequiredResources, producedResources, numberOfProducedResources);
        else
            return printThirdProductionRow(requiredResources, numberOfRequiredResources, producedResources, numberOfProducedResources);
    }

    /**
     * Prints the first line of the Development Card production section
     *
     * @param requiredResources         Resources type required to perform the production action
     * @param numberOfRequiredResources Resources number required to perform the production action
     * @param producedResources         Resources type produced by the production action
     * @param numberOfProducedResources Resources number produced by the production action
     * @return A formatted String of the first production section's line
     */
    private String printFirstProductionRow(List<Resource> requiredResources, List<Integer> numberOfRequiredResources, List<Resource> producedResources, List<Integer> numberOfProducedResources) {
        String s = "| |  ";

        if (requiredResources.size() == 1) s = s + hSpace(10);
        else s = s + numberOfRequiredResources.get(0) + pCS(" \u25A0", requiredResources.get(0).getColor()) + hSpace(7);

        if (producedResources.size() == 1) s = s + hSpace(5) + "| |";
        else s = s + numberOfProducedResources.get(0) + pCS(" \u25A0  ", producedResources.get(0).getColor()) + "| |";

        return s;
    }

    /**
     * Prints the second line of the Development Card production section
     *
     * @param requiredResources         Resources type required to perform the production action
     * @param numberOfRequiredResources Resources number required to perform the production action
     * @param producedResources         Resources type produced by the production action
     * @param numberOfProducedResources Resources number produced by the production action
     * @return A formatted String of the second production section's line
     */
    private String printSecondProductionRow(List<Resource> requiredResources, List<Integer> numberOfRequiredResources, List<Resource> producedResources, List<Integer> numberOfProducedResources) {
        String s = "| |  ";

        if (requiredResources.size() == 1)
            s = s + numberOfRequiredResources.get(0) + pCS(" \u25A0", requiredResources.get(0).getColor()) + "  -->  ";
        else s = s + "     -->  ";

        if (producedResources.size() == 1)
            s = s + numberOfProducedResources.get(0) + pCS(" \u25A0  ", producedResources.get(0).getColor()) + "| |";
        else if (producedResources.size() == 3)
            s = s + numberOfProducedResources.get(1) + pCS(" \u25A0  ", producedResources.get(1).getColor()) + "| |";
        else s = s + "     | |";

        return s;
    }

    /**
     * Prints the third line of the Development Card production section
     *
     * @param requiredResources         Resources type required to perform the production action
     * @param numberOfRequiredResources Resources number required to perform the production action
     * @param producedResources         Resources type produced by the production action
     * @param numberOfProducedResources Resources number produced by the production action
     * @return A formatted String of the third production section's line
     */
    private String printThirdProductionRow(List<Resource> requiredResources, List<Integer> numberOfRequiredResources, List<Resource> producedResources, List<Integer> numberOfProducedResources) {
        String s = "| |  ";

        if (requiredResources.size() == 2)
            s = s + numberOfRequiredResources.get(1) + pCS(" \u25A0", requiredResources.get(1).getColor()) + hSpace(7);
        else s = s + hSpace(10);

        if (producedResources.size() == 2)
            s = s + numberOfProducedResources.get(1) + pCS(" \u25A0  ", producedResources.get(1).getColor()) + "| |";
        else if (producedResources.size() == 3)
            s = s + numberOfProducedResources.get(2) + pCS(" \u25A0  ", producedResources.get(2).getColor()) + "| |";
        else s = s + "     | |";

        return s;
    }

    /**
     * Print the Development Card's Victory Points
     *
     * @param developmentCard The Card where to get the VictoryPoints
     * @return The Card's Victory Points
     */
    private String printCardVictoryPoints(DevelopmentCard developmentCard) {
        String s = "";
        s = s + "|" + hSpace(10) + developmentCard.getVictoryPoints() + hSpace(10 - (developmentCard.getVictoryPoints() / 10)) + "|";
        return s;
    }


    //--------------------------------//
    //          LEADER CARDS          //
    //--------------------------------//

    //TEMPORARY SOLUTION selectLeaders è la temporary solution

    /**
     * Prints the Leader selection Screen
     *
     * @param leaderCards The 4 Leader Cards given to the Player choice
     * @return The chosen Cards
     */
    public List<LeaderCard> selectLeaders(List<LeaderCard> leaderCards) { //void method, may be modified into return List<LeaderCard>
        List<LeaderCard> selectedLeaders = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        int index;

        for (int i = 0; i < 2; i++) {
            cls();
            System.out.println(
                    hSpace(27) + ".|'''|  '||''''| '||     '||''''| .|'''', |''||''|          '\\\\  //` .|''''|, '||   ||` '||'''|,          '||     '||''''|      /.\\      '||'''|. '||''''| '||'''|, .|'''|  \n" +
                            hSpace(27) + "||       ||   .   ||      ||   .  ||         ||               \\\\//   ||    ||  ||   ||   ||   ||           ||      ||   .      // \\\\      ||   ||  ||   .   ||   || ||      \n" +
                            hSpace(27) + "`|'''|,  ||'''|   ||      ||'''|  ||         ||                ||    ||    ||  ||   ||   ||...|'           ||      ||'''|     //...\\\\     ||   ||  ||'''|   ||...|' `|'''|, \n" +
                            hSpace(27) + " .   ||  ||       ||      ||      ||         ||                ||    ||    ||  ||   ||   || \\\\             ||      ||        //     \\\\    ||   ||  ||       || \\\\    .   || \n" +
                            hSpace(27) + " |...|' .||....| .||...| .||....| `|....'   .||.              .||.   `|....|'  `|...|'  .||  \\\\.          .||...| .||....| .//       \\\\. .||...|' .||....| .||  \\\\.  |...|' \n" +
                            hSpace(27) + "___________________________________________________________________________________________________________________________________________________________________________");

            vSpace(5);
            printLeaderChoice(leaderCards);
            vSpace(1);

            System.out.println(
                    hSpace(37) + isLeaderSelected(leaderCards.get(0), selectedLeaders, leaderCards) +
                            hSpace(34) + isLeaderSelected(leaderCards.get(1), selectedLeaders, leaderCards) +
                            hSpace(34) + isLeaderSelected(leaderCards.get(2), selectedLeaders, leaderCards) +
                            hSpace(34) + isLeaderSelected(leaderCards.get(3), selectedLeaders, leaderCards));
            vSpace(3);

            System.out.print("Please type the number of the ");
            if (i == 0) System.out.print("first ");
            else System.out.print("second ");
            System.out.print("Leader of your choice: ");

            //TEMPORARY SOLUTION
            do {
                do {
                    index = in.nextInt() - 1;
                    if (index < 0 || index > 3) {
                        System.out.print("Index is out of bounds! Please try again: ");
                    }
                } while (index > 3 || index < 0);
                if (!selectedLeaders.contains(leaderCards.get(index))) {
                    selectedLeaders.add(leaderCards.get(index));
                    break;
                } else System.out.print("Leader already selected! Please try again: ");
            } while (selectedLeaders.contains(leaderCards.get(index)));
        }

        in.nextLine();

        /*Debug only*/
        System.out.println("You selected Leader " + (leaderCards.indexOf(selectedLeaders.get(0)) + 1) + " and Leader " + (leaderCards.indexOf(selectedLeaders.get(1)) + 1));

        return selectedLeaders;
    }

    /**
     * If a Leader Cards is selected, mark it with a red SELECTED String
     *
     * @param leaderCard      The card to check if has been selected
     * @param selectedLeaders The Leaders selected by the Player
     * @param leadercards     The 4 Leader Cards given to the Player's choice
     * @return SELECTED if the Card has been selected, Leader #ofLeader otherwise
     */
    private String isLeaderSelected(LeaderCard leaderCard, List<LeaderCard> selectedLeaders, List<LeaderCard> leadercards) {
        if (selectedLeaders.contains(leaderCard)) return "\u001b[41m  SELECTED  \u001b[0m";
        else return "  Leader " + (leadercards.indexOf(leaderCard) + 1) + "  ";
    }

    /**
     * Prints the 4 Leader cards one next to the others. Used in selectLeaders() method
     *
     * @param leaderCards The cards to print
     */
    private void printLeaderChoice(List<LeaderCard> leaderCards) {
        for (int i = 0; i < 18; i++) {
            System.out.println(hSpace(30) + printLeader(leaderCards.get(0), i) +
                    hSpace(20) + printLeader(leaderCards.get(1), i) +
                    hSpace(20) + printLeader(leaderCards.get(2), i) +
                    hSpace(20) + printLeader(leaderCards.get(3), i));
        }
    }

    /**
     * Prints a LeaderCard line by line
     *
     * @param leaderCard  The Leader Card to print
     * @param lineToPrint The line to print
     * @return The corresponding line to print
     */
    private String printLeader(LeaderCard leaderCard, int lineToPrint) {
        String s = "";

        switch (lineToPrint) {
            case 0:
                s = ",------------------------.";
                break;

            case 1:
                s = printLeaderRequirements(leaderCard);
                break;

            case 2:

            case 7:
                s = "|  .------------------.  |";
                break;

            case 3:
                s = "| | " + leaderCard.getSpecialAbility().getAbilityType() + " | |";
                break;

            case 4:
                s = "|  `------------------'  |";
                break;

            case 5:

            case 15:
                s = "|                        |";
                break;

            case 6:
                s = "| Power                  |";
                break;

            case 8:
            case 9:
            case 10:
            case 11:
                s = "| " + leaderCard.getSpecialAbility().getPowerDescription(lineToPrint - 8) + " |";
                break;

            case 12:
                s = "| |--------------------| |";
                break;

            case 13:
                s = "| |        VP " + leaderCard.getVictoryPoints() + "        | |";
                break;

            case 14:
                s = "| |____________________| |";
                break;

            case 16:
                s = "| " + leaderCard.getSpecialAbility().getResourceInformation() + " |";
                break;

            case 17:
                s = "`------------------------'";
                break;

            default:
                break;
        }
        return s;
    }

    /**
     * Prints the requirement of a given Leader Card
     *
     * @param leaderCard The Leader Card to analyze
     * @return The Leader Card's requirements
     */
    private String printLeaderRequirements(LeaderCard leaderCard) {
        if (leaderCard.getDevelopmentCardRequirements().keySet().size() == 0)
            return printLeaderResourcesRequirements(leaderCard.getResourcesRequirements());
        else return printLeaderDevelopmentCardRequirements(leaderCard.getDevelopmentCardRequirements());
    }

    /**
     * If the Leader Cards requires Development Cards, this method is invoked in printLeaderRequirements()
     *
     * @param requirements The requirements of the Leader Card
     * @return A formatted String of the requirements
     */
    private String printLeaderDevelopmentCardRequirements(Map<DevelopmentCardType, Integer> requirements) {
        StringBuilder s = new StringBuilder();
        int remainingSpace = 7;

        if (requirements.keySet().size() == 1) {
            s.append("|  LevelRequired: ");
            for (Map.Entry<DevelopmentCardType, Integer> entry : requirements.entrySet()) {
                s.append(pCS(" \u25CF \u25CF", entry.getKey().getColor()));
                remainingSpace -= 4;
            }
        } else {
            s.append("|  DevCardsCost:  ");
            for (Map.Entry<DevelopmentCardType, Integer> entry : requirements.entrySet()) {
                s.append(entry.getValue()).append(pCS("\u25D8", entry.getKey().getColor())).append(" ");
                remainingSpace -= 3;
            }
        }
        s.append(hSpace(remainingSpace)).append("|");

        return s.toString();
    }

    /**
     * If the Leader Cards requires Resources, this method is invoked in printLeaderRequirements()
     *
     * @param requirements The requirements of the Leader Card
     * @return A formatted String of the requirements
     */
    private String printLeaderResourcesRequirements(Map<Resource, Integer> requirements) {
        StringBuilder s = new StringBuilder();
        int remainingSpace = 7;

        s.append("|  ResourcesCost: ");
        for (Map.Entry<Resource, Integer> entry : requirements.entrySet()) {
            s.append(entry.getValue()).append(pCS("\u25A0", entry.getKey().getColor())).append(" ");
            remainingSpace -= 3;
        }
        s.append(hSpace(remainingSpace)).append("|");

        return s.toString();
    }


    //-----------------------------//
    //          CLI UTILS          //
    //-----------------------------//

    /**
     * Inserts blank lines
     *
     * @param spaces The number of the lines
     */
    private void vSpace(int spaces) {
        for (int i = 0; i < spaces; i++) System.out.println();
    }

    /**
     * Inserts blank spaces in a String
     *
     * @param spaces The number of the spaces
     * @return A blank String which length is == spaces
     */
    private String hSpace(int spaces) {
        return " ".repeat(Math.max(0, spaces));
    }

    /**
     * Clears the terminal screen
     */
    public void cls() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * pCS = Print Colored String. Returns a colored String
     *
     * @param string The String to be colored
     * @param color  The Color
     * @return The colored String
     */
    private String pCS(String string, Color color) {
        return color + string + Color.RESET;
    }

}
