package it.polimi.ingsw.psp26.view.gui.modelcomponents.dialogcomponents;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.DevelopmentCardsGridDrawer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.RatioDrawer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.closeParentStageOfActionEvent;

public class DevelopmentCardsGridDialogDrawer extends RatioDrawer {

    private final Client client;
    private final DevelopmentCardsGridDrawer developmentGridDrawer;

    public DevelopmentCardsGridDialogDrawer(Client client, DevelopmentCardsGrid developmentCardsGrid, int maxWidth) {
        super(maxWidth);

        this.client = client;
        developmentGridDrawer = new DevelopmentCardsGridDrawer(client, developmentCardsGrid, initMaxWidth / 3);
    }

    @Override
    public Pane draw() {

        VBox rootPane = new VBox(10 * ratio);
        rootPane.setAlignment(Pos.CENTER);

        Text text = new Text("Development Cards");
        text.setId("title");
        rootPane.getChildren().add(text);

        Text description = new Text(
                "*Click on the card that you want to buy.");
        description.setId("title");
        description.setStyle("-fx-font-size: " + 40 * ratio + ";");
        rootPane.getChildren().add(description);

        rootPane.getChildren().add(new HBox(developmentGridDrawer.draw()));

        Button confirmationButton = new Button("Undo");
        confirmationButton.setId("undo-button");
        confirmationButton.setOnAction(actionEvent -> {
            client.sendUndoMessage();
            closeParentStageOfActionEvent(actionEvent);
            client.viewNext();
        });

        rootPane.getChildren().add(confirmationButton);
        return rootPane;
    }

}
