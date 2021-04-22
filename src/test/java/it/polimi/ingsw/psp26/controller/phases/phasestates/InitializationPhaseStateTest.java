package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class InitializationPhaseStateTest {

    private Phase phase;

    @Before
    public void setUp() {

        phase = new Phase(new MatchController(new VirtualView(), 0));
        phase.changeState(new InitializationPhaseState(phase));
    }

    @Test
    public void testAddPlayer() {

        String nickname = "nickname";
        String sessionToken = "sessionToken";
        phase.execute(new SessionMessage(
                sessionToken,
                MessageType.ADD_PLAYER,
                nickname
        ));

        assertEquals(phase.getMatchController().getMatch().getPlayers().get(0).getNickname(), nickname);
        assertEquals(phase.getMatchController().getMatch().getPlayers().get(0).getSessionToken(), sessionToken);
    }

    @Test
    public void testPhaseTransition() {
        int numOfPlayers = 1;
        phase.getMatchController().setMaxNumberOfPlayers(numOfPlayers);
        testAddPlayer();
        assertFalse(phase.getMatchController().isWaitingForPlayers());
        testAddPlayer();
        // if the number of players remains the specified one,
        // we can implicitly understand that the phase state has changed.
        assertEquals(numOfPlayers, phase.getMatchController().getMatch().getPlayers().size());
    }

}