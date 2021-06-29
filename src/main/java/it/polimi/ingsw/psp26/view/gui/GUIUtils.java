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

import static it.polimi.ingsw.psp26.view.ViewUtils.getCompletePath;

/**
 * Utility class that contains utility methods for the GUI package.
 */
public class GUIUtils {

    /**
     * Loads an image from disk.
     *
     * @param fileName the name of the Image to load
     * @param width    the width of the Image
     * @return the loaded Image in the correct width
     */
    public static Image loadImage(String fileName, int width) {
        return ImageLoaderCache.getInstance().loadImageFromFile(fileName, width);
    }


    /**
     * Sets round corners on the given Image.
     *
     * @param image the Image to round the corners
     * @param ratio the screen ratio
     * @return the Image with rounded corners
     */
    public static Image setRoundedCorners(Image image, float ratio) {
        return setRoundedCorners(image, ratio, 50);
    }


    /**
     * Sets round corners on the given Image.
     * The arcSize of the corners must be passed as a parameter.
     *
     * @param image   the Image to round the corners
     * @param ratio   the screen ratio
     * @param arcSize the size of the rounded corners
     * @return the Image with rounded corners
     */
    public static Image setRoundedCorners(Image image, float ratio, int arcSize) {
        return SnapshotCache.getInstance().getRoundedCornerImage(image, ratio, arcSize);
    }


    /**
     * Method that returns an ImageView containing the given Image.
     *
     * @param image     the Image to put into the ImageView
     * @param xPosition the X position of the ImageView on screen
     * @param yPosition the Y position of the ImageView on screen
     * @return the ImageView containing the given Image
     */
    public static ImageView getImageView(Image image, float xPosition, float yPosition) {
        ImageView imageView = new ImageView(image);
        imageView.setCache(true);
        imageView.setCacheHint(CacheHint.SPEED);
        imageView.setX(xPosition);
        imageView.setY(yPosition);
        return imageView;
    }


    /**
     * Sets a transparent background in the given Pane
     *
     * @param pane the Pane to set the transparent background
     * @return the Pane with the transparent background
     */
    public static Scene setTransparentBackground(Pane pane) {
        pane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(pane);
        scene.setFill(Color.TRANSPARENT);
        return scene;
    }


    /**
     * Getter of the game font.
     *
     * @param size  the size of the font
     * @param ratio the screen ratio
     * @return the game font in the correct size
     */
    public static Font getFont(int size, float ratio) {
        return Font.loadFont(getCompletePath("/gui/font/DancingScript-VariableFont_wght.ttf"), size * ratio);
    }


    /**
     * Adds a stylesheet to the given Scene.
     *
     * @param scene the Scene to add the stylesheet
     */
    public static void addStylesheet(Scene scene) {
        scene.getStylesheets().add(getCompletePath("/gui/stylesheets/stylesheet.css"));
    }


    /**
     * Closes the parent node of the node where this method is called.
     *
     * @param actionEvent the event that makes the parent node close
     */
    public static void closeParentStageOfActionEvent(Event actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage dialog = (Stage) source.getScene().getWindow();
        dialog.close();
    }


    /**
     * Checks if two Images are equal.ù
     * Images are equals if they are the sam size or if they have the same pixels.
     *
     * @param image1 the first Image to compare
     * @param image2 the second Image to compare
     * @return true if the images are equal, false otherwise
     */
    public static boolean areImagesEqual(Image image1, Image image2) {
        if (image1.getWidth() != image2.getWidth() ||
                image1.getHeight() != image2.getHeight()) return false;

        try {
            for (int i = 0; i < image1.getWidth(); i++)
                for (int j = 0; j < image1.getHeight(); j++)
                    if (image1.getPixelReader().getArgb(i, j) != image2.getPixelReader().getArgb(i, j)) return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
}
