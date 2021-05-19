package it.polimi.ingsw.psp26.view.gui.modelcomponents.dialogcomponents.switchdepots;

import it.polimi.ingsw.psp26.model.personalboard.Depot;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addSelectionShadow;

public class SwitchableDepot {

    private final Pane imageViewContainer;
    private final Depot depot;
    private final ImageView depotImageView;
    private final Pane resourcesContainer;
    private final Image originalImage;
    private final Image sourceSwitchImage;
    private SwitchableGroup switchableGroup;
    private boolean isSource;
    private boolean isTarget;

    public SwitchableDepot(Depot depot, SwitchableGroup switchableGroup, Pane imageViewContainer, ImageView depotImageView, Pane resourcesContainer) {
        this.depot = depot;

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
        } else {
            depotImageView.setImage(originalImage);
        }
    }

    public boolean isTarget() {
        return isTarget;
    }

    public void setTarget(boolean target) {
        isTarget = target;
    }

    public Depot getDepot() {
        return depot;
    }

    public Pane getImageViewContainer() {
        return imageViewContainer;
    }

    public Pane getResourcesContainer() {
        return resourcesContainer;
    }
}
