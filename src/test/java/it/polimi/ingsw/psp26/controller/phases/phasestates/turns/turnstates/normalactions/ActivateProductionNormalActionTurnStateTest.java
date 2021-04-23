package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.CanNotAddDevelopmentCardToSlotException;
import it.polimi.ingsw.psp26.exceptions.DevelopmentCardSlotOutOfBoundsException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_CARDS_TO_ACTIVATE;
import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_RESOURCE;
import static org.junit.Assert.assertEquals;

public class ActivateProductionNormalActionTurnStateTest {

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

        turn.changeState(new ActivateProductionNormalActionTurnState(turn));

    }

    @Test
    public void play() {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.ACTIVATE_PRODUCTION));
        phase.getMatchController().addObserver(message -> assertEquals(message.getMessageType(), CHOICE_CARDS_TO_ACTIVATE));

    }

    @Test
    public void play2() throws CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException {
        turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(1, turn.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(2, 2).getFirstCard());
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARDS_TO_ACTIVATE, turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions()));
        phase.getMatchController().addObserver(message -> assertEquals(message.getMessageType(), CHOICE_RESOURCE));

    }

    @Test
    public void play3() throws CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException {
        turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(1, turn.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(2, 2).getFirstCard());
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARDS_TO_ACTIVATE, turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions()));
        phase.getMatchController().addObserver(message -> assertEquals(message.getMessageType(), CHOICE_RESOURCE));

    }
}