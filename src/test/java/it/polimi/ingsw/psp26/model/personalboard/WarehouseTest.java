package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class WarehouseTest {

    private Warehouse warehouse;

    @Before
    public void setUp() throws Exception {

        warehouse = new Warehouse(new VirtualView());
    }

    @Test
    public void testAddResourceToDepot_ClassicCase() throws CanNotAddResourceToDepotException {
        warehouse.addResourceToDepot(0, Resource.COIN);
        assertEquals(Resource.COIN, warehouse.grabResources(Resource.COIN, 1).get(0));
    }

    @Test
    public void testAddResourceToWarehouse() {
    }

    @Test
    public void testGetDepots() {
    }

    @Test
    public void testGrabAllResourcesFromDepot() {
    }

    @Test
    public void testGrabAllResources() {
    }

    @Test
    public void testGrabResources() {
    }

    @Test
    public void testGetResources() {
    }

    @Test
    public void testAddLeaderDepot() {
    }
}