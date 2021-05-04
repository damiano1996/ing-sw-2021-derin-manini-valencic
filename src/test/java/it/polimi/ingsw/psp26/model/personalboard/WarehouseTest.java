package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToWarehouse;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.model.enums.Resource.COIN;
import static org.junit.Assert.assertEquals;

public class WarehouseTest {

    private Warehouse warehouse;

    @Before
    public void setUp() {
        warehouse = new Warehouse(new VirtualView(), 3);
    }

    @Test
    public void testAddResourceToDepot_ClassicCase() throws CanNotAddResourceToDepotException {
        warehouse.addResourceToDepot(0, COIN);
        assertEquals(COIN, warehouse.getAllDepots().get(0).getResources().get(0));
        assertEquals(COIN, warehouse.grabResources(COIN, 1).get(0));
    }

    @Test
    public void testAddResourceStartingIndex() throws CanNotAddResourceToWarehouse {
        warehouse.addResource(COIN, 2);
        warehouse.addResource(COIN, 2);
        warehouse.addResource(COIN, 2);
        assertEquals(3, warehouse.getBaseDepots().get(2).getResources().size());
        assertEquals(0, warehouse.getBaseDepots().get(0).getResources().size());
        assertEquals(0, warehouse.getBaseDepots().get(1).getResources().size());
    }

    @Test(expected = CanNotAddResourceToDepotException.class)
    public void testAddResourceToDepot_FullDepotCase() throws CanNotAddResourceToDepotException {
        for (int i = 0; i < warehouse.getAllDepots().get(0).getMaxNumberOfResources() + 1; i++)
            warehouse.addResourceToDepot(0, COIN);
    }

    @Test
    public void testAddResource_ClassicCaseWithLeader() throws CanNotAddResourceToWarehouse {
        warehouse.addLeaderDepot(new LeaderDepot(new VirtualView(), COIN));

        for (int i = 0; i < warehouse.getAllDepots().get(0).getMaxNumberOfResources() + 1; i++)
            warehouse.addResource(COIN);

        int nResources = warehouse.getAllDepots().get(0).getMaxNumberOfResources() + 1;
        assertEquals(nResources, warehouse.grabAllResources().size());
    }

    @Test
    public void testAddResource_WithLeaderAllBaseDepotsFull() throws CanNotAddResourceToWarehouse {
        // full base depots
        warehouse.addResource(COIN);
        warehouse.addResource(Resource.SERVANT);
        warehouse.addResource(Resource.STONE);
        // filling leader depot
        warehouse.addLeaderDepot(new LeaderDepot(new VirtualView(), Resource.SHIELD));
        warehouse.addResource(Resource.SHIELD);
        warehouse.addResource(Resource.SHIELD);

        assertEquals(COIN, warehouse.getBaseDepots().get(0).getResources().get(0));
        assertEquals(Resource.SERVANT, warehouse.getBaseDepots().get(1).getResources().get(0));
        assertEquals(Resource.STONE, warehouse.getBaseDepots().get(2).getResources().get(0));
        assertEquals(2, warehouse.getLeaderDepots().get(0).getResources().size());

        assertEquals(5, warehouse.grabAllResources().size());

    }

    @Test(expected = CanNotAddResourceToWarehouse.class)
    public void testAddResource_ClassicCaseWithNoLeader() throws CanNotAddResourceToWarehouse {
        warehouse.addLeaderDepot(new LeaderDepot(new VirtualView(), COIN));

        for (int i = 0; i < warehouse.getAllDepots().get(0).getMaxNumberOfResources() + 3; i++)
            warehouse.addResource(COIN);
    }

    @Test
    public void testGetAllDepots() {
        warehouse.addLeaderDepot(new LeaderDepot(new VirtualView(), COIN));
        assertEquals(4, warehouse.getAllDepots().size());
    }

    @Test
    public void testGetBaseDepots() {
        warehouse.addLeaderDepot(new LeaderDepot(new VirtualView(), COIN));
        assertEquals(3, warehouse.getBaseDepots().size());
    }

    @Test
    public void testGetLeaderDepots() {
        warehouse.addLeaderDepot(new LeaderDepot(new VirtualView(), COIN));
        warehouse.addLeaderDepot(new LeaderDepot(new VirtualView(), Resource.SHIELD));
        assertEquals(2, warehouse.getLeaderDepots().size());
    }

    @Test
    public void testGrabAllResources() throws CanNotAddResourceToWarehouse {
        warehouse.addResource(COIN);
        warehouse.addResource(Resource.SHIELD);
        warehouse.addResource(Resource.SERVANT);
        assertEquals(new ArrayList<>() {{
            add(COIN);
            add(Resource.SHIELD);
            add(Resource.SERVANT);
        }}, warehouse.grabAllResources());
    }

    @Test
    public void testGrabResources() throws CanNotAddResourceToDepotException {
        warehouse.addResourceToDepot(0, Resource.SHIELD);
        warehouse.addResourceToDepot(2, COIN);
        warehouse.addResourceToDepot(2, COIN);
        assertEquals(2, warehouse.grabResources(COIN, 2).size());
    }

    @Test
    public void testGetResources() throws CanNotAddResourceToWarehouse {
        List<Resource> resources = new ArrayList<>();
        resources.add(COIN);
        warehouse.addResource(COIN);
        assertEquals(resources, warehouse.getResources());
    }

    @Test
    public void testAddLeaderDepot() {
        assertEquals(0, warehouse.getLeaderDepots().size());
        warehouse.addLeaderDepot(new LeaderDepot(new VirtualView(), COIN));
        assertEquals(1, warehouse.getLeaderDepots().size());
    }
}