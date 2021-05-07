package it.polimi.ingsw.psp26.view.gui.modelcomponents;

import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.effects.AnimationEffects.addMouseOverAnimation;
import static it.polimi.ingsw.psp26.view.gui.effects.AnimationEffects.addTurnCardAnimation;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.ModelDrawUtils.getCard;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.ModelDrawUtils.getLeaderBack;

public class LeaderCardsDrawer extends RatioDrawer {

    private final List<LeaderCard> leaderCards;

    public LeaderCardsDrawer(List<LeaderCard> leaderCards, int maxWidth) {
        super(maxWidth);

        this.leaderCards = leaderCards;
    }

    @Override
    public Pane draw() {

        drawLeaders();
        return pane;
    }

    private void drawLeaders() {
        int hOffset = 100;
        int vOffset = 100;
        int vShift = 1000;

        for (int i = 0; i < leaderCards.size(); i++) {

            if (leaderCards.get(i).isActive()) {

                Image leaderCardImage = getCard(leaderCards.get(i), ratio);
                ImageView imageView = getImageView(leaderCardImage, hOffset * ratio, (vOffset + vShift * i) * ratio);
                addMouseOverAnimation(imageView, ratio);
                pane.getChildren().add(imageView);

            } else {

                Image frontLeaderCardImage = getCard(leaderCards.get(i), ratio);
                Image backLeaderCardImage = getLeaderBack(ratio);
                ImageView imageView = getImageView(backLeaderCardImage, hOffset * ratio, (vOffset + vShift * i) * ratio);
                addTurnCardAnimation(imageView, frontLeaderCardImage, backLeaderCardImage);
                addMouseOverAnimation(imageView, ratio);
                pane.getChildren().add(imageView);

            }
        }
    }

}
