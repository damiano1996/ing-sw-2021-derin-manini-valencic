package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DevelopmentCard {

    private final Map<Resource, Integer> cost;
    private final DevelopmentCardType developmentCardType;
    private final Map<Resource, Integer> productionCost;
    private final Map<Resource, Integer> productionReturn;
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

    public Map<Resource, Integer> getCost() {
        return Collections.unmodifiableMap(cost);
    }

    public DevelopmentCardType getDevelopmentCardType() {
        return developmentCardType;
    }

    public Map<Resource, Integer> getProductionCost() {
        return Collections.unmodifiableMap(productionCost);
    }

    public Map<Resource, Integer> getProductionReturn() {
        return Collections.unmodifiableMap(productionReturn);
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
