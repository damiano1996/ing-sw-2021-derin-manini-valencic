package it.polimi.ingsw.psp26.view.gui;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static it.polimi.ingsw.psp26.view.gui.DialogStage.getDialog;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.*;

public class FramePane {

    public static StackPane drawThumbNail(Stage primaryStage, Pane content, Pane contentEnhanced, int contentMaxWidth, int contentEnhancedMaxWidth, float ratio) {
        float marginFactor = 1.2f;

        @SuppressWarnings("SuspiciousNameCombination")
        StackPane thumbNailStackPane = addCoolFrame(content, contentMaxWidth, contentMaxWidth, marginFactor, false, 0, ratio);

        @SuppressWarnings("SuspiciousNameCombination")
        Stage dialog = getDialog(primaryStage, contentEnhanced, contentEnhancedMaxWidth, contentEnhancedMaxWidth, ratio);

        thumbNailStackPane.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            dialog.setX(mouseEvent.getScreenX() - 300 * ratio);
            dialog.setY(mouseEvent.getScreenY() - 300 * ratio);
            dialog.show();
        });

        dialog.getScene().getRoot().addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> dialog.close());

        return thumbNailStackPane;
    }

    public static StackPane addCoolFrame(Pane content, int contentMaxWidth, int contentMaxHeight, float marginFactor, boolean roundedCorners, int arcSize, float ratio) {
        return addBackground(content, contentMaxWidth, contentMaxHeight, "window_graphics/frame.png", marginFactor, roundedCorners, arcSize, ratio);
    }

    public static StackPane addCoolFrame(Pane content, int contentMaxWidth, int contentMaxHeight, float marginFactor, float ratio) {
        return addBackground(content, contentMaxWidth, contentMaxHeight, "window_graphics/frame.png", marginFactor, false, 0, ratio);
    }

    public static StackPane addBackground(Pane content, int contentMaxWidth, int contentMaxHeight, float marginFactor, boolean roundedCorners, int arcSize, float ratio) {
        return addBackground(content, contentMaxWidth, contentMaxHeight, "window_graphics/background.png", marginFactor, roundedCorners, arcSize, ratio);
    }

    public static StackPane addBackground(Pane content, int contentMaxWidth, int contentMaxHeight, float marginFactor, float ratio) {
        return addBackground(content, contentMaxWidth, contentMaxHeight, "window_graphics/background.png", marginFactor, false, 0, ratio);
    }

    private static StackPane addBackground(Pane content, int contentMaxWidth, int contentMaxHeight, String backgroundFileName, float marginFactor, boolean roundedCorners, int arcSize, float ratio) {
        StackPane stackPane = new StackPane();

        stackPane.setPrefSize(marginFactor * ratio * contentMaxWidth, marginFactor * ratio * contentMaxHeight);

        Image backgroundImage = new Image(
                getCompletePath(backgroundFileName),
                marginFactor * ratio * contentMaxWidth,
                marginFactor * ratio * contentMaxHeight,
                false, true
        );
        if (roundedCorners) backgroundImage = setRoundedCorners(backgroundImage, ratio, arcSize);

        ImageView imageView = getImageView(backgroundImage, 0, 0);
        stackPane.getChildren().add(imageView);

        // Building a group to center the content, otherwise StackPane will ignore the Pane...
        Group group = new Group(content);
        stackPane.getChildren().add(group);

        StackPane.setAlignment(group, Pos.CENTER);

        return stackPane;
    }
}
