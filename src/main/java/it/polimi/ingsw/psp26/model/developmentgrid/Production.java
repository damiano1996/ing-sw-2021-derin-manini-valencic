package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Production class.
 * It handles the production costs and returns.
 * It models the books inside the development and leader cards.
 */
public class Production {

    private final Map<Resource, Integer> productionCost;
    private final Map<Resource, Integer> productionReturn;

    /**
     * Constructor of the class.
     *
     * @param productionCost   hashmap representing the production cost (resource, quantity)
     * @param productionReturn hashmap representing the production return (resource, quantity)
     */
    public Production(Map<Resource, Integer> productionCost, Map<Resource, Integer> productionReturn) {
        this.productionCost = productionCost;
        this.productionReturn = productionReturn;
    }

    /**
     * Getter of the production cost.
     *
     * @return unmodifiable hashmap representing the production cost (resource, quantity)
     */
    public Map<Resource, Integer> getProductionCost() {
        return Collections.unmodifiableMap(productionCost);
    }

    /**
     * Getter of the production return.
     *
     * @return unmodifiable hashmap representing the production return (resource, quantity)
     */
    public Map<Resource, Integer> getProductionReturn() {
        return Collections.unmodifiableMap(productionReturn);
    }

    /**
     * Equals method.
     *
     * @param o object to be compared
     * @return true if equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return Objects.equals(productionCost, that.productionCost) && Objects.equals(productionReturn, that.productionReturn);
    }

}
