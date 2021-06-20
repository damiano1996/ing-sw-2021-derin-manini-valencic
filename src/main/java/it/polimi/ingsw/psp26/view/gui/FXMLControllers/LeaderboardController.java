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
    private VBox bottomBox;

    @FXML
    private Button doneButton;


    //-----------------------------------------//
    //          END MATCH LEADERBOARD          //
    //-----------------------------------------//

    /**
     * Method used to create an end Match Leaderboard.
     *
     * @param client        The client that will display the leaderboard
     * @param ratio         The ratio that will resize all the elements shown
     * @param leaderboard   The leaderboard to display
     * @param winningPlayer The nickname of the winning Player
     */
    @FXML
    public void initializeEndMatchLeaderboard(Client client, float ratio, Map<String, Integer> leaderboard, String winningPlayer) {
        setEndMatchLeaderboardTexts(client, ratio, leaderboard, winningPlayer);
        setEndMatchLeaderboardDoneButtonAction(client);
    }


    /**
     * Sets the leaderboardText, playersText, pointsText of the leaderboard.
     * The method also sets the winning text to display and inserts the values into the leaderboard.
     *
     * @param client        The client that will display the leaderboard
     * @param ratio         The ratio that will resize all the elements shown
     * @param leaderboard   The leaderboard to display
     * @param winningPlayer The nickname of the winning Player
     */
    @FXML
    private void setEndMatchLeaderboardTexts(Client client, float ratio, Map<String, Integer> leaderboard, String winningPlayer) {
        // Setting texts dimensions
        setTextDimensions(leaderboardText, 90, true, ratio);
        setTextDimensions(playersText, 60, true, ratio);
        setTextDimensions(pointsText, 60, true, ratio);

        // Setting the winningText
        setTextDimensions(winningText, 80, false, ratio);
        setWinningText(client, leaderboard, winningPlayer);

        // Inserting values into the leaderboard
        insertLeaderboardValues(ratio, leaderboard);
    }


    /**
     * Sets the action of the doneButton.
     * In this case, the action involves closing the leaderboard stage and calling client.viewNext() method.
     */
    @FXML
    private void setEndMatchLeaderboardDoneButtonAction(Client client) {
        doneButton.setOnMouseClicked(mouseEvent -> {
            closeParentStageOfActionEvent(mouseEvent);
            client.viewNext();
        });
    }


    //--------------------------------------//
    //          GLOBAL LEADERBOARD          //
    //--------------------------------------//

    /**
     * Method used to create a global Leaderboard.
     *
     * @param client      The client that will display the leaderboard
     * @param ratio       The ratio that will resize all the elements shown
     * @param leaderboard The leaderboard to display
     */
    @FXML
    public void initializeGlobalLeaderboard(Client client, float ratio, Map<String, Integer> leaderboard) {
        setGlobalLeaderboardTexts(ratio, leaderboard);
        setGlobalLeaderboardDoneButtonAction(client);
    }


    /**
     * Sets the leaderboardText, playersText, pointsText of the leaderboard.
     * The method also inserts the values into the leaderboard.
     * The winningText dimension is set to 0 since there is no winner to display in this case.
     * The bottomBox minHeight is also set.
     *
     * @param ratio       The ratio that will resize all the elements shown
     * @param leaderboard The leaderboard to display
     */
    @FXML
    private void setGlobalLeaderboardTexts(float ratio, Map<String, Integer> leaderboard) {
        // Setting texts dimensions
        setTextDimensions(leaderboardText, 50, true, ratio);
        setTextDimensions(playersText, 45, true, ratio);
        setTextDimensions(pointsText, 45, true, ratio);

        // Setting the winningText
        setTextDimensions(winningText, 0, false, ratio);
        winningText.setText("");

        // Inserting values into the leaderboard
        insertLeaderboardValues(ratio, leaderboard);

        bottomBox.setMinHeight(150);
    }


    /**
     * Sets the action of the doneButton.
     * A click sound is added to the button.
     * In this case, the action involves closing the leaderboard stage, sending an undo message from Client to Server
     * and calling client.viewNext() method.
     */
    @FXML
    private void setGlobalLeaderboardDoneButtonAction(Client client) {
        doneButton.setOnAction(actionEvent -> {

            SoundManager soundManager = SoundManager.getInstance();
            soundManager.setSoundEffect("button_click_01.wav");

            closeParentStageOfActionEvent(actionEvent);
            client.sendMenuUndoMessage();
            client.viewNext();
        });
    }


    //-----------------------------------//
    //          UTILITY METHODS          //
    //-----------------------------------//

    /**
     * Sets the desired dimension to the text and, if wanted, underlines the text.
     *
     * @param text      The text to change
     * @param dimension The desired dimension of the text
     * @param underline True if you want to underline the text, false otherwise
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
    private void setWinningText(Client client, Map<String, Integer> leaderboard, String winningPlayer) {
        if (client.getNickname().equals(winningPlayer)) {
            winningText.setText("YOU WON!");
        } else if (client.isMultiplayerMode() && !leaderboard.containsKey(winningPlayer)) {
            winningText.setText("DISCONNECTION!");
        } else {
            winningText.setText("YOU LOST!");
        }
    }


    /**
     * Method to insert the Players' nicknames and points in the leaderboard.
     */
    @FXML
    private void insertLeaderboardValues(float ratio, Map<String, Integer> leaderboard) {
        // Getting a List of ordered Players' nicknames to better insert values in the leaderboard
        List<String> orderedPlayers = getOrderedPlayersList(leaderboard);

        for (String playerNickname : orderedPlayers) {
            // Inserting Players' nicknames into the leaderboard
            Text name = new Text(playerNickname);
            setTextDimensions(name, 45, false, ratio);
            playersBox.getChildren().add(name);

            // Inserting Player's points into the leaderboard
            Text points = new Text(Integer.toString(leaderboard.get(playerNickname)));
            setTextDimensions(points, 45, false, ratio);
            pointsBox.getChildren().add(points);
        }
    }

}
