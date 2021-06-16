package it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.maincomponents.RatioDrawer;
import it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.DevelopmentCardsGridDrawer;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static it.polimi.ingsw.psp26.view.gui.maincomponents.dialogcomponents.DialogComponentsUtils.getDialogPane;

/**
 * Class to draw the development cards grid in a dialog.
 * It designs a pane containing the grid with a title, a description and the undo button.
 */
public class DevelopmentCardsGridDialogDrawer extends RatioDrawer {

    private final Client client;
    private final DevelopmentCardsGridDrawer developmentGridDrawer;

    /**
     * Class constructor.
     *
     * @param client               client object
     * @param developmentCardsGrid development cards grid object to draw
     * @param maxWidth             max width allowed for the pane
     */
    public DevelopmentCardsGridDialogDrawer(Client client, DevelopmentCardsGrid developmentCardsGrid, int maxWidth) {
        super(maxWidth);

        this.client = client;
        developmentGridDrawer = new DevelopmentCardsGridDrawer(client, developmentCardsGrid, (int) (initMaxWidth / 1.7));
    }

    /**
     * Method to draw the pane for the dialog.
     * It will contain the grid with a title, a description and an undo button.
     *
     * @return pane
     */
    @Override
    public Pane draw() {

        VBox rootPane = new VBox(10 * ratio);
        rootPane.setAlignment(Pos.CENTER);

        Text text = new Text("Development Cards");
        text.setId("title");
        text.setStyle("-fx-font-size: " + 100 * ratio + ";");
        rootPane.getChildren().add(text);

        Text description = new Text("*Click on the card that you want to buy.");

        return getDialogPane(rootPane, description, 70 * ratio, developmentGridDrawer.draw(), client);
    }

}
