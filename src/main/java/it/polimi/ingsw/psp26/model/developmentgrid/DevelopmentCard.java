package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DevelopmentCard {

    private final Map<Resource, Integer> cost;
    private final DevelopmentCardType developmentCardType;
    private final Production production;

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
        this.production = new Production(productionCost, productionReturn);
        this.victoryPoints = victoryPoints;
    }

    public Map<Resource, Integer> getCost() {
        return Collections.unmodifiableMap(cost);
    }

    public DevelopmentCardType getDevelopmentCardType() {
        return developmentCardType;
    }

    public Production getProduction() {
        return production;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    @Override
    public String toString() {
        return "DevelopmentCard{" +
                "developmentCardType=" + developmentCardType +
                ", victoryPoints=" + victoryPoints +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevelopmentCard that = (DevelopmentCard) o;
        return victoryPoints == that.victoryPoints &&
                Objects.equals(cost, that.cost) &&
                Objects.equals(developmentCardType, that.developmentCardType) &&
                Objects.equals(production, that.production);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost, developmentCardType, production, victoryPoints);
    }
}
