package it.polimi.ingsw.psp26.view.gui;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.*;

public class FramePane {

    public static StackPane drawThumbNail(Pane content, Pane contentEnhanced, int contentMaxWidth, int contentEnhancedMaxWidth) {
        float marginFactor = 1.2f;

        StackPane thumbNailStackPane = addCoolFrame(content, contentMaxWidth, marginFactor, false);

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setAlwaysOnTop(true);
        StackPane enhancedStackPane = addCoolFrame(contentEnhanced, contentEnhancedMaxWidth, marginFactor, true);
        Scene dialogScene = new Scene(enhancedStackPane, marginFactor * contentEnhancedMaxWidth, marginFactor * contentEnhancedMaxWidth);
        dialogScene.setFill(Color.TRANSPARENT);

        dialog.setScene(dialogScene);

        thumbNailStackPane.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            dialog.setX(mouseEvent.getScreenX() - 200);
            dialog.setY(mouseEvent.getScreenY() - 200);
            dialog.show();
        });

        enhancedStackPane.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> dialog.close());

        return thumbNailStackPane;
    }

    private static StackPane addCoolFrame(Pane content, int contentMaxWidth, float marginFactor, boolean roundedCorners) {
        return addBackground(content, contentMaxWidth, contentMaxWidth, "window_graphics/frame.png", marginFactor, roundedCorners);
    }

    public static StackPane addBackground(Pane content, int contentMaxWidth, int contentMaxHeight, boolean roundedCorners) {
        return addBackground(content, contentMaxWidth, contentMaxHeight, "window_graphics/background.png", 1.2f, roundedCorners);
    }

    private static StackPane addBackground(Pane content, int contentMaxWidth, int contentMaxHeight, String backgroundFileName, float marginFactor, boolean roundedCorners) {
        StackPane stackPane = new StackPane();

        stackPane.setPrefSize(marginFactor * contentMaxWidth, marginFactor * contentMaxHeight);

        Image backgroundImage = new Image(
                getCompletePath(backgroundFileName),
                marginFactor * contentMaxWidth,
                marginFactor * contentMaxHeight,
                false, true
        );
        if (roundedCorners) backgroundImage = setRoundedCorners(backgroundImage, 7);

        ImageView imageView = getImageView(backgroundImage, 0, 0);
        stackPane.getChildren().add(imageView);

        // Building a group to center the content, otherwise StackPane will ignore the Pane...
        Group group = new Group(content);
        stackPane.getChildren().add(group);

        StackPane.setAlignment(group, Pos.CENTER);

        return stackPane;
    }
}
