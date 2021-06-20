package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.network.server.memory.GameSaver;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static org.junit.Assert.assertEquals;

public class ChooseNormalActionTurnStateTest {

    private MitmObserver mitm;
    private Phase phase;
    private Turn turn;

    @Before
    public void setUp() throws Exception {

        mitm = new MitmObserver();
        phase = new Phase(new MatchController(new VirtualView(), 0));
        phase.getMatchController().addObserver(mitm);

        phase.getMatchController().getMatch().addPlayer(new Player(new VirtualView(), "nickname", "sessionToken"));
        phase.getMatchController().setMaxNumberOfPlayers(1);
        turn = new Turn(
                new PlayingPhaseState(phase),
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                10
        );

        turn.changeState(new ChooseNormalActionTurnState(turn));
    }

    @After
    public void tearDown() {
        GameSaver.getInstance().deleteDirectoryByMatchId(phase.getMatchController().getMatch().getId());
    }

    @Test
    public void playActivateProduction() throws InvalidPayloadException {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_NORMAL_ACTION, ACTIVATE_PRODUCTION));
        assertEquals(MessageType.CHOICE_PRODUCTIONS_TO_ACTIVATE, mitm.getMessages().get(0).getMessageType());
    }

    @Test
    public void playBuyCard() throws InvalidPayloadException {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_NORMAL_ACTION, BUY_CARD));
        assertEquals(MessageType.CHOICE_CARD_TO_BUY, mitm.getMessages().get(0).getMessageType());
    }

    @Test
    public void playMarketResource() throws InvalidPayloadException {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_NORMAL_ACTION, MARKET_RESOURCE));
        assertEquals(MessageType.CHOICE_ROW_COLUMN, mitm.getMessages().get(0).getMessageType());
    }

    @Test
    public void playEmptyPayload() throws InvalidPayloadException {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_NORMAL_ACTION));
        assertEquals(MessageType.CHOICE_NORMAL_ACTION, mitm.getMessages().get(0).getMessageType());
    }
}