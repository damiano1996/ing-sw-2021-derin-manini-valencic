package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.exceptions.ResourceSupplySlotOutOfBoundsException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ResourceSupplyTest {

    ResourceSupply resourceSupply;
    List<Resource> stoneResource;

    @Before
    public void setUp() {
        resourceSupply = new ResourceSupply();
        stoneResource = new ArrayList<>();
        stoneResource.add(Resource.STONE);
        stoneResource.add(Resource.STONE);
        stoneResource.add(Resource.STONE);
        stoneResource.add(Resource.STONE);
    }

    @Test
    public void grabResources() throws ResourceSupplySlotOutOfBoundsException {
        for (int i = 0; i < stoneResource.size(); i++) {
            assertEquals(stoneResource.get(i), resourceSupply.grabResources(1, 4).get(i));
        }
    }


    @Test(expected = ResourceSupplySlotOutOfBoundsException.class)
    public void grabResources_ResourceSupplySlotOutOfBoundsException() throws ResourceSupplySlotOutOfBoundsException {
        for (int i = 0; i < stoneResource.size(); i++) {
            assertEquals(stoneResource.get(i), resourceSupply.grabResources(4, 4).get(i));
        }
    }
}