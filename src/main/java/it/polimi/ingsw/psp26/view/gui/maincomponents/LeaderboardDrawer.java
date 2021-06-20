package it.polimi.ingsw.psp26.view.gui.maincomponents;

import it.polimi.ingsw.psp26.network.client.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.psp26.view.ViewUtils.getOrderedPlayersList;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.closeParentStageOfActionEvent;

/**
 * Class to draw the leaderboard.
 */
public class LeaderboardDrawer extends RatioDrawer {

    protected final Client client;
    protected final Map<String, Integer> endMatchLeaderboard;
    protected final String winningPlayer;

    /**
     * Class constructor.
     *
     * @param client              client object
     * @param endMatchLeaderboard map containing nickname -> points associations
     * @param winningPlayer       nickname of the winner
     * @param maxWidth            max width that can be used to draw the root pane
     */
    public LeaderboardDrawer(Client client, Map<String, Integer> endMatchLeaderboard, String winningPlayer, int maxWidth) {
        super(maxWidth);

        this.client = client;
        this.endMatchLeaderboard = endMatchLeaderboard;
        this.winningPlayer = winningPlayer;
    }


    /**
     * Method to draw the leaderboard.
     *
     * @return a pane containing the leaderboard
     */
    @Override
    public Pane draw() {
        AnchorPane leaderboard = new AnchorPane();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/gui/fxml/Leaderboard.fxml"));
            leaderboard = fxmlLoader.load();

            // Setting texts dimensions
            setTextDimensions((Text) fxmlLoader.getNamespace().get("leaderboardText"), 90, true);
            setTextDimensions((Text) fxmlLoader.getNamespace().get("playersText"), 60, true);
            setTextDimensions((Text) fxmlLoader.getNamespace().get("pointsText"), 60, true);

            // Setting the winningText
            Text winningText = (Text) fxmlLoader.getNamespace().get("winningText");
            setTextDimensions(winningText, 80, false);
            setWinningText(winningText);

            // Inserting values into the leaderboard
            insertLeaderboardValues(
                    (VBox) fxmlLoader.getNamespace().get("playersBox"),
                    (VBox) fxmlLoader.getNamespace().get("pointsBox"),
                    endMatchLeaderboard
            );

            setDoneButtonAction((Button) fxmlLoader.getNamespace().get("doneButton"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return leaderboard;
    }


    /**
     * Method to insert the Players' nicknames and points in the leaderboard.
     *
     * @param playerBox   The VBox containing the Players' nicknames
     * @param pointsBox   The VBox containing the Players' points
     * @param leaderboard The Map containing the values to insert
     */
    protected void insertLeaderboardValues(VBox playerBox, VBox pointsBox, Map<String, Integer> leaderboard) {
        // Getting a List of ordered Players' nicknames to better insert values in the leaderboard
        List<String> orderedPlayers = getOrderedPlayersList(leaderboard);

        for (String playerNickname : orderedPlayers) {
            // Inserting Players' nicknames into the leaderboard
            Text name = new Text(playerNickname);
            setTextDimensions(name, 45, false);
            playerBox.getChildren().add(name);

            // Inserting Player's points into the leaderboard
            Text points = new Text(Integer.toString(leaderboard.get(playerNickname)));
            setTextDimensions(points, 45, false);
            pointsBox.getChildren().add(points);
        }
    }


    /**
     * Sets the desired dimension to the text and, if wanted, underlines the text.
     *
     * @param text      The text to change
     * @param dimension The desired dimension of the text
     * @param underline True if you want to underline the text, false otherwise
     */
    protected void setTextDimensions(Text text, int dimension, boolean underline) {
        text.setId("title");
        text.setStyle("-fx-font-size: " + (dimension * ratio));
        text.setUnderline(underline);
    }


    /**
     * Sets the winningText.
     *
     * @param winningText The winningText to change
     */
    private void setWinningText(Text winningText) {
        if (client.getNickname().equals(winningPlayer)) {
            winningText.setText("YOU WON!");
        } else if (client.isMultiplayerMode() && !endMatchLeaderboard.containsKey(winningPlayer)) {
            winningText.setText("DISCONNECTION!");
        } else {
            winningText.setText("YOU LOST!");
        }
    }


    /**
     * Sets the action of the doneButton.
     *
     * @param doneButton The button to set the action to
     */
    protected void setDoneButtonAction(Button doneButton) {
        doneButton.setOnMouseClicked(mouseEvent -> {
            closeParentStageOfActionEvent(mouseEvent);
            client.viewNext();
        });
    }

}
