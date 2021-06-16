package it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents;

import it.polimi.ingsw.psp26.model.MarketTray;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.maincomponents.RatioDrawer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.MarketTrayDrawer;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents.DialogComponentsUtils.getDialogPane;

/**
 * Class to setup the pane containing the market tray to be placed in a dialog stage.
 */
public class MarketDialogDrawer extends RatioDrawer {

    private final Client client;
    private final MarketTrayDrawer marketTrayDrawer;

    /**
     * Class constructor.
     *
     * @param client     client object
     * @param marketTray market tray object that must be drawn
     * @param maxWidth   maximum allowed width for the pane
     */
    public MarketDialogDrawer(Client client, MarketTray marketTray, int maxWidth) {
        super(maxWidth);

        this.client = client;
        marketTrayDrawer = new MarketTrayDrawer(client, marketTray, initMaxWidth / 2);
    }

    /**
     * Method to draw the pane containing the marker,
     * a title, a description of the allowed actions and the undo button.
     *
     * @return pane
     */
    @Override
    public Pane draw() {

        VBox rootPane = new VBox(10 * ratio);
        rootPane.setAlignment(Pos.CENTER);

        Text text = new Text("Market");
        text.setId("title");
        text.setStyle("-fx-font-size: " + 100 * ratio + ";");
        rootPane.getChildren().add(text);

        Text description = new Text(
                "*Click on the arrow to obtain the resources on the corresponding row (or column).");
        return getDialogPane(rootPane, description, 70 * ratio, marketTrayDrawer.draw(), client);
    }

}
