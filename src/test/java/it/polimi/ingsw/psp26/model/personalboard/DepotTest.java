package it.polimi.ingsw.psp26.model.personalboard;


import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DepotTest {

    private Depot depot;
    private List<Resource> resourceList;

    @Before
    public void setUp() {
        depot = new Depot(new VirtualView(), 3);
        resourceList = new ArrayList<>();
    }

    @Test
    public void testGetMaxNumberOfResources() {
        assertEquals(3, depot.getMaxNumberOfResources());
    }

    @Test
    public void testGetResources() throws CanNotAddResourceToDepotException {
        depot.addResource(Resource.STONE);
        depot.addResource(Resource.STONE);

        resourceList.add(Resource.STONE);
        resourceList.add(Resource.STONE);

        assertEquals(resourceList, depot.getResources());
    }


    @Test
    public void testRemoveResource() throws CanNotAddResourceToDepotException {
        depot.addResource(Resource.STONE);
        depot.removeResource();

        assertEquals(resourceList, depot.getResources());
    }

    @Test
    public void testRemoveVariableNumberOfResource() throws CanNotAddResourceToDepotException {
        depot.addResource(Resource.STONE);
        depot.addResource(Resource.STONE);
        depot.removeResource(2);

        assertEquals(resourceList, depot.getResources());
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void testRemoveVariableNumberOfResource_IndexOutOfBoundsException_RemovedTooMuchResources() throws CanNotAddResourceToDepotException {
        depot.addResource(Resource.STONE);
        depot.addResource(Resource.STONE);
        depot.removeResource(3);
    }

    @Test
    public void testAddResourcesList() throws CanNotAddResourceToDepotException {
        for (int i = 0; i < 3; i++) resourceList.add(Resource.STONE);

        depot.addResource(resourceList);

        assertEquals(resourceList, depot.getResources());
    }

    @Test(expected = CanNotAddResourceToDepotException.class)
    public void testAddResourceList_CanNotAddResourceToDepotException_TooMuchResourcesInList() throws CanNotAddResourceToDepotException {
        for (int i = 0; i < 4; i++) resourceList.add(Resource.STONE);

        depot.addResource(resourceList);
    }

    @Test(expected = CanNotAddResourceToDepotException.class)
    public void testAddResourceList_CanNotAddResourceToDepotException_WrongResourceTypeInList() throws CanNotAddResourceToDepotException {
        resourceList.add(Resource.STONE);
        resourceList.add(Resource.STONE);
        resourceList.add(Resource.COIN);

        depot.addResource(Resource.STONE);

        depot.addResource(resourceList);
    }

    @Test(expected = CanNotAddResourceToDepotException.class)
    public void testAddResource_CanNotAddResourceToDepotException_WrongResourceType() throws CanNotAddResourceToDepotException {
        depot.addResource(Resource.STONE);
        depot.addResource(Resource.SERVANT);
    }

    @Test(expected = CanNotAddResourceToDepotException.class)
    public void testAddResource_CanNotAddResourceToDepotException_TooMuchResourcesInDepot() throws CanNotAddResourceToDepotException {
        for (int i = 0; i < 4; i++) depot.addResource(Resource.STONE);
    }

}