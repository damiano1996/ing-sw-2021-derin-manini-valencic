package it.polimi.ingsw.psp26.view.gui.modelcomponents.dialogcomponents.switchdepots;

import it.polimi.ingsw.psp26.view.gui.SoundManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addSelectionShadow;

public class SwitchableDepot {

    private final Pane imageViewContainer;
    private final int depotIndex;
    private final ImageView depotImageView;
    private final GridPane resourcesContainer;
    private final Image originalImage;
    private final Image sourceSwitchImage;
    private DepotsSwitchableGroup switchableGroup;
    private boolean isSource;
    private boolean isTarget;

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

    private void addProperties() {

        SwitchableDepot thisSwitchableDepot = this;

        imageViewContainer.setOnMouseClicked(mouseEvent -> {

            if (!isSource && !isTarget) {
                if (switchableGroup.canBecomeSource()) {
                    setSource(true);
                } else if (switchableGroup.canBecomeTarget()) {
                    setTarget(true);
                    switchableGroup.execute(thisSwitchableDepot);
                }

            } else {

                if (isSource) {
                    setSource(false);
                }
            }
        });
    }

    public boolean isSource() {
        return isSource;
    }

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

    public boolean isTarget() {
        return isTarget;
    }

    public void setTarget(boolean target) {
        isTarget = target;
    }

    public int getDepotIndex() {
        return depotIndex;
    }

    public Pane getImageViewContainer() {
        return imageViewContainer;
    }

    public GridPane getResourcesContainer() {
        return resourcesContainer;
    }
}
