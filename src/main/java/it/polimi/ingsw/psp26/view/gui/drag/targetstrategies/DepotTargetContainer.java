package it.polimi.ingsw.psp26.view.gui.drag.targetstrategies;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class DepotTargetContainer extends TargetContainer<Resource> {

    private final Warehouse warehouse;
    private final int depotIndex;

    public DepotTargetContainer(GridPane resourcesContainerGridPane, Pane resourceTargetPane, Warehouse warehouse, int depotIndex) {
        super(resourcesContainerGridPane, resourceTargetPane);
        this.warehouse = warehouse;
        this.depotIndex = depotIndex;
    }

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
