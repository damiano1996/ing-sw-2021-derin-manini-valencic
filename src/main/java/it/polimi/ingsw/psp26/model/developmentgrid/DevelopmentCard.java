package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class representing a DevelopmentCard.
 */
public class DevelopmentCard {

    // This attribute contains information about the Card Level and Color
    private final DevelopmentCardType developmentCardType;

    // This attribute contains information about the Production power of the Development Card
    private final Production production;

    private final Map<Resource, Integer> cost;
    private final int victoryPoints;

    /**
     * Constructor of the class.
     * It sets the DevelopmentCard parameters.
     *
     * @param cost             the cost of the DevelopmentCard
     * @param colorLevel       the DevelopmentCard Level
     * @param productionCost   the DevelopmentCard Production cost
     * @param productionReturn the DevelopmentCard Production return
     * @param victoryPoints    the DevelopmentCard victory points
     */
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
     * Getter of the DevelopmentCard cost.
     *
     * @return an unmodifiable Map containing the cost of the DevelopmentCard
     */
    public Map<Resource, Integer> getCost() {
        return Collections.unmodifiableMap(cost);
    }


    /**
     * Getter of the DevelopmentCard type.
     *
     * @return the DevelopmentCardType of the DevelopmentCard
     */
    public DevelopmentCardType getDevelopmentCardType() {
        return developmentCardType;
    }


    /**
     * Getter of the DevelopmentCard Production.
     *
     * @return the Production of the DevelopmentCard
     */
    public Production getProduction() {
        return production;
    }


    /**
     * Getter of the DevelopmentCard victory points.
     *
     * @return the victory points of the DevelopmentCard
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }


    /**
     * toString method.
     *
     * @return a String representation of the Object
     */
    @Override
    public String toString() {
        return "DevelopmentCard{" +
                "developmentCardType=" + developmentCardType +
                ", victoryPoints=" + victoryPoints +
                '}';
    }


    /**
     * Equals method.
     *
     * @param o Object to be compared
     * @return true if equals, false otherwise
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
     * hashCode method.
     *
     * @return a hashCode of the Object
     */
    @Override
    public int hashCode() {
        return Objects.hash(cost, developmentCardType, production, victoryPoints);
    }

}
