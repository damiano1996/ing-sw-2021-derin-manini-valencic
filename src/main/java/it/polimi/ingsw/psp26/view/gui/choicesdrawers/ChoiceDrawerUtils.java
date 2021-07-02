package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.view.gui.ButtonContainer;
import it.polimi.ingsw.psp26.view.gui.sounds.SoundManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addSelectionShadow;

/**
 * Class that contains methods that are used in other extension of the ChoicesDrawer interface.
 */
public class ChoiceDrawerUtils {

    /**
     * Method to add a set on action listener on the given button container.
     * It changes the images and plays sound effects on button click.
     * It sets the button container as selected when clicked at the first time and
     * it resets the button on the next click.
     *
     * @param buttonContainer button container to decorate
     * @param imageView       image view associated to the button
     * @param originalImage   original image in steady state
     */
    @SuppressWarnings("rawtypes")
    public static void addSelectionListener(ButtonContainer buttonContainer, ImageView imageView, Image originalImage) {

        buttonContainer.setOnAction(actionEvent -> {
            if (!buttonContainer.isClicked()) {
                buttonContainer.setClicked(true);
                Image imageSelected = addSelectionShadow(originalImage);
                imageView.setImage(imageSelected);
                SoundManager.getInstance().setSoundEffect(SoundManager.DIALOG_SOUND);

            } else {
                buttonContainer.setClicked(false);
                imageView.setImage(originalImage);
                SoundManager.getInstance().setSoundEffect(SoundManager.RESOURCE_SOUND);
            }
            buttonContainer.setGraphic(imageView);
        });
    }
}
