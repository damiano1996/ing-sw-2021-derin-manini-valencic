package it.polimi.ingsw.psp26.view.gui.modelcomponents.dialogcomponents.switchdepots;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.dialogcomponents.WarehousePlacerDrawer;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.ModelDrawUtils.getResourceImage;

public class SwitchableGroup {

    private final Warehouse warehouse;
    private final WarehousePlacerDrawer warehousePlacerDrawer;
    private final List<SwitchableDepot> switchableDepots;

    public SwitchableGroup(Warehouse warehouse, WarehousePlacerDrawer warehousePlacerDrawer) {
        this.warehouse = warehouse;
        this.warehousePlacerDrawer = warehousePlacerDrawer;
        this.switchableDepots = new ArrayList<>();
    }

    public void addSwitchableDepot(SwitchableDepot switchableDepot) {
        switchableDepots.add(switchableDepot);
    }

    public boolean canBecomeSource() {
        for (SwitchableDepot switchableDepot1 : switchableDepots) {
            if (switchableDepot1.isSource()) return false;
        }
        return true;
    }

    public boolean canBecomeTarget() {
        for (SwitchableDepot switchableDepot1 : switchableDepots) {
            if (switchableDepot1.isTarget()) return false;
        }
        return true;
    }

    public void execute(SwitchableDepot targetSelectedDepot) {
        // Looking for source selected depot
        SwitchableDepot sourceSelectedDepot = null;
        for (SwitchableDepot switchableDepot : switchableDepots)
            if (switchableDepot.isSource()) sourceSelectedDepot = switchableDepot;

        assert sourceSelectedDepot != null;
        // Extracting information from selected depots
        int sourceDepotIndex = switchableDepots.indexOf(sourceSelectedDepot);
        List<Resource> sourceDepotResources = sourceSelectedDepot.getDepot().grabAllResources();
        sourceSelectedDepot.getResourcesContainer().getChildren().clear();

        int targetDepotIndex = switchableDepots.indexOf(targetSelectedDepot);
        List<Resource> targetDeptResources = targetSelectedDepot.getDepot().grabAllResources();
        targetSelectedDepot.getResourcesContainer().getChildren().clear();

        // Placing resources from source to target
        placeResourcesAndDraw(sourceDepotResources, targetDepotIndex, targetSelectedDepot);
        // Placing resources from target to source
        placeResourcesAndDraw(targetDeptResources, sourceDepotIndex, sourceSelectedDepot);

        sourceSelectedDepot.setSource(false);
        targetSelectedDepot.setTarget(false);
    }

    private void placeResourcesAndDraw(List<Resource> sourceDepotResources, int targetDepotIndex, SwitchableDepot targetSelectedDepot) {
        // Placing resources from source to target
        for (Resource sourceDepotResource : sourceDepotResources) {
            try {
                warehouse.addResourceToDepot(targetDepotIndex, sourceDepotResource);
                // If everything works as expected, we can draw the resource in the target
                targetSelectedDepot.getResourcesContainer().add(
                        getImageView(getResourceImage(sourceDepotResource, warehousePlacerDrawer.getRatio()), 0, 0),
                        targetSelectedDepot.getResourcesContainer().getColumnCount() + 1, 1, 1, 1);
            } catch (CanNotAddResourceToDepotException e) {
                // otherwise we should put the resource between the resources to add
                warehousePlacerDrawer.placeResourceToAdd(sourceDepotResource);
                warehousePlacerDrawer.addResourceToAdd(sourceDepotResource);
            }
        }
    }
}
