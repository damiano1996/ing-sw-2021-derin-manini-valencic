package it.polimi.ingsw.psp26.view.gui.choicesdrawers;

import it.polimi.ingsw.psp26.exceptions.PlayerHasNoLeaderProduction;
import it.polimi.ingsw.psp26.exceptions.PlayerHasNotDevelopmentCardProduction;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.client.Client;
import it.polimi.ingsw.psp26.view.gui.ButtonContainer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.*;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.REFERENCE_WIDTH;
import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.getMinBetweenWindowWidthAndHeight;
import static it.polimi.ingsw.psp26.view.gui.choicesdrawers.ChoiceDrawerUtils.addSelectionListener;
import static it.polimi.ingsw.psp26.view.gui.effects.LightEffects.addLightEffects;
import static it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents.ModelDrawUtils.getCard;

public class ProductionChoicesDrawer implements ChoicesDrawer<Production> {

    private final Client client;

    public ProductionChoicesDrawer(Client client) {
        this.client = client;
    }

    @Override
    public ButtonContainer<Production> decorateButtonContainer(ButtonContainer<Production> productionButtonContainer) {
        Image image;
        float ratio = getMinBetweenWindowWidthAndHeight() / REFERENCE_WIDTH;
        System.out.println("ProductionChoicesDrawer - Ratio: " + ratio);

        try {

            // Checking if the Player has a DevelopmentCard Production equals to the one contained in the ButtonContainer
            DevelopmentCard developmentCard = getDevelopmentCard(productionButtonContainer.getContainedObject());
            image = getCard(developmentCard, ratio);

        } catch (PlayerHasNotDevelopmentCardProduction playerHasNotDevelopmentCardProduction) {
            try {

                // If the Player doesn't have a DevelopmentCard Production, checks if it has a Leader Production 
                // equals to the one contained in the ButtonContainer
                image = loadImage(getLeaderProductionName(productionButtonContainer.getContainedObject()), (int) (400 * ratio));
                image = setRoundedCorners(image, ratio);
                image = addLightEffects(image, ratio);

            } catch (PlayerHasNoLeaderProduction playerHasNoLeaderProduction) {

                // If the Production contained in the ButtonContainer doesn't match in any of the 2 cases above, loads a baseProduction 
                image = loadImage("production/base_production.png", (int) (400 * ratio));
                image = addLightEffects(image, ratio);

            }
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


    /**
     * Used to get the correct path for loading a Leader Production image
     * Since this method is called after the check of Development Card Productions (after getDevelopmentCard() method),
     * it assumes that, if the given Production has a FAITH_MARKER in its ProductionReturn, it is a Leader Production
     *
     * @param production The Production to check if it is a Leader Production
     * @return The path for loading the Leader Production
     * @throws PlayerHasNoLeaderProduction Thrown if the given Production is not a Leader Production
     */
    private String getLeaderProductionName(Production production) throws PlayerHasNoLeaderProduction {
        if (production.getProductionReturn().containsKey(Resource.FAITH_MARKER))
            return "production/leaderProduction_" + new ArrayList<>(production.getProductionCost().keySet()).get(0).getName().toUpperCase(Locale.ROOT) + ".png";

        throw new PlayerHasNoLeaderProduction();
    }

}
