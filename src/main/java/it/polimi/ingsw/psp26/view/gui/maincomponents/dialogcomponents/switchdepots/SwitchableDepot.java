package it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents.switchdepots;

import it.polimi.ingsw.psp26.exceptions.SourceSwitchableDepotHasNotBeenSelectedException;
import it.polimi.ingsw.psp26.view.gui.sounds.SoundManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addSelectionShadow;

/**
 * Class modelling a depot with content that can be switched with other switchable depots.
 * It will referer to a specific depot object.
 * It will be assigned to a switchable group.
 * It adds properties, to the pane representing the depot, to switch the content.
 */
public class SwitchableDepot {

    private final Pane imageViewContainer;
    private final int depotIndex;
    private final ImageView depotImageView;
    private final GridPane resourcesContainer;
    private final Image originalImage;
    private final Image sourceSwitchImage;
    private final DepotsSwitchableGroup switchableGroup;
    private boolean isSource;
    private boolean isTarget;

    /**
     * Class constructor.
     *
     * @param depotIndex         warehouse index of the relative depot object
     * @param switchableGroup    switchable group object that will contain this
     * @param imageViewContainer pane that contains the image view associated to the depot
     * @param depotImageView     image view that is related to the depot
     * @param resourcesContainer grid pane that contains the resources that are in the depot
     */
    public SwitchableDepot(int depotIndex, DepotsSwitchableGroup switchableGroup, Pane imageViewContainer, ImageView depotImageView, GridPane resourcesContainer) {
        this.depotIndex = depotIndex;

        this.switchableGroup = switchableGroup;

        this.imageViewContainer = imageViewContainer;
        this.depotImageView = depotImageView;
        this.resourcesContainer = resourcesContainer;
        originalImage = depotImageView.getImage();

        sourceSwitchImage = addSelectionShadow(originalImage);

        isSource = false;
        isTarget = false;

        addProperties();
    }

    /**
     * Method to add properties to the image view container.
     * If clicked, it will change the state as source depot or as target.
     * If it will be a target, it will call the switchable group for the execution of the switch.
     */
    private void addProperties() {

        SwitchableDepot thisSwitchableDepot = this;

        imageViewContainer.setOnMouseClicked(mouseEvent -> {

            if (!isSource && !isTarget) {
                if (switchableGroup.canBecomeSource()) {
                    setSource(true);
                } else if (switchableGroup.canBecomeTarget()) {
                    setTarget(true);
                    try {
                        switchableGroup.executeSwitch(thisSwitchableDepot);
                    } catch (SourceSwitchableDepotHasNotBeenSelectedException ignored) {
                    }
                }

            } else {

                if (isSource) {
                    setSource(false);
                }
            }
        });
    }

    /**
     * Boolean method to check if the switchable depot is marked as source.
     *
     * @return true if it is a source, false otherwise
     */
    public boolean isSource() {
        return isSource;
    }

    /**
     * Method to change the graphic of the switchable depot.
     * Moreover it play the sound effect.
     *
     * @param source true if marked as source, false otherwise
     */
    public void setSource(boolean source) {
        isSource = source;
        if (source) {
            depotImageView.setImage(sourceSwitchImage);
            SoundManager.getInstance().setSoundEffect("button_click_01.wav");

        } else {
            depotImageView.setImage(originalImage);
            SoundManager.getInstance().setSoundEffect("button_click_02.wav");

        }
    }

    /**
     * Boolean method to check if switchable depot is target.
     *
     * @return true if it is target, false otherwise
     */
    public boolean isTarget() {
        return isTarget;
    }

    /**
     * Setter to mark the switchable depot as target.
     *
     * @param target true if target, false otherwise
     */
    public void setTarget(boolean target) {
        isTarget = target;
    }

    /**
     * Getter of the index of the relative depot.
     *
     * @return depot index
     */
    public int getDepotIndex() {
        return depotIndex;
    }

    /**
     * Getter of the image view of the container.
     *
     * @return pane
     */
    public Pane getImageViewContainer() {
        return imageViewContainer;
    }

    /**
     * Getter of the grid pane containing the resources that are in the depot.
     *
     * @return grid pane
     */
    public GridPane getResourcesContainer() {
        return resourcesContainer;
    }
}
