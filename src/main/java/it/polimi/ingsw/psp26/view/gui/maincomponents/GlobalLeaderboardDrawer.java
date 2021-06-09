package it.polimi.ingsw.psp26.view.gui.maincomponents;

import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.server.memory.LeaderBoard;
import it.polimi.ingsw.psp26.view.gui.sounds.SoundManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.psp26.view.ViewUtils.getOrderedPlayersList;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.closeParentStageOfActionEvent;

public class GlobalLeaderboardDrawer extends RatioDrawer {

    private final LeaderBoard globalLeaderboard;
    private final Client client;

    public GlobalLeaderboardDrawer(Client client, int maxWidth) {
        super(maxWidth);
        this.globalLeaderboard = LeaderBoard.getInstance();
        this.client = client;
    }

    /**
     * @return An AnchorPane containing the leaderboard
     */
    public AnchorPane draw() {
        AnchorPane leaderboard = new AnchorPane();
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/gui/fxml/Leaderboard.fxml"));
            leaderboard = fxmlLoader.load();

            // Setting texts dimensions
            setTextDimensions((Text) fxmlLoader.getNamespace().get("leaderboardText"), 50, true);
            setTextDimensions((Text) fxmlLoader.getNamespace().get("playersText"), 45, true);
            setTextDimensions((Text) fxmlLoader.getNamespace().get("pointsText"), 45, true);

            Text winningText = (Text) fxmlLoader.getNamespace().get("winningText");
            setTextDimensions(winningText, 0, false);
            winningText.setText("");

            // Inserting values into the leaderboard
            insertLeaderboardValues(
                    (VBox) fxmlLoader.getNamespace().get("playersBox"),
                    (VBox) fxmlLoader.getNamespace().get("pointsBox"),
                    globalLeaderboard.getLeaderboard()
            );

            setBackButtonAction((Button) fxmlLoader.getNamespace().get("doneButton"));
            VBox box = (VBox) fxmlLoader.getNamespace().get("BottomBox");
            box.setMinHeight(150);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return leaderboard;
    }

    /**
     * Insert the Players' nicknames and points in the leaderboard
     *
     * @param playerBox   The VBox containing the Players' nicknames
     * @param pointsBox   The VBox containing the Players' points
     * @param leaderboard The Map containing the values to insert
     */
    private void insertLeaderboardValues(VBox playerBox, VBox pointsBox, Map<String, Integer> leaderboard) {
        // Getting a List of ordered Players' nicknames to better insert values in the leaderboard
        List<String> orderedPlayers = getOrderedPlayersList(leaderboard);

        for (int i = 0; i < Math.min(5, orderedPlayers.size()); i++) {

            // Inserting Players' nicknames into the leaderboard
            Text name = new Text(orderedPlayers.get(i));
            setTextDimensions(name, 40, false);
            playerBox.getChildren().add(name);

            // Inserting Player's points into the leaderboard
            Text points = new Text(Integer.toString(leaderboard.get(orderedPlayers.get(i))));
            setTextDimensions(points, 40, false);
            pointsBox.getChildren().add(points);

        }
    }

    /**
     * Sets the desired dimension to the text and, if wanted, underlines the text
     *
     * @param text      The text to change
     * @param dimension The desired dimension of the text
     * @param underline True if you want to underline the text, false otherwise
     */
    private void setTextDimensions(Text text, int dimension, boolean underline) {
        text.setId("title");
        text.setStyle("-fx-font-size: " + (dimension * ratio));
        text.setUnderline(underline);
    }

    /**
     * Sets the action of the backButton
     *
     * @param backButton The button to set the action to
     */
    private void setBackButtonAction(Button backButton) {
        backButton.setOnAction(actionEvent -> {

            SoundManager soundManager = SoundManager.getInstance();
            soundManager.setSoundEffect("button_click_01.wav");

            closeParentStageOfActionEvent(actionEvent);
            client.sendUndoMessage();
            client.viewNext();
        });
    }


}
