package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.view.gui.cache.ImageLoaderCache;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static it.polimi.ingsw.psp26.view.gui.DialogStage.getDialog;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.getGeneralRatio;
import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addLightEffects;

/**
 * Class that contains static methods to add a background to panes and to create thumbnails.
 */
public class FramePane {

    /**
     * Method that adds to the content pane a background and
     * creates a dialog in which content enhanced will be placed.
     * On mouse entered event on the content pane, the dialog will be shown.
     * On mouse exited event the the dialog will be closed.
     *
     * @param primaryStage    stage in which content pane will be placed in
     * @param content         pane that must be decorated as thumbnail
     * @param contentEnhanced larger version of the content that will be shown in the dialog
     * @param contentWidth    width of the content pane
     * @param contentHeight   height of the content pane
     * @return stack pane of the decorated content with above cited properties
     */
    public static StackPane drawThumbNail(Stage primaryStage, Pane content, Pane contentEnhanced, int contentWidth, int contentHeight) {
        StackPane thumbNailStackPane = addCoolFrame(content, false, getGeneralRatio(), (int) (contentWidth * 1.2f), (int) (contentHeight * 1.2f));

        Stage dialog = getDialog(primaryStage, contentEnhanced);

        thumbNailStackPane.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> dialog.show());

        dialog.getScene().getRoot().addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> dialog.close());

        return thumbNailStackPane;
    }

    /**
     * Method to add a background with a frame to the content pane.
     * With light effects and adaptive size.
     *
     * @param content pane to decorate with background
     * @return stack pane of the decorated content pane
     */
    public static StackPane addCoolFrame(Pane content) {
        return addBackground(content, "window_graphics/frame.png", true, false, 0, 0);
    }

    /**
     * Method to add a background with a frame to the content pane.
     *
     * @param content       pane to decorate with background
     * @param lightEffects  boolean to add or not the light effects to the background
     * @param lightRatio    ratio of the view
     * @param contentWidth  width for the background
     * @param contentHeight height for the background
     * @return stack pane of the decorated content pane
     */
    public static StackPane addCoolFrame(Pane content, boolean lightEffects, float lightRatio, int contentWidth, int contentHeight) {
        return addBackground(content, "window_graphics/frame.png", lightEffects, lightRatio, true, contentWidth, contentHeight);
    }

    /**
     * Method to add a blue background to the content pane.
     * Without light effects.
     *
     * @param content pane to decorate with background
     * @return stack pane of the decorated content pane
     */
    public static StackPane addBackground(Pane content) {
        return addBackground(content, "window_graphics/background.png", false, false, 0, 0);
    }

    /**
     * Method to add a blue background to the content pane.
     *
     * @param content       pane to decorate with background
     * @param contentWidth  width for the background
     * @param contentHeight height for the background
     * @return stack pane of the decorated content pane
     */
    public static StackPane addBackground(Pane content, int contentWidth, int contentHeight) {
        return addBackground(content, "window_graphics/background.png", false, true, contentWidth, contentHeight);
    }

    /**
     * Method to add a background to a pane.
     * With light ratio sets equal one.
     *
     * @param content            pane to decorate with background
     * @param backgroundFileName file name of the image that must be used as background
     * @param lightEffects       boolean to add or not the light effects to the background
     * @param setSize            boolean used to customize width and height
     * @param width              width for the background
     * @param height             height for the background
     * @return stack pane of the decorated content pane
     */
    private static StackPane addBackground(Pane content, String backgroundFileName, boolean lightEffects, boolean setSize, int width, int height) {
        return addBackground(content, backgroundFileName, lightEffects, 1, setSize, width, height);
    }

    /**
     * Method that adds a background to a pane.
     * Method is parametrized to manage different settings.
     *
     * @param content            pane to decorate with background
     * @param backgroundFileName file name of the image that must be used as background
     * @param lightEffects       boolean to add or not the light effects to the background
     * @param lightRatio         ratio of the view
     * @param setSize            boolean used to customize width and height
     * @param width              width for the background
     * @param height             height for the background
     * @return stack pane of the decorated content pane
     */
    private static StackPane addBackground(Pane content, String backgroundFileName, boolean lightEffects, float lightRatio, boolean setSize, int width, int height) {
        StackPane stackPane = new StackPane();

        Image backgroundImage = ImageLoaderCache.getInstance().loadImageFromFile(
                backgroundFileName,
                ((setSize) ? width : 1.2f * stackPane.getWidth()),
                ((setSize) ? height : 1.2f * stackPane.getHeight()),
                false, true
        );

        if (lightEffects) backgroundImage = addLightEffects(backgroundImage, lightRatio);

        ImageView imageView = getImageView(backgroundImage, 0, 0);
        stackPane.getChildren().add(imageView);

        if (!setSize) {
            DoubleProperty max = new SimpleDoubleProperty();
            max.bind(Bindings.max(content.widthProperty().multiply(1.2f), content.heightProperty().multiply(1.2f)));
            imageView.fitWidthProperty().bind(max);
            imageView.fitHeightProperty().bind(max);
        }

        // Building a group to center the content, otherwise StackPane will ignore the Pane...
        Group group = new Group(content);
        stackPane.getChildren().add(group);
        StackPane.setAlignment(group, Pos.CENTER);

        return stackPane;
    }
}
