package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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
        // TODO: why true for any message type?
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.GENERAL_MESSAGE));
        phase.getMatchController().addObserver(message -> assertEquals(message.getMessageType(), MessageType.CHOICE_LEADERS));
        phase.getMatchController().addObserver(message -> {
            try {
                assertEquals(message.getListPayloads().size(), 4);
            } catch (EmptyPayloadException emptyPayloadException) {
                emptyPayloadException.printStackTrace();
            }
        }); // four cards
    }

    @Test
    public void testWrongLeaderCards() {
        testSendLeaderCardsChoiceMessage();

        List<LeaderCard> twoRandomLeaders = phase.getMatchController().getMatch().drawLeaders(2);
        phase.getMatchController().addObserver(message -> assertEquals(message.getMessageType(), MessageType.ERROR_MESSAGE));

        phase.getMatchController().update(
                new SessionMessage(
                        phase.getMatchController().getMatch().getPlayers().get(0).getSessionToken(),
                        MessageType.CHOICE_LEADERS,
                        twoRandomLeaders
                )
        );
    }

    @Test
    public void testPlay() {
    }
}