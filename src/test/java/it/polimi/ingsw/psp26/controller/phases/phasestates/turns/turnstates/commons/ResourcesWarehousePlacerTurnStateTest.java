package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToWarehouse;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
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

import static it.polimi.ingsw.psp26.application.messages.MessageType.ERROR_MESSAGE;
import static it.polimi.ingsw.psp26.application.messages.MessageType.PLACE_IN_WAREHOUSE;
import static it.polimi.ingsw.psp26.model.ResourceSupply.RESOURCES_SLOTS;
import static it.polimi.ingsw.psp26.model.enums.Resource.*;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;
import static org.junit.Assert.assertEquals;

public class ResourcesWarehousePlacerTurnStateTest {

    private MitmObserver mitm;
    private Phase phase;
    private Turn turn;
    private Warehouse warehouse;

    private List<Resource> resourcesToAdd;

    @Before
    public void setUp() throws CanNotAddResourceToWarehouse {
        mitm = new MitmObserver();
        phase = new Phase(new MatchController(new VirtualView(), 0));
        phase.getMatchController().addObserver(mitm);

        phase.getMatchController().getMatch().addPlayer(
                new Player(new VirtualView(), "nickname", "sessionToken"));

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
    public void testSendWarehouseMessage() throws EmptyPayloadException {
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

    @Test
    public void testAskMeToReplace() throws EmptyPayloadException {
        testSendWarehouseMessage();

        turn.play(
                new SessionMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        PLACE_IN_WAREHOUSE,
                        Resource.COIN, SERVANT, Resource.COIN
                )
        );

        assertEquals(ERROR_MESSAGE, mitm.getMessages().get(2).getMessageType());
    }

    private void sendOrderTypeOne() throws EmptyPayloadException {
        testSendWarehouseMessage();

        turn.play(
                new SessionMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        PLACE_IN_WAREHOUSE,
                        SERVANT, SHIELD, COIN
                )
        );
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
    public void testCorrectlyAllocated() throws EmptyPayloadException {
        List<Resource> initialResources = turn.getTurnPlayer().getPersonalBoard().getWarehouse().getResources();
        List<Resource> expectedFinalResources = new ArrayList<>();
        expectedFinalResources.addAll(initialResources);
        expectedFinalResources.addAll(resourcesToAdd);

        sendOrderTypeOne();

        assertMultiplicity(expectedFinalResources);
    }

    @Test
    public void testDiscardResource() throws EmptyPayloadException {
        turn.getMatchController().getMatch().addPlayer(new Player(new VirtualView(), "nickname2", "sessionToken2"));

        resourcesToAdd.add(COIN);
        resourcesToAdd.add(COIN);

        sendOrderTypeOne();

        assertEquals(1, turn.getMatchController().getMatch().getPlayers().get(1).getPersonalBoard().getFaithTrack().getFaithPoints());
    }

    @Test
    public void testDiscardAvoidedByLeader() throws EmptyPayloadException {
        turn.getMatchController().getMatch().addPlayer(new Player(new VirtualView(), "nickname2", "sessionToken2"));

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

        sendOrderTypeOne();

        assertMultiplicity(expectedFinalResources);
    }

}