package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.List;
import java.util.Objects;

/**
 * Abstract class that represent the SpecialAbility of the LeaderCards.
 */
public abstract class SpecialAbility {

    // The characteristic Resource type of the LeaderCard
    protected final Resource resource;

    /**
     * Constructor of the class.
     * It sets the characteristic Resource of the LeaderCard.
     *
     * @param resource the characteristic Resource type of the LeaderCard
     */
    protected SpecialAbility(Resource resource) {
        this.resource = resource;
    }


    /**
     * Method to execute the SpecialAbility.
     *
     * @param resourceList list of resource the leader power interacts with
     */
    public void execute(List<Resource> resourceList) {
    }


    /**
     * Method to activate the LeaderCard.
     *
     * @param player player that is owner of this LeaderCard
     */
    public void activate(Player player) {
    }


    /**
     * Getter of the characteristic Leader Resource.
     *
     * @return the characteristic Resource type of the LeaderCard
     */
    public Resource getResource() {
        return resource;
    }


    /**
     * toString method.
     *
     * @return a String representation of the Object
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "-" + getResource();
    }


    /**
     * Equals method.
     *
     * @param o Object to be compared
     * @return true if equals, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecialAbility that = (SpecialAbility) o;
        return resource == that.resource;
    }


    /**
     * hashCode method.
     *
     * @return a hashCode of the Object
     */
    @Override
    public int hashCode() {
        return Objects.hash(resource);
    }

}
