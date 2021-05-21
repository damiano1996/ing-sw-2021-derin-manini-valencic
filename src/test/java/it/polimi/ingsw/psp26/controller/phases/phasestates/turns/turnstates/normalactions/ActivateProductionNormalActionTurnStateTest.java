package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY;
import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_RESOURCE_FROM_WAREHOUSE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ActivateProductionNormalActionTurnStateTest {

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

        turn.changeState(new ActivateProductionNormalActionTurnState(turn));

    }

    @Test
    public void testSendActivateProductionMessage() throws EmptyPayloadException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, InvalidPayloadException {

        turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(1, turn.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(2, 2).getFirstCard());
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_NORMAL_ACTION, MessageType.ACTIVATE_PRODUCTION));

        assertEquals(MessageType.CHOICE_PRODUCTIONS_TO_ACTIVATE, mitm.getMessages().get(0).getMessageType());
        assertEquals(2, mitm.getMessages().get(0).getListPayloads().size());
    }

    @Test
    public void testSendChoiceResourceToActivateNormalProduction() throws EmptyPayloadException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, InvalidPayloadException, CanNotAddResourceToDepotException, CanNotAddResourceToWarehouse {

        DevelopmentCard chosenCard = turn.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(2, 2).getFirstCard();

        turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(1, chosenCard);
        List<Resource> resourcesProduced = new ArrayList<>();
        List<Resource> warehouseExtraResources = new ArrayList<>();
        int depotIndex = 2;

        for (Resource resource : chosenCard.getProduction().getProductionCost().keySet()) {
            System.out.println(resource);
            System.out.println(chosenCard.getProduction().getProductionCost().get(resource));
            if (chosenCard.getProduction().getProductionCost().get(resource) == 1) {
                turn.getTurnPlayer().getPersonalBoard().getWarehouse().addResource(resource);

            } else {
                for (int j = 0; j < turn.getTurnPlayer().getPersonalBoard().getWarehouse().getAllDepots().get(depotIndex).getMaxNumberOfResources(); j++) {
                    turn.getTurnPlayer().getPersonalBoard().getWarehouse().addResourceToDepot(depotIndex, resource);
                    if (j >= chosenCard.getProduction().getProductionCost().get(resource))
                        warehouseExtraResources.add(resource);
                }

                depotIndex--;

            }
        }
        for (Depot depot : turn.getTurnPlayer().getPersonalBoard().getWarehouse().getAllDepots()) {
            System.out.println(depot.getResources());
        }

        for (Resource resource : chosenCard.getProduction().getProductionReturn().keySet()) {
            for (int j = 0; j < chosenCard.getProduction().getProductionReturn().get(resource); j++) {
                System.out.println(resource);
                if (resource != Resource.FAITH_MARKER)
                    resourcesProduced.add(resource);
            }

        }

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_NORMAL_ACTION, MessageType.ACTIVATE_PRODUCTION));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_PRODUCTIONS_TO_ACTIVATE, chosenCard.getProduction()));

        assertEquals(MessageType.CHOICE_PRODUCTIONS_TO_ACTIVATE, mitm.getMessages().get(0).getMessageType());
        assertEquals(resourcesProduced, turn.getTurnPlayer().getPersonalBoard().getStrongbox());
        assertEquals(warehouseExtraResources, turn.getTurnPlayer().getPersonalBoard().getWarehouse().getResources());

    }

    @Test
    public void testSendChoiceCardsToActivateNoUnknownResource() throws CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, EmptyPayloadException, CanNotAddResourceToStrongboxException, InvalidPayloadException {

        DevelopmentCard card = turn.getMatchController().getMatch().getDevelopmentGrid().getDevelopmentGridCell(2, 2).getFirstCard();

        for (Resource resource : card.getProduction().getProductionCost().keySet()) {
            for (int i = 0; i < card.getProduction().getProductionCost().get(resource); i++) {
                turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(resource);
            }
        }

        turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(1, card);

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_PRODUCTIONS_TO_ACTIVATE, card.getProduction()));

        List<Resource> resourceProduced = getProdResources(card);

        resourceProduced = resourceProduced.stream().filter(x -> !x.equals(Resource.EMPTY)).filter(x -> !x.equals(Resource.FAITH_MARKER)).collect(Collectors.toList());

        assertArrayEquals(resourceProduced.toArray(), turn.getTurnPlayer().getPersonalBoard().getStrongbox().toArray());
    }

    @Test
    public void testSendChoiceCardsToActivateCostUnknown() throws InvalidPayloadException, CanNotAddResourceToStrongboxException {

        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.STONE);
        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.STONE);

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_PRODUCTIONS_TO_ACTIVATE, turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().get(0)));

        assertEquals(MessageType.GENERAL_MESSAGE, mitm.getMessages().get(0).getMessageType());
        assertEquals(MessageType.CHOICE_RESOURCE_FROM_WAREHOUSE, mitm.getMessages().get(1).getMessageType());

    }

    @Test
    public void testSendChoiceResourceToActivateCostUnknown() throws CanNotAddResourceToStrongboxException, InvalidPayloadException {
        List<Resource> resource2 = new ArrayList<>();

        resource2.add(Resource.COIN);

        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.STONE);
        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.STONE);

        playCollection();

        assertArrayEquals(resource2.toArray(), turn.getTurnPlayer().getPersonalBoard().getStrongbox().toArray());
    }

    @Test
    public void testSendChoiceResourceToActivateCostUnknownLeaderProduction() throws CanNotAddResourceToStrongboxException, InvalidPayloadException {

        leaderCardSetter();
        List<Resource> resource2 = new ArrayList<>();
        if (turn.getTurnPlayer().getLeaderCards().size() > 0) {

            Resource leaderResource = turn.getTurnPlayer().getLeaderCards().get(0).getAbilityResource();
            resource2.add(leaderResource);
            turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(leaderResource);

            turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_PRODUCTIONS_TO_ACTIVATE, turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().get(1)));
            turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE_FROM_WAREHOUSE, Resource.STONE));

            assertEquals(MessageType.GENERAL_MESSAGE, mitm.getMessages().get(0).getMessageType());
            assertEquals(MessageType.CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY, mitm.getMessages().get(1).getMessageType());
            assertArrayEquals(resource2.toArray(), turn.getTurnPlayer().getPersonalBoard().getStrongbox().toArray());
        }
    }

    @Test
    public void testSendChoiceResourceToActivateNotEnoughResources() throws CanNotAddResourceToStrongboxException, InvalidPayloadException {

        turn.getTurnPlayer().getPersonalBoard().addResourceToStrongbox(Resource.STONE);

        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_PRODUCTIONS_TO_ACTIVATE, turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().get(0)));

        assertEquals(MessageType.GENERAL_MESSAGE, mitm.getMessages().get(0).getMessageType());
    }

    private void playCollection() throws InvalidPayloadException {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_PRODUCTIONS_TO_ACTIVATE, turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().get(0)));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE_FROM_WAREHOUSE, Resource.STONE));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE_FROM_WAREHOUSE, Resource.STONE));
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY, Resource.COIN));
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

    private void leaderCardSetter() {

        List<LeaderCard> leaderCards = new ArrayList<>(phase.getMatchController().getMatch().drawLeaders(8));
        List<LeaderCard> leaderCardsAdded = leaderCards.stream().filter(x -> x.getAbilityToString().contains("ProductionAbility")).collect(Collectors.toList());
        if (leaderCardsAdded.size() > 0) {
            turn.getTurnPlayer().setLeaderCards(leaderCardsAdded.subList(0, 1));
            turn.getTurnPlayer().getLeaderCards().get(0).activate(turn.getTurnPlayer());
        }

    }


}
