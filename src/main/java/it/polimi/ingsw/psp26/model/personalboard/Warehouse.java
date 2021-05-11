package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToWarehouse;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.network.server.MessageUtils.getPlayerModelUpdateMessage;

/**
 * Class to manage the depots of the warehouse and the depots added by the leader cards.
 */
public class Warehouse extends Observable<SessionMessage> {

    private final List<Depot> baseDepots;
    private final List<LeaderDepot> leaderDepots;
    private final String sessionToken;

    /**
     * Class constructor.
     *
     * @param virtualView        virtual view that must be notified
     * @param numberOfBaseDepots number of base depots
     */
    public Warehouse(VirtualView virtualView, int numberOfBaseDepots, String sessionToken) {
        super();
        addObserver(virtualView);

        baseDepots = new ArrayList<>();
        leaderDepots = new ArrayList<>();
        // creating the base depots
        for (int i = 0; i < numberOfBaseDepots; i++)
            baseDepots.add(new Depot(virtualView, i + 1));
        this.sessionToken = sessionToken;
    }

    /**
     * Method to add resource to a given depot.
     * It checks the rule: "no same resource among different base depots".
     * If depot is of leader type the rule mustn't be considered.
     * Then tries to add the resource to the warehouse.
     *
     * @param indexDepot index of the depot
     * @param resource   resource to add to the depot
     * @throws CanNotAddResourceToDepotException if there is a depot (different from the chosen) that contains
     *                                           the given resource and this depot is between the first three.
     *                                           Or if in the depot there is no more space.
     */
    public void addResourceToDepot(int indexDepot, Resource resource) throws CanNotAddResourceToDepotException {
        // checking if there is another base depot containing this kind of resource
        if (indexDepot < baseDepots.size())
            for (int i = 0; i < baseDepots.size(); i++)
                if (baseDepots.get(i).getContainedResourceType().equals(resource) && i != indexDepot)
                    throw new CanNotAddResourceToDepotException();

        getDepotByIndex(indexDepot).addResource(resource);

        notifyObservers(getPlayerModelUpdateMessage(sessionToken));
    }

    /**
     * Getter of the depot by index.
     * Since the base depot list is split by the leader depot list,
     * this method returns the indexed depot as if the two lists were connected (in order: baseDepots - leaderDepots)
     *
     * @param indexDepot index of the depot
     * @return depot
     */
    private Depot getDepotByIndex(int indexDepot) {
        if (indexDepot < baseDepots.size())
            return baseDepots.get(indexDepot);
        else
            return leaderDepots.get(indexDepot - baseDepots.size());
    }

    /**
     * Method tries to add a resource to the warehouse iterating over all the depots.
     *
     * @param resource resource to add
     * @throws CanNotAddResourceToWarehouse if there is no depot to place the resource
     */
    public void addResource(Resource resource) throws CanNotAddResourceToWarehouse {
        addResource(resource, 0);

        notifyObservers(getPlayerModelUpdateMessage(sessionToken));
    }

    /**
     * Method to add a resource to the warehouse starting from a desired depot index.
     * If the resource can be placed in the requested depot index, it will be placed,
     * otherwise the resource will be placed in the first available
     *
     * @param resource           resource to add to the warehouse
     * @param startingDepotIndex index of the desired depot
     * @throws CanNotAddResourceToWarehouse if the resource cannot be placed
     */
    public void addResource(Resource resource, int startingDepotIndex) throws CanNotAddResourceToWarehouse {
        boolean added = false;

        for (int i = startingDepotIndex; i < baseDepots.size() + leaderDepots.size(); i++) {
            try {
                addResourceToDepot(i, resource);
                added = true;
                break;
            } catch (CanNotAddResourceToDepotException ignored) {
            }
        }
        if (!added) throw new CanNotAddResourceToWarehouse();

        notifyObservers(getPlayerModelUpdateMessage(sessionToken));
    }

    /**
     * Getter of all depots.
     *
     * @return unmodifiable list of depots
     */
    public List<Depot> getAllDepots() {
        return Collections.unmodifiableList(getAllModifiableDepots());
    }

    /**
     * Getter of all depots.
     *
     * @return modifiable list of depots
     */
    private List<Depot> getAllModifiableDepots() {
        List<Depot> allDepots = new ArrayList<>();
        allDepots.addAll(baseDepots);
        allDepots.addAll(leaderDepots);
        return allDepots;
    }

    /**
     * Getter of the base depots
     *
     * @return list of depots
     */
    public List<Depot> getBaseDepots() {
        return Collections.unmodifiableList(baseDepots);
    }

    /**
     * Getter of the leader depots
     *
     * @return list of leader depots
     */
    public List<LeaderDepot> getLeaderDepots() {
        return Collections.unmodifiableList(leaderDepots);
    }

    /**
     * Method to grab all the resources from all the depots, removing them from the depots.
     *
     * @return list of the resources
     */
    public List<Resource> grabAllResources() {
        List<Resource> grabbedResources = baseDepots.stream().map(Depot::grabAllResources).flatMap(List::stream).collect(Collectors.toList());
        grabbedResources.addAll(leaderDepots.stream().map(Depot::grabAllResources).flatMap(List::stream).collect(Collectors.toList()));
        return grabbedResources;
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

        for (Depot depot : getAllModifiableDepots())
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
        return getAllModifiableDepots().stream().map(Depot::getResources).flatMap(Collection::stream).collect(Collectors.toUnmodifiableList());
    }

    /**
     * Method to add a leaderDepot to the warehouse.
     *
     * @param leaderDepot leaderDepot to add
     */
    public void addLeaderDepot(LeaderDepot leaderDepot) {
        leaderDepots.add(leaderDepot);

        notifyObservers(getPlayerModelUpdateMessage(sessionToken));
    }

    /**
     * Equals method
     *
     * @param o object to be compared
     * @return true if equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return Objects.equals(baseDepots, warehouse.baseDepots) && Objects.equals(leaderDepots, warehouse.leaderDepots);
    }

    /**
     * Hashing method.
     *
     * @return hashed object
     */
    @Override
    public int hashCode() {
        return Objects.hash(baseDepots, leaderDepots);
    }

}
