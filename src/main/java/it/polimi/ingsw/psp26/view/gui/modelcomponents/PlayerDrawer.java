package it.polimi.ingsw.psp26.view.gui.modelcomponents;

import it.polimi.ingsw.psp26.model.Player;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static it.polimi.ingsw.psp26.view.ViewUtils.toTitleStyle;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getFont;

public class PlayerDrawer {

    public static BorderPane drawPlayer(Player player, int maxWidth, float ratio) {
        BorderPane borderPane = new BorderPane();

        Text nickname = new Text(toTitleStyle(player.getNickname()));
        nickname.setFont(getFont(50, ratio));
        nickname.setFill(Color.WHITE);
        borderPane.setTop(nickname);

        borderPane.setLeft(new PersonalBoardDrawer(player.getPersonalBoard(), (int) (maxWidth * 0.8), player.hasInkwell()).draw());
        borderPane.setRight(new LeaderCardsDrawer(player.getLeaderCards(), (int) (maxWidth * 0.8)).draw());

        return borderPane;
    }
}
