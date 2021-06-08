package it.polimi.ingsw.psp26.controller.phases.phasestates.turns;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.ChooseNormalActionTurnState;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TurnTest {

    private MitmObserver mitm;

    private PlayingPhaseState playingPhaseState;
    private MatchController matchController;
    private Player playerOne;
    private Player playerTwo;

    private Turn turn;

    @Before
    public void setUp() throws Exception {
        mitm = new MitmObserver();

        VirtualView virtualView = new VirtualView();

        matchController = new MatchController(virtualView, 0);
        playerOne = new Player(virtualView, "Nickname1", "SessionToken1");
        playerTwo = new Player(virtualView, "Nickname2", "SessionToken2");
        matchController.getMatch().addPlayer(playerOne);
        matchController.getMatch().addPlayer(playerTwo);
        matchController.setMaxNumberOfPlayers(2);

        Phase phase = new Phase(matchController);
        phase.getMatchController().addObserver(mitm);

        playingPhaseState = new PlayingPhaseState(phase, false);

        int turnNumber = 1;

        turn = new Turn(playingPhaseState, matchController, playerOne, turnNumber);
    }


    @Test
    public void testChangeState() {
        TurnState turnState = new ChooseNormalActionTurnState(turn);
        turn.changeState(turnState);

        assertEquals(turnState, turn.getTurnState());
    }


    @Test
    public void testGetTurnPlayer() {
        assertEquals(playerOne, turn.getTurnPlayer());
    }


    @Test
    public void testSetTurnPlayer() {
        turn.setTurnPlayer(playerTwo);

        assertEquals(playerTwo, turn.getTurnPlayer());
    }


    @Test
    public void testGetTurnNumber() {
        assertEquals(1, turn.getTurnNumber());
    }


    @Test
    public void testSetTurnNumber() {
        turn.setTurnNumber(4);

        assertEquals(4, turn.getTurnNumber());
    }


    @Test
    public void testGetTurnPhase() {
        assertEquals(TurnPhase.RESOURCE_PLACER_TO_LEADER_ACTION, turn.getTurnPhase());
    }


    @Test
    public void testSetTurnPhase() {
        turn.setTurnPhase(TurnPhase.LEADER_ACTION_TO_END);

        assertEquals(TurnPhase.LEADER_ACTION_TO_END, turn.getTurnPhase());
    }


    @Test
    public void testGetPlayingPhaseState() {
        assertEquals(playingPhaseState, turn.getPlayingPhaseState());
    }


    @Test
    public void testGetMatchController() {
        assertEquals(matchController, turn.getMatchController());
    }


    @Test
    public void testNotifyAllPlayers() throws EmptyPayloadException {
        turn.notifyAllPlayers("Message");

        assertEquals(mitm.getMessages().get(0).getMessageType(), MessageType.NOTIFICATION_UPDATE);
        assertTrue(mitm.getMessages().get(0).getPayload().toString().contains("Message"));
    }


    @Test
    public void testPlayerSkipsTurn() throws InvalidPayloadException, EmptyPayloadException {
        SessionMessage deathMessage = new SessionMessage(playerTwo.getSessionToken(), MessageType.DEATH);
        turn.setTurnPlayer(playerTwo);

        turn.play(deathMessage);

        assertEquals(mitm.getMessages().get(0).getMessageType(), MessageType.NOTIFICATION_UPDATE);
        assertTrue(mitm.getMessages().get(0).getPayload().toString().contains(turn.getTurnPlayer().getNickname() + " lost the connection. He skips the turn."));
    }

}