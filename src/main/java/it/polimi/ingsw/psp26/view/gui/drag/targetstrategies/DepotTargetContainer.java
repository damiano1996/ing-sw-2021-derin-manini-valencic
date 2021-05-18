package it.polimi.ingsw.psp26.view.gui.drag.targetstrategies;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToWarehouse;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import javafx.scene.layout.Pane;

public class DepotTargetContainer extends TargetContainer<Resource> {

    private final Warehouse warehouse;
    private final int depotIndex;

    public DepotTargetContainer(Pane pane, Warehouse warehouse, int depotIndex) {
        super(pane);
        this.warehouse = warehouse;
        this.depotIndex = depotIndex;
    }

    @Override
    public boolean place(Resource resource) {
        try {
            System.out.println("DepotTargetContainer - trying to place the resource.");
            warehouse.addResource(resource, depotIndex);
            System.out.println("DepotTargetContainer - resource has been placed.");
            return true;
        } catch (CanNotAddResourceToWarehouse canNotAddResourceToWarehouse) {
            System.out.println("DepotTargetContainer - resource cannot be placed");
            return false;
        }
    }
}
