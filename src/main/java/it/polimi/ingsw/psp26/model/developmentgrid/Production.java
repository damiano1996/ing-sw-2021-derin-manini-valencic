package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class Production {

    private final Map<Resource, Integer> productionCost;
    private final Map<Resource, Integer> productionReturn;

    public Production(Map<Resource, Integer> productionCost, Map<Resource, Integer> productionReturn) {
        this.productionCost = productionCost;
        this.productionReturn = productionReturn;
    }

    public Map<Resource, Integer> getProductionCost() {
        return Collections.unmodifiableMap(productionCost);
    }

    public Map<Resource, Integer> getProductionReturn() {
        return Collections.unmodifiableMap(productionReturn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return Objects.equals(productionCost, that.productionCost) && Objects.equals(productionReturn, that.productionReturn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productionCost, productionReturn);
    }
}
