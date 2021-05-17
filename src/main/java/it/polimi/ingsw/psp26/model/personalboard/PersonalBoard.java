package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.exceptions.CanNotAddDevelopmentCardToSlotException;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToStrongboxException;
import it.polimi.ingsw.psp26.exceptions.DevelopmentCardSlotOutOfBoundsException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.network.server.MessageUtils.getPlayerModelUpdateMessage;

/**
 * Class to model the personal board.
 */
public class PersonalBoard extends Observable<SessionMessage> {

    private final FaithTrack faithTrack;
    private final Warehouse warehouse;
    private final List<List<DevelopmentCard>> developmentCardsSlots;
    private final List<Production> productions;
    private final List<Resource> strongbox;
    private final String sessionToken;

    /**
     * Constructor of the class.
     * It initializes all the components inside it and notifies the observers.
     *
     * @param virtualView virtual view to be notified on models changes
     */
    public PersonalBoard(VirtualView virtualView, String sessionToken) {
        super();
        addObserver(virtualView);

        faithTrack = new FaithTrack(virtualView, sessionToken);
        developmentCardsSlots = new ArrayList<>() {{
            add(new ArrayList<>());
            add(new ArrayList<>());
            add(new ArrayList<>());
        }};

        // productions with base
        productions = new ArrayList<>() {{
            add(new Production(
                    new HashMap<>() {{
                        put(Resource.UNKNOWN, 2);
                    }},
                    new HashMap<>() {{
                        put(Resource.UNKNOWN, 1);
                    }}));
        }};

        this.warehouse = new Warehouse(virtualView, 3, sessionToken);
        strongbox = new ArrayList<>();
        this.sessionToken = sessionToken;
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

        for (int i = 0; i < developmentCardsSlots.size(); i++) {
            try {
                int nCardsInSlot = getDevelopmentCardsSlot(i).size();
                if (nCardsInSlot > 0)
                    visibleCards.add(getDevelopmentCardsSlot(i).get(nCardsInSlot - 1));
            } catch (DevelopmentCardSlotOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        return Collections.unmodifiableList(visibleCards);
    }

    /**
     * Method to add a production object to the list
     *
     * @param production production object
     */
    public void addProduction(Production production) {
        productions.add(production);
    }

    /**
     * Method return all visible productions:
     * productions contained in the top development cards and external productions,
     * such as the base production and the leader production.
     *
     * @return list of visible productions
     */
    public List<Production> getAllVisibleProductions() {
        List<DevelopmentCard> visibleDevelopmentCards = getVisibleDevelopmentCards();
        List<Production> visibleProductions = visibleDevelopmentCards.stream().map(DevelopmentCard::getProduction).collect(Collectors.toList());
        visibleProductions.addAll(productions);
        return Collections.unmodifiableList(visibleProductions);
    }

    /**
     * Getter of the warehouse.
     *
     * @return warehouse object that handles the depots
     */
    public Warehouse getWarehouse() {
        return warehouse;
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

        notifyObservers(getPlayerModelUpdateMessage(sessionToken));
    }

    /**
     * Method to add a list of resources to the strongbox.
     *
     * @param resources list of resources to add
     */
    public void addResourcesToStrongbox(List<Resource> resources) throws CanNotAddResourceToStrongboxException {
        for (Resource resource : resources) addResourceToStrongbox(resource);

        notifyObservers(getPlayerModelUpdateMessage(sessionToken));
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

        notifyObservers(getPlayerModelUpdateMessage(sessionToken));
    }

    /**
     * Checks if a card can be placed in a development card slot.
     *
     * @param indexSlot       where to place the card
     * @param developmentCard the card to place
     * @return true if the card can be placed, false otherwise
     */
    public boolean isCardPlaceable(int indexSlot, DevelopmentCard developmentCard) {
        boolean isCardPlaceable;

        if (developmentCardsSlots.get(indexSlot).size() == 0) {
            isCardPlaceable = (developmentCardsSlots
                    .get(indexSlot)
                    .size() == 0 &&
                    developmentCard
                            .getDevelopmentCardType()
                            .getLevel()
                            .getLevelNumber() == 1);
        } else {
            isCardPlaceable = (developmentCardsSlots
                    .get(indexSlot)
                    .get(developmentCardsSlots.get(indexSlot).size() - 1)
                    .getDevelopmentCardType().getLevel().getLevelNumber() <
                    developmentCard
                            .getDevelopmentCardType()
                            .getLevel()
                            .getLevelNumber());
        }
        return isCardPlaceable;
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
     * Method to grab resources from warehouse and strongbox.
     * By default the resources are grabbed before from the warehouse and after from the strongbox.
     *
     * @param resource          resource type to grab
     * @param numberOfResources quantity of resources to grab
     * @return list containing the requested resources
     */
    public List<Resource> grabResourcesFromWarehouseAndStrongbox(Resource resource, int numberOfResources) {
        List<Resource> grabbedResources = warehouse.grabResources(resource, numberOfResources);

        if (grabbedResources.size() < numberOfResources)
            grabbedResources.addAll(grabResourcesFromStrongbox(resource, numberOfResources));

        return grabbedResources;
    }

    /**
     * Method to grab all the resources from warehouse and strongbox.
     *
     * @return list of resources
     */
    public List<Resource> grabAllAvailableResources() {
        List<Resource> allResources = warehouse.grabAllResources();
        allResources.addAll(strongbox);
        strongbox.clear();
        return allResources;
    }

    /**
     * Getter of all the resources contained between strongbox and warehouse.
     *
     * @return list of resources
     */
    public List<Resource> getAllAvailableResources() {
        List<Resource> resources = new ArrayList<>(warehouse.getResources());
        resources.addAll(strongbox);
        return Collections.unmodifiableList(resources);
    }

}
