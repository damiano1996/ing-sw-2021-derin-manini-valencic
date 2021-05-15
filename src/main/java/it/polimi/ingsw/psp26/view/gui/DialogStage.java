package it.polimi.ingsw.psp26.view.gui;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static it.polimi.ingsw.psp26.view.gui.FramePane.addCoolFrame;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.addStylesheet;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.setTransparentBackground;

public class DialogStage {


    public static Stage getDialog(Stage primaryStage, Pane content) {

        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(primaryStage);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initStyle(StageStyle.TRANSPARENT);
        // dialog.setAlwaysOnTop(true);
        dialog.centerOnScreen();

        Pane stackPane = addCoolFrame(content);

        Scene scene = setTransparentBackground(stackPane);
        addStylesheet(scene);
        dialog.setScene(scene);

        return dialog;
    }
}
