package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid.COLORS;
import static it.polimi.ingsw.psp26.model.enums.Resource.*;

public class DevelopmentCardsInitializer {

    private static DevelopmentCardsInitializer instance;

    private final List<DevelopmentCard> developmentCards;

    private DevelopmentCardsInitializer() {
        developmentCards = createDevelopmentCards();
    }

    public static DevelopmentCardsInitializer getInstance() {
        if (instance == null)
            instance = new DevelopmentCardsInitializer();

        return instance;
    }


    /**
     * Filter the Development Cards based on their Type
     *
     * @param developmentCardType The Type that the wanted Cards must have
     * @return A List of the selected Development Cards
     */
    public List<DevelopmentCard> getByDevelopmentCardType(DevelopmentCardType developmentCardType) {
        return developmentCards
                .stream()
                .filter(x -> x.getDevelopmentCardType().equals(developmentCardType)).collect(Collectors.toList());
    }


    /**
     * Creates all the cards, Level by Level, by calling auxiliary methods that build Development Cards based on their Level
     *
     * @return A List of all the Development Cards
     */
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


    /**
     * Creates Development Cards with these characteristics:
     * Level: 1
     * Victory Points: 1
     * Production and costs are based on the Level and Victory Points value
     *
     * @return A List of Development Cards of the described characteristics
     */
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
                    new DevelopmentCardType(COLORS[i], Level.FIRST),
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


    /**
     * Creates Development Cards with these characteristics:
     * Level: 1
     * Victory Points: 2
     * Production and costs are based on the Level and Victory Points value
     *
     * @return A List of Development Cards of the described characteristics
     */
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
                    new DevelopmentCardType(COLORS[i], Level.FIRST),
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


    /**
     * Creates Development Cards with these characteristics:
     * Level: 1
     * Victory Points: 3
     * Production and costs are based on the Level and Victory Points value
     *
     * @return A List of Development Cards of the described characteristics
     */
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
                    new DevelopmentCardType(COLORS[i], Level.FIRST),
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


    /**
     * Creates Development Cards with these characteristics:
     * Level: 1
     * Victory Points: 4
     * Production and costs are based on the Level and Victory Points value
     *
     * @return A List of Development Cards of the described characteristics
     */
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
                    new DevelopmentCardType(COLORS[i], Level.FIRST),
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


    /**
     * Creates Development Cards with these characteristics:
     * Level: 2
     * Victory Points: 5
     * Production and costs are based on the Level and Victory Points value
     *
     * @return A List of Development Cards of the described characteristics
     */
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
                    new DevelopmentCardType(COLORS[i], Level.SECOND),
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


    /**
     * Creates Development Cards with these characteristics:
     * Level: 2
     * Victory Points: 6
     * Production and costs are based on the Level and Victory Points value
     *
     * @return A List of Development Cards of the described characteristics
     */
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
                    new DevelopmentCardType(COLORS[i], Level.SECOND),
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


    /**
     * Creates Development Cards with these characteristics:
     * Level: 2
     * Victory Points: 7
     * Production and costs are based on the Level and Victory Points value
     *
     * @return A List of Development Cards of the described characteristics
     */
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
                    new DevelopmentCardType(COLORS[i], Level.SECOND),
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


    /**
     * Creates Development Cards with these characteristics:
     * Level: 2
     * Victory Points: 8
     * Production and costs are based on the Level and Victory Points value
     *
     * @return A List of Development Cards of the described characteristics
     */
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
                        put(costs1[finalI], 3);
                        put(costs2[finalI], 3);
                    }},
                    new DevelopmentCardType(COLORS[i], Level.SECOND),
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


    /**
     * Creates Development Cards with these characteristics:
     * Level: 3
     * Victory Points: 9
     * Production and costs are based on the Level and Victory Points value
     *
     * @return A List of Development Cards of the described characteristics
     */
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
                    new DevelopmentCardType(COLORS[i], Level.THIRD),
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


    /**
     * Creates Development Cards with these characteristics:
     * Level: 3
     * Victory Points: 10
     * Production and costs are based on the Level and Victory Points value
     *
     * @return A List of Development Cards of the described characteristics
     */
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
                    new DevelopmentCardType(COLORS[i], Level.THIRD),
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


    /**
     * Creates Development Cards with these characteristics:
     * Level: 3
     * Victory Points: 11
     * Production and costs are based on the Level and Victory Points value
     *
     * @return A List of Development Cards of the described characteristics
     */
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
                    new DevelopmentCardType(COLORS[i], Level.THIRD),
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


    /**
     * Creates Development Cards with these characteristics:
     * Level: 3
     * Victory Points: 12
     * Production and costs are based on the Level and Victory Points value
     *
     * @return A List of Development Cards of the described characteristics
     */
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
                    new DevelopmentCardType(COLORS[i], Level.THIRD),
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
