package it.polimi.ingsw.psp26.view.cli;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.ChangeResourcesBetweenDepotsException;
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
    private final PersonalBoardCli personalBoardCli;

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
        this.personalBoardCli = new PersonalBoardCli(pw);

        placedResources = new ArrayList<>();
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
        int depotIndex = 0;

        for (int i = 0; i < resources.size(); i++) {
            cliUtils.cls();

            printWarehouseAndResources(warehouse, resources, discardedResources, i);
            setParameters(false);

            while (!resourceAdded) {

                askForInput(resources.get(i).getName().replaceAll("\\s+", ""));

                while (!correctDepotIndexInserted) {
                    try {
                        depotIndex = getDepotIndex();
                        correctDepotIndexInserted = true;
                    } catch (SkipResourceException e) {
                        discardedResources.add(resources.get(i));
                        setParameters(true);
                    } catch (DepotOutOfBoundException e) {
                        cliUtils.pPCS("WRONG INDEX INSERTED! Insert Depot index again: ", Color.RED, 38, 21);
                        cliUtils.clearLine(40, 21);
                        cliUtils.clearLine(38, 69);
                    } catch (ChangeResourcesBetweenDepotsException e) {
                        changeResourcesDepots(warehouse);
                        i--;
                        setParameters(true);
                    }
                }

                if (!discardResource) {
                    try {
                        warehouse.addResourceToDepot(depotIndex, resources.get(i));
                        placedResources.add(resources.get(i));
                        resourceAdded = true;
                    } catch (CanNotAddResourceToDepotException e) {
                        cliUtils.pPCS("RESOURCE CAN'T BE ADDED! Please try again", Color.RED, 38, 21);
                        cliUtils.clearLine(38, 62);
                        cliUtils.clearLine(40, 35);
                        correctDepotIndexInserted = false;
                    }
                }

            }

        }

        return placedResources;
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
     * @return The index (an Integer between 0 and 2)
     * @throws DepotOutOfBoundException              The index is not in the correct bounds
     * @throws SkipResourceException                 The player skips this Resource
     * @throws ChangeResourcesBetweenDepotsException The Player wants to change Resources between Depots
     */
    private int getDepotIndex() throws DepotOutOfBoundException, SkipResourceException, ChangeResourcesBetweenDepotsException {
        Scanner in = new Scanner(System.in);
        String depotString = in.nextLine();

        if (depotString.isEmpty()) throw new DepotOutOfBoundException();
        if (depotString.charAt(0) == 'd') throw new SkipResourceException();
        if (depotString.charAt(0) == 'c') throw new ChangeResourcesBetweenDepotsException();
        if (checkAsciiRange(depotString.charAt(0))) throw new DepotOutOfBoundException();

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
        pw.println(cliUtils.hSpace(20) + "Press 'd' if you want to discard the current Resource or you can't proceed any further.");
        cliUtils.vSpace(1);
        pw.println(cliUtils.hSpace(20) + "Press 'c' if you want to change the Resources between two Depots.");
        cliUtils.vSpace(4);
        pw.print(cliUtils.hSpace(20) + "Depot number: ");
        pw.flush();
    }


    /**
     * Checks if the inserted char is a number or a character
     *
     * @param c The char to examine
     * @return True if c is a number, false if it's not a number
     */
    private boolean checkAsciiRange(char c) {
        return (48 > (int) c || (int) c > 57);
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
     */
    private void changeResourcesDepots(Warehouse warehouse) {
        cliUtils.clearLine(38, 21);

        int sourceDepot = 0;
        int destinationDepot = 0;
        boolean endingInput = false;

        while (!endingInput) {
            try {
                sourceDepot = askForIndicesInChangePhase("Please, insert the source Depot number: ");
                cliUtils.clearLine(38, 21);
                destinationDepot = askForIndicesInChangePhase("Now insert the destination Depot number: ");
                endingInput = true;
            } catch (DepotOutOfBoundException e) {
                cliUtils.pPCS("INCORRECT INDICES PLEASE TRY AGAIN", Color.RED, 38, 21);
                cliUtils.clearLine(40, 61);
            }
        }

        List<Resource> tempResourcesSource = warehouse.getAllDepots().get(sourceDepot).grabAllResources();
        List<Resource> tempResourcesDestination = warehouse.getAllDepots().get(destinationDepot).grabAllResources();

        try {
            warehouse.getAllDepots().get(sourceDepot).addMultipleResources(tempResourcesDestination);
            warehouse.getAllDepots().get(destinationDepot).addMultipleResources(tempResourcesSource);
        } catch (CanNotAddResourceToDepotException e) {
            restoreOriginalSituation(warehouse, sourceDepot, destinationDepot, tempResourcesSource, tempResourcesDestination);
        }
    }


    /**
     * Asks the indices of the Depots when the Player wants to change Resources between them
     *
     * @param phrase A phrase that will ask the Depot index
     * @return The Depot index
     * @throws DepotOutOfBoundException Thrown if the index is not correct
     */
    private int askForIndicesInChangePhase(String phrase) throws DepotOutOfBoundException {
        Scanner in = new Scanner(System.in);
        cliUtils.pPCS(phrase, Color.WHITE, 40, 21);

        String depotIndex = in.nextLine();

        if (depotIndex.isEmpty() || checkAsciiRange(depotIndex.charAt(0))) throw new DepotOutOfBoundException();
        if ((Integer.parseInt(depotIndex) - 1) >= 3 || (Integer.parseInt(depotIndex) - 1) < 0)
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
            warehouse.getAllDepots().get(sourceDepot).addMultipleResources(tempResourcesSource);
            warehouse.getAllDepots().get(destinationDepot).addMultipleResources(tempResourcesDestination);
        } catch (CanNotAddResourceToDepotException e) {
            e.printStackTrace();
        }
    }

}
