package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.exceptions.NoImageException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.view.gui.ButtonContainer;
import javafx.scene.image.ImageView;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.getGeneralRatio;
import static it.polimi.ingsw.psp26.view.gui.choicesdrawers.ChoiceDrawerUtils.addSelectionListener;
import static it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.ModelDrawUtils.getResourceImage;

public class ResourceChoicesDrawer implements ChoicesDrawer<Resource> {

    @Override
    public ButtonContainer<Resource> decorateButtonContainer(ButtonContainer<Resource> resourceButtonContainer) {
        try {
            resourceButtonContainer.setGraphic(getResourceImageView(resourceButtonContainer.getContainedObject()));
            addSelectionListener(
                    resourceButtonContainer,
                    getResourceImageView(resourceButtonContainer.getContainedObject()),
                    getResourceImage(resourceButtonContainer.getContainedObject(), getGeneralRatio())
            );
        } catch (NoImageException ignored) {
        }

        resourceButtonContainer.setStyle("-fx-background-color: transparent;");
        return resourceButtonContainer;
    }

    private ImageView getResourceImageView(Resource resource) throws NoImageException {
        return getImageView(getResourceImage(resource, getGeneralRatio()), 0, 0);
    }
}
