package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.Server;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class LeaderDepotTest {

    LeaderDepot coinDepot;
    VirtualView virtualView;

    @Before
    public void setUp() {
        virtualView = new VirtualView(new Server());
        coinDepot = new LeaderDepot(virtualView, Resource.COIN);
    }

    @Test
    public void testGetMaxNumberOfResources() {
        assertEquals(2, coinDepot.getMaxNumberOfResources());
    }

    @Test
    public void testGetDepotResource() {
        assertEquals(coinDepot.getDepotResource(), Resource.COIN);
    }

    @Test
    public void testAddResource() throws CanNotAddResourceToDepotException {
        List<Resource> coinList = new ArrayList<>();
        coinList.add(Resource.COIN);
        coinList.add(Resource.COIN);

        coinDepot.addResource(Resource.COIN);
        coinDepot.addResource(Resource.COIN);

        assertEquals(coinDepot.getResources(), coinList);
    }

    @Test(expected = CanNotAddResourceToDepotException.class)
    public void testAddResource_CanNotAddResourceToDepotException_WrongResourceType() throws CanNotAddResourceToDepotException {
        coinDepot.addResource(Resource.STONE);
    }

    @Test(expected = CanNotAddResourceToDepotException.class)
    public void testAddResource_CanNotAddResourceToDepotException_TooMuchResourcesInDepot() throws CanNotAddResourceToDepotException {
        for (int i = 0; i < 4; i++) coinDepot.addResource(Resource.COIN);
    }

    @Test
    public void testEquals_TrueCase() throws CanNotAddResourceToDepotException {
        LeaderDepot coinDepotTwo = new LeaderDepot(virtualView, Resource.COIN);
        coinDepotTwo.addResource(Resource.COIN);

        coinDepot.addResource(Resource.COIN);

        assertEquals(coinDepotTwo, coinDepot);
    }

    @Test
    public void testEquals_FalseCase() throws CanNotAddResourceToDepotException {
        LeaderDepot stoneDepot = new LeaderDepot(virtualView, Resource.STONE);
        stoneDepot.addResource(Resource.STONE);

        coinDepot.addResource(Resource.COIN);

        assertNotEquals(stoneDepot, coinDepot);
    }
}