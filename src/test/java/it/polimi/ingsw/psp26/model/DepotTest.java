package it.polimi.ingsw.psp26.model;


import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DepotTest {

    private Depot emptyDepot;
    private Depot depotWithResources;

    @Before
    public void setUp() throws CanNotAddResourceToDepotException {
        depotWithResources = new Depot(3);
        depotWithResources.addResource(Resource.STONE);
        depotWithResources.addResource(Resource.STONE);
        emptyDepot = new Depot(1);
    }

    @Test
    public void testGetMaxNumberOfResources() {
        assertEquals(3, depotWithResources.getMaxNumberOfResources());
    }

    @Test
    public void testGetResources() {
        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(Resource.STONE);
        resourceList.add(Resource.STONE);
        assertEquals(resourceList, depotWithResources.getResources());
    }

    @Test(expected = CanNotAddResourceToDepotException.class)
    public void testAddResource_ExceptionCase() throws CanNotAddResourceToDepotException {
        depotWithResources.addResource(Resource.SERVANT);
    }
}