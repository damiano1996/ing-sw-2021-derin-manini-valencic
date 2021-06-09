package it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents.switchdepots;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents.WarehousePlacerDrawer;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.ModelDrawUtils.getResourceImage;

public class DepotsSwitchableGroup {

    private final WarehousePlacerDrawer warehousePlacerDrawer;
    private final List<SwitchableDepot> switchableDepots;

    public DepotsSwitchableGroup(WarehousePlacerDrawer warehousePlacerDrawer) {
        this.warehousePlacerDrawer = warehousePlacerDrawer;
        this.switchableDepots = new ArrayList<>();
    }

    public void addSwitchableDepot(SwitchableDepot switchableDepot) {
        switchableDepots.add(switchableDepot);
    }

    public boolean canBecomeSource() {
        for (SwitchableDepot switchableDepot1 : switchableDepots) {
            if (switchableDepot1.isSource()) {
                warehousePlacerDrawer.hideMessage();
                return false;
            }
        }
        warehousePlacerDrawer.showMessage("Source depot has been selected.");
        return true;
    }

    public boolean canBecomeTarget() {
        for (SwitchableDepot switchableDepot1 : switchableDepots) {
            if (switchableDepot1.isTarget()) {
                warehousePlacerDrawer.hideMessage();
                return false;
            }
        }
        warehousePlacerDrawer.showMessage("Destination depot has been selected.");
        return true;
    }

    public void execute(SwitchableDepot targetSelectedDepot) {
        // Looking for source selected depot
        SwitchableDepot sourceSelectedDepot = null;
        for (SwitchableDepot switchableDepot : switchableDepots)
            if (switchableDepot.isSource()) sourceSelectedDepot = switchableDepot;

        assert sourceSelectedDepot != null;
        // Checking that the resources can be switched between the selected depots
        if (warehousePlacerDrawer.getWarehouse().getBaseDepots().get(sourceSelectedDepot.getDepotIndex()).getResources().size() <=
                warehousePlacerDrawer.getWarehouse().getBaseDepots().get(targetSelectedDepot.getDepotIndex()).getMaxNumberOfResources() &&
                warehousePlacerDrawer.getWarehouse().getBaseDepots().get(targetSelectedDepot.getDepotIndex()).getResources().size() <=
                        warehousePlacerDrawer.getWarehouse().getBaseDepots().get(sourceSelectedDepot.getDepotIndex()).getMaxNumberOfResources()) {

            // Extracting information from selected depots
            int sourceDepotIndex = switchableDepots.indexOf(sourceSelectedDepot);
            List<Resource> sourceDepotResources = warehousePlacerDrawer.getWarehouse().getBaseDepots().get(sourceSelectedDepot.getDepotIndex()).grabAllResources();
            sourceSelectedDepot.getResourcesContainer().getChildren().clear();

            int targetDepotIndex = switchableDepots.indexOf(targetSelectedDepot);
            List<Resource> targetDeptResources = warehousePlacerDrawer.getWarehouse().getBaseDepots().get(targetSelectedDepot.getDepotIndex()).grabAllResources();
            targetSelectedDepot.getResourcesContainer().getChildren().clear();

            // Placing resources from source to target
            placeResourcesAndDraw(sourceDepotResources, targetDepotIndex, targetSelectedDepot);
            // Placing resources from target to source
            placeResourcesAndDraw(targetDeptResources, sourceDepotIndex, sourceSelectedDepot);

            warehousePlacerDrawer.hideMessage();

        } else {
            warehousePlacerDrawer.showMessage("Selected depots cannot be switched!");
        }

        sourceSelectedDepot.setSource(false);
        targetSelectedDepot.setTarget(false);
    }

    private void placeResourcesAndDraw(List<Resource> sourceDepotResources, int targetDepotIndex, SwitchableDepot targetSelectedDepot) {
        // Placing resources from source to target
        for (Resource sourceDepotResource : sourceDepotResources) {
            try {
                warehousePlacerDrawer.getWarehouse().addResourceToDepot(targetDepotIndex, sourceDepotResource);
                targetSelectedDepot.getResourcesContainer().add(
                        getImageView(getResourceImage(sourceDepotResource, warehousePlacerDrawer.getRatio()), 0, 0),
                        targetSelectedDepot.getResourcesContainer().getColumnCount() + 1, 1, 1, 1);
            } catch (CanNotAddResourceToDepotException ignored) {
            }
        }
    }
}
