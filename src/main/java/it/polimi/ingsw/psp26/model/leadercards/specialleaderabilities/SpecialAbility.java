package it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities;

import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.List;
import java.util.Objects;

public abstract class SpecialAbility {

    // The characteristic Resource type of the Leader Card
    protected final Resource resource;

    protected SpecialAbility(Resource resource) {
        this.resource = resource;
    }


    /**
     * Method to execute the special ability.
     *
     * @param resourceList list of resource the leader power interacts with
     */
    public void execute(List<Resource> resourceList) {
    }


    /**
     * Method to activate the leader.
     *
     * @param player player that is owner of this leader
     */
    public void activate(Player player) {
    }


    /**
     * @return The characteristic Resource type of the Leader Card
     */
    public Resource getResource() {
        return resource;
    }


    /**
     * toString method
     *
     * @return A String representation of the Object
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "-" + getResource();
    }


    /**
     * Equals method
     *
     * @param o Object to be compared
     * @return True if equals, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecialAbility that = (SpecialAbility) o;
        return resource == that.resource;
    }


    /**
     * hashCode method
     *
     * @return A hashCode of the Object
     */
    @Override
    public int hashCode() {
        return Objects.hash(resource);
    }
    
}
