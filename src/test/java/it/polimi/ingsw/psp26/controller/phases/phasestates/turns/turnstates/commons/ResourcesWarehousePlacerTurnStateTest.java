package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToWarehouse;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.SpecialDepotAbility;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import it.polimi.ingsw.psp26.testutils.MitmObserver;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.model.ResourceSupply.RESOURCES_SLOTS;
import static it.polimi.ingsw.psp26.model.enums.Resource.*;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;
import static org.junit.Assert.assertEquals;

public class ResourcesWarehousePlacerTurnStateTest {

    private MitmObserver mitm;
    private Phase phase;
    private Turn turn;
    private Warehouse warehouse;
    private VirtualView virtualView;

    private List<Resource> resourcesToAdd;

    @Before
    public void setUp() throws CanNotAddResourceToWarehouse {
        virtualView = new VirtualView();
        mitm = new MitmObserver();
        phase = new Phase(virtualView.getMatchController());
        phase.getMatchController().addObserver(mitm);
        Player player = new Player(virtualView, "nickname", "sessionToken");

        phase.getMatchController().getMatch().addPlayer(player);

        turn = new Turn(
                new PlayingPhaseState(phase),
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                0
        );

        resourcesToAdd = new ArrayList<>() {{
            add(Resource.COIN);
            add(Resource.SHIELD);
        }};

        turn.getTurnPlayer().getPersonalBoard().getWarehouse().addResource(SERVANT);
        turn.getTurnPlayer().getPersonalBoard().getWarehouse().addResource(Resource.SHIELD);
        turn.getTurnPlayer().getPersonalBoard().getWarehouse().addResource(Resource.COIN);

        turn.changeState(new ResourcesWarehousePlacerTurnState(turn, resourcesToAdd));
    }

    @Test
    public void testSendWarehouseMessage() throws EmptyPayloadException, InvalidPayloadException {
        turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.GENERAL_MESSAGE));
        assertEquals(PLACE_IN_WAREHOUSE, mitm.getMessages().get(0).getMessageType());
        assertEquals(PLACE_IN_WAREHOUSE, mitm.getMessages().get(1).getMessageType());

        Warehouse warehouseExpected = turn.getTurnPlayer().getPersonalBoard().getWarehouse();
        Warehouse warehouseActual = (Warehouse) mitm.getMessages().get(0).getPayload();
        System.out.println(warehouseExpected.getLeaderDepots());
        System.out.println(castElements(LeaderDepot.class, warehouseActual.getLeaderDepots()));

        assertEquals(warehouseExpected.getBaseDepots(), warehouseActual.getBaseDepots());
        assertEquals(castElements(LeaderDepot.class, warehouseExpected.getLeaderDepots()), castElements(LeaderDepot.class, warehouseActual.getLeaderDepots()));
        assertEquals(warehouseExpected, warehouseActual);
        assertEquals(resourcesToAdd, castElements(Resource.class, mitm.getMessages().get(1).getListPayloads()));
    }

    private void sendResources(Resource... resources) throws EmptyPayloadException, InvalidPayloadException {
        testSendWarehouseMessage();

        turn.play(
                new SessionMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        PLACE_IN_WAREHOUSE,
                        (Object[]) resources
                )
        );
    }

    @Test
    public void testAskMeToReplace() throws EmptyPayloadException, InvalidPayloadException {
        sendResources(COIN, SERVANT, COIN);
        assertEquals(ERROR_MESSAGE, mitm.getMessages().get(2).getMessageType());
    }


    private void assertMultiplicity(List<Resource> expectedFinalResources) {
        // for each resource we check if the multiplicity is correct
        for (Resource resource : RESOURCES_SLOTS) {
            System.out.println("Looking for resource: " + resource);
            assertEquals(
                    Collections.frequency(expectedFinalResources, resource),
                    Collections.frequency(turn.getTurnPlayer().getPersonalBoard().getWarehouse().getResources(), resource)
            );
        }
    }

    @Test
    public void testCorrectlyAllocatedEmptyCase() throws EmptyPayloadException, InvalidPayloadException {
        turn.getTurnPlayer().getPersonalBoard().grabAllAvailableResources();
        assertEquals(0, turn.getTurnPlayer().getPersonalBoard().getWarehouse().getBaseDepots().get(0).getResources().size());
        assertEquals(0, turn.getTurnPlayer().getPersonalBoard().getWarehouse().getBaseDepots().get(1).getResources().size());
        assertEquals(0, turn.getTurnPlayer().getPersonalBoard().getWarehouse().getBaseDepots().get(2).getResources().size());

        sendResources(EMPTY, EMPTY, COIN);

        assertEquals(0, turn.getTurnPlayer().getPersonalBoard().getWarehouse().getBaseDepots().get(0).getResources().size());
        assertEquals(0, turn.getTurnPlayer().getPersonalBoard().getWarehouse().getBaseDepots().get(1).getResources().size());
        // two since one COIN is already in the depot
        assertEquals(1, turn.getTurnPlayer().getPersonalBoard().getWarehouse().getBaseDepots().get(2).getResources().size());
    }


    @Test
    public void testCorrectlyAllocated() throws EmptyPayloadException, InvalidPayloadException {
        List<Resource> initialResources = turn.getTurnPlayer().getPersonalBoard().getWarehouse().getResources();
        List<Resource> expectedFinalResources = new ArrayList<>();
        expectedFinalResources.addAll(initialResources);
        expectedFinalResources.addAll(resourcesToAdd);

        sendResources(SERVANT, SHIELD, COIN);

        assertMultiplicity(expectedFinalResources);
    }

    @Test
    public void testDiscardResource() throws EmptyPayloadException, InvalidPayloadException {
        turn.getMatchController().getMatch().addPlayer(new Player(virtualView, "nickname2", "sessionToken2"));

        resourcesToAdd.add(COIN);
        resourcesToAdd.add(COIN);

        sendResources(SERVANT, SHIELD, COIN);

        assertEquals(1, turn.getMatchController().getMatch().getPlayers().get(1).getPersonalBoard().getFaithTrack().getFaithPoints());
    }

    @Test
    public void testDiscardAvoidedByLeader() throws EmptyPayloadException, InvalidPayloadException {
        turn.getMatchController().getMatch().addPlayer(new Player(virtualView, "nickname2", "sessionToken2"));

        // Creating a new leader and activate it with coin depot as special ability.
        // In this way we can check that the remaining resource will be placed in the leader depot.
        LeaderCard leaderCard = new LeaderCard(new HashMap<>(), new HashMap<>(), 0, new SpecialDepotAbility(COIN));
        leaderCard.activate(turn.getTurnPlayer());
        turn.getTurnPlayer().setLeaderCards(new ArrayList<>() {{
            add(leaderCard);
        }});

        // two COIN resources will exceed the capacity of the base depots
        resourcesToAdd.add(COIN);
        resourcesToAdd.add(COIN);

        List<Resource> initialResources = turn.getTurnPlayer().getPersonalBoard().getWarehouse().getResources();
        List<Resource> expectedFinalResources = new ArrayList<>();
        expectedFinalResources.addAll(initialResources);
        expectedFinalResources.addAll(resourcesToAdd);

        sendResources(SERVANT, SHIELD, COIN);

        assertMultiplicity(expectedFinalResources);
    }

    @Test
    public void testBadSwitch() throws CanNotAddResourceToDepotException, EmptyPayloadException, InvalidPayloadException {
        // adding one SHIELD to cause the error
        turn.getTurnPlayer().getPersonalBoard().getWarehouse().addResourceToDepot(1, SHIELD);

        sendResources(SHIELD, SERVANT, COIN);

        System.out.println(mitm.getMessages().get(2));
        assertEquals(ERROR_MESSAGE, mitm.getMessages().get(2).getMessageType());
    }

    @Test
    public void testGoodSwitch() throws EmptyPayloadException, InvalidPayloadException {
        sendResources(SHIELD, SERVANT, COIN);
        assertEquals(CHOICE_NORMAL_ACTION, mitm.getMessages().get(2).getMessageType());
    }

    @Test
    public void testGoodSwitch_CaseEmpty() throws EmptyPayloadException, InvalidPayloadException, CanNotAddResourceToDepotException {
        turn.getTurnPlayer().getPersonalBoard().grabAllAvailableResources();
        turn.getTurnPlayer().getPersonalBoard().getWarehouse().addResourceToDepot(0, SHIELD);
        turn.getTurnPlayer().getPersonalBoard().getWarehouse().addResourceToDepot(1, STONE);
        turn.getTurnPlayer().getPersonalBoard().getWarehouse().addResourceToDepot(1, STONE);

        resourcesToAdd = new ArrayList<>() {{
            add(STONE);
            add(SHIELD);
        }};
        turn.changeState(new ResourcesWarehousePlacerTurnState(turn, resourcesToAdd));

        sendResources(EMPTY, SHIELD, STONE);
        assertEquals(CHOICE_NORMAL_ACTION, mitm.getMessages().get(2).getMessageType());
    }

}