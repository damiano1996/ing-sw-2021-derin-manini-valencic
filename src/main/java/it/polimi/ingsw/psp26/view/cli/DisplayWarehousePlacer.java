package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.DepotOutOfBoundException;
import it.polimi.ingsw.psp26.exceptions.SkipResourceException;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DisplayWarehousePlacer {

    private final PrintWriter pw;
    private final CliUtils cliUtils;
    private final DepotCli depotCli;

    List<Resource> placedResources;
    List<Resource> discardedResources;

    //If the Player enters an incorrect index, ask it again
    boolean correctDepotIndexInserted;
    //If the Player chooses an invalid position, ask it again
    boolean resourceAdded;
    //If the player decides to discard the Resource, add it to discardedResources and proceed with the next Resource
    boolean discardResource;

    public DisplayWarehousePlacer(PrintWriter pw) {
        this.pw = pw;
        this.cliUtils = new CliUtils(pw);
        this.depotCli = new DepotCli(pw);

        placedResources = new ArrayList<>();
        discardedResources = new ArrayList<>();
    }


    /**
     * Show the screen that appears after getting Resources from the Market
     *
     * @param warehouse The Warehouse to print
     * @param resources The resources get prom the Market to insert into the Warehouse
     */
    public void displayMarketResourcesSelection(Warehouse warehouse, List<Resource> resources) {
        int depotIndex = 0;

        for (int i = 0; i < resources.size(); i++) {
            cliUtils.cls();

            printWarehouseAndResources(warehouse, resources, i);
            setParameters(false);

            while (!resourceAdded) {

                askForInput(resources.get(i).getName());

                while (!correctDepotIndexInserted) {
                    try {
                        depotIndex = getDepotIndex();
                        correctDepotIndexInserted = true;
                    } catch (SkipResourceException e) {
                        discardedResources.add(resources.get(i));
                        setParameters(true);
                    } catch (DepotOutOfBoundException e) {
                        cliUtils.pPCS("INDEX OUT OF BOUNDS! Insert Depot index again: ", Color.WHITE, 39, 21);
                    }
                }

                if (!discardResource) {
                    try {
                        warehouse.addResourceToDepot(depotIndex, resources.get(i)); //TODO non puoi usarlo nel client
                        placedResources.add(resources.get(i));
                        resourceAdded = true;
                    } catch (CanNotAddResourceToDepotException e) {
                        cliUtils.pPCS("RESOURCE CAN'T BE ADDED PLEASE TRY AGAIN          ", Color.WHITE, 39, 21);
                        correctDepotIndexInserted = false;
                    }
                }

            }

        }

        printFinalView(warehouse);
    }


    /**
     * Prints the given Resources
     *
     * @param resources     The Resources to print
     * @param startingIndex The index where the list starts to be printed
     */
    private void printResources(List<Resource> resources, int startingIndex, String listDescription) { //TODO mettere la lista delle risorse scartate a schermo
        cliUtils.pPCS(listDescription, Color.WHITE, 22, 74);
        for (int i = startingIndex; i < resources.size(); i++)
            pw.print(cliUtils.hSpace(8) + cliUtils.pCS("\u2588\u2588  ", resources.get(i).getColor()));
    }


    /**
     * Asks the Player the index where he wants to insert the Resource
     * If the Player enters 'q' skip the current Resource and add it to the discard list
     *
     * @return The index (an Integer between 0 and 2)
     * @throws DepotOutOfBoundException The index is not in the correct bounds
     * @throws SkipResourceException    The player skips this Resource
     */
    private int getDepotIndex() throws DepotOutOfBoundException, SkipResourceException {
        Scanner in = new Scanner(System.in);
        String depotString = in.nextLine();

        if (depotString.charAt(0) == 'q') throw new SkipResourceException();

        int depotIndex = Integer.parseInt(depotString) - 1;
        if (depotIndex >= 3 || depotIndex < 0) throw new DepotOutOfBoundException();
        return depotIndex;
    }


    /**
     * Prints the input phrases on screen
     *
     * @param resourceName The resource that the Player is currently positioning
     */
    private void askForInput(String resourceName) {
        cliUtils.setCursorPosition(30, 21);
        pw.println("Please type the Depot number where you want to store " + resourceName);
        pw.println(cliUtils.hSpace(20) + "The Resources that can't be added to the Warehouse will be discarded.");
        cliUtils.vSpace(1);
        pw.println(cliUtils.hSpace(20) + "Press 'q' if you want to discard the current Resource or you can't proceed any further.");
        cliUtils.vSpace(2);
        pw.print(cliUtils.hSpace(20) + "Depot number: ");
        pw.flush();
    }


    /**
     * Prints the Warehouse and the Resources to insert
     *
     * @param warehouse     The Player's Warehouse
     * @param resources     The Resources to insert
     * @param resourceIndex The index from where the resources are shown
     */
    private void printWarehouseAndResources(Warehouse warehouse, List<Resource> resources, int resourceIndex) {
        cliUtils.printFigure("/titles/WarehouseConfigurationTitle", 1, 21);
        depotCli.printWarehouse(warehouse, 7, 74);
        printResources(resources, resourceIndex, "Resources left to insert: ");
    }


    /**
     * Sets the parameters to control the insetion process
     */
    private void setParameters(boolean parameterValue) {
        correctDepotIndexInserted = parameterValue;
        resourceAdded = parameterValue;
        discardResource = parameterValue;
    }


    /**
     * Prints a final message before sending the Lists to the server
     *
     * @param warehouse The completed Warehouse
     */
    private void printFinalView(Warehouse warehouse) {
        cliUtils.cls();
        cliUtils.printFigure("/titles/WarehouseConfigurationTitle", 1, 21);
        depotCli.printWarehouse(warehouse, 7, 74);

        cliUtils.setCursorPosition(22, 21);
        pw.println("This is your final Warehouse configuration that will be send to server.");

        //debug
        cliUtils.vSpace(1);
        System.out.println(cliUtils.hSpace(20) + "Placed:      " + placedResources);
        System.out.println(cliUtils.hSpace(20) + "Discarded:   " + discardedResources);
    }
}
