package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.view.gui.CheckBoxContainer;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static it.polimi.ingsw.psp26.view.gui.GUIConfigurations.REFERENCE_WIDTH;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.*;
import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addSelectionShadow;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.ModelDrawUtils.getCard;

public class LeaderCardChoicesDrawer implements ChoicesDrawer<LeaderCard> {

    @Override
    public Button decorateButton(Button button, LeaderCard choice) {
        button.setGraphic(getCardImageView(choice));
        return button;
    }

    @Override
    public CheckBoxContainer decorateCheckBoxContainer(CheckBoxContainer checkBoxContainer, LeaderCard choice) {
        checkBoxContainer.setGraphic(getCardImageView(choice));

        checkBoxContainer.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (checkBoxContainer.isSelected()) {
                ImageView imageView = getCardImageView(choice);
                Image image = imageView.getImage();
                image = addSelectionShadow(image);
                imageView.setImage(image);
                checkBoxContainer.setGraphic(imageView);
            } else {
                checkBoxContainer.setGraphic(getCardImageView(choice));
            }
        });
        return checkBoxContainer;
    }

    private ImageView getCardImageView(LeaderCard leaderCard) {
        Image leaderCardImage = getCard(leaderCard, 0.5f * getWindowWidth() / REFERENCE_WIDTH);
        return getImageView(leaderCardImage, 0, 0);
    }
}
