package it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import javafx.scene.image.Image;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.loadImage;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.setRoundedCorners;
import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addLightEffects;

/**
 * Class that contains several methods used to draw model components.
 * It is an utility class.
 */
public class ModelDrawUtils {

    /**
     * Getter of the image of a development card.
     *
     * @param developmentCard development card
     * @param ratio           ratio for the image
     * @return image representing the development card
     */
    public static Image getCard(DevelopmentCard developmentCard, float ratio) {
        return getCard("development_cards/" + developmentCard.toString() + ".png", ratio);
    }

    /**
     * Getter of the image of a leader card.
     *
     * @param leaderCard leader card
     * @param ratio      ratio for the image
     * @return image representing the leader card
     */
    public static Image getCard(LeaderCard leaderCard, float ratio) {
        return getCard("leader_cards/" + leaderCard.toString() + ".png", ratio);
    }

    /**
     * Getter of the leader card back.
     *
     * @param ratio ratio for the image
     * @return image representing the back side of the leader cards
     */
    public static Image getLeaderBack(float ratio) {
        return getCard("leader_cards/BACK.png", ratio);
    }

    /**
     * Getter of a card from file name.
     * It loads the image from the file name and adds rounded corners and light effects.
     *
     * @param fileName file name
     * @param ratio    ratio that will be multiplied by the default card size
     * @return image
     */
    private static Image getCard(String fileName, float ratio) {
        Image image = loadImage(fileName, (int) (700 * ratio));
        image = setRoundedCorners(image, ratio);
        image = addLightEffects(image, ratio);
        return image;
    }

    /**
     * Getter of a resource image.
     * Given the resource object, it loads the associated image and adds the light effects.
     *
     * @param resource resource object
     * @param ratio    ratio for the image
     * @return image representing the resource
     */
    public static Image getResourceImage(Resource resource, float ratio) {
        Image resourceImage = loadImage("resources/" + resource + ".png", (int) (100 * ratio));
        resourceImage = addLightEffects(resourceImage, ratio);
        return resourceImage;
    }
}
