package it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents.switchdepots;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.NoImageException;
import it.polimi.ingsw.psp26.exceptions.SourceSwitchableDepotHasNotBeenSelectedException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents.WarehousePlacerDrawer;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.ModelDrawUtils.getResourceImage;

/**
 * Class that defines a group of depots and handles the switch of the resources between them.
 */
public class DepotsSwitchableGroup {

    private final WarehousePlacerDrawer warehousePlacerDrawer;
    private final List<SwitchableDepot> switchableDepots;

    /**
     * Class constructor.
     *
     * @param warehousePlacerDrawer warehouse placer drawer object
     */
    public DepotsSwitchableGroup(WarehousePlacerDrawer warehousePlacerDrawer) {
        this.warehousePlacerDrawer = warehousePlacerDrawer;
        this.switchableDepots = new ArrayList<>();
    }

    /**
     * Method to add a switchable depot object in the group.
     *
     * @param switchableDepot switchable depot object
     */
    public void addSwitchableDepot(SwitchableDepot switchableDepot) {
        switchableDepots.add(switchableDepot);
    }

    /**
     * Method to check if a switchable depot can become the source for a switch.
     * If no other depots are selected, it will return true.
     * Only one switchable depot of the group can be source at time.
     *
     * @return true if no switchable depot is already a source, false otherwise
     */
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

    /**
     * Method to check if a switchable depot can become the target for a switch.
     * If no other depots are selected as target, it will return true.
     * Only one switchable depot of the group can be a target at time.
     *
     * @return true if no switchable depot is already a target, false otherwise
     */
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

    /**
     * Method to switch contents between the source and the target switchable depots.
     * The method searches in the group the source depot. If no source depot exists an exception will be thrown.
     * If source depot exists, the switch can be considered.
     * Under the rules of the game, if the resources can be switched:
     * the switch will be performed, both between images and resources contained in the depots objects.
     * Otherwise, a message error will be displayed in the warehouse placer drawer pane.
     *
     * @param targetSelectedDepot switchable depot object
     * @throws SourceSwitchableDepotHasNotBeenSelectedException if source depot has not been selected
     */
    public void executeSwitch(SwitchableDepot targetSelectedDepot) throws SourceSwitchableDepotHasNotBeenSelectedException {
        // Looking for source selected depot
        SwitchableDepot sourceSelectedDepot = null;
        for (SwitchableDepot switchableDepot : switchableDepots)
            if (switchableDepot.isSource()) sourceSelectedDepot = switchableDepot;

        if (sourceSelectedDepot == null) throw new SourceSwitchableDepotHasNotBeenSelectedException();
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

    /**
     * Method to place resources in the switchable depot,
     * both in the warehouse and in the corresponding pane.
     *
     * @param sourceDepotResources list of resources coming from the source depot
     * @param targetDepotIndex     index of the target depot
     * @param targetSelectedDepot  target switchable depot object
     */
    private void placeResourcesAndDraw(List<Resource> sourceDepotResources, int targetDepotIndex, SwitchableDepot targetSelectedDepot) {
        // Placing resources from source to target
        for (Resource sourceDepotResource : sourceDepotResources) {
            try {
                warehousePlacerDrawer.getWarehouse().addResourceToDepot(targetDepotIndex, sourceDepotResource);
                targetSelectedDepot.getResourcesContainer().add(
                        getImageView(getResourceImage(sourceDepotResource, warehousePlacerDrawer.getRatio()), 0, 0),
                        targetSelectedDepot.getResourcesContainer().getColumnCount() + 1, 1, 1, 1);
            } catch (CanNotAddResourceToDepotException | NoImageException ignored) {
            }
        }
    }
}
