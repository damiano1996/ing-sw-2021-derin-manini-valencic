package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.view.gui.ButtonContainer;
import it.polimi.ingsw.psp26.view.gui.sounds.SoundManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addSelectionShadow;

public class ChoiceDrawerUtils {

    @SuppressWarnings("rawtypes")
    public static void addSelectionListener(ButtonContainer buttonContainer, ImageView imageView, Image originalImage) {

        buttonContainer.setOnAction(actionEvent -> {
            if (!buttonContainer.isClicked()) {
                buttonContainer.setClicked(true);
                Image imageSelected = addSelectionShadow(originalImage);
                imageView.setImage(imageSelected);
                SoundManager.getInstance().setSoundEffect("button_click_01.wav");

            } else {
                buttonContainer.setClicked(false);
                imageView.setImage(originalImage);
                SoundManager.getInstance().setSoundEffect("button_click_02.wav");
            }
            buttonContainer.setGraphic(imageView);
        });
    }
}
