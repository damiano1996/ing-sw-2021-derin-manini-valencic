package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static it.polimi.ingsw.psp26.utils.CollectionsUtils.castElements;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LeaderCardsAssignmentTest {

    private MitmObserver mitm;
    private Phase phase;
    private Turn turn;

    @Before
    public void setUp() {
        mitm = new MitmObserver();
        VirtualView virtualView = new VirtualView();
        phase = new Phase(virtualView.getMatchController());
        phase.getMatchController().addObserver(mitm);

        phase.getMatchController().getMatch().addPlayer(new Player(virtualView, "nickname", "sessionToken"));

        turn = new Turn(
                new PlayingPhaseState(phase),
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                0
        );

        turn.changeState(new LeaderCardsAssignmentTurnState(turn));
    }

    @Test
    public void testSendLeaderCardsChoiceMessage() throws InvalidPayloadException {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.GENERAL_MESSAGE));

        assertEquals(MessageType.CHOICE_LEADERS, mitm.getMessages().get(0).getMessageType());
        assertEquals(4, mitm.getMessages().get(0).getListPayloads().size());
    }

    @Test
    public void testWrongLeaderCards() throws InvalidPayloadException {
        testSendLeaderCardsChoiceMessage();

        List<LeaderCard> twoRandomLeaders = phase.getMatchController().getMatch().drawLeaders(2);

        turn.play(
                new SessionMessage(
                        phase.getMatchController().getMatch().getPlayers().get(0).getSessionToken(),
                        MessageType.CHOICE_LEADERS,
                        twoRandomLeaders
                )
        );

        assertEquals(MessageType.ERROR_MESSAGE, mitm.getMessages().get(1).getMessageType());
        assertEquals(MessageType.CHOICE_LEADERS, mitm.getMessages().get(2).getMessageType());
    }

    @Test
    public void testGoodLeaderChoice() throws InvalidPayloadException {
        testSendLeaderCardsChoiceMessage();

        List<LeaderCard> selectedLeaders = castElements(LeaderCard.class, mitm.getMessages().get(0).getListPayloads()).subList(0, 2);

        turn.play(
                new SessionMessage(
                        phase.getMatchController().getMatch().getPlayers().get(0).getSessionToken(),
                        MessageType.CHOICE_LEADERS,
                        selectedLeaders.toArray()
                )
        );

        assertTrue(turn.getTurnPlayer().getLeaderCards().contains(selectedLeaders.get(0)));
        assertTrue(turn.getTurnPlayer().getLeaderCards().contains(selectedLeaders.get(1)));
        assertEquals(2, turn.getTurnPlayer().getLeaderCards().size());
    }
}