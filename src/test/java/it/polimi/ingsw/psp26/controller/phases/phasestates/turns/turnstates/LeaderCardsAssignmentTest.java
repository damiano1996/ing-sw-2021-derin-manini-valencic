package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LeaderCardsAssignmentTest {

    private Phase phase;
    private Turn turn;

    @Before
    public void setUp() {

        phase = new Phase(new MatchController(new VirtualView(), 0));
        phase.getMatchController().getMatch().addPlayer(new Player(new VirtualView(), "nickname", "sessionToken"));

        turn = new Turn(
                new PlayingPhaseState(phase),
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                10
        );

        turn.changeState(new LeaderCardsAssignmentTurnState(turn));
    }

    @Test
    public void testSendLeaderCardsChoiceMessage() {
        phase.getMatchController().addObserver(message -> assertEquals(message.getMessageType(), MessageType.CHOICE_LEADERS));
    }

    @Test
    public void testPlay() {
    }
}