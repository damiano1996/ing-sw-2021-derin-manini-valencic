package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.view.gui.ButtonContainer;
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
            } else {
                buttonContainer.setClicked(false);
                imageView.setImage(originalImage);
            }
            buttonContainer.setGraphic(imageView);
        });
    }
}
