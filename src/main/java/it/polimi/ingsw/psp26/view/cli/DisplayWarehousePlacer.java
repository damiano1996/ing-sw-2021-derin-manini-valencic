package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.ChangeResourcesBetweenDepotsException;
import it.polimi.ingsw.psp26.exceptions.DepotOutOfBoundException;
import it.polimi.ingsw.psp26.exceptions.SkipResourceException;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.utils.ViewUtils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DisplayWarehousePlacer {

    private final PrintWriter pw;
    private final CliUtils cliUtils;
    private final DepotCli depotCli;
    private final PersonalBoardCli personalBoardCli;
    private final List<Resource> discardedResources;

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
        this.personalBoardCli = new PersonalBoardCli(pw);
        discardedResources = new ArrayList<>();
    }


    /**
     * Show the screen that appears after getting Resources from the Market
     *
     * @param warehouse The Warehouse to print
     * @param resources The resources get prom the Market to insert into the Warehouse
     * @return The Resources placed in the Warehouse
     */
    public List<Resource> displayMarketResourcesSelection(Warehouse warehouse, List<Resource> resources) {
        discardedResources.clear();
        int depotIndex = 0;

        for (int i = 0; i < resources.size(); i++) {
            cliUtils.cls();

            printWarehouseAndResources(warehouse, resources, discardedResources, i);
            setParameters(false);

            while (!resourceAdded) {

                askForInput(warehouse.getAllDepots().size(), resources.get(i).getName().replaceAll("\\s+", ""));

                while (!correctDepotIndexInserted) {
                    try {
                        depotIndex = getDepotIndex(warehouse.getAllDepots().size());
                        correctDepotIndexInserted = true;
                    } catch (SkipResourceException e) {
                        discardedResources.add(resources.get(i));
                        setParameters(true);
                    } catch (DepotOutOfBoundException | NumberFormatException e) {
                        executeCatchDepotOutOfBound();
                    } catch (ChangeResourcesBetweenDepotsException e) {
                        changeResourcesDepots(warehouse, resources);
                        i--;
                        setParameters(true);
                    }
                }

                if (!discardResource) {
                    try {
                        warehouse.addResourceToDepot(depotIndex, resources.get(i));
                        resourceAdded = true;
                    } catch (CanNotAddResourceToDepotException e) {
                        executeCatchCanNotAddResourcesToDepot();
                    }
                }

            }

        }

        showFinalScreen(warehouse, resources, discardedResources, resources.size());
        return createListToSend(warehouse);
    }


    /**
     * Executes the corresponding catch in displayMarketResourcesSelection()
     */
    private void executeCatchDepotOutOfBound() {
        cliUtils.pPCS("WRONG INDEX INSERTED! Insert Depot index again: ", Color.RED, 38, 21);
        cliUtils.clearLine(40, 21);
        cliUtils.clearLine(38, 69);
    }


    /**
     * Executes the corresponding catch displayMarketResourcesSelection()
     */
    private void executeCatchCanNotAddResourcesToDepot() {
        cliUtils.pPCS("RESOURCE CAN'T BE ADDED! Please try again", Color.RED, 38, 21);
        cliUtils.clearLine(38, 62);
        cliUtils.clearLine(40, 35);
        correctDepotIndexInserted = false;
    }


    /**
     * Prints the Warehouse and the Resources to insert
     *
     * @param warehouse          The Player's Warehouse
     * @param resourcesLeft      The Resources to insert
     * @param resourcesDiscarded The Player's discarded Resources
     * @param resourceIndex      The index from where the resources are shown
     */
    private void printWarehouseAndResources(Warehouse warehouse, List<Resource> resourcesLeft, List<Resource> resourcesDiscarded, int resourceIndex) {
        cliUtils.printFigure("/titles/WarehouseConfigurationTitle", 1, 21);
        depotCli.printWarehouse(warehouse, 7, 74);
        personalBoardCli.printLeaderDepots(warehouse.getLeaderDepots(), 15, 132);
        printLeaderDepotNumber(warehouse.getLeaderDepots().size());
        printResources(resourcesLeft, resourceIndex, "Resources left to insert: ", 22, 74);
        printResources(resourcesDiscarded, 0, "Resources discarded: ", 10, 132);
    }


    /**
     * Prints the given Resources
     *
     * @param resources       The Resources to print
     * @param startingIndex   The index where the list starts to be printed
     * @param listDescription A description that will be printed near the List of Resources
     * @param startingRow     The row where the Resources are going to be printed
     * @param startingColumn  The column where the Resources are going to be printed
     */
    private void printResources(List<Resource> resources, int startingIndex, String listDescription, int startingRow, int startingColumn) {
        cliUtils.pPCS(listDescription, Color.WHITE, startingRow, startingColumn);
        for (int i = startingIndex; i < resources.size(); i++)
            pw.print(cliUtils.hSpace(8) + cliUtils.pCS("\u2588\u2588  ", resources.get(i).getColor()));
    }


    /**
     * Asks the Player the index where he wants to insert the Resource
     * If the Player enters 'd' skip the current Resource and add it to the discard list
     *
     * @param warehouseDepotSize The max index the Player can enter
     * @return The index (an Integer between 0 and 4)
     * @throws DepotOutOfBoundException              The index is not in the correct bounds
     * @throws SkipResourceException                 The player skips this Resource
     * @throws ChangeResourcesBetweenDepotsException The Player wants to change Resources between Depots
     */
    private int getDepotIndex(int warehouseDepotSize) throws DepotOutOfBoundException, SkipResourceException, ChangeResourcesBetweenDepotsException {
        Scanner in = new Scanner(System.in);
        String depotString = in.nextLine();

        if (depotString.isEmpty()) throw new DepotOutOfBoundException();
        if (depotString.equals("d")) throw new SkipResourceException();
        if (depotString.equals("c")) throw new ChangeResourcesBetweenDepotsException();
        if (ViewUtils.checkAsciiRange(depotString.charAt(0))) throw new DepotOutOfBoundException();

        int depotIndex = Integer.parseInt(depotString) - 1;
        if (depotIndex >= warehouseDepotSize || depotIndex < 0) throw new DepotOutOfBoundException();
        return depotIndex;
    }


    /**
     * Prints the input phrases on screen
     *
     * @param warehouseDepotSize The max index the Player can enter
     * @param resourceName       The resource that the Player is currently positioning
     */
    private void askForInput(int warehouseDepotSize, String resourceName) {
        cliUtils.setCursorPosition(30, 21);
        pw.println("Please type the Depot number [1;" + warehouseDepotSize + "]  where you want to store " + resourceName + ".");
        pw.println(cliUtils.hSpace(20) + "The Resources that can't be added to the Warehouse will be discarded.");
        cliUtils.vSpace(1);
        pw.println(cliUtils.hSpace(20) + "Press 'd' if you want to discard the current Resource or you can't proceed any further.");
        cliUtils.vSpace(1);
        pw.println(cliUtils.hSpace(20) + "Press 'c' if you want to change the Resources between two Depots.");
        cliUtils.vSpace(4);
        pw.print(cliUtils.hSpace(20) + "Depot number: ");
        pw.flush();
    }


    /**
     * Sets the parameters to control the insertion process
     *
     * @param parameterValue The boolean value to set in the parameters
     */
    private void setParameters(boolean parameterValue) {
        correctDepotIndexInserted = parameterValue;
        resourceAdded = parameterValue;
        discardResource = parameterValue;
    }


    /**
     * Changes the Resources between two depots
     *
     * @param warehouse The Warehouse containing the Depots
     * @param resources The Resource to insert List
     */
    private void changeResourcesDepots(Warehouse warehouse, List<Resource> resources) {
        cliUtils.clearLine(38, 21);

        int sourceDepot = 0;
        int destinationDepot = 0;
        boolean endingInput = false;

        while (!endingInput) {
            try {
                sourceDepot = askForIndicesInChangePhase(warehouse.getAllDepots().size(), "Please, insert the source Depot number: ");
                cliUtils.clearLine(38, 21);
                destinationDepot = askForIndicesInChangePhase(warehouse.getAllDepots().size(), "Now insert the destination Depot number: ");
                endingInput = true;
            } catch (DepotOutOfBoundException | NumberFormatException e) {
                cliUtils.pPCS("INCORRECT INDICES PLEASE TRY AGAIN", Color.RED, 38, 21);
                cliUtils.clearLine(40, 61);
            }
        }

        List<Resource> tempResourcesSource = warehouse.getAllDepots().get(sourceDepot).grabAllResources();
        List<Resource> tempResourcesDestination = warehouse.getAllDepots().get(destinationDepot).grabAllResources();

        try {
            resources.addAll(addMultipleResources(warehouse, sourceDepot, tempResourcesDestination));
            resources.addAll(addMultipleResources(warehouse, destinationDepot, tempResourcesSource));
        } catch (CanNotAddResourceToDepotException e) {
            restoreOriginalSituation(warehouse, sourceDepot, destinationDepot, tempResourcesSource, tempResourcesDestination);
        }
    }


    /**
     * Asks the indices of the Depots when the Player wants to change Resources between them
     *
     * @param warehouseDepotSize The max index the Player can enter
     * @param phrase             A phrase that will ask the Depot index
     * @return The Depot index
     * @throws DepotOutOfBoundException Thrown if the index is not correct
     */
    private int askForIndicesInChangePhase(int warehouseDepotSize, String phrase) throws DepotOutOfBoundException {
        Scanner in = new Scanner(System.in);
        cliUtils.pPCS(phrase, Color.WHITE, 40, 21);

        String depotIndex = in.nextLine();

        if (depotIndex.isEmpty() || ViewUtils.checkAsciiRange(depotIndex.charAt(0)))
            throw new DepotOutOfBoundException();
        if ((Integer.parseInt(depotIndex) - 1) >= warehouseDepotSize || (Integer.parseInt(depotIndex) - 1) < 0)
            throw new DepotOutOfBoundException();

        return (Integer.parseInt(depotIndex) - 1);
    }


    /**
     * If it's not possible to change Resources between two Depots, restores the original situation
     *
     * @param warehouse                The Warehouse that contains the depots
     * @param sourceDepot              The source Depot for the change
     * @param destinationDepot         The destination Depot for the change
     * @param tempResourcesSource      A temporary List of the source Depot's Resources
     * @param tempResourcesDestination A temporary List of the destination Depot's Resources
     */
    private void restoreOriginalSituation(Warehouse warehouse, int sourceDepot, int destinationDepot, List<Resource> tempResourcesSource, List<Resource> tempResourcesDestination) {
        warehouse.getAllDepots().get(sourceDepot).grabAllResources();
        warehouse.getAllDepots().get(destinationDepot).grabAllResources();

        try {
            addMultipleResources(warehouse, sourceDepot, tempResourcesSource);
            addMultipleResources(warehouse, destinationDepot, tempResourcesDestination);
        } catch (CanNotAddResourceToDepotException e) {
            e.printStackTrace();
        }
    }


    /**
     * Add a list of resources to a depot
     *
     * @param warehouse    The warehouse from where the Depot is taken
     * @param depotIndex   The index of the Depot where the Resources are going to be added
     * @param resourceList The resources to add
     * @return The Resources to put back in the Resource to insert List
     * @throws CanNotAddResourceToDepotException Thrown if the resources can't be added to the depot
     */
    private List<Resource> addMultipleResources(Warehouse warehouse, int depotIndex, List<Resource> resourceList) throws CanNotAddResourceToDepotException {
        List<Resource> resources = new ArrayList<>();
        int maxNumberOfResources = warehouse.getAllDepots().get(depotIndex).getMaxNumberOfResources();

        //If the Resource to insert in a Depot are greater than the maxNumberOfResources the Depot can contain,
        //puts the remaining Resources in the Resource to insert List
        if (maxNumberOfResources < resourceList.size()) {

            for (int i = 0; i < maxNumberOfResources; i++)
                warehouse.addResourceToDepot(depotIndex, resourceList.get(i));

            for (int i = maxNumberOfResources; i < resourceList.size(); i++)
                resources.add(resourceList.get(i));

        }

        //Otherwise add the Resources normally
        else {
            for (Resource resource : resourceList) {
                warehouse.addResourceToDepot(depotIndex, resource);
            }
        }

        return resources;
    }


    /**
     * Prints the Depot number the Player has to enter to store a Resource in a LeaderDepot
     *
     * @param numberOfLeaderDepots The number of Leder Depots
     */
    private void printLeaderDepotNumber(int numberOfLeaderDepots) {
        cliUtils.setCursorPosition(14, 135);
        for (int i = 0; i < numberOfLeaderDepots; i++) {
            pw.print(cliUtils.pCS("Depot  " + (i + 4) + cliUtils.hSpace(10), Color.GREY));
        }
    }


    /**
     * Creates the List to send to the Server by taking the Resource's type contained in each Depot
     *
     * @param warehouse Resources to insert into the List are taken from here
     * @return The list to send
     */
    private List<Resource> createListToSend(Warehouse warehouse) {
        List<Resource> resourcesInWarehouse = new ArrayList<>();
        for (int i = 0; i < warehouse.getAllDepots().size(); i++) {
            resourcesInWarehouse.add(warehouse.getAllDepots().get(i).getContainedResourceType());
        }
        return resourcesInWarehouse;
    }


    /**
     * Shows the final configuration of the Warehouse that will be send to Server
     *
     * @param warehouse          The Player's Warehouse
     * @param resourcesLeft      The Resources to insert
     * @param resourcesDiscarded The Player's discarded Resources
     * @param resourceIndex      The index from where the resources are shown
     */
    private void showFinalScreen(Warehouse warehouse, List<Resource> resourcesLeft, List<Resource> resourcesDiscarded, int resourceIndex) {
        Scanner in = new Scanner(System.in);

        cliUtils.cls();
        printWarehouseAndResources(warehouse, resourcesLeft, resourcesDiscarded, resourceIndex);
        cliUtils.pPCS("This is the final configuration that will be send to Server. Press ENTER to proceed.", Color.WHITE, 25, 21);

        in.nextLine();
    }

}
