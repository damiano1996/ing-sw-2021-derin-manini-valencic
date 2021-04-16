package it.polimi.ingsw.psp26.controller.phases;

import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhaseTest {

    private MatchController matchController;
    private Phase phase;

    @Before
    public void setUp() {
        matchController = new MatchController(new VirtualView(), 0);
        phase = new Phase(matchController);
    }

    @Test
    public void testExecute() {
    }

    @Test
    public void testChangeState() {
    }

    @Test
    public void testGetMatchController() {
        assertEquals(matchController, phase.getMatchController());
    }
}