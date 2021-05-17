package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.view.gui.ButtonContainer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.getGeneralRatio;
import static it.polimi.ingsw.psp26.view.gui.choicesdrawers.ChoiceDrawerUtils.addSelectionListener;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.ModelDrawUtils.getCard;

public class LeaderCardChoicesDrawer implements ChoicesDrawer<LeaderCard> {

    @Override
    public ButtonContainer<LeaderCard> decorateButtonContainer(ButtonContainer<LeaderCard> leaderCardButtonContainer) {
        leaderCardButtonContainer.setGraphic(getCardImageView(leaderCardButtonContainer.getContainedObject()));
        addSelectionListener(
                leaderCardButtonContainer,
                getCardImageView(leaderCardButtonContainer.getContainedObject()),
                getCard(leaderCardButtonContainer.getContainedObject(), (float) (getGeneralRatio() * 0.5))
        );
        leaderCardButtonContainer.setStyle("-fx-background-color: transparent;");
        return leaderCardButtonContainer;
    }

    private ImageView getCardImageView(LeaderCard leaderCard) {
        Image leaderCardImage = getCard(leaderCard, (float) (getGeneralRatio() * 0.5));
        return getImageView(leaderCardImage, 0, 0);
    }
}
