package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.enums.Resource;

public abstract class SpecialAbility {

    private final Resource resource;

    protected SpecialAbility(Resource resource) {
        this.resource = resource;
    }

    public void execute() {
    }

    public Resource getResource() {
        return resource;
    }

    public abstract String getAbilityType();

    public abstract String getPowerDescription(int linetoPrint);

    public abstract String getResourceInformation();
}
