package it.polimi.ingsw.psp26.view.gui.modelcomponents;

import it.polimi.ingsw.psp26.model.Player;
import javafx.scene.layout.BorderPane;

public class PlayerDrawer {

    public static BorderPane drawPlayer(Player player, int maxWidth) {
        BorderPane borderPane = new BorderPane();

        borderPane.setLeft(new PersonalBoardDrawer(player.getPersonalBoard(), (int) (maxWidth * 0.9)).draw());
        borderPane.setRight(new LeaderCardsDrawer(player.getLeaderCards(), (int) (maxWidth * 0.9)).draw());

        return borderPane;
    }
}
