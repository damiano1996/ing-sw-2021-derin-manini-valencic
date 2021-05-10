package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.ActivateProductionNormalActionTurnState;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY;
import static it.polimi.ingsw.psp26.application.messages.MessageType.NOTIFICATION_UPDATE;
import static org.junit.Assert.*;

public class CheckVaticanReportTurnStateTest {

    private MitmObserver mitm;
    private Phase phase;
    private Turn turn;

    @Before
    public void setUp() throws Exception {
        mitm = new MitmObserver();
        VirtualView virtualView = new VirtualView();
        phase = new Phase(virtualView.getMatchController());
        phase.getMatchController().addObserver(mitm);

        phase.getMatchController().getMatch().addPlayer(new Player(virtualView, "nickname", "sessionToken"));
        phase.getMatchController().setMaxNumberOfPlayers(1);
        turn = new Turn(
                new PlayingPhaseState(phase),
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                10
        );

        turn.changeState(new CheckVaticanReportTurnState(turn));

    }

    @Test
    public void testPlaySinglePlayerActivateTile() throws InvalidPayloadException {

        turn.getTurnPlayer().getPersonalBoard().getFaithTrack().addFaithPoints(8);

        boolean tileBeforeTurn = turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections()[0].isPopesFavorTileActive();

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_NORMAL_ACTION, MessageType.ACTIVATE_PRODUCTION));

        assertEquals(tileBeforeTurn, false);
        assertTrue(turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections()[0].isPopesFavorTileActive());
    }

    @Test
    public void testPlaySinglePlayerNotActivatedTile() throws InvalidPayloadException {

        turn.getTurnPlayer().getPersonalBoard().getFaithTrack().addFaithPoints(7);

        boolean tileBeforeTurn = turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections()[0].isPopesFavorTileActive();

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_NORMAL_ACTION, MessageType.ACTIVATE_PRODUCTION));

        assertEquals(tileBeforeTurn, false);
        assertEquals(tileBeforeTurn, turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections()[0].isPopesFavorTileActive());
    }

    @Test
    public void testPlaySinglePlayerBlackCrossActivate() throws InvalidPayloadException {

        turn.getTurnPlayer().getPersonalBoard().getFaithTrack().addFaithPoints(7);

        turn.getTurnPlayer().getPersonalBoard().getFaithTrack().moveBlackCrossPosition(8);

        boolean tileBeforeTurn = turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections()[0].isPopesFavorTileActive();

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_NORMAL_ACTION, MessageType.ACTIVATE_PRODUCTION));

        turn.getTurnPlayer().getPersonalBoard().getFaithTrack().addFaithPoints(2);

        turn.changeState(new CheckVaticanReportTurnState(turn));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_NORMAL_ACTION, MessageType.ACTIVATE_PRODUCTION));

        assertTrue(!turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections()[0].isPopesFavorTileActive());

    }
}