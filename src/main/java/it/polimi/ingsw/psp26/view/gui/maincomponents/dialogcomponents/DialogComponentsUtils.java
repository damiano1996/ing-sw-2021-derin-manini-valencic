package it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents;

import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.sounds.SoundManager;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.closeParentStageOfActionEvent;

public class DialogComponentsUtils {

    /**
     * Method to set the pane for the dialog.
     * It collects common setups for the pane that must be shown in a dialog.
     *
     * @param rootPane    root pane in which other components will be placed
     * @param description text object containing the description of the actions that are allowed
     * @param fontSize    font size
     * @param draw        pane (usually for a model component)
     * @param client      client object
     * @return decorated root pane
     */
    public static Pane getDialogPane(VBox rootPane, Text description, float fontSize, Pane draw, Client client) {
        description.setId("title");
        description.setStyle("-fx-font-size: " + fontSize + ";");
        rootPane.getChildren().add(description);

        rootPane.getChildren().add(new HBox(draw));

        rootPane.getChildren().add(getUndoButton(client));
        return rootPane;
    }

    /**
     * Method to get a button for undo action.
     * It initializes a button with the action event.
     * If the button will be triggered by the event,
     * it will request to the server for the undo option.
     *
     * @param client client object
     * @return button object
     */
    public static Button getUndoButton(Client client) {
        Button confirmationButton = new Button("Undo");
        confirmationButton.setId("undo-button");
        confirmationButton.setOnAction(actionEvent -> {

            SoundManager.getInstance().setSoundEffect("button_click_01.wav");

            client.sendUndoMessage();
            closeParentStageOfActionEvent(actionEvent);
            client.viewNext();
        });
        return confirmationButton;
    }
}
