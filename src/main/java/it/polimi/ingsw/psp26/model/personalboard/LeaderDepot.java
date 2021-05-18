package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.Objects;

public class LeaderDepot extends Depot {

    private final Resource depotResource;

    /**
     * Constructor of the class.
     *
     * @param depotResource the only ResourceType that can be inserted in this depot
     * @param virtualView   view that must be notified in case of model changes
     */
    public LeaderDepot(VirtualView virtualView, Resource depotResource) {
        super(virtualView, 2);
        this.depotResource = depotResource;
    }

    /**
     * Getter of the resource managed by the leader depot.
     *
     * @return resource
     */
    public Resource getLeaderDepotResource() {
        return depotResource;
    }

    /**
     * Overrides the method isAdmissible() to check if the Resource Type to insert is correct
     *
     * @param resource Resource to be added to the Depot
     * @return true if the Resource can be added, false in the other case
     */
    protected boolean isAdmissible(Resource resource) {
        return super.isAdmissible(resource) && resource.equals(depotResource);
    }

    /**
     * Equals method.
     *
     * @param o object to be compared
     * @return true if the given object is equal to this, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LeaderDepot that = (LeaderDepot) o;
        return depotResource == that.depotResource;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), depotResource);
    }

    @Override
    public String toString() {
        return "LeaderDepot{" +
                "depotResource=" + depotResource +
                "}";
    }
}
