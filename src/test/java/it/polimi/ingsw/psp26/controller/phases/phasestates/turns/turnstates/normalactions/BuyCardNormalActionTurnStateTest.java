package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToStrongboxException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.psp26.application.messages.MessageType.BUY_CARD;
import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_CARD_TO_BUY;
import static org.junit.Assert.assertEquals;

public class BuyCardNormalActionTurnStateTest {

    private Phase phase;
    private Turn turn;

    @Before
    public void setUp() throws Exception {

        phase = new Phase(new MatchController(new VirtualView(), 0));
        phase.getMatchController().getMatch().addPlayer(new Player(new VirtualView(), "nickname", "sessionToken"));
        phase.getMatchController().setMaxNumberOfPlayers(1);
        turn = new Turn(
                new PlayingPhaseState(phase),
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                10
        );

        turn.changeState(new BuyCardNormalActionTurnState(turn));

    }

    @Test
    public void play2() throws CanNotAddResourceToStrongboxException {

        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.STONE);
        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.STONE);
        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.COIN);
        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.COIN);
        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.SERVANT);
        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.SERVANT);

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), BUY_CARD));
        phase.getMatchController().addObserver(message -> assertEquals(message.getMessageType(), CHOICE_CARD_TO_BUY));

    }

    @Test
    public void play() {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARD_TO_BUY, turn.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(2, 2).getFirstCard()));
        phase.getMatchController().addObserver(message -> assertEquals(message.getMessageType(), CHOICE_CARD_TO_BUY));

    }

    @Test
    public void play3() {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARD_TO_BUY, turn.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(2, 2).getFirstCard()));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_POSITION, 1));
        phase.getMatchController().addObserver(message -> assertEquals(message.getMessageType(), CHOICE_CARD_TO_BUY));

    }
}