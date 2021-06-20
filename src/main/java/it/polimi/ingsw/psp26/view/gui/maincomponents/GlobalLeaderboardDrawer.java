package it.polimi.ingsw.psp26.view.gui.maincomponents;

import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.network.server.memory.LeaderBoard;
import it.polimi.ingsw.psp26.view.gui.FXMLControllers.LeaderboardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

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
            fxmlLoader.setLocation(getClass().getResource("/gui/fxml/leaderboard.fxml"));

            leaderboard = fxmlLoader.load();

            LeaderboardController leaderboardController = fxmlLoader.getController();
            leaderboardController.initializeGlobalLeaderboard(client, ratio, leaderboardToDisplay);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return leaderboard;
    }

}
