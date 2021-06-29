package it.polimi.ingsw.psp26.view.gui.FXMLControllers;

import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.sounds.SoundManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.psp26.view.ViewUtils.getOrderedPlayersList;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.closeParentStageOfActionEvent;

/**
 * Class that controls the leaderboard.fxml file
 */
public class LeaderboardController {

    @FXML
    private Text leaderboardText;

    @FXML
    private Text playersText;

    @FXML
    private Text pointsText;

    @FXML
    private Text winningText;

    @FXML
    private VBox playersBox;

    @FXML
    private VBox pointsBox;

    @FXML
    private Button doneButton;


    //-------------------------------//
    //          LEADERBOARD          //
    //-------------------------------//

    /**
     * Method used to create a Leaderboard.
     *
     * @param client        the client that will display the Leaderboard
     * @param ratio         the ratio that will resize all the elements shown
     * @param leaderboard   the Leaderboard to display
     * @param winningPlayer the nickname of the winning Player
     */
    @FXML
    public void initializeLeaderboard(Client client, float ratio, Map<String, Integer> leaderboard, String winningPlayer) {
        setLeaderboardTexts(client, ratio, leaderboard, winningPlayer);
        setLeaderboardDoneButtonAction(client, winningPlayer.equals(""));
    }


    /**
     * Sets the leaderboardText, playersText, pointsText of the Leaderboard.
     * The method also sets the winning text to display and inserts the values into the Leaderboard.
     *
     * @param client        the client that will display the leaderboard
     * @param ratio         the ratio that will resize all the elements shown
     * @param leaderboard   the leaderboard to display
     * @param winningPlayer the nickname of the winning Player
     */
    @FXML
    private void setLeaderboardTexts(Client client, float ratio, Map<String, Integer> leaderboard, String winningPlayer) {
        // Setting texts dimensions
        setTextDimensions(leaderboardText, 120, true, ratio);
        setTextDimensions(playersText, 90, true, ratio);
        setTextDimensions(pointsText, 90, true, ratio);

        // Setting the winningText
        setTextDimensions(
                winningText,
                winningPlayer.equals("") ? 0 : 110,
                false,
                ratio
        );
        setWinningText(client, winningPlayer);

        // Inserting values into the leaderboard
        insertLeaderboardValues(ratio, leaderboard);
    }


    /**
     * Sets the action of the doneButton.
     * The action involves closing the Leaderboard stage and calling client.viewNext() and client.sendMenuUndoMessage() method.
     */
    @FXML
    private void setLeaderboardDoneButtonAction(Client client, boolean undoOption) {
        doneButton.setOnMouseClicked(mouseEvent -> {
            SoundManager soundManager = SoundManager.getInstance();
            soundManager.setSoundEffect(SoundManager.DIALOGSOUND);

            closeParentStageOfActionEvent(mouseEvent);
            if (undoOption) client.sendMenuUndoMessage();
            client.viewNext();
        });
    }


    //-----------------------------------//
    //          UTILITY METHODS          //
    //-----------------------------------//

    /**
     * Sets the desired dimension to the text and, if wanted, underlines the text.
     *
     * @param text      the text to change
     * @param dimension the desired dimension of the text
     * @param underline true if you want to underline the text, false otherwise
     */
    @FXML
    private void setTextDimensions(Text text, int dimension, boolean underline, float ratio) {
        text.setId("title");
        text.setStyle("-fx-font-size: " + (dimension * ratio));
        text.setUnderline(underline);
    }


    /**
     * Sets the winningText.
     */
    @FXML
    private void setWinningText(Client client, String winningPlayer) {
        if (winningPlayer.equals("")) {
            winningText.setText("");
        } else {

            if (client.getNickname().equals(winningPlayer)) {
                winningText.setText("YOU WON!");
            } else {
                winningText.setText("YOU LOST!");
            }

        }
    }


    /**
     * Method to insert the Players' nicknames and points in the Leaderboard.
     */
    @FXML
    private void insertLeaderboardValues(float ratio, Map<String, Integer> leaderboard) {
        // Getting a List of ordered Players' nicknames to better insert values in the leaderboard
        List<String> orderedPlayers = getOrderedPlayersList(leaderboard);

        for (String playerNickname : orderedPlayers) {
            // Inserting Players' nicknames into the leaderboard
            Text name = new Text(playerNickname);
            setTextDimensions(name, 70, false, ratio);
            playersBox.getChildren().add(name);

            // Inserting Player's points into the leaderboard
            Text points = new Text(Integer.toString(leaderboard.get(playerNickname)));
            setTextDimensions(points, 70, false, ratio);
            pointsBox.getChildren().add(points);
        }
    }

}
