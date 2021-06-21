package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static it.polimi.ingsw.psp26.utils.CollectionsUtils.grabElements;

/**
 * Class to model the depot (one shelf of the warehouse).
 */
public class Depot extends Observable<SessionMessage> {

    private final int maxNumberOfResources;
    private final List<Resource> resources;

    /**
     * Constructor of the class.
     *
     * @param virtualView          view that must be notified in case of model changes
     * @param maxNumberOfResources max number of resources that can be allocated in the depot
     */
    public Depot(VirtualView virtualView, int maxNumberOfResources) {
        addObserver(virtualView);

        this.maxNumberOfResources = maxNumberOfResources;
        resources = new ArrayList<>();
    }

    /**
     * Used when recovering a Match.
     * It resets the List of Observers and adds the new VirtualView passed as a parameter.
     *
     * @param virtualView the new VirtualView to add to the Observers List
     */
    public void restoreVirtualView(VirtualView virtualView) {
        resetObservers();
        addObserver(virtualView);
    }

    /**
     * Getter of the max number of resources that are allowed.
     *
     * @return max number of resources
     */
    public int getMaxNumberOfResources() {
        return maxNumberOfResources;
    }

    /**
     * Getter of the resources contained in the Depot.
     *
     * @return list of the resources in the Depot
     */
    public List<Resource> getResources() {
        return Collections.unmodifiableList(resources);
    }

    /**
     * Method to add a Resource to the Depot.
     *
     * @param resource resource to add
     * @throws CanNotAddResourceToDepotException if Depot has reached the maximum number of Resources or
     *                                           if the Resource is not admissible given the others
     */
    protected void addResource(Resource resource) throws CanNotAddResourceToDepotException {
        if (resources.size() == maxNumberOfResources) throw new CanNotAddResourceToDepotException();
        if (isAdmissible(resource)) resources.add(resource);
        else throw new CanNotAddResourceToDepotException();
    }

    /**
     * Method to obtain all the Resources from the Depot.
     *
     * @return ist containing all the resources of the Depot
     */
    public List<Resource> grabAllResources() {
        return grabResources(resources.size());
    }

    /**
     * Method to grab resources from the Depot.
     *
     * @param quantity quantity of Resources to be removed from the Depot
     * @return list containing the number of requested Resources
     * @throws IndexOutOfBoundsException if trying to remove more Resources than the Depot contains
     */
    public List<Resource> grabResources(int quantity) throws IndexOutOfBoundsException {
        return grabElements(resources, quantity);
    }

    /**
     * Checks if a Resource could be added to a specified Depot.
     *
     * @param resource resource to be added to the Depot
     * @return true if the Resource can be added, false in the other case
     */
    protected boolean isAdmissible(Resource resource) {
        return (resources.size() == 0 || resources.contains(resource)) &&
                !resource.equals(Resource.FAITH_MARKER) &&
                !resource.equals(Resource.EMPTY);
    }

    /**
     * Getter of the type of Resources contained in the Depot.
     *
     * @return contained Resource type
     */
    public Resource getContainedResourceType() {
        if (resources.size() > 0) return resources.get(0);
        else return Resource.EMPTY;
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
        Depot depot = (Depot) o;
        return maxNumberOfResources == depot.maxNumberOfResources && Objects.equals(resources, depot.resources);
    }

    /**
     * Hashing method.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(maxNumberOfResources, resources);
    }

}
