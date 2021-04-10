package it.polimi.ingsw.psp26.model.leadercards;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.ProductionAbility;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.ResourceDiscountAbility;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.SpecialDepotAbility;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.WhiteMarbleAbility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static it.polimi.ingsw.psp26.model.enums.Color.*;
import static it.polimi.ingsw.psp26.model.enums.Level.SECOND;
import static it.polimi.ingsw.psp26.model.enums.Level.UNDEFINED;
import static it.polimi.ingsw.psp26.model.enums.Resource.*;

public class LeaderCardsInitializer {

    private static LeaderCardsInitializer instance;

    private final List<LeaderCard> leaderCards;

    public LeaderCardsInitializer() {
        leaderCards = createLeaderCards();
    }

    public static LeaderCardsInitializer getInstance() {
        if (instance == null)
            instance = new LeaderCardsInitializer();

        return instance;
    }

    public List<LeaderCard> getLeaderCards() {
        return Collections.unmodifiableList(leaderCards);
    }

    private List<LeaderCard> createLeaderCards() {
        List<LeaderCard> cards = new ArrayList<>();

        cards.addAll(createVPTwo());
        cards.addAll(createVPThree());
        cards.addAll(createVPFour());
        cards.addAll(createVPFive());

        return Collections.unmodifiableList(cards);
    }

    private List<LeaderCard> createVPTwo() {
        Color[] developmentCardRequirementsColors1 = new Color[]{YELLOW, YELLOW, GREEN, BLUE};
        Color[] developmentCardRequirementsColors2 = new Color[]{GREEN, PURPLE, BLUE, PURPLE};

        Resource[] specialAbilityResources = new Resource[]{SERVANT, COIN, STONE, SHIELD};

        List<LeaderCard> cards = new ArrayList<>();
        for (int i = 0; i < specialAbilityResources.length; i++) {
            int finalI = i;
            cards.add(new LeaderCard(
                    new HashMap<>(),
                    new HashMap<>() {{
                        put(new DevelopmentCardType(
                                developmentCardRequirementsColors1[finalI], UNDEFINED), 1);
                        put(new DevelopmentCardType(
                                developmentCardRequirementsColors2[finalI], UNDEFINED), 1);
                    }},
                    2,
                    new ResourceDiscountAbility(specialAbilityResources[i])
            ));
        }
        return cards;
    }

    private List<LeaderCard> createVPThree() {
        Resource[] resourceRequirements = new Resource[]{SERVANT, COIN, STONE, SHIELD};

        Resource[] specialAbilityResources = new Resource[]{SHIELD, STONE, SERVANT, COIN};

        List<LeaderCard> cards = new ArrayList<>();
        for (int i = 0; i < specialAbilityResources.length; i++) {
            int finalI = i;
            cards.add(new LeaderCard(
                    new HashMap<>() {{
                        put(resourceRequirements[finalI], 5);
                    }},
                    new HashMap<>(),
                    3,
                    new SpecialDepotAbility(specialAbilityResources[i])
            ));
        }
        return cards;
    }

    private List<LeaderCard> createVPFour() {
        Color[] developmentCardRequirementsColors = new Color[]{BLUE, GREEN, PURPLE, YELLOW};

        Resource[] specialAbilityResources = new Resource[]{SERVANT, COIN, STONE, SHIELD};

        List<LeaderCard> cards = new ArrayList<>();
        for (int i = 0; i < specialAbilityResources.length; i++) {
            int finalI = i;
            cards.add(new LeaderCard(
                    new HashMap<>(),
                    new HashMap<>() {{
                        put(new DevelopmentCardType(
                                developmentCardRequirementsColors[finalI], SECOND), 1);
                    }},
                    4,
                    new ProductionAbility(specialAbilityResources[i])
            ));
        }
        return cards;
    }

    private List<LeaderCard> createVPFive() {
        Color[] developmentCardRequirementsColors1 = new Color[]{YELLOW, PURPLE, BLUE, GREEN};
        Color[] developmentCardRequirementsColors2 = new Color[]{BLUE, GREEN, YELLOW, PURPLE};

        Resource[] specialAbilityResources = new Resource[]{SERVANT, COIN, STONE, SHIELD};

        List<LeaderCard> cards = new ArrayList<>();
        for (int i = 0; i < specialAbilityResources.length; i++) {
            int finalI = i;
            cards.add(new LeaderCard(
                    new HashMap<>(),
                    new HashMap<>() {{
                        put(new DevelopmentCardType(
                                developmentCardRequirementsColors1[finalI], UNDEFINED), 2);
                        put(new DevelopmentCardType(
                                developmentCardRequirementsColors2[finalI], UNDEFINED), 1);
                    }},
                    5,
                    new WhiteMarbleAbility(specialAbilityResources[i])
            ));
        }
        return cards;
    }
}
