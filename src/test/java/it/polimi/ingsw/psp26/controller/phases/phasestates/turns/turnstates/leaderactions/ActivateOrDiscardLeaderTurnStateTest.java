package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.SpecialDepotAbility;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ActivateOrDiscardLeaderTurnStateTest {

    private MitmObserver mitm;
    private Phase phase;
    private Turn turn;

    private LeaderCard leaderCard;
    private DevelopmentCardType developmentCardType;

    @Before
    public void setUp() {
        mitm = new MitmObserver();
        phase = new Phase(new MatchController(new VirtualView(), 0));
        phase.getMatchController().addObserver(mitm);

        phase.getMatchController().getMatch().addPlayer(new Player(new VirtualView(), "nickname", "sessionToken"));

        turn = new Turn(
                new PlayingPhaseState(phase),
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                0
        );

        developmentCardType = new DevelopmentCardType(Color.GREEN, Level.FIRST);

        leaderCard = new LeaderCard(new HashMap<>() {{
            put(Resource.COIN, 2);
        }}, new HashMap<>() {{
            put(developmentCardType, 1);
        }}, 0, new SpecialDepotAbility(Resource.COIN));

        turn.getTurnPlayer().setLeaderCards(new ArrayList<>() {{
            add(leaderCard);
        }});

        turn.changeState(new ActivateOrDiscardLeaderTurnState(turn));
    }

    private void choiceLeaders(MessageType action, MessageType expected) throws InvalidPayloadException {
        turn.play(
                new SessionMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        MessageType.CHOICE_LEADER_ACTION,
                        action
                )
        );
        assertEquals(expected, mitm.getMessages().get(0).getMessageType());
    }

    @Test
    public void testChoiceLeadersToDiscard() throws InvalidPayloadException {
        choiceLeaders(MessageType.DISCARD_LEADER, MessageType.CHOICE_LEADERS);
    }

    @Test
    public void testDiscardLeader() throws InvalidPayloadException {
        assertEquals(0, turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getFaithPoints());

        choiceLeaders(MessageType.DISCARD_LEADER, MessageType.CHOICE_LEADERS);

        turn.play(
                new SessionMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        MessageType.CHOICE_LEADERS,
                        mitm.getMessages().get(0).getArrayPayloads())
        );

        assertEquals(1, turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getFaithPoints());
    }

    @Test
    public void testChoiceLeadersToActivate_NoRequirementsOne() throws InvalidPayloadException {
        choiceLeaders(MessageType.ACTIVATE_LEADER, MessageType.ERROR_MESSAGE);
    }

    @Test
    public void testChoiceLeadersToActivate_NoRequirementsTwo() throws CanNotAddResourceToStrongboxException, InvalidPayloadException {
        // adding resources to satisfy requirements
        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.COIN);
        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.COIN);


        choiceLeaders(MessageType.ACTIVATE_LEADER, MessageType.ERROR_MESSAGE);
    }

    @Test
    public void testChoiceLeadersToActivate_WithRequirementsSatisfied() throws CanNotAddResourceToStrongboxException, DevelopmentCardSlotOutOfBoundsException, CanNotAddDevelopmentCardToSlotException, InvalidPayloadException {
        // adding resources to satisfy requirements
        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.COIN);
        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.COIN);
        turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(0, new DevelopmentCard(new HashMap<>(), developmentCardType, new HashMap<>(), new HashMap<>(), 0));

        choiceLeaders(MessageType.ACTIVATE_LEADER, MessageType.CHOICE_LEADERS);
    }

    @Test
    public void testActivateLeader() throws EmptyPayloadException, DevelopmentCardSlotOutOfBoundsException, CanNotAddDevelopmentCardToSlotException, CanNotAddResourceToStrongboxException, InvalidPayloadException {
        assertFalse(turn.getTurnPlayer().getLeaderCards().get(0).isActive());

        testChoiceLeadersToActivate_WithRequirementsSatisfied();
        turn.play(
                new SessionMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        MessageType.CHOICE_LEADERS,
                        mitm.getMessages().get(0).getPayload()
                )
        );

        assertTrue(turn.getTurnPlayer().getLeaderCards().get(0).isActive());
    }

    @Test
    public void testAtLeastOneLeaderActivatableOrDiscardable() throws DevelopmentCardSlotOutOfBoundsException, CanNotAddDevelopmentCardToSlotException, InvalidPayloadException, CanNotAddResourceToStrongboxException, EmptyPayloadException {
        ActivateOrDiscardLeaderTurnState activateOrDiscardLeaderTurnState = new ActivateOrDiscardLeaderTurnState(turn);
        assertFalse(activateOrDiscardLeaderTurnState.isAtLeastOneLeaderActivatable());
        assertTrue(activateOrDiscardLeaderTurnState.isAtLeastOneLeaderDiscardable());

        // adding requirements
        testChoiceLeadersToActivate_WithRequirementsSatisfied();
        assertTrue(activateOrDiscardLeaderTurnState.isAtLeastOneLeaderActivatable());

        // requesting activation
        turn.play(
                new SessionMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        MessageType.CHOICE_LEADERS,
                        mitm.getMessages().get(0).getPayload()
                )
        );

        // no more activatable, no more discardable
        assertFalse(activateOrDiscardLeaderTurnState.isAtLeastOneLeaderActivatable());
        assertFalse(activateOrDiscardLeaderTurnState.isAtLeastOneLeaderDiscardable());
    }
}