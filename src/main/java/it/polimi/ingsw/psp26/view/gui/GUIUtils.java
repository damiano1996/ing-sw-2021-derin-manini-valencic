package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.view.gui.cache.ImageLoaderCache;
import it.polimi.ingsw.psp26.view.gui.cache.snapshots.SnapshotCache;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static it.polimi.ingsw.psp26.configurations.Configurations.RESOURCES_PATH;

public class GUIUtils {


    public static String getCompletePath(String fileName) {
        return "file:" + RESOURCES_PATH + "gui/" + fileName;
    }

    public static Image loadImage(String fileName, int width) {
        return ImageLoaderCache.getInstance().loadImageFromFile(fileName, width);
    }

    public static Image setRoundedCorners(Image image, float ratio) {
        return setRoundedCorners(image, ratio, 50);
    }

    public static Image setRoundedCorners(Image image, float ratio, int arcSize) {
        return SnapshotCache.getInstance().getRoundedCornerImage(image, ratio, arcSize);
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

    public static boolean areImagesEqual(Image image1, Image image2) {
        if (image1.getWidth() != image2.getWidth() ||
                image1.getHeight() != image1.getHeight()) return false;

        try {
            for (int i = 0; i < image1.getWidth(); i++)
                for (int j = 0; j < image1.getWidth(); j++)
                    if (image1.getPixelReader().getArgb(i, j) != image2.getPixelReader().getArgb(i, j)) return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
