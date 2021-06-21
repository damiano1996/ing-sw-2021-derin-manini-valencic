package it.polimi.ingsw.psp26.view.cli.manualtesting;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.view.cli.CLI;
import it.polimi.ingsw.psp26.view.cli.CliUtils;
import it.polimi.ingsw.psp26.view.cli.MarketCli;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class used to test MarketCli related methods.
 */
public class TestDisplayMarket {

    CLI cli;
    CliUtils cliUtils;
    MarketCli marketCli;
    Scanner in;
    PrintWriter pw;

    public TestDisplayMarket() {
        pw = new PrintWriter(System.out);
        in = new Scanner(System.in);
        marketCli = new MarketCli(pw);
        cliUtils = new CliUtils(pw);
        cli = new CLI();
    }


    /**
     * Used to launch tests.
     */
    public static void main(String[] args) throws CanNotAddResourceToDepotException {
        TestDisplayMarket testDisplayMarket = new TestDisplayMarket();
        testDisplayMarket.testMethod();
    }


    /**
     * Call here the methods to test.
     */
    public void testMethod() throws CanNotAddResourceToDepotException {
        testMarket();
        testPrintPlayerResources();
        testWarehouseConfiguration();
    }


    /**
     * Test the ability to display the MarketTray.
     */
    private void testMarket() {

        //---MARKET-TEST---// Press Enter 3 times

        MarketTray marketTray = new MarketTray(new VirtualView());

        cliUtils.cls();
        marketCli.displayMarketScreen(marketTray);
        in.nextLine();

        marketTray.pushMarbleFromSlideToRow(1);
        cliUtils.cls();
        marketCli.displayMarketScreen(marketTray);
        in.nextLine();

        marketTray.pushMarbleFromSlideToColumn(3);
        cliUtils.cls();
        marketCli.displayMarketScreen(marketTray);
        in.nextLine();
    }


    /**
     * Test the ability to display Player's Resources.
     */
    private void testPrintPlayerResources() {

        //---PLAYER-RESOURCES-TEST---// Press Enter 1 time

        cliUtils.cls();

        List<Resource> resources = new ArrayList<>();
        resources.add(Resource.STONE);
        resources.add(Resource.COIN);
        resources.add(Resource.COIN);
        resources.add(Resource.SHIELD);
        resources.add(Resource.SHIELD);
        resources.add(Resource.SERVANT);
        resources.add(Resource.SHIELD);
        resources.add(Resource.COIN);
        resources.add(Resource.STONE);
        resources.add(Resource.STONE);

        cliUtils.printPlayerResources(resources, 10, 10);
        in.nextLine();
    }


    /**
     * Test the ability to rearrange the Warehouse.
     */
    private void testWarehouseConfiguration() throws CanNotAddResourceToDepotException {

        //---WAREHOUSE-CONFIGURATION-TEST---// Follow terminal instructions

        VirtualView virtualView = new VirtualView();
        PersonalBoard personalBoard = new PersonalBoard(virtualView, "player");
        List<Resource> resources = new ArrayList<>();

        personalBoard.getWarehouse().addLeaderDepot(new LeaderDepot(virtualView, Resource.COIN));
        personalBoard.getWarehouse().addLeaderDepot(new LeaderDepot(virtualView, Resource.SHIELD));

        personalBoard.getWarehouse().addResourceToDepot(1, Resource.STONE);
        personalBoard.getWarehouse().addResourceToDepot(2, Resource.SHIELD);
        personalBoard.getWarehouse().addResourceToDepot(2, Resource.SHIELD);

        resources.add(Resource.STONE);
        resources.add(Resource.STONE);
        resources.add(Resource.COIN);
        resources.add(Resource.SHIELD);
        resources.add(Resource.COIN);
        resources.add(Resource.COIN);
        resources.add(Resource.SERVANT);

        try {
            cli.displayWarehouseNewResourcesAssignment(personalBoard.getWarehouse(), resources);
        } catch (Exception ignored) {
        }
        in.nextLine();
    }

}
