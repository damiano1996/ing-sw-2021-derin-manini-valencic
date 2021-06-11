package it.polimi.ingsw.psp26.view.gui.drag.targetstrategies;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Class to model a depot target container.
 */
public class DepotTargetContainer extends TargetContainer<Resource> {

    private final Warehouse warehouse;
    private final int depotIndex;

    /**
     * Constructor of the class.
     *
     * @param resourcesContainerGridPane grid pane used to locate resources of the depot
     * @param resourceTargetPane         pane used as target region for the resources
     * @param warehouse                  warehouse object
     * @param depotIndex                 index of the depot associated to this target container
     */
    public DepotTargetContainer(GridPane resourcesContainerGridPane, Pane resourceTargetPane, Warehouse warehouse, int depotIndex) {
        super(resourcesContainerGridPane, resourceTargetPane);
        this.warehouse = warehouse;
        this.depotIndex = depotIndex;
    }

    /**
     * Method to place the given resource in the depot.
     *
     * @param resource resource that must be placed in
     * @return true if the resource has been placed correctly, false otherwise
     */
    @Override
    public boolean place(Resource resource) {

        try {

            System.out.println("DepotTargetContainer - Trying to place the resource.");
            warehouse.addResourceToDepot(depotIndex, resource);
            System.out.println("DepotTargetContainer - Resource has been placed successfully.");
            return true;

        } catch (CanNotAddResourceToDepotException e) {

            System.out.println("DepotTargetContainer - Resource cannot be placed.");
            return false;

        }

    }
}
