package it.polimi.ingsw.psp26.model;

import java.util.List;

public class LeaderCard {

    private final List<Resource> resourcesRequirements;
    private final List<DevelopmentCard> developmentCardRequirements;
    private final int victoryPoints;
    private final SpecialAbility specialAbility;

    private boolean active;

    public LeaderCard(List<Resource> resourcesRequirements, List<DevelopmentCard> developmentCardRequirements, int victoryPoints, SpecialAbility specialAbility) {
        this.resourcesRequirements = resourcesRequirements;
        this.developmentCardRequirements = developmentCardRequirements;
        this.victoryPoints = victoryPoints;
        this.specialAbility = specialAbility;
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    public List<Resource> getResourcesRequirements() {
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
