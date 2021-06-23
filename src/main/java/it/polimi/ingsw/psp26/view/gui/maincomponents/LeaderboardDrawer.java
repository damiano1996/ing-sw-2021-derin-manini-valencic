package it.polimi.ingsw.psp26.view.gui.maincomponents;

import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.FXMLControllers.LeaderboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Map;

/**
 * Class to draw the leaderboard.
 */
public class LeaderboardDrawer extends RatioDrawer {

    protected final Client client;
    protected final Map<String, Integer> leaderboardToDisplay;
    protected final String winningPlayer;

    /**
     * Class constructor.
     *
     * @param client               client object
     * @param leaderboardToDisplay map containing nickname -> points associations
     * @param winningPlayer        nickname of the winner
     * @param maxWidth             max width that can be used to draw the root pane
     */
    public LeaderboardDrawer(Client client, Map<String, Integer> leaderboardToDisplay, String winningPlayer, int maxWidth) {
        super(maxWidth);

        this.client = client;
        this.leaderboardToDisplay = leaderboardToDisplay;
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
            fxmlLoader.setLocation(getClass().getResource("/gui/fxml/leaderboard.fxml"));

            leaderboard = fxmlLoader.load();

            LeaderboardController leaderboardController = fxmlLoader.getController();
            leaderboardController.initializeLeaderboard(client, ratio, leaderboardToDisplay, winningPlayer);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return leaderboard;
    }

}
