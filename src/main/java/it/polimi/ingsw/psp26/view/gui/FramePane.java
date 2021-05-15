package it.polimi.ingsw.psp26.view.gui;

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
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getCompletePath;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addLightEffects;

public class FramePane {

    public static StackPane drawThumbNail(Stage primaryStage, Pane content, Pane contentEnhanced, int contentWidth, int contentHeight) {
        StackPane thumbNailStackPane = addCoolFrame(content, contentWidth, contentHeight);

        Stage dialog = getDialog(primaryStage, contentEnhanced);

        thumbNailStackPane.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> dialog.show());

        dialog.getScene().getRoot().addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> dialog.close());

        return thumbNailStackPane;
    }


    public static StackPane addCoolFrame(Pane content) {
        return addBackground(content, "window_graphics/frame.png", true, false, 0, 0);
    }

    public static StackPane addCoolFrame(Pane content, int contentWidth, int contentHeight) {
        return addBackground(content, "window_graphics/frame.png", false, true, contentWidth, contentHeight);
    }

    public static StackPane addBackground(Pane content, int contentWidth, int contentHeight) {
        return addBackground(content, "window_graphics/background.png", false, true, contentWidth, contentHeight);
    }

    private static StackPane addBackground(Pane content, String backgroundFileName, boolean lightEffects, boolean setSize, int width, int height) {
        StackPane stackPane = new StackPane();

        Image backgroundImage = new Image(
                getCompletePath(backgroundFileName),
                1.2f * ((setSize) ? width : stackPane.getWidth()),
                1.2f * ((setSize) ? height : stackPane.getHeight()),
                false, true
        );

        if (lightEffects) backgroundImage = addLightEffects(backgroundImage, 1);

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
