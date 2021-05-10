package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
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

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static org.junit.Assert.assertEquals;

public class ChooseLeaderActionTurnStateTest {

    private MitmObserver mitm;
    private Phase phase;
    private Turn turn;

    private LeaderCard leaderCard;

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

        turn.changeState(new ChooseLeaderActionTurnState(turn, TurnPhase.RESOURCE_PLACER_TO_LEADER_ACTION));

        leaderCard = new LeaderCard(new HashMap<>() {{
            put(Resource.COIN, 2);
        }}, new HashMap<>() {{
            put(new DevelopmentCardType(Color.GREEN, Level.FIRST), 1);
        }}, 0, new SpecialDepotAbility(Resource.COIN));

        turn.getTurnPlayer().setLeaderCards(new ArrayList<>() {{
            add(leaderCard);
        }});
    }

    @Test
    public void testChoicesMessage() throws InvalidPayloadException {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.GENERAL_MESSAGE));
        assertEquals(MessageType.CHOICE_LEADER_ACTION, mitm.getMessages().get(0).getMessageType());
    }

    private void goTo(MessageType messageType) throws InvalidPayloadException {
        testChoicesMessage();
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_LEADER_ACTION, messageType));
    }

    @Test
    public void testGoToDiscard() throws InvalidPayloadException {
        goTo(DISCARD_LEADER);
        assertEquals(MessageType.CHOICE_LEADERS, mitm.getMessages().get(1).getMessageType());
    }

    @Test
    public void testGoToActivate() throws InvalidPayloadException {
        goTo(ACTIVATE_LEADER);
        // we will receive an error message since the player doesn't have satisfied requirements.
        assertEquals(ERROR_MESSAGE, mitm.getMessages().get(1).getMessageType());
    }

    @Test
    public void testGoToChooseNormalAction() throws InvalidPayloadException {
        goTo(SKIP_LEADER_ACTION);
        assertEquals(CHOICE_NORMAL_ACTION, mitm.getMessages().get(1).getMessageType());
    }
}