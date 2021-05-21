package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.view.gui.ButtonContainer;
import javafx.scene.image.ImageView;

public class ProductionChoicesDrawer implements ChoicesDrawer<Production> {

    @Override
    public ButtonContainer<Production> decorateButtonContainer(ButtonContainer<Production> productionButtonContainer) {
        productionButtonContainer.setGraphic(getCardImageView(productionButtonContainer.getContainedObject()));
//        addSelectionListener(
//                productionButtonContainer,
//                getCardImageView(productionButtonContainer.getContainedObject()),
//                getCard(productionButtonContainer.getContainedObject(), (float) (getGeneralRatio() * 0.5))
//        );
//        productionButtonContainer.setStyle("-fx-background-color: transparent;");
        return productionButtonContainer;
    }

    private ImageView getCardImageView(Production production) {
//        Image leaderCardImage = getCard(leaderCard, (float) (getGeneralRatio() * 0.5));
        return new ImageView();//getImageView(leaderCardImage, 0, 0);
    }
}
