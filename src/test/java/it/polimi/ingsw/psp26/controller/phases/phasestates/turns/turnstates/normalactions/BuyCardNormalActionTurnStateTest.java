package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.CanNotAddDevelopmentCardToSlotException;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToStrongboxException;
import it.polimi.ingsw.psp26.exceptions.DevelopmentCardSlotOutOfBoundsException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static org.junit.Assert.assertEquals;

public class BuyCardNormalActionTurnStateTest {

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

        turn.changeState(new BuyCardNormalActionTurnState(turn));

    }

    @Test
    public void testSendPlayBuyCardMessage() throws CanNotAddResourceToStrongboxException {

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), BUY_CARD));
        assertEquals(MessageType.CHOICE_CARD_TO_BUY, mitm.getMessages().get(0).getMessageType());

    }

    @Test
    public void testSendCardToBuyPlay() throws CanNotAddResourceToStrongboxException {

        DevelopmentCard card = buyCardResourceSetter();

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARD_TO_BUY, card));
        assertEquals(MessageType.CHOICE_POSITION, mitm.getMessages().get(0).getMessageType());

    }

    @Test
    public void testSendCardToBuyNotEnoughResourcesPlay() {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARD_TO_BUY, turn.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(2, 2).getFirstCard()));
        assertEquals(MessageType.CHOICE_CARD_TO_BUY, mitm.getMessages().get(0).getMessageType());

    }

    @Test
    public void testSendCardToBuyAndPlacePlay() throws CanNotAddResourceToStrongboxException {

        DevelopmentCard card = buyCardResourceSetter();

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARD_TO_BUY, card));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_POSITION, 1));

        Assert.assertEquals(turn.getTurnPlayer().getPersonalBoard().getVisibleDevelopmentCards().get(0), card);

    }

    @Test
    public void testSendCardToBuyAndPlaceWrongPositionPlay() throws CanNotAddResourceToStrongboxException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException {

        DevelopmentCard card = buyCardResourceSetter();

        turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(1, turn.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(1, 2).getFirstCard());
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARD_TO_BUY, card));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_POSITION, 1));

        assertEquals(MessageType.CHOICE_POSITION, mitm.getMessages().get(0).getMessageType());


    }

    private DevelopmentCard buyCardResourceSetter() throws CanNotAddResourceToStrongboxException {
        DevelopmentCard card = turn.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(2, 2).getFirstCard();
        for (Resource resource : card.getCost().keySet()) {
            for (int i = 0; i < card.getCost().get(resource); i++) {
                turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(resource);
            }
        }
        return card;
    }
}