package it.polimi.ingsw.psp26.view.gui.effects;

import it.polimi.ingsw.psp26.view.gui.cache.snapshots.SnapshotCache;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class LightEffects {

    private static DropShadow getPrimaryLightShadow(float ratio) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(10 * ratio);
        dropShadow.setOffsetY(10 * ratio);
        dropShadow.setRadius(20 * ratio);
        dropShadow.setWidth(40 * ratio);
        dropShadow.setColor(new Color(0, 0, 0, 0.7));
        return dropShadow;
    }

    private static DropShadow getSecondaryLightShadow(float ratio) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(-2 * ratio);
        dropShadow.setOffsetY(-2 * ratio);
        dropShadow.setRadius(40 * ratio);
        dropShadow.setWidth(10 * ratio);
        dropShadow.setColor(new Color(0, 0, 0, 0.4));
        return dropShadow;
    }

    private static Image addShadow(Image image, DropShadow dropShadow) {
        return SnapshotCache.getInstance().getShadowedImage(image, dropShadow);
    }

    public static Image addLightEffects(Image image, float ratio) {
        image = addShadow(image, getPrimaryLightShadow(ratio));
        image = addShadow(image, getSecondaryLightShadow(ratio));
        return image;
    }

    private static DropShadow getSelectionShadow() {
        return getSelectionShadow(Color.YELLOW);
    }

    private static DropShadow getSelectionShadow(Color color) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(0);
        dropShadow.setColor(color);
        return dropShadow;
    }

    public static Image addSelectionShadow(Image image) {
        return addShadow(image, getSelectionShadow());
    }

    public static Image addSelectionShadow(Image image, Color color) {
        return addShadow(image, getSelectionShadow(color));
    }
}
