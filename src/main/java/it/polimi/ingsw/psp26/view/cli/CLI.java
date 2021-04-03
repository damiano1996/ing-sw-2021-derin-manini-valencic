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
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.*;
import java.util.stream.Collectors;

public class CLI {

    //CLI testing. Only press start to test CLI functionalities
    public static void main(String[] args) throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, CanNotAddResourceToDepotException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException {

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
        List<Resource> strongbox = new ArrayList<>();
        cli.printPersonalBoard(player);
        in.nextLine();
        cli.cls();

        player.getPersonalBoard().getWarehouseDepots().get(0).addResource(Resource.SHIELD);
        player.getPersonalBoard().getWarehouseDepots().get(1).addResource(Resource.COIN);
        player.getPersonalBoard().getWarehouseDepots().get(2).addResource(Resource.SERVANT);
        player.getPersonalBoard().getStrongbox().add(Resource.STONE);
        player.getPersonalBoard().getStrongbox().add(Resource.SERVANT);
        player.getPersonalBoard().addResourcesToStrongbox(strongbox);
        player.getPersonalBoard().addDevelopmentCard(0, developmentGrid.drawCard(Color.GREEN, Level.FIRST));
        player.getPersonalBoard().addDevelopmentCard(1, developmentGrid.drawCard(Color.GREEN, Level.FIRST));
        player.getPersonalBoard().addDevelopmentCard(2, developmentGrid.drawCard(Color.YELLOW, Level.FIRST));
        cli.printPersonalBoard(player);
        in.nextLine();
        cli.cls();

        player.getPersonalBoard().getWarehouseDepots().get(1).addResource(Resource.COIN);
        player.getPersonalBoard().getWarehouseDepots().get(2).addResource(Resource.SERVANT);
        player.getPersonalBoard().getStrongbox().add(Resource.COIN);
        player.getPersonalBoard().getStrongbox().add(Resource.STONE);
        player.getPersonalBoard().getStrongbox().add(Resource.SHIELD);
        player.getPersonalBoard().addResourcesToStrongbox(strongbox);
        player.getPersonalBoard().addDevelopmentCard(1, developmentGrid.drawCard(Color.BLUE, Level.SECOND));
        player.getPersonalBoard().addDevelopmentCard(2, developmentGrid.drawCard(Color.PURPLE, Level.SECOND));
        cli.printPersonalBoard(player);
        in.nextLine();
        cli.cls();

        player.getPersonalBoard().getWarehouseDepots().get(2).addResource(Resource.SERVANT);
        player.getPersonalBoard().getStrongbox().add(Resource.COIN);
        player.getPersonalBoard().getStrongbox().add(Resource.SHIELD);
        player.getPersonalBoard().getStrongbox().add(Resource.SERVANT);
        player.getPersonalBoard().getStrongbox().add(Resource.COIN);
        player.getPersonalBoard().addResourcesToStrongbox(strongbox);
        player.getPersonalBoard().addDevelopmentCard(2, developmentGrid.drawCard(Color.GREEN, Level.THIRD));
        cli.printPersonalBoard(player);
        in.nextLine();


        //---PRINT-PLAYER-LEADER-CARDS-TEST---// Press Enter 4 times

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


        //FAITH-TRACH-MOVEMENT-TEST---// Press Enter 25 times

        cli.cls();
        for (int i = 0; i < 25; i++) {
            cli.printFaithTrack(personalBoard.getFaithTrack());
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


    //----------TITLE----------//

    private void printTitle() {
        cls();
        Color.GREEN.setColor();
        System.out.println("#####   ##    ##                                                                            /##           ##### /##                                                                                 \n" +
                "  ######  /#### #####                                                                          #/ ###       ######  / ##            #                                                                                    \n" +
                " /#   /  /  ##### #####                        #                                              ##   ###     /#   /  /  ##           ###                                                                                   \n" +
                "/    /  /   # ##  # ##                        ##                                              ##          /    /  /   ##            #                                                                                    \n" +
                "    /  /    #     #                           ##                                              ##              /  /    /                                                                                                  \n" +
                "   ## ##    #     #      /###      /###     ######## /##  ###  /###     /###          /###    ######         ## ##   /       /##  ###   ###  /###     /###      /###     /###      /###   ###  /###     /###      /##    \n" +
                "   ## ##    #     #     / ###  /  / #### / ######## / ###  ###/ #### / / #### /      / ###  / #####          ## ##  /       / ###  ###   ###/ #### / / ###  /  / #### / / #### /  / ###  / ###/ #### / / ###  /  / ###   \n" +
                "   ## ##    #     #    /   ###/  ##  ###/     ##   /   ###  ##   ###/ ##  ###/      /   ###/  ##             ## ###/       /   ###  ##    ##   ###/ /   ###/  ##  ###/ ##  ###/  /   ###/   ##   ###/ /   ###/  /   ###  \n" +
                "   ## ##    #     #   ##    ##  ####          ##  ##    ### ##       ####          ##    ##   ##             ## ##  ###   ##    ### ##    ##    ## ##    ##  ####     ####      ##    ##    ##    ## ##        ##    ### \n" +
                "   ## ##    #     ##  ##    ##    ###         ##  ########  ##         ###         ##    ##   ##             ## ##    ##  ########  ##    ##    ## ##    ##    ###      ###     ##    ##    ##    ## ##        ########  \n" +
                "   #  ##    #     ##  ##    ##      ###       ##  #######   ##           ###       ##    ##   ##             #  ##    ##  #######   ##    ##    ## ##    ##      ###      ###   ##    ##    ##    ## ##        #######   \n" +
                "      /     #      ## ##    ##        ###     ##  ##        ##             ###     ##    ##   ##                /     ##  ##        ##    ##    ## ##    ##        ###      ### ##    ##    ##    ## ##        ##        \n" +
                "  /##/      #      ## ##    /#   /###  ##     ##  ####    / ##        /###  ##     ##    ##   ##            /##/      ### ####    / ##    ##    ## ##    /#   /###  ## /###  ## ##    /#    ##    ## ###     / ####    / \n" +
                " /  #####           ## ####/ ## / #### /      ##   ######/  ###      / #### /       ######    ##           /  ####    ##   ######/  ### / ###   ### ####/ ## / #### / / #### /   ####/ ##   ###   ### ######/   ######/  \n" +
                "/     ##                ###   ##   ###/        ##   #####    ###        ###/         ####      ##         /    ##     #     #####    ##/   ###   ### ###   ##   ###/     ###/     ###   ##   ###   ### #####     #####   \n" +
                "#                                                                                                         #                                                                                                              \n" +
                " ##                                                                                                        ##                                                                                                            ");
        Color.RESET.setColor();
        vSpace(4);
        System.out.println(
                "                                                                   _____   ______ _______ _______ _______      _______ __   _ _______ _______  ______\n" +
                        "                                                                  |_____] |_____/ |______ |______ |______      |______ | \\  |    |    |______ |_____/\n" +
                        "                                                                  |       |    \\_ |______ ______| ______|      |______ |  \\_|    |    |______ |    \\_");
    }

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
                System.out.print(hSpace(90) + "Enter Nickname: ");
                nickname = in.nextLine();
            } else {
                printTitle();
                vSpace(3);
                System.out.println(hSpace(90) + "Enter Nickname: " + nickname);
                vSpace(1);
                System.out.print(hSpace(90) + "Enter IP-Address: ");
                in.nextLine();
                //ipAddress = in.nextLine();
            }
        }
    }


    //----------PERSONAL-BOARD----------//

    public void printPersonalBoard(Player player) {

        printFaithTrack(player.getPersonalBoard().getFaithTrack());

        vSpace(1);

        printWarehouse(player.getPersonalBoard().getWarehouseDepots());

        vSpace(1);

        printDevelopmentCardSlotsAndStrongbox(player.getPersonalBoard().getDevelopmentCardsSlots(), player.getPersonalBoard().getStrongbox());

        printLeaderCardsInPersonalBoard(player.getLeaderCards());
    }

    private void printLeaderCardsInPersonalBoard(List<LeaderCard> leaderCards) {
        System.out.print(hSpace(200));
        for (int i = 0; i < leaderCards.size(); i++) System.out.print(",----------." + hSpace(5));
        vSpace(1);
        System.out.print(hSpace(200));
        for (int i = 0; i < leaderCards.size(); i++) System.out.print("|          |" + hSpace(5));
        vSpace(1);
        System.out.print(hSpace(200));
        for (int i = 0; i < leaderCards.size(); i++) System.out.print("| Leader " + (i + 1) + " |" + hSpace(5));
        vSpace(1);
        System.out.print(hSpace(200));
        for (int i = 0; i < leaderCards.size(); i++) System.out.print("|          |" + hSpace(5));
        vSpace(1);
        System.out.print(hSpace(200));
        for (int i = 0; i < leaderCards.size(); i++) System.out.print("|          |" + hSpace(5));
        vSpace(1);
        System.out.print(hSpace(200));
        for (LeaderCard leaderCard : leaderCards)
            System.out.print("| " + isLeaderCardActive(leaderCard) + " |" + hSpace(5));
        vSpace(1);
        System.out.print(hSpace(200));
        for (int i = 0; i < leaderCards.size(); i++) System.out.print("|          |" + hSpace(5));
        vSpace(1);
        System.out.print(hSpace(200));
        for (int i = 0; i < leaderCards.size(); i++) System.out.print("'----------'" + hSpace(5));
        vSpace(1);
    }

    private String isLeaderCardActive(LeaderCard leaderCard) {
        if (leaderCard.isActive()) return " ACTIVE ";
        else return "INACTIVE";
    }

    public void printPlayerLeaderCards(List<LeaderCard> leaderCards) {
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
        vSpace(1);
    }


    //----------STRONGBOX----------//

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

        }
        return s;
    }


    //----------DEVELOPMENT-CARD-SLOTS----------// AL MOMENTO TI STAMPA PURE LO STRONGBOX

    private void printDevelopmentCardSlotsAndStrongbox(List<List<DevelopmentCard>> developmentSlots, List<Resource> strongbox) {
        for (int j = 0; j <= 15; j++)
            System.out.println(
                    printStrongbox(j, strongbox) + hSpace(20) + "\u2502" + hSpace(8) + printDevelopmentSlotCard(developmentSlots.get(0), j) +
                            hSpace(5) + "\u2502" + hSpace(8) + printDevelopmentSlotCard(developmentSlots.get(1), j) +
                            hSpace(5) + "\u2502" + hSpace(8) + printDevelopmentSlotCard(developmentSlots.get(2), j) + hSpace(5) + "\u2502");
        vSpace(1);
    }


    /**
     * It has the same function of the printDevelopmentGridCard() method, but instead prints the cards on the player board
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


    //----------WAREHOUSE----------//

    private void printWarehouse(List<Depot> warehouseDepots) {
        System.out.println(hSpace(8) + "        ________\n" + hSpace(10) +
                "  ,=='        `==.");
        System.out.println(hSpace(8) + "  ╭/ " + printDepot(warehouseDepots.get(0)) + " \\╮");
        System.out.println(hSpace(8) + "  || ______________ ||\n" + hSpace(10) +
                "|)                ||");
        System.out.println(hSpace(8) + "  || " + printDepot(warehouseDepots.get(1)) + " ||");
        System.out.println(hSpace(8) + "  || ______________ (|\n" + hSpace(10) +
                "||                ||");
        System.out.println(hSpace(8) + "  )| " + printDepot(warehouseDepots.get(2)) + " ||");
        System.out.println(hSpace(8) + "  || ______________ ||\n" + hSpace(10) +
                "||                ||\n" + hSpace(10) +
                "||___.-__;----.___||");
    }

    private String printDepot(Depot depot) {
        StringBuilder s = new StringBuilder();
        List<Resource> resources = new ArrayList<>(depot.getResources());

        if (resources.size() == 0) return hSpace(14);

        s.append(hSpace(6 - (2 * resources.size())));
        for (Resource resource : resources) s.append(pCS("  \u2588\u2588", resource.getColor()));
        s.append(hSpace(8 - (2 * resources.size())));
        return s.toString();
    }


    //----------FAITH-TRACK----------//

    private void printFaithTrack(FaithTrack faithTrack) {
        System.out.println(
                "                                    +-------+-------+-------+-------+-------+-------+                               +-------+-------+-------+-------+-------+-------+-------+\n" +
                        "                                    |       |       |       |       |       |       |                               |       |       |       |       |       |       |       |");
        firstLine(faithTrack.getFaithPoints());
        System.out.println(
                "                                    |       |       |       |       |       |       |                               |       |       |       |       |       |       |       |\n" +
                        "                                    +-------+-------+-------+-------+-------+-------+                               +-------+-------+-------+-------+-------+-------+-------+\n" +
                        "                                    |       |                               |       |                               |       |");
        midLine(faithTrack.getFaithPoints());
        System.out.println(
                "                                    |       |                               |       |                               |       |\n" +
                        "                    +-------+-------+-------+                               +-------+-------+-------+-------+-------+-------+\n" +
                        "                    |       |       |       |                               |       |       |       |       |       |       |");
        lastine(faithTrack.getFaithPoints());
        System.out.println(
                "                    |       |       |       |                               |       |       |       |       |       |       |\n" +
                        "                    +-------+-------+-------+                               +-------+-------+-------+-------+-------+-------+");
    }

    private void printCross() {
        System.out.print("  \u001b[41;1m \u2020 \u001b[0m  ");
    }

    private void printRow(int start, int end, int fp) {
        for (int i = start; i <= end; i++) {
            if (i != fp) System.out.print(hSpace(7));
            else printCross();
            System.out.print("|");
        }
    }

    private void firstLine(int fp) {
        System.out.print(hSpace(36) + "|");
        printRow(4, 9, fp);
        System.out.print(hSpace(31) + "|");
        printRow(18, 24, fp);
        vSpace(1);
    }

    private void midLine(int fp) {
        System.out.print(hSpace(36) + "|");
        printRow(3, 3, fp);
        System.out.print(hSpace(31) + "|");
        printRow(10, 10, fp);
        System.out.print(hSpace(31) + "|");
        printRow(17, 17, fp);
        vSpace(1);
    }

    private void lastine(int fp) {
        System.out.print(hSpace(20) + "|");
        printRow(0, 2, fp);
        System.out.print(hSpace(31) + "|");
        printRow(11, 16, fp);
        vSpace(1);
    }


    //--------------------------//
    //          MARKET          //
    //--------------------------//

    //   .d88b.
    //   8b88d8    marble protothype
    //   `Y88P'

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

    private void printMarketMarbleRows(MarketTray marketTray) {
        for (int i = 2; i >= 0; i--) {
            Resource[] resources = marketTray.getMarblesOnRow(i);
            System.out.println(
                    "         |           |    " + pCS("___", resources[3].getColor()) + "  " + pCS("___", resources[2].getColor()) + "  " + pCS("___", resources[1].getColor()) + "  " + pCS("___", resources[0].getColor()) + "    |\n" +
                            "         |           |   " + pCS("/   \\", resources[3].getColor()) + pCS("/   \\", resources[2].getColor()) + pCS("/   \\", resources[1].getColor()) + pCS("/   \\", resources[0].getColor()) + "   | <-----\n" +
                            "         |           |   " + pCS("\\___/", resources[3].getColor()) + pCS("\\___/", resources[2].getColor()) + pCS("\\___/", resources[1].getColor()) + pCS("\\___/", resources[0].getColor()) + "   |");
        }
    }

    private void printMarbleOnSlide(MarketTray marketTray) {
        Color marbleOnSlideColor = marketTray.getMarbleOnSlide().getColor();
        System.out.println(
                "            /                             " + pCS("___", marbleOnSlideColor) + "   |\n" +
                        "           /                             " + pCS("/   \\", marbleOnSlideColor) + "  |\n" +
                        "          /                              " + pCS("\\___/", marbleOnSlideColor) + "  |");
    }

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


    //----------DEVELOPMENT-CARD-GRID----------//

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
     * Used to print the DevelopmentGridCard.
     *
     * @param developmentGridCell If the cell contains one or more cards, print the one on the top. Otherwise, leave a blank space.
     * @param cardLineToPrint     Each row of the card is printed individually to provide maximum control over variations of the grid structure
     * @return One line of the grid
     */
    private String printDevelopmentGridCard(DevelopmentGridCell developmentGridCell, int cardLineToPrint) {
        if (!developmentGridCell.isEmpty())
            return printDevelopmentCard(developmentGridCell.getFirstCard(), cardLineToPrint, developmentGridCell.getDevelopmentCardsSize());
        return printEmptyDevelopmentCardSpace();
    }


    //----------DEVELOPMENT-CARDS----------//

    /**
     * Used to print DevelopmentCards.
     *
     * @param developmentCard The desired card to print
     * @param cardLineToPrint Each row of the card is printed individually to provide maximum control over variations of the grid structure
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

    private String printEmptyDevelopmentCardSpace() {
        return hSpace(26);
    }

    private String printHorizontalBorder(int numberOfCards) {
        return "\u2502".repeat(Math.max(0, numberOfCards - 1)) + hSpace(4 - numberOfCards);
    }

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

    private String printDevelopmentCardLevel(DevelopmentCard developmentCard) {
        String s = "| ";

        s = s + printLevelDots(developmentCard.getDevelopmentCardType().getLevel().getLevelNumber(), developmentCard.getDevelopmentCardType().getColor());
        s = s + hSpace(8 - (2 * developmentCard.getDevelopmentCardType().getLevel().getLevelNumber())) + "lvl   " + hSpace(6 - (2 * developmentCard.getDevelopmentCardType().getLevel().getLevelNumber()));
        s = s + printLevelDots(developmentCard.getDevelopmentCardType().getLevel().getLevelNumber(), developmentCard.getDevelopmentCardType().getColor());
        s = s + "|";
        return s;
    }

    private String printLevelDots(int level, Color color) {
        return pCS("\u25CF ", color).repeat(Math.max(0, level));
    }

    /**
     * Used to print the Production section of a DevelopmentCard.
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

    private String printFirstProductionRow(List<Resource> requiredResources, List<Integer> numberOfRequiredResources, List<Resource> producedResources, List<Integer> numberOfProducedResources) {
        String s = "| |  ";

        if (requiredResources.size() == 1) s = s + hSpace(10);
        else s = s + numberOfRequiredResources.get(0) + pCS(" \u25A0", requiredResources.get(0).getColor()) + hSpace(7);

        if (producedResources.size() == 1) s = s + hSpace(5) + "| |";
        else s = s + numberOfProducedResources.get(0) + pCS(" \u25A0  ", producedResources.get(0).getColor()) + "| |";

        return s;
    }

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

    private String printCardVictoryPoints(DevelopmentCard developmentCard) {
        String s = "";
        s = s + "|" + hSpace(10) + developmentCard.getVictoryPoints() + hSpace(10 - (developmentCard.getVictoryPoints() / 10)) + "|";
        return s;
    }


    //----------LEADER-CARDS----------//

    //TEMPORARY SOLUTION
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

        /*Debug only*/ System.out.println("You selected Leader " + (leaderCards.indexOf(selectedLeaders.get(0)) + 1) + " and Leader " + (leaderCards.indexOf(selectedLeaders.get(1)) + 1));

        return selectedLeaders;
    }

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

    private String printLeaderRequirements(LeaderCard leaderCard) {
        if (leaderCard.getDevelopmentCardRequirements().keySet().size() == 0)
            return printLeaderResourcesRequirements(leaderCard.getResourcesRequirements());
        else return printLeaderDevelopmentCardRequirements(leaderCard.getDevelopmentCardRequirements());
    }

    private String printLeaderDevelopmentCardRequirements(HashMap<DevelopmentCardType, Integer> requirements) {
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

    private String printLeaderResourcesRequirements(HashMap<Resource, Integer> requirements) {
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


    //----------CLI-UTILS----------//

    private void vSpace(int spaces) {
        for (int i = 0; i < spaces; i++) System.out.println();
    }

    private String hSpace(int spaces) {
        return " ".repeat(Math.max(0, spaces));
    }

    public void cls() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * pCS = print colored string
     * Returns a colored String
     *
     * @param string the string to be colored
     * @param color  the color
     * @return the colored string
     */
    private String pCS(String string, Color color) {
        return color + string + Color.RESET;
    }

}
