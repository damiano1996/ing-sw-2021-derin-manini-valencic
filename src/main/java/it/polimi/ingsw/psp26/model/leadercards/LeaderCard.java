package it.polimi.ingsw.psp26.model.leadercards;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.SpecialAbility;

import java.util.HashMap;
import java.util.List;

public class LeaderCard {

    private final HashMap<Resource, Integer> resourcesRequirements;
    private final List<DevelopmentCard> developmentCardRequirements;
    private final int victoryPoints;
    private final SpecialAbility specialAbility;

    private boolean active;

    public LeaderCard(HashMap<Resource, Integer> resourcesRequirements, List<DevelopmentCard> developmentCardRequirements, int victoryPoints, SpecialAbility specialAbility) {
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

    public List<DevelopmentCard> getDevelopmentCardRequirements() {
        return developmentCardRequirements;
    }

    public SpecialAbility getSpecialAbility() {
        return specialAbility;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }
}
