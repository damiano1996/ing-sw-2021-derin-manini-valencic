package it.polimi.ingsw.psp26.view.gui;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static it.polimi.ingsw.psp26.view.gui.FramePane.addCoolFrame;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.addStylesheet;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.setTransparentBackground;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.getWindowHeight;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.getWindowWidth;

/**
 * Utility class that contains a method used when a Stage must be shown as a Dialog window.
 */
public class DialogStage {

    /**
     * Method used to get the DialogStage.
     * First, the method sets the Owner, the Modality and the Style of the Stage.
     * It then sets the dimensions and the position of the Stage.
     * A frame is then added.
     * At the end, the method applies the css stylesheet.
     *
     * @param primaryStage the Owner of the DialogStage
     * @param content      the elements that will be displayed as a DialogStage
     * @return the built DialogStage
     */
    public static Stage getDialog(Stage primaryStage, Pane content) {
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.centerOnScreen();

        primaryStage.xProperty().addListener((obs, oldVal, newVal) -> dialog.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - dialog.getWidth() / 2));
        primaryStage.yProperty().addListener((obs, oldVal, newVal) -> dialog.setY(primaryStage.getY() + primaryStage.getHeight() / 2 - dialog.getHeight() / 2));

        int sideSize = Math.min(getWindowWidth(), getWindowHeight());
        dialog.setWidth(sideSize);
        dialog.setHeight(sideSize);
        dialog.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - dialog.getWidth() / 2);
        dialog.setY(primaryStage.getY() + primaryStage.getHeight() / 2 - dialog.getHeight() / 2);

        Pane stackPane = addCoolFrame(content);

        Scene scene = setTransparentBackground(stackPane);
        addStylesheet(scene);
        dialog.setScene(scene);

        return dialog;
    }
}
