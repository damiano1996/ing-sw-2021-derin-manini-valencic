package it.polimi.ingsw.psp26.view.gui.modelcomponents.dialogcomponents;

import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.ActionTokenDrawer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.RatioDrawer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.List;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.closeParentStageOfActionEvent;

public class ActionTokenDialogDrawer extends RatioDrawer {

    private final Client client;
    private final ActionTokenDrawer actionTokenDrawer;

    public ActionTokenDialogDrawer(Client client, List<ActionToken> actionTokensToDisplay, int maxWidth) {
        super(maxWidth);

        this.client = client;
        actionTokenDrawer = new ActionTokenDrawer(actionTokensToDisplay, maxWidth);
    }


    @Override
    public Pane draw() {
        return adjustLayout();
    }


    /**
     * @return A correct BorderPane of the Action Tokens dialog
     */
    private BorderPane adjustLayout() {
        BorderPane borderPane = new BorderPane();

        // Creating the dialog title
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);

        Text actionTokenTitle = new Text("Lorenzo plays!");
        actionTokenTitle.setId("title");
        actionTokenTitle.setStyle("-fx-font-size: " + 60);
        titleBox.getChildren().add(actionTokenTitle);


        // Filling the BorderPane
        borderPane.setTop(titleBox);
        borderPane.setCenter(actionTokenDrawer.draw());
        borderPane.setBottom(createDoneButton());

        return borderPane;
    }


    /**
     * @return A Button that lets the Match continue
     */
    private HBox createDoneButton() {
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);

        // Creating the button
        Button doneButton = new Button("Done");
        doneButton.setOnAction(actionEvent -> {
            closeParentStageOfActionEvent(actionEvent);
            client.viewNext();
        });

        buttonBox.getChildren().add(doneButton);

        return buttonBox;
    }

}
