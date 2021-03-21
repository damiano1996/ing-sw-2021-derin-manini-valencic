package it.polimi.ingsw.psp26.model;

import java.util.HashMap;

public class DevelopmentCard {

    private final HashMap<Integer, Resource> cost;
    private final Color color;
    private final Level level;
    private final HashMap<Integer, Resource> productionCost;
    private final HashMap<Integer, Resource> productionReturn;
    private final int victoryPoints;


    public DevelopmentCard(HashMap<Integer, Resource> cost, Color color, Level level, HashMap<Integer, Resource> productionCost, HashMap<Integer, Resource> productionReturn, int victoryPoints) {
        this.cost = cost;
        this.color = color;
        this.level = level;
        this.productionCost = productionCost;
        this.productionReturn = productionReturn;
        this.victoryPoints = victoryPoints;
    }

    public HashMap<Integer, Resource> getCost() {
        return cost;
    }

    public Color getColor() {
        return color;
    }

    public Level getLevel() {
        return level;
    }

    public HashMap<Integer, Resource> getProductionCost() {
        return productionCost;
    }

    public HashMap<Integer, Resource> getProductionReturn() {
        return productionReturn;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }
}
