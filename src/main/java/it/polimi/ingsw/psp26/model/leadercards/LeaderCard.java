package it.polimi.ingsw.psp26.model.leadercards;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.SpecialAbility;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Leader card class.
 */
public class LeaderCard {

    private final Map<Resource, Integer> resourcesRequirements;
    private final Map<DevelopmentCardType, Integer> developmentCardRequirements;
    private final int victoryPoints;
    private final SpecialAbility specialAbility;

    private boolean active;

    /**
     * Constructor of the class.
     *
     * @param resourcesRequirements       hashmap containing the resources requirements (resource, quantity)
     * @param developmentCardRequirements hashmap containing the development card type requirements (card type, quantity)
     * @param victoryPoints               victory points
     * @param specialAbility              special ability of the leader
     */
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

    /**
     * Method to check if leader is active or not.
     *
     * @return true if activated, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Method to activate the leader.
     *
     * @param ownerPlayer player that is owner of this leader
     */
    public void activate(Player ownerPlayer) {
        active = true;
        specialAbility.activate(ownerPlayer);
    }

    /**
     * Method to execute the special ability.
     *
     * @param resourceList list of resource the leader power interacts with
     */
    public void execute(List<Resource> resourceList) {
        if (isActive()) specialAbility.execute(resourceList);
    }

    /**
     * Getter of the resource requirements.
     *
     * @return unmodifiable hashmap containing resources and requested quantity
     */
    public Map<Resource, Integer> getResourcesRequirements() {
        return Collections.unmodifiableMap(resourcesRequirements);
    }

    /**
     * Getter of the development card type requirements
     *
     * @return unmodifiable hashmap containing the development card type and the requested quantity
     */
    public Map<DevelopmentCardType, Integer> getDevelopmentCardRequirements() {
        return Collections.unmodifiableMap(developmentCardRequirements);
    }

    /**
     * Getter of the victory points.
     *
     * @return int number of victory points
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * Method to get the special ability in string.
     *
     * @return string of the special ability
     */
    public String getAbilityToString() {
        return specialAbility.toString();
    }

    /**
     * Getter of the resource managed by the special ability.
     *
     * @return resource
     */
    public Resource getAbilityResource() {
        return specialAbility.getResource();
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
        LeaderCard that = (LeaderCard) o;
        return victoryPoints == that.victoryPoints &&
                active == that.active &&
                Objects.equals(resourcesRequirements, that.resourcesRequirements) &&
                Objects.equals(developmentCardRequirements, that.developmentCardRequirements) &&
                Objects.equals(specialAbility, that.specialAbility);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourcesRequirements, developmentCardRequirements, victoryPoints, specialAbility, active);
    }
}
