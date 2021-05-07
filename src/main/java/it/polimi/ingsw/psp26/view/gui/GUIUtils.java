package it.polimi.ingsw.psp26.view.gui;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.psp26.configurations.Configurations.RESOURCES_PATH;

public class GUIUtils {

    private final static Map<String, Image> images = new HashMap<>();

    public static String getCompletePath(String fileName) {
        return "file:" + RESOURCES_PATH + "gui/" + fileName;
    }

    public static Image loadImage(String fileName, int width) {
        String imageIdentifier = fileName + "__" + width;
        if (images.containsKey(imageIdentifier)) return images.get(imageIdentifier);

        System.out.println("Loading image: " + fileName);
        Image image = new Image(getCompletePath(fileName), width, width, true, true);
        images.put(imageIdentifier, image);
        return image;
    }

    public static Image setRoundedCorners(Image image, float ratio) {
        ImageView imageView = new ImageView(image);

        Rectangle clip = new Rectangle(image.getWidth(), image.getHeight());
        clip.setArcWidth(50 * ratio);
        clip.setArcHeight(50 * ratio);
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
