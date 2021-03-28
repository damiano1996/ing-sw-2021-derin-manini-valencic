package it.polimi.ingsw.psp26.view;

import java.util.Scanner;

import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;

public class CLI {

    //CLI testing
    public static void main(String[] args) {
        CLI cli = new CLI();
        FaithTrack faithTrack = new FaithTrack();
        MarketTray marketTray = new MarketTray();
        Scanner in = new Scanner(System.in);


        //Market test (press enter 3 times)
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


        //Title test
        cli.printTitle();
        in.nextLine();


        //FaithTrack test (press enter 25 times)
        cli.cls();
        for (int i = 0; i < 25; i++) {
            cli.printFaithTrack(faithTrack);
            in.nextLine();
            faithTrack.moveMarkerPosition(1);
            cli.cls();
        }

    }



    //----------TITLE----------//

    public void printTitle() {
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



    //----------FAITHTRACK----------//

    public void printFaithTrack(FaithTrack faithTrack) {
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
        System.out.print("   \u001b[41;1m\u2020\u001b[0m   ");
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



    //----------MARKET----------//

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
