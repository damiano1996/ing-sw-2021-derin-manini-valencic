package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DepotCli {

    private final PrintWriter pw;
    private final CliUtils cliUtils;

    public DepotCli() {
        this.cliUtils = new CliUtils();
        this.pw = new PrintWriter(System.out);
    }

    /**
     * Used to print the Strongbox
     *
     * @param strongbox      The Strongbox Resources to print
     * @param startingRow    The starting row where the Strongbox is going to be printed
     * @param startingColumn The starting column where the Strongbox is going to be printed
     */
    public void displayStrongbox(List<Resource> strongbox, int startingRow, int startingColumn) {
        cliUtils.printFigure("Strongbox", startingRow, startingColumn);

        printStrongboxResources(Resource.STONE, getStrongboxResourcesNumber(strongbox, Resource.STONE), startingRow, startingColumn);
        printStrongboxResources(Resource.SERVANT, getStrongboxResourcesNumber(strongbox, Resource.SERVANT), startingRow, startingColumn);
        printStrongboxResources(Resource.SHIELD, getStrongboxResourcesNumber(strongbox, Resource.SHIELD), startingRow, startingColumn);
        printStrongboxResources(Resource.COIN, getStrongboxResourcesNumber(strongbox, Resource.COIN), startingRow, startingColumn);

        cliUtils.setCursorBottomLeft();
    }

    /**
     * Prints the Warehouse and the Resources in it
     *
     * @param warehouseDepots A list of Depots that will be printed
     * @param startingRow     The starting row where the Warehouse is going to be printed
     * @param startingColumn  The starting column where the Warehouse is going to be printed
     */
    public void printWarehouse(List<Depot> warehouseDepots, int startingRow, int startingColumn) {
        cliUtils.printFigure("Warehouse", startingRow, startingColumn);
        for (int i = 0; i < 3; i++) printDepot(warehouseDepots.get(i), startingRow, startingColumn, i + 1);

        cliUtils.setCursorBottomLeft();
    }

    /**
     * Show the screen that appears after getting Resources from the Market
     *
     * @param warehouseDepots The Warehouse to print
     * @param resources       The resources get prom the Market to insert into the Warehouse
     */
    public void displayMarketResourcesSelection(List<Depot> warehouseDepots, List<Resource> resources) { //TODO temporary solution
        Scanner in = new Scanner(System.in);
        cliUtils.cls();
        pw.flush();

        cliUtils.printFigure("WarehouseConfigurationTitle", 1, 21);

        printWarehouse(warehouseDepots, 7, 74);

        cliUtils.setCursorPosition(22, 74);
        pw.print("Resources left to insert:");
        for (Resource resource : resources)
            pw.print(cliUtils.hSpace(8) + cliUtils.pCS("\u2588\u2588  ", resource.getColor()));
        pw.flush();

        cliUtils.setCursorPosition(30, 21);
        pw.println("Please type the ResourceType and the number of the Depot in which you want to store it.");
        pw.print(cliUtils.hSpace(20) + "The Resources that can't be added to the Warehouse will be discarded.");
        pw.flush();

        //Possibile soluzione soggetta a futuri cambiamenti
        cliUtils.vSpace(1);
        pw.println(cliUtils.hSpace(20) + cliUtils.pCS("[RESOURCETYPE - DEPOTNUMBER]", Color.GREY));
        cliUtils.vSpace(1);
        pw.print("\u001b[20C"); //moving cursor right of 20 spaces

        pw.flush();

        in.nextLine(); //temporary solution
    }

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
     * Prints the Resources contained in the Strongbox
     *
     * @param resourceType    The Resource contained
     * @param resourceNumbers The number of the Resource contained
     * @param startingRow     The starting row of the Strongbox
     * @param startingColumn  The starting column of the Strongbox
     */
    private void printStrongboxResources(Resource resourceType, int resourceNumbers, int startingRow, int startingColumn) {
        switch (resourceType) {
            case SHIELD:
                cliUtils.setCursorPosition(startingRow + 9, startingColumn + 11);
                break;

            case STONE:
                cliUtils.setCursorPosition(startingRow + 9, startingColumn + 24);
                break;

            case COIN:
                cliUtils.setCursorPosition(startingRow + 11, startingColumn + 11);
                break;

            case SERVANT:
                cliUtils.setCursorPosition(startingRow + 11, startingColumn + 24);
                break;

            default:
                break;
        }

        pw.print(cliUtils.pCS("\u2588\u2588", resourceType.getColor()) + "  " + resourceNumbers);
        pw.flush();
    }

    /**
     * Used in printWarehouse() to get the correct representation of Resources
     *
     * @param depot          The current Depot that will be printed
     * @param startingRow    The starting row where the Warehouse is going to be printed
     * @param startingColumn The starting column where the Warehouse is going to be printed
     */
    private void printDepot(Depot depot, int startingRow, int startingColumn, int depotNumber) {
        cliUtils.setCursorPosition(startingRow + ((3 * depotNumber) - 1), startingColumn + 3);

        cliUtils.saveCursorPosition();
        cliUtils.moveCursor("rg", 20);
        pw.print(cliUtils.pCS("Depot " + depotNumber, Color.GREY));
        pw.flush();
        cliUtils.restoreCursorPosition();

        List<Resource> resources = new ArrayList<>(depot.getResources());

        if (resources.size() > 0) {

            StringBuilder s = new StringBuilder();

            s.append(cliUtils.hSpace(6 - (2 * resources.size())));
            for (Resource resource : resources) s.append(cliUtils.pCS("  \u2588\u2588", resource.getColor()));
            s.append(cliUtils.hSpace(8 - (2 * resources.size())));

            pw.print(s.toString());
            pw.flush();
        }
    }

}
