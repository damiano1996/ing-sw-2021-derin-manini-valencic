package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;

import java.io.PrintWriter;
import java.util.List;

/**
 * Class that contains all the methods related to display the Warehouse.
 */
public class DepotCli {

    private final PrintWriter pw;
    private final CliUtils cliUtils;

    /**
     * Constructor of the class.
     * It creates a new PrintWriter and a new CliUtils to use.
     *
     * @param pw the PrintWriter to use
     */
    public DepotCli(PrintWriter pw) {
        this.pw = pw;
        this.cliUtils = new CliUtils(pw);
    }


    /**
     * Used to print the Strongbox.
     * Prints each Resource type and the number of the corresponding Resources the Player has in its Strongbox.
     *
     * @param strongbox      the Strongbox Resources to print
     * @param startingRow    the starting row where the Strongbox is going to be printed
     * @param startingColumn the starting column where the Strongbox is going to be printed
     */
    public void displayStrongbox(List<Resource> strongbox, int startingRow, int startingColumn) {
        cliUtils.printFigure("Strongbox", startingRow, startingColumn);

        printStrongboxResources(Resource.STONE, cliUtils.getResourcesNumber(strongbox, Resource.STONE), startingRow, startingColumn);
        printStrongboxResources(Resource.SERVANT, cliUtils.getResourcesNumber(strongbox, Resource.SERVANT), startingRow, startingColumn);
        printStrongboxResources(Resource.SHIELD, cliUtils.getResourcesNumber(strongbox, Resource.SHIELD), startingRow, startingColumn);
        printStrongboxResources(Resource.COIN, cliUtils.getResourcesNumber(strongbox, Resource.COIN), startingRow, startingColumn);
    }


    /**
     * Prints the Warehouse and the Resources in it.
     *
     * @param warehouse      the Warehouse to print
     * @param startingRow    the starting row where the Warehouse is going to be printed
     * @param startingColumn the starting column where the Warehouse is going to be printed
     */
    public void printWarehouse(Warehouse warehouse, int startingRow, int startingColumn) {
        cliUtils.printFigure("Warehouse", startingRow, startingColumn);
        for (int i = 0; i < warehouse.getBaseDepots().size(); i++)
            printDepot(warehouse.getBaseDepots().get(i), startingRow, startingColumn, i + 1);
    }


    /**
     * Prints the Resources contained in the Strongbox.
     *
     * @param resourceType    the Resource contained
     * @param resourceNumbers the number of the Resource contained
     * @param startingRow     the starting row of the Strongbox
     * @param startingColumn  the starting column of the Strongbox
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
     * Used in printWarehouse() to get the correct representation of Resources.
     * The position of the Resources contained in the given Depot is aligned considering the number of Resources contained in it.
     * It also prints the name of the Depot next to it.
     *
     * @param depot          depot to print
     * @param startingRow    the starting row where the Warehouse is going to be printed
     * @param startingColumn the starting column where the Warehouse is going to be printed
     */
    private void printDepot(Depot depot, int startingRow, int startingColumn, int depotNumber) {
        cliUtils.pPCS("Depot" + depotNumber, Color.GREY, startingRow + ((3 * depotNumber) - 1), startingColumn + 23);
        cliUtils.setCursorPosition(startingRow + ((3 * depotNumber) - 1), startingColumn + 3);

        List<Resource> resources = depot.getResources();

        if (resources.size() > 0) {
            StringBuilder s = new StringBuilder();

            s.append(cliUtils.hSpace(6 - (2 * resources.size())));
            for (Resource resource : resources) s.append(cliUtils.pCS("  \u2588\u2588", resource.getColor()));
            s.append(cliUtils.hSpace(8 - (2 * resources.size())));

            pw.print(s);
            pw.flush();
        }
    }

}
