package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.view.gui.ButtonContainer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.REFERENCE_WIDTH;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.getMinBetweenWindowWidthAndHeight;
import static it.polimi.ingsw.psp26.view.gui.choicesdrawers.ChoiceDrawerUtils.addSelectionListener;
import static it.polimi.ingsw.psp26.view.gui.maincomponents.ModelDrawUtils.getCard;

public class LeaderCardChoicesDrawer implements ChoicesDrawer<LeaderCard> {

    @Override
    public ButtonContainer<LeaderCard> decorateButtonContainer(ButtonContainer<LeaderCard> leaderCardButtonContainer) {
        float ratio = getMinBetweenWindowWidthAndHeight() / 1.5f / REFERENCE_WIDTH;
        Image leaderCardImage = getCard(leaderCardButtonContainer.getContainedObject(), ratio);
        ImageView leaderCardImageView = getImageView(leaderCardImage, 0, 0);

        leaderCardButtonContainer.setGraphic(leaderCardImageView);
        addSelectionListener(
                leaderCardButtonContainer,
                leaderCardImageView,
                leaderCardImage
        );
        leaderCardButtonContainer.setStyle("-fx-background-color: transparent;");
        return leaderCardButtonContainer;
    }

}
