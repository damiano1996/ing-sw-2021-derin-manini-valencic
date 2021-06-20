package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.singleplayer;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.network.server.memory.GameSaver;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LorenzoMagnificoTurnStateTest {

    private MitmObserver mitm;
    private Turn turn;

    @Before
    public void setUp() throws Exception {
        mitm = new MitmObserver();
        VirtualView virtualView = new VirtualView();
        Phase phase = new Phase(virtualView.getMatchController());
        phase.getMatchController().addObserver(mitm);

        phase.getMatchController().getMatch().addPlayer(new Player(virtualView, "nickname", "sessionToken"));
        phase.getMatchController().setMaxNumberOfPlayers(1);
        turn = new Turn(
                new PlayingPhaseState(phase),
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                10
        );

        turn.changeState(new LorenzoMagnificoTurnState(turn, turn.getTurnPhase()));
    }

    @After
    public void tearDown() {
        GameSaver.getInstance().deleteDirectoryByMatchId(turn.getMatchController().getMatch().getId());
    }


    @Test
    public void testLorenzoPlay() throws InvalidPayloadException, EmptyPayloadException {
        int numberOfTokens = turn.getMatchController().getMatch().getActionTokens().size();

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.SKIP_LEADER_ACTION));

        String lorenzoPlay = mitm.getMessages().get(1).getPayload().toString();

        int numberOfRemainingTokens;
        if (lorenzoPlay.contains("BlackCrossShuffleActionToken")) numberOfRemainingTokens = numberOfTokens;
        else numberOfRemainingTokens = numberOfTokens - 1;

        assertEquals(mitm.getMessages().get(1).getMessageType(), MessageType.LORENZO_PLAY);
        assertEquals(numberOfRemainingTokens, turn.getMatchController().getMatch().getActionTokens().size());
    }

}