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

import static it.polimi.ingsw.psp26.view.ViewUtils.changePosition;
import static it.polimi.ingsw.psp26.view.ViewUtils.createListToSend;

/**
 * Class that displays the screen where the Player can rearrange its Warehouse.
 */
public class DisplayWarehousePlacer {

    private final PrintWriter pw;
    private final CliUtils cliUtils;
    private final DepotCli depotCli;
    private final PersonalBoardCli personalBoardCli;
    private final List<Resource> discardedResources;

    //If the Player enters an incorrect index, ask it again
    private boolean correctDepotIndexInserted;
    //If the Player chooses an invalid position, ask it again
    private boolean resourceAdded;
    //If the player decides to discard the Resource, add it to discardedResources and proceed with the next Resource
    private boolean discardResource;

    /**
     * Constructor of the class.
     * It creates a new PrintWriter, a new CliUtils, a new DepotCli and a new PersonalBoardCli to use.
     * It also initialize the discardedResources List.
     *
     * @param pw the PrintWriter to use
     */
    public DisplayWarehousePlacer(PrintWriter pw) {
        this.pw = pw;
        this.cliUtils = new CliUtils(pw);
        this.depotCli = new DepotCli(pw);
        this.personalBoardCli = new PersonalBoardCli(pw);
        discardedResources = new ArrayList<>();
    }


    /**
     * Shows the screen that appears after getting Resources from the Market.
     * While the Player is rearranging its Warehouse, it has to enter the number of the Depot in which he wants to store the
     * first Resource on the left in the "Resources left to insert" List.
     * It is possible to change the Resources contained between two Depots: to do so, the Player must enter c+Enter, and then
     * follow the instructions printed on the screen.
     * It is also possible to discard the first Resource on the left in the "Resources left to insert" List: to do so, the Player
     * must enter d+Enter.
     *
     * @param warehouse the Warehouse to print
     * @param resources the resources get prom the Market to insert into the Warehouse
     * @return the Resources placed in the Warehouse
     */
    public List<Resource> displayMarketResourcesSelection(Warehouse warehouse, List<Resource> resources) {
        discardedResources.clear();
        int depotIndex = 0;

        for (int i = 0; i < resources.size(); i++) {
            cliUtils.cls();

            printWarehouseAndResources(warehouse, resources, discardedResources, i);
            setParameters(false);

            while (!resourceAdded) {

                askForInput(warehouse.getAllDepots().size(), resources.get(i).getName());

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
                        changeResourcesDepots(warehouse);
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
     * Executes the (DepotOutOfBoundException | NumberFormatException) catch in displayMarketResourcesSelection().
     */
    private void executeCatchDepotOutOfBound() {
        cliUtils.pPCS("WRONG INDEX INSERTED! Insert Depot index again: ", Color.RED, 38, 21);
        cliUtils.clearLine(40, 21);
        cliUtils.clearLine(38, 69);
    }


    /**
     * Executes the (CanNotAddResourceToDepotException) catch displayMarketResourcesSelection().
     */
    private void executeCatchCanNotAddResourcesToDepot() {
        cliUtils.pPCS("RESOURCE CAN'T BE ADDED! Please try again", Color.RED, 38, 21);
        cliUtils.clearLine(38, 62);
        cliUtils.clearLine(40, 35);
        correctDepotIndexInserted = false;
    }


    /**
     * Prints the Warehouse and the Resources to insert.
     *
     * @param warehouse          the Player's Warehouse
     * @param resourcesLeft      the Resources to insert
     * @param resourcesDiscarded the Player's discarded Resources
     * @param resourceIndex      the index from where the resources are shown
     */
    private void printWarehouseAndResources(Warehouse warehouse, List<Resource> resourcesLeft, List<Resource> resourcesDiscarded, int resourceIndex) {
        cliUtils.printFigure("/titles/WarehouseConfigurationTitle", 1, 21);
        depotCli.printWarehouse(warehouse, 7, 74);
        personalBoardCli.printLeaderDepots(warehouse.getLeaderDepots(), 15, 132);
        printLeaderDepotNumber(warehouse.getLeaderDepots().size());
        printResources(resourcesLeft, resourceIndex, "Resources left to insert: ", 23, 74);
        printResources(resourcesDiscarded, 0, "Resources discarded: ", 10, 132);
    }


    /**
     * Prints the given Resources.
     *
     * @param resources       the Resources to print
     * @param startingIndex   the index where the list starts to be printed
     * @param listDescription a description that will be printed near the List of Resources
     * @param startingRow     the row where the Resources are going to be printed
     * @param startingColumn  the column where the Resources are going to be printed
     */
    private void printResources(List<Resource> resources, int startingIndex, String listDescription, int startingRow, int startingColumn) {
        cliUtils.pPCS(listDescription, Color.WHITE, startingRow, startingColumn);
        for (int i = startingIndex; i < resources.size(); i++)
            pw.print(cliUtils.hSpace(8) + cliUtils.pCS("\u2588\u2588  ", resources.get(i).getColor()));
    }


    /**
     * Asks the Player the index where he wants to insert the Resource.
     * If the Player enters d+Enter skip the current Resource and add it to the discard list.
     * If the Player enters c+Enter shows the instructions for changing Resources between Depots.
     *
     * @param warehouseDepotSize the max index the Player can enter
     * @return the index (an Integer between 0 and 4)
     * @throws DepotOutOfBoundException              the index is not in the correct bounds
     * @throws SkipResourceException                 the player skips this Resource
     * @throws ChangeResourcesBetweenDepotsException the Player wants to change Resources between Depots
     */
    private int getDepotIndex(int warehouseDepotSize) throws DepotOutOfBoundException, SkipResourceException, ChangeResourcesBetweenDepotsException {
        Scanner in = new Scanner(System.in);
        String depotString = in.nextLine();

        if (depotString.isEmpty()) throw new DepotOutOfBoundException();
        if (depotString.equals("d")) throw new SkipResourceException();
        if (depotString.equals("c")) throw new ChangeResourcesBetweenDepotsException();
        if (cliUtils.checkAsciiRange(depotString.charAt(0))) throw new DepotOutOfBoundException();

        int depotIndex = Integer.parseInt(depotString) - 1;
        if (depotIndex >= warehouseDepotSize || depotIndex < 0) throw new DepotOutOfBoundException();
        return depotIndex;
    }


    /**
     * Prints the input phrases on screen.
     *
     * @param warehouseDepotSize the max index the Player can enter
     * @param resourceName       the resource that the Player is currently positioning
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
     * Sets the parameters to control the insertion process.
     *
     * @param parameterValue the boolean value to set in the parameters
     */
    private void setParameters(boolean parameterValue) {
        correctDepotIndexInserted = parameterValue;
        resourceAdded = parameterValue;
        discardResource = parameterValue;
    }


    /**
     * Changes the Resources between two depots.
     * Controls are made to ensure the change will not beak the game rules.
     *
     * @param warehouse the Warehouse containing the Depots
     */
    private void changeResourcesDepots(Warehouse warehouse) {
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

        // Checking if the change is possible
        int sourceDepotResourceSize = warehouse.getAllDepots().get(sourceDepot).getResources().size();
        int destinationDepotResourceSize = warehouse.getAllDepots().get(destinationDepot).getResources().size();

        if (sourceDepotResourceSize <= warehouse.getAllDepots().get(destinationDepot).getMaxNumberOfResources() &&
                destinationDepotResourceSize <= warehouse.getAllDepots().get(sourceDepot).getMaxNumberOfResources()) {
            changePosition(warehouse, sourceDepot, destinationDepot);
        }
    }


    /**
     * Asks the indices of the Depots when the Player wants to change Resources between them.
     *
     * @param warehouseDepotSize the max index the Player can enter
     * @param phrase             a phrase that will ask the Depot index
     * @return the Depot index
     * @throws DepotOutOfBoundException thrown if the index is not correct
     */
    private int askForIndicesInChangePhase(int warehouseDepotSize, String phrase) throws DepotOutOfBoundException {
        Scanner in = new Scanner(System.in);
        cliUtils.pPCS(phrase, Color.WHITE, 40, 21);

        String depotIndex = in.nextLine();

        if (depotIndex.isEmpty() || cliUtils.checkAsciiRange(depotIndex.charAt(0)))
            throw new DepotOutOfBoundException();
        if ((Integer.parseInt(depotIndex) - 1) >= warehouseDepotSize || (Integer.parseInt(depotIndex) - 1) < 0)
            throw new DepotOutOfBoundException();

        return (Integer.parseInt(depotIndex) - 1);
    }


    /**
     * Prints the Depot number the Player has to enter to store a Resource in a LeaderDepot.
     *
     * @param numberOfLeaderDepots the number of LeaderDepots
     */
    private void printLeaderDepotNumber(int numberOfLeaderDepots) {
        cliUtils.setCursorPosition(14, 135);
        for (int i = 0; i < numberOfLeaderDepots; i++) {
            pw.print(cliUtils.pCS("Depot  " + (i + 4) + cliUtils.hSpace(10), Color.GREY));
        }
    }


    /**
     * Shows the final configuration of the Warehouse that will be send to Server.
     *
     * @param warehouse          the Player's Warehouse
     * @param resourcesLeft      the Resources to insert
     * @param resourcesDiscarded the Player's discarded Resources
     * @param resourceIndex      the index from where the resources are shown
     */
    private void showFinalScreen(Warehouse warehouse, List<Resource> resourcesLeft, List<Resource> resourcesDiscarded, int resourceIndex) {
        Scanner in = new Scanner(System.in);

        cliUtils.cls();
        printWarehouseAndResources(warehouse, resourcesLeft, resourcesDiscarded, resourceIndex);
        cliUtils.pPCS("This is the final configuration that will be send to Server. Press ENTER to proceed.", Color.WHITE, 25, 21);

        in.nextLine();
    }

}
