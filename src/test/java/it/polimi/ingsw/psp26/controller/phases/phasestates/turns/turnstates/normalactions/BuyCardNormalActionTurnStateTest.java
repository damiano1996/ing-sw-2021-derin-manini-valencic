package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.CanNotAddDevelopmentCardToSlotException;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToStrongboxException;
import it.polimi.ingsw.psp26.exceptions.DevelopmentCardSlotOutOfBoundsException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.network.server.memory.GameSaver;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static org.junit.Assert.assertEquals;

public class BuyCardNormalActionTurnStateTest {

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

        turn.changeState(new BuyCardNormalActionTurnState(turn));
    }

    @After
    public void tearDown() {
        GameSaver.getInstance().deleteDirectoryByMatchId(phase.getMatchController().getMatch().getId());
    }

    @Test
    public void testSendPlayBuyCardMessage() throws InvalidPayloadException {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_NORMAL_ACTION, BUY_CARD));
        assertEquals(MessageType.CHOICE_CARD_TO_BUY, mitm.getMessages().get(0).getMessageType());

    }

    @Test
    public void testSendCardToBuyPlay() throws CanNotAddResourceToStrongboxException, InvalidPayloadException {
        DevelopmentCard card = buyCardResourceSetter();

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARD_TO_BUY, card));
        assertEquals(MessageType.CHOICE_DEVELOPMENT_CARD_SLOT_POSITION, mitm.getMessages().get(0).getMessageType());
    }

    @Test
    public void testSendCardToBuyNotEnoughResourcesPlay() throws InvalidPayloadException {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARD_TO_BUY, turn.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(2, 2).getFirstCard()));
        assertEquals(MessageType.GENERAL_MESSAGE, mitm.getMessages().get(0).getMessageType());

    }

    @Test
    public void testSendCardToBuyAndPlacePlay() throws CanNotAddResourceToStrongboxException, InvalidPayloadException {
        DevelopmentCard card = buyCardResourceSetter();

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARD_TO_BUY, card));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_DEVELOPMENT_CARD_SLOT_POSITION, "Slot 2"));

        Assert.assertEquals(turn.getTurnPlayer().getPersonalBoard().getVisibleDevelopmentCards().get(0), card);
    }

    @Test(expected = CanNotAddDevelopmentCardToSlotException.class)
    public void testSendCardToBuyAndPlaceWrongPositionPlay() throws CanNotAddResourceToStrongboxException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, InvalidPayloadException {
        DevelopmentCard card = buyCardResourceSetter();

        // An Exception will be raised here
        turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(1, turn.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(1, 2).getFirstCard());
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARD_TO_BUY, card));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_DEVELOPMENT_CARD_SLOT_POSITION, 1));

        assertEquals(MessageType.CHOICE_DEVELOPMENT_CARD_SLOT_POSITION, mitm.getMessages().get(0).getMessageType());


    }

    @Test
    public void testSendCardToBuyWithLeadersActive() throws CanNotAddResourceToStrongboxException, InvalidPayloadException {
        DevelopmentCard card = buyCardResourceSetter();
        leaderCardSetter();

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_NORMAL_ACTION, BUY_CARD));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARD_TO_BUY, card));
        assertEquals(MessageType.CHOICE_DEVELOPMENT_CARD_SLOT_POSITION, mitm.getMessages().get(1).getMessageType());

    }

    @Test
    public void testSendPositionCardBoughtWithLeadersActive() throws CanNotAddResourceToStrongboxException, InvalidPayloadException {
        DevelopmentCard card = buyCardResourceSetter();
        leaderCardSetter();

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_NORMAL_ACTION, BUY_CARD));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARD_TO_BUY, card));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_DEVELOPMENT_CARD_SLOT_POSITION, "Slot 2"));
        
        Assert.assertEquals(turn.getTurnPlayer().getPersonalBoard().getVisibleDevelopmentCards().get(0), card);
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

    private void leaderCardSetter() {
        List<LeaderCard> leaderCardsAdded = new ArrayList<>();
        List<Resource> resourceToRemove = new ArrayList<>();
        int modified;
        List<LeaderCard> leaderCards = new ArrayList<>(phase.getMatchController().getMatch().drawLeaders(8));

        for (Resource resource : turn.getTurnPlayer().getPersonalBoard().getStrongbox()) {
            modified = leaderCardsAdded.size();
            leaderCardsAdded.addAll(leaderCards.stream().filter(x -> x.getAbilityResource().equals(resource)).filter(x -> x.getAbilityToString().contains("ResourceDiscountAbility")).collect(Collectors.toList()));
            if (leaderCardsAdded.size() != modified) {
                resourceToRemove.add(resource);
            }
        }
        if (leaderCardsAdded.size() > 0) {
            turn.getTurnPlayer().setLeaderCards(leaderCardsAdded);
            for (LeaderCard leaderCard : leaderCardsAdded) {
                turn.getTurnPlayer().getPersonalBoard().grabResourcesFromStrongbox(resourceToRemove.get(0), 1);
                resourceToRemove.remove(0);
                leaderCard.activate(turn.getTurnPlayer());
            }
        }
    }
    
}