package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.psp26.model.enums.Resource.COIN;
import static it.polimi.ingsw.psp26.model.enums.Resource.SHIELD;
import static org.junit.Assert.*;

public class BenefitsTurnStateTest {

    private MitmObserver mitm;
    private Phase phase;
    private Turn turn;

    @Before
    public void setUp() {
        mitm = new MitmObserver();
        phase = new Phase(new MatchController(new VirtualView(), 0));
        phase.getMatchController().addObserver(mitm);

        phase.getMatchController().getMatch().addPlayer(new Player(new VirtualView(), "nickname", "sessionToken"));
    }

    @Test
    public void testFirstPlayerInkwell() throws InvalidPayloadException {
        turn = new Turn(
                new PlayingPhaseState(phase),
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                0
        );

        turn.changeState(new BenefitsTurnState(turn));

        assertFalse(turn.getTurnPlayer().hasInkwell());
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.GENERAL_MESSAGE));
        assertEquals(MessageType.GENERAL_MESSAGE, mitm.getMessages().get(0).getMessageType());
        assertTrue(turn.getTurnPlayer().hasInkwell());
    }

    @Test
    public void testAfterFirstPlayerChoiceResources() throws InvalidPayloadException {
        for (int i = 0; i < 3; i++) {
            phase.getMatchController().getMatch().addPlayer(new Player(new VirtualView(), "nickname" + (i + 1), "sessionToken" + (i + 1)));

            turn = new Turn(
                    new PlayingPhaseState(phase),
                    phase.getMatchController(),
                    phase.getMatchController().getMatch().getPlayers().get(i + 1),
                    i + 1
            );

            turn.changeState(new BenefitsTurnState(turn));

            turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.GENERAL_MESSAGE));
            assertEquals(MessageType.GENERAL_MESSAGE, mitm.getMessages().get(2 * i).getMessageType());
            assertEquals(MessageType.CHOICE_RESOURCE, mitm.getMessages().get(2 * i + 1).getMessageType());
        }
    }

    @Test
    public void testAfterResourcesChosen() throws InvalidPayloadException {
        phase.getMatchController().getMatch().addPlayer(new Player(new VirtualView(), "nickname2", "sessionToken2"));

        turn = new Turn(
                new PlayingPhaseState(phase),
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                1
        );

        turn.changeState(new BenefitsTurnState(turn));

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_RESOURCE, COIN, SHIELD));
        assertEquals(MessageType.PLACE_IN_WAREHOUSE, mitm.getMessages().get(0).getMessageType());
    }
}