package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Class to model the personal board.
 */
public class PersonalBoard extends Observable<Message> {

    private final FaithTrack faithTrack;
    private final List<List<DevelopmentCard>> developmentCardsSlots;
    private final List<Depot> warehouseDepots;
    private final List<Resource> strongbox;
    private final DevelopmentCard baseCard;

    /**
     * Constructor of the class.
     * It initializes all the components inside it and notifies the observers.
     *
     * @param virtualView virtual view to be notified on models changes
     */
    public PersonalBoard(VirtualView virtualView) {
        super();
        addObserver(virtualView);

        faithTrack = new FaithTrack(virtualView);
        developmentCardsSlots = new ArrayList<>(3);
        warehouseDepots = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            developmentCardsSlots.add(new ArrayList<>());
            warehouseDepots.add(new Depot(virtualView, i + 1));
        }
        strongbox = new ArrayList<>();
        baseCard = new DevelopmentCard(
                new HashMap<>() {{
                    put(Resource.EMPTY, 0);
                }},
                new DevelopmentCardType(Color.WHITE, Level.UNDEFINED),
                new HashMap<>() {{
                    put(Resource.COIN, 2);
                    put(Resource.SERVANT, 2);
                    put(Resource.STONE, 2);
                    put(Resource.SHIELD, 2);
                }},
                new HashMap<>() {{
                    put(Resource.COIN, 1);
                    put(Resource.SERVANT, 1);
                    put(Resource.STONE, 1);
                    put(Resource.SHIELD, 1);
                }},
                0);

        notifyObservers(new Message()); // TODO: to be completed
    }

    /**
     * Getter of the faith track.
     *
     * @return the faith track object
     */
    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    /**
     * Getter of the development card slots.
     *
     * @return list containing slots (sub-lists) of development cards
     */
    public List<List<DevelopmentCard>> getDevelopmentCardsSlots() {
        return Collections.unmodifiableList(developmentCardsSlots);
    }

    /**
     * Getter of the development card slot given the index of the slot.
     *
     * @param slotIndex index of the slot
     * @return list of development cards contained in the slot
     * @throws DevelopmentCardSlotOutOfBoundsException if slotIndex is out of bounds
     */
    public List<DevelopmentCard> getDevelopmentCardsSlot(int slotIndex) throws DevelopmentCardSlotOutOfBoundsException {
        if (slotIndex >= developmentCardsSlots.size()) throw new DevelopmentCardSlotOutOfBoundsException();
        else return Collections.unmodifiableList(developmentCardsSlots.get(slotIndex));
    }

    /**
     * Getter of the development cards that are on top (visible).
     *
     * @return list of development cards
     */
    public List<DevelopmentCard> getVisibleDevelopmentCards() {
        List<DevelopmentCard> visibleCards = new ArrayList<>();
        for (List<DevelopmentCard> developmentCardsSlot : developmentCardsSlots) {
            if (developmentCardsSlot.size() > 0)
                visibleCards.add(developmentCardsSlot.get(developmentCardsSlot.size() - 1));
        }
        return Collections.unmodifiableList(visibleCards);
    }

    /**
     * Getter of the warehouse depots.
     *
     * @return list containing the depots of the warehouse
     */
    public List<Depot> getWarehouseDepots() {
        return warehouseDepots;
    }

    /**
     * Getter of the warehouse depot by index.
     *
     * @param index index of the depot
     * @return depot in the corresponding position
     * @throws DepotOutOfBoundException if index out of bounds
     */
    public Depot getWarehouseDepot(int index) throws DepotOutOfBoundException {
        if (index >= warehouseDepots.size()) throw new DepotOutOfBoundException();
        else return warehouseDepots.get(index);
    }

    /**
     * Getter of the strongbox.
     *
     * @return list containing resources of the strongbox
     */
    public List<Resource> getStrongbox() {
        return Collections.unmodifiableList(strongbox);
    }

    /**
     * Method to add a resource to the strongbox.
     *
     * @param resource resource to add
     */
    public void addResourceToStrongbox(Resource resource) throws CanNotAddResourceToStrongboxException {
        if (resource.equals(Resource.EMPTY) || resource.equals(Resource.FAITH_MARKER))
            throw new CanNotAddResourceToStrongboxException();
        strongbox.add(resource);
    }

    /**
     * Method to add a list of resources to the strongbox.
     *
     * @param resources list of resources to add
     */
    public void addResourcesToStrongbox(List<Resource> resources) throws CanNotAddResourceToStrongboxException {
        for (Resource resource : resources) addResourceToStrongbox(resource);
        notifyObservers(new Message()); // TODO: to be completed
    }

    /**
     * Method to add a development card to the personal board.
     *
     * @param indexSlot       index of the slot in which the card must be added
     * @param developmentCard development card to be added
     * @throws CanNotAddDevelopmentCardToSlotException if card cannot be added
     * @throws DevelopmentCardSlotOutOfBoundsException if index is out of bounds
     */
    public void addDevelopmentCard(int indexSlot, DevelopmentCard developmentCard) throws CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException {
        if (indexSlot > developmentCardsSlots.size()) throw new DevelopmentCardSlotOutOfBoundsException();
        if (isCardPlaceable(indexSlot, developmentCard)) developmentCardsSlots.get(indexSlot).add(developmentCard);
        else throw new CanNotAddDevelopmentCardToSlotException();

        notifyObservers(new Message()); // TODO: to be completed
    }

    /**
     * Checks if a card can be placed in a development card slot.
     *
     * @param indexSlot       where to place the card
     * @param developmentCard the card to place
     * @return true if the card can be placed, false otherwise
     */
    private boolean isCardPlaceable(int indexSlot, DevelopmentCard developmentCard) {
        return (developmentCardsSlots.get(indexSlot).size() == 0 ||
                developmentCardsSlots
                        .get(indexSlot)
                        .get(developmentCardsSlots.get(indexSlot).size() - 1)
                        .getDevelopmentCardType().getLevel().getLevelNumber() <
                        developmentCard
                                .getDevelopmentCardType()
                                .getLevel()
                                .getLevelNumber());
    }

    /**
     * Method to add a Leader Depot to the warehouseDepots List when activating a Depot Leader Card
     *
     * @param leaderDepot the LeaderDepot to add
     */
    public void addLeaderDepot(LeaderDepot leaderDepot) {
        warehouseDepots.add(leaderDepot);
    }

    /**
     * Method that provides a fixed exchange of resource 2:1
     *
     * @param resourceRequirement the list of resources to give in
     * @param product             the resource given back
     */
    public Resource baseProductionPower(List<Resource> resourceRequirement, Resource product) throws WrongBasicPowerResourceRequirementException {
        if (resourceRequirement.size() != 2) throw new WrongBasicPowerResourceRequirementException();
        return product;
    }

    /**
     * Method to grab resources from strongbox.
     *
     * @param resource          the resource type to remove
     * @param numberOfResources the number of resource of that type to remove
     * @return list of grabbed resources
     */
    public List<Resource> grabResourcesFromStrongbox(Resource resource, int numberOfResources) {
        List<Resource> grabbedResources = new ArrayList<>();
        for (int i = 0; i < numberOfResources; i++) {
            if (strongbox.remove(resource))
                grabbedResources.add(resource);
        }
        return grabbedResources;
    }

    /**
     * Method to grab resources from warehouse in the specified quantity.
     *
     * @param resource          resource to grab
     * @param numberOfResources quantity of resources to grab
     * @return list containing the requested resources
     * @throws NegativeNumberOfElementsToGrabException if number of resources is negative
     */
    public List<Resource> grabResourcesFromWarehouse(Resource resource, int numberOfResources) throws NegativeNumberOfElementsToGrabException {
        for (Depot depot : warehouseDepots) {
            if (depot.getResources().size() > 0)
                if (depot.getResources().get(0).equals(resource))
                    return depot.grabResources(Math.min(numberOfResources, depot.getResources().size()));
        }
        return new ArrayList<>();
    }

    /**
     * Method to grab resources from warehouse and strongbox.
     * By default the resources are grabbed before from the warehouse and after from the strongbox.
     *
     * @param resource          resource type to grab
     * @param numberOfResources quantity of resources to grab
     * @return list containing the requested resources
     * @throws NegativeNumberOfElementsToGrabException if the number of resources to grab is negative
     */
    public List<Resource> grabResourcesFromWarehouseAndStrongbox(Resource resource, int numberOfResources) throws NegativeNumberOfElementsToGrabException {
        List<Resource> grabbedResources = new ArrayList<>(grabResourcesFromWarehouse(resource, numberOfResources));

        if (grabbedResources.size() < numberOfResources)
            grabbedResources.addAll(grabResourcesFromStrongbox(resource, numberOfResources));

        return grabbedResources;
    }

}
