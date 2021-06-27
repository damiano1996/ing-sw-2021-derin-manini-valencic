package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.exceptions.NoImageException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.view.gui.ButtonContainer;
import javafx.scene.image.ImageView;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.getGeneralRatio;
import static it.polimi.ingsw.psp26.view.gui.choicesdrawers.ChoiceDrawerUtils.addSelectionListener;
import static it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.ModelDrawUtils.getResourceImage;

/**
 * Class to decorate a button container containing a resource object.
 */
public class ResourceChoicesDrawer implements ChoicesDrawer<Resource> {

    /**
     * Method to decorate the button.
     * It adds the image of the resource and the selection properties.
     *
     * @param resourceButtonContainer button container with resource inside
     * @return decorated button
     */
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

    /**
     * Method to get an image view containing the image associated to the given resource.
     *
     * @param resource resource object
     * @return image view
     * @throws NoImageException if no image exists for the given resource
     */
    private ImageView getResourceImageView(Resource resource) throws NoImageException {
        return getImageView(getResourceImage(resource, getGeneralRatio()), 0, 0);
    }
}
