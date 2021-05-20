package it.polimi.ingsw.psp26.view.gui.modelcomponents.dialogcomponents;

import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.MarketTrayDrawer;
import it.polimi.ingsw.psp26.view.gui.modelcomponents.RatioDrawer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.closeParentStageOfActionEvent;

public class MarketDialogDrawer extends RatioDrawer {

    private final Client client;
    private final MarketTrayDrawer marketTrayDrawer;

    public MarketDialogDrawer(Client client, MarketTray marketTray, int maxWidth) {
        super(maxWidth);

        this.client = client;
        marketTrayDrawer = new MarketTrayDrawer(client, marketTray, initMaxWidth / 3);
    }

    @Override
    public Pane draw() {

        VBox rootPane = new VBox(10 * ratio);
        rootPane.setAlignment(Pos.CENTER);

        Text text = new Text("Market");
        text.setId("title");
        rootPane.getChildren().add(text);

        Text description = new Text(
                "*Click on the arrow to obtain the resources on the corresponding row (or column).");
        description.setId("title");
        description.setStyle("-fx-font-size: " + 40 * ratio + ";");
        rootPane.getChildren().add(description);

        rootPane.getChildren().add(new HBox(marketTrayDrawer.draw()));

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
