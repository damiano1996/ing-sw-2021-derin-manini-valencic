package it.polimi.ingsw.psp26.view.gui;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static it.polimi.ingsw.psp26.configurations.Configurations.RESOURCES_PATH;

public class GUIUtils {

    public static String getCompletePath(String fileName) {
        return "file:" + RESOURCES_PATH + "gui/" + fileName;
    }

    public static Image loadImage(String fileName, int width) {
        System.out.println("Loading image: " + fileName);
        return new Image(getCompletePath(fileName), width, width, true, true);
    }

    public static Image setRoundedCorners(Image image) {
        ImageView imageView = new ImageView(image);

        Rectangle clip = new Rectangle(image.getWidth(), image.getHeight());
        clip.setArcWidth(50);
        clip.setArcHeight(50);
        imageView.setClip(clip);

        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        return imageView.snapshot(snapshotParameters, null);
    }

    public static ImageView getImageView(Image image, float xPosition, float yPosition) {
        ImageView imageView = new ImageView(image);
        imageView.setX(xPosition);
        imageView.setY(yPosition);
        return imageView;
    }
}
