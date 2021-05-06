package it.polimi.ingsw.psp26.view.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.loadImage;

public class ThumbBox {

    public static StackPane drawThumbBox(Pane content, int contentMaxWidth) {
        StackPane stackPane = new StackPane();

        int stackPaneSize = (int) (1.2 * contentMaxWidth);
        stackPane.setPrefSize(stackPaneSize, stackPaneSize);

        Image backgroundImage = loadImage("window_graphics/frame.png", stackPaneSize);

        ImageView imageView = getImageView(backgroundImage, 0, 0);
        stackPane.getChildren().add(imageView);

        stackPane.getChildren().add(content);

        return stackPane;
    }
}
