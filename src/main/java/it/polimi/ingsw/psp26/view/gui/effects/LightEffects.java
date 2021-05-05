package it.polimi.ingsw.psp26.view.gui.effects;

import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class LightEffects {

    private static DropShadow getPrimaryLightShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(10);
        dropShadow.setOffsetY(10);
        dropShadow.setRadius(20);
        dropShadow.setWidth(40);
        return dropShadow;
    }

    private static DropShadow getSecondaryLightShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(-2);
        dropShadow.setOffsetY(-2);
        dropShadow.setRadius(40);
        dropShadow.setWidth(10);
        return dropShadow;
    }

    private static Image addShadow(Image image, DropShadow dropShadow) {
        ImageView imageView = new ImageView(image);
        imageView.setEffect(dropShadow);

        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        return imageView.snapshot(snapshotParameters, null);
    }

    public static Image addLightEffects(Image image) {
        image = addShadow(image, getPrimaryLightShadow());
        image = addShadow(image, getSecondaryLightShadow());
        return image;
    }

    private static DropShadow getSelectionShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(0);
        dropShadow.setColor(Color.YELLOW);
        return dropShadow;
    }

    public static Image addSelectionShadow(Image image) {
        return addShadow(image, getSelectionShadow());
    }

}
