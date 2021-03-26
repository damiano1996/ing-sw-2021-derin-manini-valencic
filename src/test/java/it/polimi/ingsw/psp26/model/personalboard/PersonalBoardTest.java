package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.exceptions.CanNotAddDevelopmentCardToSlotException;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.DepotOutOfBoundException;
import it.polimi.ingsw.psp26.exceptions.DevelopmentCardSlotOutOfBoundsException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PersonalBoardTest {

    DevelopmentCard developmentCard1;
    DevelopmentCard developmentCard2;
    DevelopmentCard developmentCard3;
    PersonalBoard personalBoard;
    List<List<DevelopmentCard>> cardSlots;
    private HashMap<Resource, Integer> cost;
    private Color color;
    private Level level;
    private HashMap<Resource, Integer> productionCost;
    private HashMap<Resource, Integer> productionReturn;
    private int victoryPoints;

    @Before
    public void setUp() {
        cost = new HashMap<>() {{
            put(Resource.SHIELD, 7);
        }};
        color = Color.GREEN;
        level = Level.FIRST;
        productionCost = new HashMap<>() {{
            put(Resource.SERVANT, 1);
        }};
        productionReturn = new HashMap<>() {{
            put(Resource.COIN, 1);
            put(Resource.FAITH_MARKERS, 3);
        }};
        victoryPoints = 11;

        developmentCard1 = new DevelopmentCard(cost, color, level, productionCost, productionReturn, victoryPoints);
        cost = new HashMap<>() {{
            put(Resource.COIN, 3);
        }};
        color = Color.GREEN;
        level = Level.SECOND;
        productionCost = new HashMap<>() {{
            put(Resource.SERVANT, 5);
        }};
        productionReturn = new HashMap<>() {{
            put(Resource.COIN, 1);
            put(Resource.FAITH_MARKERS, 1);
        }};
        victoryPoints = 1;
        developmentCard2 = new DevelopmentCard(cost, color, level, productionCost, productionReturn, victoryPoints);

        cost = new HashMap<>() {{
            put(Resource.SERVANT, 2);
        }};
        color = Color.GREEN;
        level = Level.THIRD;
        productionCost = new HashMap<>() {{
            put(Resource.SHIELD, 1);
        }};
        productionReturn = new HashMap<>() {{
            put(Resource.COIN, 5);
            put(Resource.FAITH_MARKERS, 3);
        }};
        victoryPoints = 13;
        developmentCard3 = new DevelopmentCard(cost, color, level, productionCost, productionReturn, victoryPoints);

        cardSlots = new ArrayList<>();
        for (int i = 0; i < 3; i++) cardSlots.add(new ArrayList<>());
        cardSlots.get(0).add(developmentCard1);
        cardSlots.get(0).add(developmentCard2);
        cardSlots.get(2).add(developmentCard3);

    }

    @Test
    public void testGetFaithTrack() {
        personalBoard = new PersonalBoard();
        FaithTrack faithTrack = new FaithTrack();
        assertTrue(faithTrack.equals(personalBoard.getFaithTrack()));
    }

    @Test
    public void testGetDevelopmentCardsSlots() throws CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException {
        personalBoard = new PersonalBoard();
        personalBoard.addDevelopmentCard(0, developmentCard1);
        personalBoard.addDevelopmentCard(0, developmentCard2);
        personalBoard.addDevelopmentCard(2, developmentCard3);

        for (int i = 0; i < cardSlots.size() - 1; i++) {
            for (int j = 0; j < cardSlots.get(i).size() - 1; i++) {
                assertTrue(cardSlots.get(i).get(j).equals(personalBoard.getDevelopmentCardsSlots().get(i).get(j)));
            }
        }
    }

    @Test(expected = CanNotAddDevelopmentCardToSlotException.class)
    public void testGetDevelopmentCardsSlots_CanNotAddDevelopmentCardToSlotException() throws CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException {
        personalBoard = new PersonalBoard();
        personalBoard.addDevelopmentCard(0, developmentCard3);
        personalBoard.addDevelopmentCard(0, developmentCard1);
        personalBoard.addDevelopmentCard(2, developmentCard2);

        for (int i = 0; i < cardSlots.size() - 1; i++) {
            for (int j = 0; j < cardSlots.get(i).size() - 1; i++) {
                assertTrue(cardSlots.get(i).get(j).equals(personalBoard.getDevelopmentCardsSlots().get(i).get(j)));
            }
        }
    }

    @Test
    public void testGetDevelopmentCardsSlot() throws CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException {
        int indexSlot = 0;
        personalBoard = new PersonalBoard();
        personalBoard.addDevelopmentCard(0, developmentCard1);
        personalBoard.addDevelopmentCard(0, developmentCard2);
        personalBoard.addDevelopmentCard(2, developmentCard3);

        for (int i = 0; i < cardSlots.get(indexSlot).size() - 1; i++) {
            assertTrue(cardSlots.get(indexSlot).get(i).equals(personalBoard.getDevelopmentCardsSlot(indexSlot).get(i)));
        }
    }

    @Test(expected = DevelopmentCardSlotOutOfBoundsException.class)
    public void testGetDevelopmentCardsSlot_DevelopmentCardSlotOutOfBoundsException() throws CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException {
        int indexSlot = 3;
        personalBoard = new PersonalBoard();
        personalBoard.addDevelopmentCard(0, developmentCard1);
        personalBoard.addDevelopmentCard(0, developmentCard2);
        personalBoard.addDevelopmentCard(2, developmentCard3);

        cardSlots.add(new ArrayList<>());
        cardSlots.get(3).add(developmentCard3);

        for (int i = 0; i <= cardSlots.get(indexSlot).size() - 1; i++) {
            assertTrue(cardSlots.get(indexSlot).get(i).equals(personalBoard.getDevelopmentCardsSlot(indexSlot).get(i)));
        }
    }

    @Test
    public void testGetVisibleDevelopmentCards() throws CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException {
        personalBoard = new PersonalBoard();
        personalBoard.addDevelopmentCard(0, developmentCard1);
        personalBoard.addDevelopmentCard(0, developmentCard2);
        personalBoard.addDevelopmentCard(2, developmentCard3);

        List<DevelopmentCard> developmentCards = new ArrayList<>();
        developmentCards.add(developmentCard2);
        developmentCards.add(developmentCard3);

        for (int i = 0; i < developmentCards.size(); i++) {
            assertTrue(developmentCards.get(i).equals(personalBoard.getVisibleDevelopmentCards().get(i)));
        }
    }

    @Test
    public void testGetWarehouseDepots() throws CanNotAddResourceToDepotException {
        PersonalBoard personalBoard = new PersonalBoard();
        personalBoard.getWarehouseDepots().get(0).addResource(Resource.STONE);
        personalBoard.getWarehouseDepots().get(1).addResource(Resource.COIN);
        personalBoard.getWarehouseDepots().get(1).addResource(Resource.COIN);
        personalBoard.getWarehouseDepots().get(2).addResource(Resource.SHIELD);
        personalBoard.getWarehouseDepots().get(2).addResource(Resource.SHIELD);
        personalBoard.getWarehouseDepots().get(2).addResource(Resource.SHIELD);

        List<Depot> depots = new ArrayList<>();
        depots.add(new Depot(1));
        depots.add(new Depot(2));
        depots.add(new Depot(3));
        depots.get(0).addResource(Resource.STONE);
        depots.get(1).addResource(Resource.COIN);
        depots.get(1).addResource(Resource.COIN);
        depots.get(2).addResource(Resource.SHIELD);
        depots.get(2).addResource(Resource.SHIELD);
        depots.get(2).addResource(Resource.SHIELD);

        for (int i = 0; i < depots.size(); i++) {
            assertTrue(depots.get(i).equals(personalBoard.getWarehouseDepots().get(i)));
        }
    }

    @Test(expected = CanNotAddResourceToDepotException.class)
    public void testGetWarehouseDepots_CanNotAddResourceToDepotException_WrongResourceType() throws CanNotAddResourceToDepotException {
        PersonalBoard personalBoard = new PersonalBoard();
        personalBoard.getWarehouseDepots().get(0).addResource(Resource.STONE);
        personalBoard.getWarehouseDepots().get(1).addResource(Resource.COIN);
        personalBoard.getWarehouseDepots().get(1).addResource(Resource.SERVANT);

        List<Depot> depots = new ArrayList<>();
        depots.add(new Depot(1));
        depots.add(new Depot(2));
        depots.add(new Depot(3));
        depots.get(0).addResource(Resource.STONE);
        depots.get(1).addResource(Resource.COIN);
        depots.get(1).addResource(Resource.COIN);

        for (int i = 0; i < depots.size(); i++) {
            assertTrue(depots.get(i).equals(personalBoard.getWarehouseDepots().get(i)));
        }
    }

    @Test(expected = CanNotAddResourceToDepotException.class)
    public void testGetWarehouseDepots_CanNotAddResourceToDepotException_WrongMaxNumberOfResource() throws CanNotAddResourceToDepotException {
        PersonalBoard personalBoard = new PersonalBoard();
        personalBoard.getWarehouseDepots().get(0).addResource(Resource.STONE);
        personalBoard.getWarehouseDepots().get(1).addResource(Resource.COIN);
        personalBoard.getWarehouseDepots().get(1).addResource(Resource.COIN);
        personalBoard.getWarehouseDepots().get(0).addResource(Resource.STONE);

        List<Depot> depots = new ArrayList<>();
        depots.add(new Depot(1));
        depots.add(new Depot(2));
        depots.add(new Depot(3));
        depots.get(0).addResource(Resource.STONE);
        depots.get(1).addResource(Resource.COIN);
        depots.get(1).addResource(Resource.COIN);

        for (int i = 0; i < depots.size(); i++) {
            assertTrue(depots.get(i).equals(personalBoard.getWarehouseDepots().get(i)));
        }
    }

    @Test
    public void testGetWarehouseDepot() throws CanNotAddResourceToDepotException, DepotOutOfBoundException {
        PersonalBoard personalBoard = new PersonalBoard();
        personalBoard.getWarehouseDepots().get(0).addResource(Resource.STONE);
        personalBoard.getWarehouseDepots().get(1).addResource(Resource.COIN);
        personalBoard.getWarehouseDepots().get(1).addResource(Resource.COIN);

        Depot depot = new Depot(2);

        assertTrue(depot.equals(personalBoard.getWarehouseDepot(1)));
    }

    @Test(expected = DepotOutOfBoundException.class)
    public void testGetWarehouseDepot_DepotOutOfBoundException() throws CanNotAddResourceToDepotException, DepotOutOfBoundException {
        PersonalBoard personalBoard = new PersonalBoard();
        personalBoard.getWarehouseDepots().get(0).addResource(Resource.STONE);
        personalBoard.getWarehouseDepots().get(1).addResource(Resource.COIN);
        personalBoard.getWarehouseDepots().get(1).addResource(Resource.COIN);

        Depot depot = new Depot(2);

        assertTrue(depot.equals(personalBoard.getWarehouseDepot(5)));
    }

    @Test
    public void testGetStrongbox() {
        List<Resource> resources = new ArrayList<>();
        resources.add(Resource.COIN);
        resources.add(Resource.STONE);
        resources.add(Resource.SERVANT);
        resources.add(Resource.COIN);
        resources.add(Resource.SHIELD);

        personalBoard = new PersonalBoard();
        personalBoard.addResourceToStrongbox(resources);

        assertEquals(resources, personalBoard.getStrongbox());
    }

}