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
        Image leaderCardImage = getCard(leaderCardButtonContainer.getContainedObject(), (float) (getGeneralRatio() * 0.5));
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
