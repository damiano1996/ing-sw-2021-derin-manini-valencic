package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MatchControllerTest {

    private VirtualView virtualView;
    private MatchController matchController;

    @Before
    public void setUp() {
        virtualView = new VirtualView();
        matchController = new MatchController(virtualView, 0);
    }

    @Test
    public void testAddPlayer() throws InvalidPayloadException {
        matchController.update(new SessionMessage("sessionToken", MessageType.ADD_PLAYER, "nickname"));
        assertEquals("sessionToken", matchController.getMatch().getPlayers().get(0).getSessionToken());
    }

    @Test
    public void testIsWaitingForPlayers() throws InvalidPayloadException {
        matchController.setMaxNumberOfPlayers(1);
        assertTrue(matchController.isWaitingForPlayers());
        testAddPlayer();
        assertFalse(matchController.isWaitingForPlayers());
    }

    @Test
    public void testStopWaitingForPlayers() {
        assertTrue(matchController.isWaitingForPlayers());
        matchController.stopWaitingForPlayers();
        assertFalse(matchController.isWaitingForPlayers());
    }

    @Test
    public void testSetMaxNumberOfPlayers() {
        matchController.setMaxNumberOfPlayers(4);
        assertEquals(4, matchController.getMaxNumberOfPlayers());
    }

}