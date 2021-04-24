package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static org.junit.Assert.*;

public class MarketResourceNormalActionTurnStateTest {
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

        turn.changeState(new MarketResourceNormalActionTurnState(turn));

    }

    @Test
    public void testSendMarketResourceMessage() {

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(),CHOICE_NORMAL_ACTION, MARKET_RESOURCE));
        assertEquals(MessageType.CHOICE_ROW_COLUMN, mitm.getMessages().get(0).getMessageType());
    }

    @Test
    public void playSendChoiceRowColumn() {

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(),CHOICE_ROW_COLUMN, 2));
        assertEquals(MessageType.CHOICE_RESOURCE, mitm.getMessages().get(0).getMessageType());
    }

    @Test
    public void playSendToWareHousePlacer() {

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(),CHOICE_ROW_COLUMN, 2));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(),CHOICE_RESOURCE, Resource.STONE));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(),CHOICE_RESOURCE, Resource.COIN));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(),CHOICE_RESOURCE, Resource.SHIELD));

        assertEquals(MessageType.PLACE_IN_WAREHOUSE, mitm.getMessages().get(3).getMessageType());
    }
}