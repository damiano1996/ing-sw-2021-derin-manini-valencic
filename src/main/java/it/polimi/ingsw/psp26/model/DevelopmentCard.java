package it.polimi.ingsw.psp26.model;

import java.util.HashMap;

public class DevelopmentCard {

    private final HashMap<Resource, Integer> cost;
    private final Color color;
    private final Level level;
    private final HashMap<Resource, Integer> productionCost;
    private final HashMap<Resource, Integer> productionReturn;
    private final int victoryPoints;


    public DevelopmentCard(HashMap<Resource, Integer> cost, Color color, Level level, HashMap<Resource, Integer> productionCost, HashMap<Resource, Integer> productionReturn, int victoryPoints) {
        this.cost = cost;
        this.color = color;
        this.level = level;
        this.productionCost = productionCost;
        this.productionReturn = productionReturn;
        this.victoryPoints = victoryPoints;
    }

    public HashMap<Resource, Integer> getCost() {
        return cost;
    }

    public Color getColor() {
        return color;
    }

    public Level getLevel() {
        return level;
    }

    public HashMap<Resource, Integer> getProductionCost() {
        return productionCost;
    }

    public HashMap<Resource, Integer> getProductionReturn() {
        return productionReturn;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public boolean equals(DevelopmentCard developmentCard) {
        return developmentCard.cost.equals(cost) &&
                developmentCard.color.equals(color) &&
                developmentCard.level.equals(level) &&
                developmentCard.productionCost.equals(productionCost) &&
                developmentCard.productionReturn.equals(productionReturn) &&
                developmentCard.victoryPoints == victoryPoints;
    }
}
