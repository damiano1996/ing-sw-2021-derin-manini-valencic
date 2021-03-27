package it.polimi.ingsw.psp26.model.leadercards;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.SpecialAbility;

import java.util.HashMap;

public class LeaderCard {

    private final HashMap<Resource, Integer> resourcesRequirements;
    private final HashMap<DevelopmentCardType, Integer> developmentCardRequirements;
    private final int victoryPoints;
    private final SpecialAbility specialAbility;

    private boolean active;

    public LeaderCard(
            HashMap<Resource, Integer> resourcesRequirements,
            HashMap<DevelopmentCardType, Integer> developmentCardRequirements,
            int victoryPoints,
            SpecialAbility specialAbility
    ) {
        this.resourcesRequirements = resourcesRequirements;
        this.developmentCardRequirements = developmentCardRequirements;
        this.victoryPoints = victoryPoints;
        this.specialAbility = specialAbility;
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        active = true;
    }

    public HashMap<Resource, Integer> getResourcesRequirements() {
        return resourcesRequirements;
    }

    public HashMap<DevelopmentCardType, Integer> getDevelopmentCardRequirements() {
        return developmentCardRequirements;
    }

    public SpecialAbility getSpecialAbility() {
        return specialAbility;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }
}
