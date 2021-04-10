package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MatchControllerTest {

    private VirtualView virtualView;
    private MatchController matchController;

    @Before
    public void setUp() {
        virtualView = new VirtualView();
        matchController = new MatchController(virtualView, 0);
    }

    @Test
    public void testUpdate() {
    }

    @Test
    public void testGetMatch() {
        assertNotNull(matchController.getMatch());
    }

    @Test
    public void testGetVirtualView() {
        assertEquals(virtualView, matchController.getVirtualView());
    }
}