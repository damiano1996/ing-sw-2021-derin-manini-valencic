package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.exceptions.ResourceSupplySlotOutOfBoundsException;

import java.util.ArrayList;
import java.util.List;

public class ResourceSupply {

    private final Resource[] slots;

    public ResourceSupply() {
        slots = new Resource[]{Resource.COIN, Resource.STONE, Resource.SERVANT, Resource.SHIELD};
    }

    /**
     * Attention to add method
     */
    public List<Resource> grabResources(int indexSlot, int amountOfResources) throws ResourceSupplySlotOutOfBoundsException {
        if (indexSlot < 0 || indexSlot >= slots.length) throw new ResourceSupplySlotOutOfBoundsException();

        List<Resource> resources = new ArrayList<>();
        for (int i = 0; i < amountOfResources; i++) {
            resources.add(slots[indexSlot]);
        }
        return resources;
    }

}
