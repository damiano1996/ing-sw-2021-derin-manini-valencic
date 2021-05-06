package it.polimi.ingsw.psp26.view.gui.modelcomponents;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import javafx.scene.image.Image;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.loadImage;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.setRoundedCorners;
import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addLightEffects;

public class ModelDrawUtils {

    public static Image getImage(DevelopmentCard developmentCard, float ratio) {
        Image developmentCardImage = loadImage("development_cards/" + developmentCard.toString() + ".png", (int) (700 * ratio));
        developmentCardImage = setRoundedCorners(developmentCardImage, ratio);
        developmentCardImage = addLightEffects(developmentCardImage, ratio);
        return developmentCardImage;
    }
}
