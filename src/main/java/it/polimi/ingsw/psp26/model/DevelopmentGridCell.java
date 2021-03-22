package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DevelopmentGridCell {

    private final Color color;
    private final Level level;
    private final List<DevelopmentCard> developmentCards;

    public DevelopmentGridCell(Color color, Level level, int quantity, HashMap<Resource, Integer> cost, HashMap<Resource, Integer> productionCost, HashMap<Resource, Integer> productionReturn, int victoryPoints) {
        this.color = color;
        this.level = level;

        developmentCards = new ArrayList<>();
        for (int i = 0; i < quantity; i++)
            developmentCards.add(new DevelopmentCard(cost, color, level, productionCost, productionReturn, victoryPoints));
    }

    public Color getColor() {
        return color;
    }

    public Level getLevel() {
        return level;
    }

    public DevelopmentCard drawCard() throws NoMoreDevelopmentCardsException {
        try {
            return developmentCards.remove(0);
        } catch (Exception e) {
            throw new NoMoreDevelopmentCardsException();
        }
    }

    public boolean isEmpty() {
        return developmentCards.size() == 0;
    }
}
