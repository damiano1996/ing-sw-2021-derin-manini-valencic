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

public class DialogStage {


    public static Stage getDialog(Stage primaryStage, Pane content) {

        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(primaryStage);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initStyle(StageStyle.TRANSPARENT);
        // dialog.setAlwaysOnTop(true);
        dialog.centerOnScreen();

        primaryStage.xProperty().addListener((obs, oldVal, newVal) -> dialog.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - dialog.getWidth() / 2));
        primaryStage.yProperty().addListener((obs, oldVal, newVal) -> dialog.setY(primaryStage.getY() + primaryStage.getHeight() / 2 - dialog.getHeight() / 2));

        int sideSize = Math.max(getWindowWidth(), getWindowHeight());
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
