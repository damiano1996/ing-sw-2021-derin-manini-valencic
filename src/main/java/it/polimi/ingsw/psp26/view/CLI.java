package it.polimi.ingsw.psp26.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGridCell;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;

public class CLI {

    //CLI testing
    public static void main(String[] args) throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException {
        CLI cli = new CLI();
        FaithTrack faithTrack = new FaithTrack();
        MarketTray marketTray = new MarketTray();
        DevelopmentGrid developmentGrid = new DevelopmentGrid();
        Scanner in = new Scanner(System.in);


        //DevelpmentGridTest
        cli.cls();
        developmentGrid.drawCard(Color.GREEN, Level.SECOND);
        //developmentGrid.drawCard(Color.GREEN, Level.SECOND);
        /*developmentGrid.drawCard(Color.GREEN, Level.SECOND);
        developmentGrid.drawCard(Color.GREEN, Level.SECOND);
        */
        developmentGrid.drawCard(Color.PURPLE, Level.SECOND);
        developmentGrid.drawCard(Color.PURPLE, Level.SECOND);
        developmentGrid.drawCard(Color.PURPLE, Level.SECOND);

        /*developmentGrid.drawCard(Color.PURPLE, Level.SECOND);
        developmentGrid.drawCard(Color.YELLOW, Level.THIRD);
        developmentGrid.drawCard(Color.YELLOW, Level.THIRD);
        developmentGrid.drawCard(Color.YELLOW, Level.THIRD);
        developmentGrid.drawCard(Color.YELLOW, Level.THIRD);*/

        cli.printDevelopmentGrid(developmentGrid);
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



    //----------FAITH-TRACK----------//

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



    //----------DEVELOPMENT-CARD-GRID----------//

    public void printDevelopmentGrid(DevelopmentGrid developmentGrid) {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j <= 13; j++) System.out.println(hSpace(12) + printDevelopmentGridCard(developmentGrid.getDevelopmentGridCell(i,0), j) +
                                                             hSpace(12) + printDevelopmentGridCard(developmentGrid.getDevelopmentGridCell(i,1), j) +
                                                             hSpace(12) + printDevelopmentGridCard(developmentGrid.getDevelopmentGridCell(i,2), j) +
                                                             hSpace(12) + printDevelopmentGridCard(developmentGrid.getDevelopmentGridCell(i,3), j));
            vSpace(1);
        }
    }

    private String printDevelopmentGridCard(DevelopmentGridCell developmentGridCell, int cardLineToPrint) {
        String s = "";

        if (!developmentGridCell.isEmpty()) {
            switch (cardLineToPrint) {
                case 0:
                    s = ",---------------------.";
                    break;

                case 1:
                    s = printDevelopmentCardRequirements(developmentGridCell.getFirstCard());
                    break;

                case 2:
                    s = "| " + pCS(".-----------------.", developmentGridCell.getFirstCard().getDevelopmentCardType().getColor()) + " |";
                    break;

                case 3:
                    s = printDevelopmentCardLevel(developmentGridCell.getFirstCard());
                    break;

                case 4:
                    s = "| " + pCS("`-----------------'", developmentGridCell.getFirstCard().getDevelopmentCardType().getColor()) + " |";
                    break;

                case 5:
                case 11:
                    s = "|                     |";
                    break;

                case 6:
                    s = "| .-----------------. |";
                    break;

                case 7:
                    s = printProductionLines(developmentGridCell.getFirstCard(), 1);
                    break;

                case 8:
                    s = printProductionLines(developmentGridCell.getFirstCard(), 2);
                    break;

                case 9:
                    s = printProductionLines(developmentGridCell.getFirstCard(), 3);
                    break;

                case 10:
                    s = "| |_________________| |";
                    break;

                case 12:
                    s = printCardVictoryPoints(developmentGridCell.getFirstCard());
                    break;

                case 13:
                    s = "`---------------------'";
                    break;

                default:
                    break;
            }
            return s;
        }
        return hSpace(23);
    }

    //private String printHorizontalBorder(int numberOfCards) {
    //    return "\u2502".repeat(Math.max(0, numberOfCards));
    //}

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

    /**SCRIVI MEGLIO STO COMMENTO
     * Transform the HashMaps in Lists and use them to print the Production
     * Use the line parameter to print all the Production
     * @param developmentCard the cart from which print the Production
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

        if (line == 1) return printFirstProductionRow(requiredResources, numberOfRequiredResources, producedResources, numberOfProducedResources);
        else if (line == 2) return printSecondProductionRow(requiredResources, numberOfRequiredResources, producedResources, numberOfProducedResources);
        else return printThirdProductionRow(requiredResources, numberOfRequiredResources, producedResources, numberOfProducedResources);
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

        if (requiredResources.size() == 1) s = s + numberOfRequiredResources.get(0) + pCS(" \u25A0", requiredResources.get(0).getColor()) + "  -->  ";
        else s = s + "     -->  ";

        if (producedResources.size() == 1) s = s + numberOfProducedResources.get(0) + pCS(" \u25A0  ", producedResources.get(0).getColor()) + "| |";
        else if (producedResources.size() == 3) s = s + numberOfProducedResources.get(1) + pCS(" \u25A0  ", producedResources.get(1).getColor()) + "| |";
        else s = s + "     | |";

        return s;
    }

    private String printThirdProductionRow(List<Resource> requiredResources, List<Integer> numberOfRequiredResources, List<Resource> producedResources, List<Integer> numberOfProducedResources) {
        String s = "| |  ";

        if (requiredResources.size() == 2) s = s + numberOfRequiredResources.get(1) + pCS(" \u25A0", requiredResources.get(1).getColor()) + hSpace(7);
        else s = s + hSpace(10);

        if (producedResources.size() == 2) s = s + numberOfProducedResources.get(1) + pCS(" \u25A0  ", producedResources.get(1).getColor()) + "| |";
        else if (producedResources.size() == 3) s = s + numberOfProducedResources.get(2) + pCS(" \u25A0  ", producedResources.get(2).getColor()) + "| |";
        else s = s + "     | |";

        return s;
    }

    private String printCardVictoryPoints(DevelopmentCard developmentCard) {
        String s = "";
        s = s + "|" + hSpace(10) + developmentCard.getVictoryPoints() + hSpace(10 - (developmentCard.getVictoryPoints() / 10)) + "|";
        return s;
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
