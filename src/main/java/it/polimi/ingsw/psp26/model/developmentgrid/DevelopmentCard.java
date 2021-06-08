package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DevelopmentCard {

    // This attribute contains information about the Card Level and Color
    private final DevelopmentCardType developmentCardType;

    // This attribute contains information about the Production power of the Development Card
    private final Production production;

    private final Map<Resource, Integer> cost;
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


    /**
     * Getter of the Development Card cost
     *
     * @return An unmodifiable Map containing the cost of the Card
     */
    public Map<Resource, Integer> getCost() {
        return Collections.unmodifiableMap(cost);
    }


    /**
     * Getter of the Development Card type
     *
     * @return The DevelopmentCardType of the Card
     */
    public DevelopmentCardType getDevelopmentCardType() {
        return developmentCardType;
    }


    /**
     * Getter of the Development Card Production
     *
     * @return The Production of the Card
     */
    public Production getProduction() {
        return production;
    }


    /**
     * Getter of the Development Card victory points
     *
     * @return The Victory Points of the Card
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }


    /**
     * toString method
     *
     * @return A String representation of the Object
     */
    @Override
    public String toString() {
        return "DevelopmentCard{" +
                "developmentCardType=" + developmentCardType +
                ", victoryPoints=" + victoryPoints +
                '}';
    }


    /**
     * Equals method
     *
     * @param o Object to be compared
     * @return True if equals, false otherwise
     */
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


    /**
     * hashCode method
     *
     * @return A hashCode of the Object
     */
    @Override
    public int hashCode() {
        return Objects.hash(cost, developmentCardType, production, victoryPoints);
    }

}
