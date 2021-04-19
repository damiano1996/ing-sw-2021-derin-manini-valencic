package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.exceptions.ResourceSupplySlotOutOfBoundsException;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to model the resource supply.
 */
public class ResourceSupply {

    public static final Resource[] RESOURCES_SLOTS = new Resource[]{Resource.COIN, Resource.STONE, Resource.SERVANT, Resource.SHIELD};

    /**
     * Constructor of the class.
     * It initializes an array specifying which resource is contained in each slot.
     */
    public ResourceSupply() {
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
        if (indexSlot < 0 || indexSlot >= RESOURCES_SLOTS.length) throw new ResourceSupplySlotOutOfBoundsException();

        List<Resource> resources = new ArrayList<>();
        for (int i = 0; i < amountOfResources; i++) {
            resources.add(RESOURCES_SLOTS[indexSlot]);
        }
        return resources;
    }

}
