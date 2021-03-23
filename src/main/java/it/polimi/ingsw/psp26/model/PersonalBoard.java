package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.exceptions.CanNotAddDevelopmentCardToSlotException;
import it.polimi.ingsw.psp26.exceptions.DepotOutOfBoundException;
import it.polimi.ingsw.psp26.exceptions.DevelopmentCardSlotOutOfBoundsException;

import java.util.ArrayList;
import java.util.List;

public class PersonalBoard {

    private FaithTrack faithTrack;
    private List<List<DevelopmentCard>> developmentCardsSlots;
    private List<Depot> warehouseDepots;
    private List<Resource> strongbox;


    public PersonalBoard() {
        faithTrack = new FaithTrack();
        developmentCardsSlots = new ArrayList<>(3);
        warehouseDepots = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            developmentCardsSlots.add(new ArrayList<>());
            warehouseDepots.add(new Depot(i + 1));
        }
        strongbox = new ArrayList<>();
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public List<List<DevelopmentCard>> getDevelopmentCardsSlots() {
        return developmentCardsSlots;
    }

    public List<DevelopmentCard> getDevelopmentCardsSlot(int index) throws DevelopmentCardSlotOutOfBoundsException {
        if (index >= developmentCardsSlots.size()) throw new DevelopmentCardSlotOutOfBoundsException();
        else return developmentCardsSlots.get(index);
    }

    public List<DevelopmentCard> getVisibleDevelopmentCards() {
        List<DevelopmentCard> visibleCards = new ArrayList<>();
        for (List<DevelopmentCard> developmentCardsSlot : developmentCardsSlots) {
            if (developmentCardsSlot.size() > 0) visibleCards.add(developmentCardsSlot.get(developmentCardsSlot.size() - 1));
        }
        return visibleCards;
    }

    public List<Depot> getWarehouseDepots() {
        return warehouseDepots;
    }

    public Depot getWarehouseDepot(int index) throws DepotOutOfBoundException {
        if (index >= warehouseDepots.size()) throw new DepotOutOfBoundException();
        else return warehouseDepots.get(index);
    }

    public List<Resource> getStrongbox() {
        return strongbox;
    }

    public void addDevelopmentCard(int indexSlot, DevelopmentCard developmentCard) throws CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException {
        if (indexSlot > developmentCardsSlots.size()) throw  new DevelopmentCardSlotOutOfBoundsException();
        if (isCardPleaceable(indexSlot, developmentCard)) developmentCardsSlots.get(indexSlot).add(developmentCard);
        else throw new CanNotAddDevelopmentCardToSlotException();
    }

    /**
     * tells if a card can be placed in a developmentCardSlot
     *
     * @param indexSlot       where to place the card
     * @param developmentCard the card to place
     * @return true if the card can be places, false if not
     */
    private boolean isCardPleaceable(int indexSlot, DevelopmentCard developmentCard) {
        return (developmentCardsSlots.get(indexSlot).size() == 0 || developmentCardsSlots.get(indexSlot).get(developmentCardsSlots.get(indexSlot).size() - 1).getLevel().getLevelNumber() < developmentCard.getLevel().getLevelNumber());
    }

    public void addResourceToStrongbox(List<Resource> resource) {
        strongbox.addAll(resource);
    }


}
