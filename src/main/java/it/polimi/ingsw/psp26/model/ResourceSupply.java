package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.exceptions.ResourceSupplySlotOutOfBoundsException;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to model the resource supply.
 */
public class ResourceSupply {

    private final Resource[] slots;

    /**
     * Constructor of the class.
     * It initializes an array specifying which resource is contained in each slot.
     */
    public ResourceSupply() {
        slots = new Resource[]{Resource.COIN, Resource.STONE, Resource.SERVANT, Resource.SHIELD};
    }

    /**
     * Method to grad a resource from the slot.
     *
     * @param indexSlot         index of the selected slot
     * @param amountOfResources quantity of resources to grab from the slot
     * @return list containing the requested resources
     * @throws ResourceSupplySlotOutOfBoundsException if the requested index is out of bounds
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
