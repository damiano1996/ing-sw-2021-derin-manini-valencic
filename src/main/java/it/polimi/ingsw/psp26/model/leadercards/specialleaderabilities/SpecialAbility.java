package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.List;
import java.util.Objects;

public abstract class SpecialAbility {

    protected final Resource resource;

    protected SpecialAbility(Resource resource) {
        this.resource = resource;
    }

    public void execute(List<Resource> resourceList) {
    }

    public void activate(Player player) {
    }

    public Resource getResource() {
        return resource;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + getResource();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecialAbility that = (SpecialAbility) o;
        return resource == that.resource;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource);
    }
}
