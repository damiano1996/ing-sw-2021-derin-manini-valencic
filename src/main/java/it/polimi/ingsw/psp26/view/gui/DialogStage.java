package it.polimi.ingsw.psp26.view.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.addStylesheet;

public class DialogStage {


    public static Stage getDialog(Stage primaryStage, Pane content) {

        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(primaryStage);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.initStyle(StageStyle.TRANSPARENT);
        // dialog.setAlwaysOnTop(true);
        dialog.centerOnScreen();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(DialogStage.class.getResource("/gui/fxml/dialog.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();

            StackPane stackPane = (StackPane) fxmlLoader.getNamespace().get("container");
            stackPane.getChildren().add(content);

            // Scene scene = setTransparentBackground(content);
            Scene scene = new Scene(anchorPane);
            addStylesheet(scene);
            dialog.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dialog;
    }
}
