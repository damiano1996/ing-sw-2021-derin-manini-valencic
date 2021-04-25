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
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_RESOURCE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ActivateProductionNormalActionTurnStateTest {

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

        turn.changeState(new ActivateProductionNormalActionTurnState(turn));

    }

    @Test
    public void testSendActivateProductionMessage() throws EmptyPayloadException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException {

        turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(1, turn.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(2, 2).getFirstCard());
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_NORMAL_ACTION, MessageType.ACTIVATE_PRODUCTION));

        assertEquals(MessageType.CHOICE_CARDS_TO_ACTIVATE, mitm.getMessages().get(0).getMessageType());
        assertEquals(2, mitm.getMessages().get(0).getListPayloads().size());
    }

    @Test
    public void testSendChoiceCardsToActivateNoUnknownResource() throws CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, EmptyPayloadException, CanNotAddResourceToStrongboxException {

        DevelopmentCard card = turn.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(2, 2).getFirstCard();

        for (Resource resource : card.getProduction().getProductionCost().keySet()) {
            for (int i = 0; i < card.getProduction().getProductionCost().get(resource); i++) {
                turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(resource);
            }
        }

        turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(1, card);

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARDS_TO_ACTIVATE, card.getProduction()));

        List<Resource> resourceProduced = getProdResources(card);

        resourceProduced = resourceProduced.stream().filter(x -> !x.equals(Resource.EMPTY)).filter(x -> !x.equals(Resource.FAITH_MARKER)).collect(Collectors.toList());

        assertArrayEquals(resourceProduced.toArray(), turn.getTurnPlayer().getPersonalBoard().getStrongbox().toArray());
    }

    @Test
    public void testSendChoiceCardsToActivateCostUnknown() throws CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, CanNotAddResourceToStrongboxException {

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARDS_TO_ACTIVATE, turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().get(0)));

        assertEquals(MessageType.CHOICE_RESOURCE, mitm.getMessages().get(0).getMessageType());

    }

    @Test
    public void testSendChoiceResourceToActivateCostUnknown() throws CanNotAddResourceToStrongboxException {


        List<Resource> resource2 = new ArrayList<>();
        resource2.add(Resource.COIN);

        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.STONE);
        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.STONE);
        playCollection();

        assertArrayEquals(resource2.toArray(), turn.getTurnPlayer().getPersonalBoard().getStrongbox().toArray());
    }

    @Test
    public void testSendChoiceResourceToActivateNotEnoughResources() throws CanNotAddResourceToStrongboxException {

        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.STONE);

        playCollection();

        assertEquals(MessageType.CHOICE_CARDS_TO_ACTIVATE, mitm.getMessages().get(4).getMessageType());
    }

    private void playCollection() {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARDS_TO_ACTIVATE, turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().get(0)));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE, Resource.STONE));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE, Resource.STONE));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE, Resource.COIN));
    }

    private List<Resource> getProdResources(DevelopmentCard card) {
        List<Resource> resourceProduced = new ArrayList<>();
        for (Resource resource : card.getProduction().getProductionReturn().keySet()) {
            for (int i = 0; i < card.getProduction().getProductionReturn().get(resource); i++) {
                resourceProduced.add(resource);
            }
        }
        return resourceProduced;
    }


}
