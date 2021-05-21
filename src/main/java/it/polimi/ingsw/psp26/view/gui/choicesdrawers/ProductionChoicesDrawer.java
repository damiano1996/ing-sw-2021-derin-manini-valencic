package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.exceptions.PlayerHasNotDevelopmentCardProduction;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.ButtonContainer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getImageView;
import static it.polimi.ingsw.psp26.view.gui.GUIUtils.loadImage;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.getGeneralRatio;
import static it.polimi.ingsw.psp26.view.gui.choicesdrawers.ChoiceDrawerUtils.addSelectionListener;
import static it.polimi.ingsw.psp26.view.gui.modelcomponents.ModelDrawUtils.getCard;

public class ProductionChoicesDrawer implements ChoicesDrawer<Production> {

    private final Client client;

    public ProductionChoicesDrawer(Client client) {
        this.client = client;
    }

    @Override
    public ButtonContainer<Production> decorateButtonContainer(ButtonContainer<Production> productionButtonContainer) {

        Image image;
        try {
            DevelopmentCard developmentCard = getDevelopmentCard(productionButtonContainer.getContainedObject());
            image = getCard(developmentCard, (float) (getGeneralRatio() * 0.5));
        } catch (PlayerHasNotDevelopmentCardProduction playerHasNotDevelopmentCardProduction) {
            image = loadImage("production/base_production.png", (int) (200 * getGeneralRatio()));
        }

        ImageView productionImageView = getImageView(image, 0, 0);

        productionButtonContainer.setGraphic(productionImageView);
        addSelectionListener(
                productionButtonContainer,
                productionImageView,
                image
        );
        productionButtonContainer.setStyle("-fx-background-color: transparent;");
        return productionButtonContainer;
    }

    private DevelopmentCard getDevelopmentCard(Production production) throws PlayerHasNotDevelopmentCardProduction {
        for (DevelopmentCard developmentCard : client.getCachedModel().getMyPlayerCached().getObject().getPersonalBoard().getDevelopmentCardsSlots()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList())) {
            if (production.equals(developmentCard.getProduction())) return developmentCard;
        }
        throw new PlayerHasNotDevelopmentCardProduction();
    }

}
