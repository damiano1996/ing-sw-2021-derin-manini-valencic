package it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents;

import it.polimi.ingsw.psp26.model.Player;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static it.polimi.ingsw.psp26.view.ViewUtils.toTitleStyle;

/**
 * Class containing a static method to draw a board of a player with his nickname and leader cards.
 */
public class PlayerDrawer {

    /**
     * Method to draw the nickname of the player, the personal board and the leader cards.
     *
     * @param player                   player object
     * @param maxWidth                 max width for the pane
     * @param ratio                    ratio for the drawings
     * @param hideNotActiveLeaderCards boolean to hide non-active leaders
     * @param hideBlackCross           boolean to hide black cross
     * @return border pane containing the player's components
     */
    public static BorderPane drawPlayer(Player player, int maxWidth, float ratio, boolean hideNotActiveLeaderCards, boolean hideBlackCross) {
        BorderPane borderPane = new BorderPane();

        Text nickname = new Text(toTitleStyle(player.getNickname()));
        nickname.setStyle("-fx-font-size: " + 70 * ratio + ";");
        nickname.setFill(Color.WHITE);
        borderPane.setTop(nickname);

        borderPane.setLeft(new PersonalBoardDrawer(player.getPersonalBoard(), (int) (maxWidth * 0.8), player.hasInkwell(), hideBlackCross).draw());
        borderPane.setRight(new LeaderCardsDrawer(player.getLeaderCards(), player.getPersonalBoard().getWarehouse().getLeaderDepots(), (int) (maxWidth * 0.8), hideNotActiveLeaderCards).draw());

        return borderPane;
    }
}
