package it.polimi.ingsw.psp26.view.gui.modelcomponents;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import javafx.scene.image.Image;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.loadImage;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.setRoundedCorners;
import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addLightEffects;

public class ModelDrawUtils {

    public static Image getCard(DevelopmentCard developmentCard, float ratio) {
        return getCard("development_cards/" + developmentCard.toString() + ".png", ratio);
    }

    public static Image getCard(LeaderCard leaderCard, float ratio) {
        return getCard("leader_cards/" + leaderCard.toString() + ".png", ratio);
    }

    public static Image getLeaderBack(float ratio) {
        return getCard("leader_cards/BACK.png", ratio);
    }

    private static Image getCard(String fileName, float ratio) {
        Image image = loadImage(fileName, (int) (700 * ratio));
        image = setRoundedCorners(image, ratio);
        image = addLightEffects(image, ratio);
        return image;
    }
}
