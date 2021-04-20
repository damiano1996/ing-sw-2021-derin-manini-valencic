package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;

public abstract class SpecialAbility {

    protected final Resource resource;

    protected SpecialAbility(Resource resource) {
        this.resource = resource;
    }

    public void execute(Player player) {
    }

    public void activate(Player player) {
    }

    public Resource getResource() {
        return resource;
    }

    @Override
    public String toString() {
        return ProductionAbility.class.getSimpleName() + ":" + getResource();
    }

}
