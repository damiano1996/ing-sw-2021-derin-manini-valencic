package it.polimi.ingsw.psp26.view.gui.effects;

import it.polimi.ingsw.psp26.view.gui.cache.snapshots.SnapshotCache;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Class containing static methods to add light effects to images and image views.
 */
public class LightEffects {

    /**
     * Method to create a drop shadow object to simulate a primary light.
     *
     * @param ratio ratio of the view
     * @return drop shadow object
     */
    private static DropShadow getPrimaryLightShadow(float ratio) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(10 * ratio);
        dropShadow.setOffsetY(10 * ratio);
        dropShadow.setRadius(20 * ratio);
        dropShadow.setWidth(40 * ratio);
        dropShadow.setColor(new Color(0, 0, 0, 0.7));
        return dropShadow;
    }

    /**
     * Method to create a drop shadow object to simulate a secondary light.
     *
     * @param ratio ratio of the view
     * @return drop shadow object
     */
    private static DropShadow getSecondaryLightShadow(float ratio) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(-2 * ratio);
        dropShadow.setOffsetY(-2 * ratio);
        dropShadow.setRadius(40 * ratio);
        dropShadow.setWidth(10 * ratio);
        dropShadow.setColor(new Color(0, 0, 0, 0.4));
        return dropShadow;
    }

    /**
     * Method to get the image decorated with the shadow effect.
     * It uses the SnapshotCache singleton object.
     *
     * @param image      image to decorate
     * @param dropShadow drop shadow object
     * @return image with shadow
     */
    private static Image addShadow(Image image, DropShadow dropShadow) {
        return SnapshotCache.getInstance().getShadowedImage(image, dropShadow);
    }

    /**
     * Method to add primary and secondary lights to the image.
     *
     * @param image image to decorate
     * @param ratio ratio of the view
     * @return image with light effects
     */
    public static Image addLightEffects(Image image, float ratio) {
        image = addShadow(image, getPrimaryLightShadow(ratio));
        image = addShadow(image, getSecondaryLightShadow(ratio));
        return image;
    }

    /**
     * Method create a drop shadow of yellow color.
     *
     * @return drop shadow object
     */
    private static DropShadow getSelectionShadow() {
        return getSelectionShadow(Color.YELLOW);
    }

    /**
     * Method to create a drop shadow object of a given light color
     *
     * @param color color object
     * @return drop shadow object
     */
    private static DropShadow getSelectionShadow(Color color) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(0);
        dropShadow.setColor(color);
        return dropShadow;
    }

    /**
     * Method to add the shadow of yellow color to the image.
     *
     * @param image image to decorate
     * @return image with yellow shadow
     */
    public static Image addSelectionShadow(Image image) {
        return addShadow(image, getSelectionShadow());
    }

    /**
     * Method to add shadow of a given color to the image.
     *
     * @param image image to decorate
     * @param color color object
     * @return image with shadow of the given color
     */
    public static Image addSelectionShadow(Image image, Color color) {
        return addShadow(image, getSelectionShadow(color));
    }
}
