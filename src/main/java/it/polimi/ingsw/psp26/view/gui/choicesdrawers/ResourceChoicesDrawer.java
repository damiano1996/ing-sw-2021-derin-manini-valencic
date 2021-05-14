package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.view.gui.CheckBoxContainer;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getGeneralRatio;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addSelectionShadow;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.ModelDrawUtils.getResource;

public class ResourceChoicesDrawer implements ChoicesDrawer<Resource> {

    @Override
    public Button decorateButton(Button button, Resource choice) {
        button.setGraphic(getResourceImageView(choice));
        return button;
    }

    @Override
    public CheckBoxContainer decorateCheckBoxContainer(CheckBoxContainer checkBoxContainer, Resource choice) {
        checkBoxContainer.setGraphic(getResourceImageView(choice));

        checkBoxContainer.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (checkBoxContainer.isSelected()) {
                ImageView imageView = getResourceImageView(choice);
                Image image = imageView.getImage();
                image = addSelectionShadow(image);
                imageView.setImage(image);
                checkBoxContainer.setGraphic(imageView);
            } else {
                checkBoxContainer.setGraphic(getResourceImageView(choice));
            }
        });
        return checkBoxContainer;
    }

    private ImageView getResourceImageView(Resource resource) {
        return getImageView(getResource(resource, getGeneralRatio()), 0, 0);
    }
}
