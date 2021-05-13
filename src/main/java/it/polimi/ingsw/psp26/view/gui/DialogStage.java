package it.polimi.ingsw.psp26.view.gui;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static it.polimi.ingsw.psp26.view.gui.FramePane.addCoolFrame;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.addStylesheet;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.setTransparentBackground;

public class DialogStage {

    public static Stage getDialog(Pane content, int maxContentWidth, int maxContentHeight, float marginFactor, boolean roundedCorners, int arcSize, float ratio) {

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setAlwaysOnTop(true);
        dialog.centerOnScreen();

        StackPane stackPane = addCoolFrame(content, maxContentWidth, maxContentHeight, marginFactor, roundedCorners, arcSize, ratio);

        Scene scene = setTransparentBackground(stackPane);
        addStylesheet(scene);
        dialog.setScene(scene);

        return dialog;
    }

    public static Stage getDialog(Pane content, int maxContentWidth, int maxContentHeight, float marginFactor, float ratio) {
        return getDialog(content, maxContentWidth, maxContentHeight, marginFactor, true, 350, ratio);
    }
}
