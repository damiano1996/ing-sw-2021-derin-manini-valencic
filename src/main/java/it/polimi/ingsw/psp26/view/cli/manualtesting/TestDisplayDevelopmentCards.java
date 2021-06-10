package it.polimi.ingsw.psp26.view.cli.manualtesting;

import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.view.cli.CLI;
import it.polimi.ingsw.psp26.view.cli.CliUtils;
import it.polimi.ingsw.psp26.view.cli.DevelopmentCardsCli;

import java.io.PrintWriter;
import java.util.Scanner;

public class TestDisplayDevelopmentCards {

    CLI cli;
    DevelopmentCardsCli developmentCardsCli;
    CliUtils cliUtils;
    Scanner in;
    PrintWriter pw;

    public TestDisplayDevelopmentCards() {
        pw = new PrintWriter(System.out);
        in = new Scanner(System.in);
        cliUtils = new CliUtils(pw);
        developmentCardsCli = new DevelopmentCardsCli(pw);
        cli = new CLI();
    }


    /**
     * Used to launch tests
     */
    public static void main(String[] args) throws ColorDoesNotExistException, NoMoreDevelopmentCardsException, LevelDoesNotExistException {
        TestDisplayDevelopmentCards testDisplayDevelopmentCards = new TestDisplayDevelopmentCards();
        testDisplayDevelopmentCards.testMethod();
    }


    /**
     * Call here the methods to test
     */
    public void testMethod() throws ColorDoesNotExistException, NoMoreDevelopmentCardsException, LevelDoesNotExistException {
        testDevelopmentGrid();
    }


    /**
     * Test the ability to display the Development Grid
     */
    private void testDevelopmentGrid() throws ColorDoesNotExistException, NoMoreDevelopmentCardsException, LevelDoesNotExistException {

        //---DEVELOPMENT-GRID-SHOW-ALL-CARDS-TEST---// Press Enter 4 times

        DevelopmentCardsGrid developmentCardsGrid = new DevelopmentCardsGrid(new VirtualView());

        for (int i = 0; i < 4; i++) {
            cliUtils.cls();
            developmentCardsCli.displayDevelopmentGrid(developmentCardsGrid);
            in.nextLine();
            developmentCardsGrid.drawCard(Color.PURPLE, Level.FIRST);
            developmentCardsGrid.drawCard(Color.PURPLE, Level.SECOND);
            developmentCardsGrid.drawCard(Color.PURPLE, Level.THIRD);
            developmentCardsGrid.drawCard(Color.BLUE, Level.FIRST);
            developmentCardsGrid.drawCard(Color.BLUE, Level.SECOND);
            developmentCardsGrid.drawCard(Color.BLUE, Level.THIRD);
            developmentCardsGrid.drawCard(Color.YELLOW, Level.FIRST);
            developmentCardsGrid.drawCard(Color.YELLOW, Level.SECOND);
            developmentCardsGrid.drawCard(Color.YELLOW, Level.THIRD);
            developmentCardsGrid.drawCard(Color.GREEN, Level.FIRST);
            developmentCardsGrid.drawCard(Color.GREEN, Level.SECOND);
            developmentCardsGrid.drawCard(Color.GREEN, Level.THIRD);
        }
    }

}
