package it.polimi.ingsw.psp26.view.gui.maincomponents;

import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.server.memory.LeaderBoard;
import it.polimi.ingsw.psp26.view.gui.sounds.SoundManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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
 * Class to draw the global leaderboard.
 */
public class GlobalLeaderboardDrawer extends LeaderboardDrawer {


    /**
     * Class constructor.
     *
     * @param client   client object
     * @param maxWidth max width that can be used to draw the root pane
     */
    public GlobalLeaderboardDrawer(Client client, int maxWidth) {
        super(client, LeaderBoard.getInstance().getLeaderboard(), "", maxWidth);
    }
     /**
     * Method to draw the global leaderboard.
     *
     * @return a pane containing the leaderboard
     */
    public Pane draw() {
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
            winningText.setText(winningPlayer);

            // Inserting values into the leaderboard
            insertLeaderboardValues(
                    (VBox) fxmlLoader.getNamespace().get("playersBox"),
                    (VBox) fxmlLoader.getNamespace().get("pointsBox"),
                    endMatchLeaderboard
            );

            setDoneButtonAction((Button) fxmlLoader.getNamespace().get("doneButton"));
            VBox box = (VBox) fxmlLoader.getNamespace().get("BottomBox");
            box.setMinHeight(150);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return leaderboard;
    }


    /**
     * Sets the action of the backButton
     *
     * @param backButton The button to set the action to
     */
    @Override
    protected void setDoneButtonAction(Button backButton) {
        backButton.setOnAction(actionEvent -> {

            SoundManager soundManager = SoundManager.getInstance();
            soundManager.setSoundEffect("button_click_01.wav");

            closeParentStageOfActionEvent(actionEvent);
            client.sendUndoMessage();
            client.viewNext();
        });
    }


}
