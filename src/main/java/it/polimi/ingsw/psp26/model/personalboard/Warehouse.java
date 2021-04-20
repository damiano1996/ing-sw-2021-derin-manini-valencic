package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToWarehouse;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to manage the depots of the warehouse and the depots added by the leader cards.
 */
public class Warehouse extends Observable<SessionMessage> {

    private static final int NUMBER_OF_DEFAULT_DEPOTS = 3;
    private final List<Depot> depots;

    /**
     * Class constructor.
     *
     * @param virtualView virtual view that must be notified
     */
    public Warehouse(VirtualView virtualView) {
        super();
        addObserver(virtualView);

        depots = new ArrayList<>();
        initializeDepots(virtualView);
    }

    /**
     * Method to initialize the depots.
     *
     * @param virtualView virtual view that must be notified
     */
    private void initializeDepots(VirtualView virtualView) {
        for (int i = 0; i < NUMBER_OF_DEFAULT_DEPOTS; i++)
            depots.add(new Depot(virtualView, i + 1));
    }

    /**
     * Method to add resource to a given depot.
     *
     * @param indexDepot index of the depot
     * @param resource   resource to add to the depot
     * @throws CanNotAddResourceToDepotException if there is a depot (different from the chosen) that contains
     *                                           the given resource and this depot is between the first three.
     *                                           Or if in the depot there is no more space.
     */
    public void addResourceToDepot(int indexDepot, Resource resource) throws CanNotAddResourceToDepotException {
        // checking if there is another depot containing this kind of resource
        for (int i = 0; i < depots.size(); i++)
            if (depots.get(i).getContainedResourceType().equals(resource) &&
                    i != indexDepot &&
                    indexDepot < NUMBER_OF_DEFAULT_DEPOTS &&
                    i < NUMBER_OF_DEFAULT_DEPOTS)
                throw new CanNotAddResourceToDepotException();

        depots.get(indexDepot).addResource(resource);
    }

    /**
     * Method tries to add a resource to the warehouse iterating over all the depots.
     *
     * @param resource resource to add
     * @throws CanNotAddResourceToWarehouse if there is no depot to place the resource
     */
    public void addResource(Resource resource) throws CanNotAddResourceToWarehouse {
        boolean added = false;

        for (int i = 0; i < depots.size(); i++) {
            try {
                addResourceToDepot(i, resource);
                added = true;
                break;
            } catch (CanNotAddResourceToDepotException ignored) {
            }
        }
        if (!added) throw new CanNotAddResourceToWarehouse();
    }

    /**
     * Getter of all depots.
     *
     * @return unmodifiable list of depots
     */
    public List<Depot> getAllDepots() {
        return Collections.unmodifiableList(depots);
    }

    /**
     * Getter of the base depots
     *
     * @return list of depots
     */
    public List<Depot> getBaseDepots() {
        return Collections.unmodifiableList(depots.subList(0, NUMBER_OF_DEFAULT_DEPOTS));
    }

    /**
     * Getter of the leader depots
     *
     * @return list of leader depots
     */
    public List<Depot> getLeaderDepots() {
        return Collections.unmodifiableList(depots.subList(NUMBER_OF_DEFAULT_DEPOTS, depots.size()));
    }

    /**
     * Method to grab all the resources from all the depots, removing them from the depots.
     *
     * @return list of the resources
     */
    public List<Resource> grabAllResources() {
        return depots.stream().map(Depot::grabAllResources).flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * Method to grab resources of a certain type from the depots.
     * Returns the resources and remove them from the warehouse.
     *
     * @param resource          resource to grab
     * @param numberOfResources quantity
     * @return list of grabbed resources
     */
    public List<Resource> grabResources(Resource resource, int numberOfResources) {
        List<Resource> grabbedResources = new ArrayList<>();

        for (Depot depot : depots)
            if (depot.getContainedResourceType().equals(resource))
                grabbedResources.addAll(depot.grabResources(Math.min(numberOfResources - grabbedResources.size(), depot.getResources().size())));

        return grabbedResources;
    }

    /**
     * Method to get all the resources contained in the warehouse.
     *
     * @return list of resources
     */
    public List<Resource> getResources() {
        return depots.stream().map(Depot::getResources).flatMap(Collection::stream).collect(Collectors.toUnmodifiableList());
    }

    /**
     * Method to add a leaderDepot to the warehouse.
     *
     * @param leaderDepot leaderDepot to add
     */
    public void addLeaderDepot(LeaderDepot leaderDepot) {
        depots.add(leaderDepot);
    }

}
