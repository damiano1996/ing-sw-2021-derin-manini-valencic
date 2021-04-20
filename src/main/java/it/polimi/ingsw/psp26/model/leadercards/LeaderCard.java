package it.polimi.ingsw.psp26.model.leadercards;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.SpecialAbility;

import java.util.Collections;
import java.util.Map;

public class LeaderCard {

    private final Map<Resource, Integer> resourcesRequirements;
    private final Map<DevelopmentCardType, Integer> developmentCardRequirements;
    private final int victoryPoints;
    private final SpecialAbility specialAbility;

    private boolean active;

    public LeaderCard(
            Map<Resource, Integer> resourcesRequirements,
            Map<DevelopmentCardType, Integer> developmentCardRequirements,
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

    public void activate(Player ownerPlayer) {
        active = true;
        specialAbility.activate(ownerPlayer);
    }

    public void execute(Player ownerPlayer) {
        specialAbility.execute(ownerPlayer);
    }

    public Map<Resource, Integer> getResourcesRequirements() {
        return Collections.unmodifiableMap(resourcesRequirements);
    }

    public Map<DevelopmentCardType, Integer> getDevelopmentCardRequirements() {
        return Collections.unmodifiableMap(developmentCardRequirements);
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public String getAbilityName() {
        return specialAbility.getName();
    }

    public Resource getAbilityResource() {
        return specialAbility.getResource();
    }
}
