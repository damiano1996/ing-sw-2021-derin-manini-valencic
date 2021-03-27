package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.HashMap;
import java.util.Objects;

public class DevelopmentCard {

    private final HashMap<Resource, Integer> cost;
    private final DevelopmentCardType developmentCardType;
    private final HashMap<Resource, Integer> productionCost;
    private final HashMap<Resource, Integer> productionReturn;
    private final int victoryPoints;


    public DevelopmentCard(
            HashMap<Resource, Integer> cost,
            DevelopmentCardType colorLevel,
            HashMap<Resource, Integer> productionCost,
            HashMap<Resource, Integer> productionReturn,
            int victoryPoints
    ) {
        this.cost = cost;
        this.developmentCardType = colorLevel;
        this.productionCost = productionCost;
        this.productionReturn = productionReturn;
        this.victoryPoints = victoryPoints;
    }

    public HashMap<Resource, Integer> getCost() {
        return cost;
    }

    public DevelopmentCardType getDevelopmentCardType() {
        return developmentCardType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevelopmentCard that = (DevelopmentCard) o;
        return victoryPoints == that.victoryPoints &&
                Objects.equals(cost, that.cost) &&
                Objects.equals(developmentCardType, that.developmentCardType) &&
                Objects.equals(productionCost, that.productionCost) &&
                Objects.equals(productionReturn, that.productionReturn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost, developmentCardType, productionCost, productionReturn, victoryPoints);
    }
}
