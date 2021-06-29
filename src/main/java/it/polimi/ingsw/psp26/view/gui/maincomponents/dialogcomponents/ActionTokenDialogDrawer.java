package it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents;

import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.maincomponents.RatioDrawer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.ActionTokenDrawer;
import it.polimi.ingsw.psp26.view.gui.sounds.SoundManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.List;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.closeParentStageOfActionEvent;

/**
 * Class to provide the content for the dialog that has to show the action tokens.
 * It uses the ActionTokenDrawer to setup the pane for the dialog, adding the title.
 */
public class ActionTokenDialogDrawer extends RatioDrawer {

    private final Client client;
    private final ActionTokenDrawer actionTokenDrawer;

    /**
     * Class constructor.
     *
     * @param client                client object
     * @param actionTokensToDisplay list of action tokens that must be displayed
     * @param maxWidth              maximum width for the pane
     */
    public ActionTokenDialogDrawer(Client client, List<ActionToken> actionTokensToDisplay, int maxWidth) {
        super(maxWidth);

        this.client = client;
        actionTokenDrawer = new ActionTokenDrawer(actionTokensToDisplay, maxWidth);
    }


    /**
     * Method to create the pane containing the action tokens.
     *
     * @return pane
     */
    @Override
    public Pane draw() {
        BorderPane borderPane = new BorderPane();

        // Creating the dialog title
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);

        Text actionTokenTitle = new Text("Lorenzo plays!");
        actionTokenTitle.setId("title");
        actionTokenTitle.setStyle("-fx-font-size: " + 60 + ";");
        titleBox.getChildren().add(actionTokenTitle);

        // Filling the BorderPane
        borderPane.setTop(titleBox);
        borderPane.setCenter(actionTokenDrawer.draw());
        borderPane.setBottom(createDoneButton());

        return borderPane;
    }


    /**
     * Method to setup the button to exit from the dialog and to request for the next action.
     *
     * @return horizontal box containing the button
     */
    private HBox createDoneButton() {
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);

        // Creating the button
        Button doneButton = new Button("Done");
        doneButton.setOnAction(actionEvent -> {

            SoundManager soundManager = SoundManager.getInstance();

            soundManager.setSoundEffect(SoundManager.DIALOGSOUND);
            closeParentStageOfActionEvent(actionEvent);
            client.viewNext();
        });

        buttonBox.getChildren().add(doneButton);

        return buttonBox;
    }

}
