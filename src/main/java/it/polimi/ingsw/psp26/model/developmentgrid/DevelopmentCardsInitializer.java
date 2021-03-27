package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid.COLORS;
import static it.polimi.ingsw.psp26.model.enums.Resource.*;

public class DevelopmentCardsInitializer {

    private static DevelopmentCardsInitializer instance;

    private final List<DevelopmentCard> cards;

    private DevelopmentCardsInitializer() {
        cards = createDevelopmentCards();
    }

    public static DevelopmentCardsInitializer getInstance() {
        if (instance == null)
            instance = new DevelopmentCardsInitializer();

        return instance;
    }

    public List<DevelopmentCard> getByColorAndLevel(Color color, Level level) {
        return cards
                .stream()
                .filter(x -> x.getColor().equals(color) && x.getLevel().equals(level))
                .collect(Collectors.toList());
    }

    private List<DevelopmentCard> createDevelopmentCards() {
        List<DevelopmentCard> cards = new ArrayList<>();

        cards.addAll(createFirstLevelVPOne());
        cards.addAll(createFirstLevelVPTwo());
        cards.addAll(createFirstLevelVPThree());
        cards.addAll(createFirstLevelVPFour());

        cards.addAll(createSecondLevelVPFive());
        cards.addAll(createSecondLevelVPSix());
        cards.addAll(createSecondLevelVPSeven());
        cards.addAll(createSecondLevelVPEight());

        cards.addAll(createThirdLevelVPNine());
        cards.addAll(createThirdLevelVPTen());
        cards.addAll(createThirdLevelVPEleven());
        cards.addAll(createThirdLevelVPTwelve());

        return cards;
    }

    private List<DevelopmentCard> createFirstLevelVPOne() {
        Resource[] costs = new Resource[]{SHIELD, COIN, STONE, SERVANT};
        Resource[] productionCosts = new Resource[]{COIN, SHIELD, SERVANT, STONE};
        Resource[] productionReturns = new Resource[]{FAITH_MARKER, FAITH_MARKER, FAITH_MARKER, FAITH_MARKER};

        List<DevelopmentCard> cards = new ArrayList<>();
        for (int i = 0; i < COLORS.length; i++) {
            int finalI = i;
            cards.add(new DevelopmentCard(
                    new HashMap<>() {{
                        put(costs[finalI], 2);
                    }},
                    COLORS[i], Level.FIRST,
                    new HashMap<>() {{
                        put(productionCosts[finalI], 1);
                    }},
                    new HashMap<>() {{
                        put(productionReturns[finalI], 1);
                    }},
                    1));
        }
        return cards;
    }

    private List<DevelopmentCard> createFirstLevelVPTwo() {
        Resource[] costs1 = new Resource[]{SHIELD, COIN, SHIELD, SHIELD};
        Resource[] costs2 = new Resource[]{STONE, STONE, COIN, COIN};
        Resource[] costs3 = new Resource[]{SERVANT, SERVANT, STONE, SERVANT};
        Resource[] productionCosts = new Resource[]{STONE, SERVANT, SHIELD, COIN};
        Resource[] productionReturns = new Resource[]{SERVANT, STONE, COIN, SHIELD};

        List<DevelopmentCard> cards = new ArrayList<>();
        for (int i = 0; i < COLORS.length; i++) {
            int finalI = i;
            cards.add(new DevelopmentCard(
                    new HashMap<>() {{
                        put(costs1[finalI], 1);
                        put(costs2[finalI], 1);
                        put(costs3[finalI], 1);
                    }},
                    COLORS[i], Level.FIRST,
                    new HashMap<>() {{
                        put(productionCosts[finalI], 1);
                    }},
                    new HashMap<>() {{
                        put(productionReturns[finalI], 1);
                    }},
                    2));
        }
        return cards;
    }

    private List<DevelopmentCard> createFirstLevelVPThree() {
        Resource[] costs = new Resource[]{SHIELD, COIN, STONE, SERVANT};
        Resource[] productionCosts = new Resource[]{SERVANT, STONE, SHIELD, COIN};
        Resource[] productionReturns1 = new Resource[]{COIN, COIN, COIN, SERVANT};
        Resource[] productionReturns2 = new Resource[]{SHIELD, SERVANT, SERVANT, SHIELD};
        Resource[] productionReturns3 = new Resource[]{STONE, SHIELD, STONE, STONE};

        List<DevelopmentCard> cards = new ArrayList<>();
        for (int i = 0; i < COLORS.length; i++) {
            int finalI = i;
            cards.add(new DevelopmentCard(
                    new HashMap<>() {{
                        put(costs[finalI], 3);
                    }},
                    COLORS[i], Level.FIRST,
                    new HashMap<>() {{
                        put(productionCosts[finalI], 2);
                    }},
                    new HashMap<>() {{
                        put(productionReturns1[finalI], 1);
                        put(productionReturns2[finalI], 1);
                        put(productionReturns3[finalI], 1);
                    }},
                    3));
        }
        return cards;
    }

    private List<DevelopmentCard> createFirstLevelVPFour() {
        Resource[] costs1 = new Resource[]{SHIELD, COIN, STONE, SERVANT};
        Resource[] costs2 = new Resource[]{COIN, SERVANT, SHIELD, STONE};
        Resource[] productionCosts1 = new Resource[]{STONE, SHIELD, COIN, COIN};
        Resource[] productionCosts2 = new Resource[]{SERVANT, STONE, SERVANT, SHIELD};
        Resource[] productionReturns1 = new Resource[]{COIN, SERVANT, SHIELD, STONE};
        Resource[] productionReturns2 = new Resource[]{FAITH_MARKER, FAITH_MARKER, FAITH_MARKER, FAITH_MARKER};

        List<DevelopmentCard> cards = new ArrayList<>();
        for (int i = 0; i < COLORS.length; i++) {
            int finalI = i;
            cards.add(new DevelopmentCard(
                    new HashMap<>() {{
                        put(costs1[finalI], 2);
                        put(costs2[finalI], 2);
                    }},
                    COLORS[i], Level.FIRST,
                    new HashMap<>() {{
                        put(productionCosts1[finalI], 1);
                        put(productionCosts2[finalI], 1);
                    }},
                    new HashMap<>() {{
                        put(productionReturns1[finalI], 2);
                        put(productionReturns2[finalI], 1);
                    }},
                    4));
        }
        return cards;
    }

    private List<DevelopmentCard> createSecondLevelVPFive() {
        Resource[] costs = new Resource[]{SHIELD, COIN, STONE, SERVANT};
        Resource[] productionCosts = new Resource[]{STONE, SERVANT, SHIELD, COIN};
        Resource[] productionReturns = new Resource[]{FAITH_MARKER, FAITH_MARKER, FAITH_MARKER, FAITH_MARKER};

        List<DevelopmentCard> cards = new ArrayList<>();
        for (int i = 0; i < COLORS.length; i++) {
            int finalI = i;
            cards.add(new DevelopmentCard(
                    new HashMap<>() {{
                        put(costs[finalI], 4);
                    }},
                    COLORS[i], Level.SECOND,
                    new HashMap<>() {{
                        put(productionCosts[finalI], 1);
                    }},
                    new HashMap<>() {{
                        put(productionReturns[finalI], 2);
                    }},
                    5));
        }
        return cards;
    }

    private List<DevelopmentCard> createSecondLevelVPSix() {
        Resource[] costs1 = new Resource[]{SHIELD, COIN, STONE, SERVANT};
        Resource[] costs2 = new Resource[]{SERVANT, STONE, SHIELD, COIN};
        Resource[] productionCosts1 = new Resource[]{SHIELD, COIN, STONE, COIN};
        Resource[] productionCosts2 = new Resource[]{SERVANT, STONE, SHIELD, SERVANT};
        Resource[] productionReturns = new Resource[]{STONE, SERVANT, COIN, SHIELD};

        List<DevelopmentCard> cards = new ArrayList<>();
        for (int i = 0; i < COLORS.length; i++) {
            int finalI = i;
            cards.add(new DevelopmentCard(
                    new HashMap<>() {{
                        put(costs1[finalI], 3);
                        put(costs2[finalI], 2);
                    }},
                    COLORS[i], Level.SECOND,
                    new HashMap<>() {{
                        put(productionCosts1[finalI], 1);
                        put(productionCosts2[finalI], 1);
                    }},
                    new HashMap<>() {{
                        put(productionReturns[finalI], 3);
                    }},
                    6));
        }
        return cards;
    }

    private List<DevelopmentCard> createSecondLevelVPSeven() {
        Resource[] costs = new Resource[]{SHIELD, COIN, STONE, SERVANT};
        Resource[] productionCosts = new Resource[]{COIN, SERVANT, SHIELD, STONE};
        Resource[] productionReturns1 = new Resource[]{STONE, SHIELD, SERVANT, COIN};
        Resource[] productionReturns2 = new Resource[]{FAITH_MARKER, FAITH_MARKER, FAITH_MARKER, FAITH_MARKER};

        List<DevelopmentCard> cards = new ArrayList<>();
        for (int i = 0; i < COLORS.length; i++) {
            int finalI = i;
            cards.add(new DevelopmentCard(
                    new HashMap<>() {{
                        put(costs[finalI], 5);
                    }},
                    COLORS[i], Level.SECOND,
                    new HashMap<>() {{
                        put(productionCosts[finalI], 2);
                    }},
                    new HashMap<>() {{
                        put(productionReturns1[finalI], 2);
                        put(productionReturns2[finalI], 2);
                    }},
                    7));
        }
        return cards;
    }

    private List<DevelopmentCard> createSecondLevelVPEight() {
        Resource[] costs1 = new Resource[]{SHIELD, COIN, STONE, SERVANT};
        Resource[] costs2 = new Resource[]{COIN, STONE, SERVANT, SHIELD};
        Resource[] productionCosts = new Resource[]{COIN, SERVANT, SHIELD, STONE};
        Resource[] productionReturns1 = new Resource[]{SHIELD, STONE, COIN, SERVANT};
        Resource[] productionReturns2 = new Resource[]{FAITH_MARKER, FAITH_MARKER, FAITH_MARKER, FAITH_MARKER};

        List<DevelopmentCard> cards = new ArrayList<>();
        for (int i = 0; i < COLORS.length; i++) {
            int finalI = i;
            cards.add(new DevelopmentCard(
                    new HashMap<>() {{
                        put(costs1[finalI], 5);
                        put(costs2[finalI], 5);
                    }},
                    COLORS[i], Level.SECOND,
                    new HashMap<>() {{
                        put(productionCosts[finalI], 1);
                    }},
                    new HashMap<>() {{
                        put(productionReturns1[finalI], 2);
                        put(productionReturns2[finalI], 1);
                    }},
                    8));
        }
        return cards;
    }

    private List<DevelopmentCard> createThirdLevelVPNine() {
        Resource[] costs = new Resource[]{SHIELD, COIN, STONE, SERVANT};
        Resource[] productionCosts = new Resource[]{SERVANT, SERVANT, SHIELD, STONE};
        Resource[] productionReturns1 = new Resource[]{STONE, SHIELD, SERVANT, COIN};
        Resource[] productionReturns2 = new Resource[]{FAITH_MARKER, FAITH_MARKER, FAITH_MARKER, FAITH_MARKER};

        List<DevelopmentCard> cards = new ArrayList<>();
        for (int i = 0; i < COLORS.length; i++) {
            int finalI = i;
            cards.add(new DevelopmentCard(
                    new HashMap<>() {{
                        put(costs[finalI], 6);
                    }},
                    COLORS[i], Level.THIRD,
                    new HashMap<>() {{
                        put(productionCosts[finalI], 2);
                    }},
                    new HashMap<>() {{
                        put(productionReturns1[finalI], 3);
                        put(productionReturns2[finalI], 2);
                    }},
                    9));
        }
        return cards;
    }

    private List<DevelopmentCard> createThirdLevelVPTen() {
        Resource[] costs1 = new Resource[]{SHIELD, COIN, STONE, SERVANT};
        Resource[] costs2 = new Resource[]{SERVANT, STONE, SERVANT, COIN};
        Resource[] productionCosts1 = new Resource[]{COIN, COIN, STONE, STONE};
        Resource[] productionCosts2 = new Resource[]{SERVANT, SHIELD, SERVANT, SHIELD};
        Resource[] productionReturns1 = new Resource[]{SHIELD, SERVANT, COIN, COIN};
        Resource[] productionReturns2 = new Resource[]{STONE, STONE, SHIELD, SERVANT};
        Resource[] productionReturns3 = new Resource[]{FAITH_MARKER, FAITH_MARKER, FAITH_MARKER, FAITH_MARKER};

        List<DevelopmentCard> cards = new ArrayList<>();
        for (int i = 0; i < COLORS.length; i++) {
            int finalI = i;
            cards.add(new DevelopmentCard(
                    new HashMap<>() {{
                        put(costs1[finalI], 5);
                        put(costs2[finalI], 2);
                    }},
                    COLORS[i], Level.THIRD,
                    new HashMap<>() {{
                        put(productionCosts1[finalI], 1);
                        put(productionCosts2[finalI], 1);
                    }},
                    new HashMap<>() {{
                        put(productionReturns1[finalI], 2);
                        put(productionReturns2[finalI], 2);
                        put(productionReturns3[finalI], 1);
                    }},
                    10));
        }
        return cards;
    }

    private List<DevelopmentCard> createThirdLevelVPEleven() {
        Resource[] costs = new Resource[]{SHIELD, COIN, STONE, SERVANT};
        Resource[] productionCosts = new Resource[]{SERVANT, STONE, SHIELD, COIN};
        Resource[] productionReturns1 = new Resource[]{COIN, SHIELD, SERVANT, STONE};
        Resource[] productionReturns2 = new Resource[]{FAITH_MARKER, FAITH_MARKER, FAITH_MARKER, FAITH_MARKER};

        List<DevelopmentCard> cards = new ArrayList<>();
        for (int i = 0; i < COLORS.length; i++) {
            int finalI = i;
            cards.add(new DevelopmentCard(
                    new HashMap<>() {{
                        put(costs[finalI], 7);
                    }},
                    COLORS[i], Level.THIRD,
                    new HashMap<>() {{
                        put(productionCosts[finalI], 1);
                    }},
                    new HashMap<>() {{
                        put(productionReturns1[finalI], 1);
                        put(productionReturns2[finalI], 3);
                    }},
                    11));
        }
        return cards;
    }

    private List<DevelopmentCard> createThirdLevelVPTwelve() {
        Resource[] costs1 = new Resource[]{SHIELD, COIN, STONE, SERVANT};
        Resource[] costs2 = new Resource[]{COIN, STONE, SERVANT, SHIELD};
        Resource[] productionCosts = new Resource[]{STONE, SERVANT, SHIELD, COIN};
        Resource[] productionReturns1 = new Resource[]{SHIELD, COIN, STONE, SERVANT};
        Resource[] productionReturns2 = new Resource[]{COIN, SHIELD, SERVANT, STONE};

        List<DevelopmentCard> cards = new ArrayList<>();
        for (int i = 0; i < COLORS.length; i++) {
            int finalI = i;
            cards.add(new DevelopmentCard(
                    new HashMap<>() {{
                        put(costs1[finalI], 4);
                        put(costs2[finalI], 4);
                    }},
                    COLORS[i], Level.THIRD,
                    new HashMap<>() {{
                        put(productionCosts[finalI], 1);
                    }},
                    new HashMap<>() {{
                        put(productionReturns1[finalI], 1);
                        put(productionReturns2[finalI], 3);
                    }},
                    12));
        }
        return cards;
    }

}
