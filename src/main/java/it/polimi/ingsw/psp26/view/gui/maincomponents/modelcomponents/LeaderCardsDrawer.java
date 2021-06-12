package it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents;

import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;
import it.polimi.ingsw.psp26.view.gui.maincomponents.RatioDrawer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.effects.AnimationEffects.addMouseOverAnimation;
import static it.polimi.ingsw.psp26.view.gui.effects.AnimationEffects.addTurnCardAnimation;
import static it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.ModelDrawUtils.*;

/**
 * Class to draw leader cards.
 */
public class LeaderCardsDrawer extends RatioDrawer {

    private final List<LeaderCard> leaderCards;
    private final List<LeaderDepot> leaderDepots;
    private final boolean hideNotActiveLeaderCards;

    /**
     * Class constructor.
     *
     * @param leaderCards              leader cards of the player that must be drawn
     * @param leaderDepots             leader depots of the player
     * @param maxWidth                 max width for the root pane
     * @param hideNotActiveLeaderCards boolean to hide non active leader
     */
    public LeaderCardsDrawer(List<LeaderCard> leaderCards, List<LeaderDepot> leaderDepots, int maxWidth, boolean hideNotActiveLeaderCards) {
        super(maxWidth);

        this.leaderCards = leaderCards;
        this.leaderDepots = leaderDepots;
        this.hideNotActiveLeaderCards = hideNotActiveLeaderCards;
    }

    /**
     * Method to draw leader cards.
     * It returns the pane containing them
     *
     * @return pane
     */
    @Override
    public Pane draw() {
        drawLeaders();
        return pane;
    }

    /**
     * Method to draw leader cards on the root pane.
     * if leader is active it draws the front image of the leader, otherwise it draws the back
     * Passing with the mouse over an non-active leader, the front will be shown,
     * but only if the hideNotActiveLeaderCards has been set to false.
     * <p>
     * In case of leader with depot ability, resources will be drawn over it.
     */
    private void drawLeaders() {
        int hOffset = 100;
        int vOffset = 100;
        int vShift = 1000;

        for (int i = 0; i < leaderCards.size(); i++) {
            LeaderCard leaderToDraw = leaderCards.get(i);

            if (leaderToDraw.isActive()) {
                Image leaderCardImage = getCard(leaderToDraw, ratio);
                ImageView imageView = getImageView(leaderCardImage, hOffset * ratio, (vOffset + vShift * i) * ratio);
                addMouseOverAnimation(imageView, ratio);

                pane.getChildren().add(imageView);

                if (leaderToDraw.getAbilityToString().contains("SpecialDepot"))
                    drawResourcesInLeaderDepot(leaderToDraw, (int) imageView.getX(), (int) imageView.getY());

            } else {

                Image frontLeaderCardImage = getCard(leaderToDraw, ratio);
                Image backLeaderCardImage = getLeaderBack(ratio);
                ImageView imageView = getImageView(backLeaderCardImage, hOffset * ratio, (vOffset + vShift * i) * ratio);
                if (!hideNotActiveLeaderCards)
                    addTurnCardAnimation(imageView, frontLeaderCardImage, backLeaderCardImage);
                pane.getChildren().add(imageView);

            }
        }
    }


    /**
     * Draws the Resources contained in the Leader Depots directly on the Leader Cards.
     *
     * @param leaderCard The card where the Resources will be drawn
     * @param cardX      The X position of the card
     * @param cardY      The Y position of the card
     */
    private void drawResourcesInLeaderDepot(LeaderCard leaderCard, int cardX, int cardY) {
        Image resourceImage = getResourceImage(leaderCard.getAbilityResource(), ratio);

        for (int i = 0; i < leaderDepots.get(getCorrectLeaderIndex(leaderCard)).getResources().size(); i++) {

            ImageView imageView = getImageView(resourceImage, cardX + 100 * ratio + i * 170 * ratio, cardY + 550 * ratio);
            addMouseOverAnimation(imageView, ratio);

            pane.getChildren().add(imageView);

        }
    }


    /**
     * Used to get the correct index of the Leader Card in the leaderDepots List in order to display the correct amount of Resources.
     *
     * @param leaderCard The Leader Card that has to be drawn
     * @return The index of the Leader Card in the leaderDepots List
     */
    private int getCorrectLeaderIndex(LeaderCard leaderCard) {
        int leaderIndex;

        for (leaderIndex = 0; leaderIndex < leaderDepots.size() - 1; leaderIndex++)
            if (leaderDepots.get(leaderIndex).getLeaderDepotResource().equals(leaderCard.getAbilityResource())) break;

        return leaderIndex;
    }

}
