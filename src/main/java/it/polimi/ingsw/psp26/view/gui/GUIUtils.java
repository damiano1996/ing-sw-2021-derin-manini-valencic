package it.polimi.ingsw.psp26.view.gui;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
        return setRoundedCorners(image, ratio, 50);
    }

    public static Image setRoundedCorners(Image image, float ratio, int arcSize) {
        ImageView imageView = new ImageView(image);

        Rectangle clip = new Rectangle(image.getWidth(), image.getHeight());
        clip.setArcWidth(arcSize * ratio);
        clip.setArcHeight(arcSize * ratio);
        imageView.setClip(clip);

        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        return imageView.snapshot(snapshotParameters, null);
    }

    public static ImageView getImageView(Image image, float xPosition, float yPosition) {
        ImageView imageView = new ImageView(image);
        imageView.setCache(true);
        imageView.setCacheHint(CacheHint.SPEED);
        imageView.setX(xPosition);
        imageView.setY(yPosition);
        return imageView;
    }

    public static Scene setTransparentBackground(Pane pane) {
        pane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(pane);
        scene.setFill(Color.TRANSPARENT);
        return scene;
    }

    public static Font getFont(int size, float ratio) {
        return Font.loadFont(getCompletePath("font/DancingScript-VariableFont_wght.ttf"), size * ratio);
    }

    public static void addStylesheet(Scene scene) {
        scene.getStylesheets().add(getCompletePath("stylesheets/stylesheet.css"));
    }

    public static void closeParentStageOfActionEvent(Event actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage dialog = (Stage) source.getScene().getWindow();
        dialog.close();
    }

}
