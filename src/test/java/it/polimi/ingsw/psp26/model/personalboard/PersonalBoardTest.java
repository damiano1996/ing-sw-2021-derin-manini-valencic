package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PersonalBoardTest {

    DevelopmentCard developmentCard1;
    DevelopmentCard developmentCard2;
    DevelopmentCard developmentCard3;

    DevelopmentGrid developmentGrid;

    PersonalBoard personalBoard;

    List<List<DevelopmentCard>> cardSlots;

    private VirtualView virtualView;

    @Before
    public void setUp() throws NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException, CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException {
        virtualView = new VirtualView();

        developmentGrid = new DevelopmentGrid(virtualView);
        personalBoard = new PersonalBoard(virtualView, new Player(virtualView, "nickname", "sessionToken"));

        developmentCard1 = developmentGrid.drawCard(Color.GREEN, Level.FIRST);
        developmentCard2 = developmentGrid.drawCard(Color.GREEN, Level.SECOND);
        developmentCard3 = developmentGrid.drawCard(Color.GREEN, Level.THIRD);

        cardSlots = new ArrayList<>();
        for (int i = 0; i < 3; i++) cardSlots.add(new ArrayList<>());

        cardSlots.get(0).add(developmentCard1);
        cardSlots.get(0).add(developmentCard2);
        cardSlots.get(2).add(developmentCard3);

        personalBoard.addDevelopmentCard(0, developmentCard1);
        personalBoard.addDevelopmentCard(0, developmentCard2);
        personalBoard.addDevelopmentCard(2, developmentCard3);

    }

    @Test
    public void testGetFaithTrack() {
        FaithTrack faithTrack = new FaithTrack(virtualView);
        assertEquals(faithTrack, personalBoard.getFaithTrack());
    }

    @Test
    public void testGetDevelopmentCardsSlots() {
        for (int i = 0; i < cardSlots.size() - 1; i++) {
            for (int j = 0; j < cardSlots.get(i).size() - 1; j++) {
                assertEquals(cardSlots.get(i).get(j), personalBoard.getDevelopmentCardsSlots().get(i).get(j));
            }
        }
    }

    @Test
    public void testGetDevelopmentCardsSlot() throws DevelopmentCardSlotOutOfBoundsException {
        int indexSlot = 0;

        for (int i = 0; i < cardSlots.get(indexSlot).size() - 1; i++) {
            assertEquals(cardSlots.get(indexSlot).get(i), personalBoard.getDevelopmentCardsSlot(indexSlot).get(i));
        }
    }

    @Test(expected = DevelopmentCardSlotOutOfBoundsException.class)
    public void testGetDevelopmentCardsSlot_DevelopmentCardSlotOutOfBoundsException() throws DevelopmentCardSlotOutOfBoundsException {
        int indexSlot = 4;
        personalBoard.getDevelopmentCardsSlot(indexSlot);
    }

    @Test(expected = CanNotAddDevelopmentCardToSlotException.class)
    public void testAddDevelopmentCard_CanNotAddDevelopmentCardToSlotException() throws CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException {
        DevelopmentCard developmentCard = developmentGrid.drawCard(Color.YELLOW, Level.SECOND);
        personalBoard.addDevelopmentCard(0, developmentCard);
    }

    @Test(expected = DevelopmentCardSlotOutOfBoundsException.class)
    public void testAddDevelopmentCard_DevelopmentCardSlotOutOfBoundsException() throws CanNotAddDevelopmentCardToSlotException, DevelopmentCardSlotOutOfBoundsException, NoMoreDevelopmentCardsException, LevelDoesNotExistException, ColorDoesNotExistException {
        DevelopmentCard developmentCard = developmentGrid.drawCard(Color.YELLOW, Level.SECOND);
        personalBoard.addDevelopmentCard(4, developmentCard);
    }

    @Test
    public void testGetVisibleDevelopmentCards() {
        List<DevelopmentCard> visibleDevelopmentCards = new ArrayList<>();
        visibleDevelopmentCards.add(developmentCard2);
        visibleDevelopmentCards.add(developmentCard3);

        for (int i = 0; i < visibleDevelopmentCards.size(); i++) {
            assertEquals(visibleDevelopmentCards.get(i), personalBoard.getVisibleDevelopmentCards().get(i));
        }
    }

    @Test
    public void testGetWarehouseDepots() throws CanNotAddResourceToDepotException {
        personalBoard.getWarehouseDepots().get(0).addResource(Resource.STONE);
        for (int i = 0; i < personalBoard.getWarehouseDepots().get(1).getMaxNumberOfResources(); i++)
            personalBoard.getWarehouseDepots().get(1).addResource(Resource.COIN);
        for (int i = 0; i < personalBoard.getWarehouseDepots().get(2).getMaxNumberOfResources(); i++)
            personalBoard.getWarehouseDepots().get(2).addResource(Resource.SHIELD);

        List<Depot> depots = new ArrayList<>();
        for (int i = 1; i <= 3; i++) depots.add(new Depot(virtualView, i));

        depots.get(0).addResource(Resource.STONE);
        for (int i = 0; i < depots.get(1).getMaxNumberOfResources(); i++) depots.get(1).addResource(Resource.COIN);
        for (int i = 0; i < depots.get(2).getMaxNumberOfResources(); i++) depots.get(2).addResource(Resource.SHIELD);

        for (int i = 0; i < depots.size(); i++) {
            assertEquals(depots.get(i), personalBoard.getWarehouseDepots().get(i));
        }
    }

    @Test(expected = CanNotAddResourceToDepotException.class)
    public void testGetWarehouseDepots_CanNotAddResourceToDepotException_WrongResourceType() throws CanNotAddResourceToDepotException {
        personalBoard.getWarehouseDepots().get(0).addResource(Resource.STONE);
        personalBoard.getWarehouseDepots().get(1).addResource(Resource.COIN);
        personalBoard.getWarehouseDepots().get(1).addResource(Resource.SERVANT);
    }

    @Test(expected = CanNotAddResourceToDepotException.class)
    public void testGetWarehouseDepots_CanNotAddResourceToDepotException_WrongMaxNumberOfResource() throws CanNotAddResourceToDepotException {
        personalBoard.getWarehouseDepots().get(0).addResource(Resource.STONE);
        personalBoard.getWarehouseDepots().get(0).addResource(Resource.STONE);
    }

    @Test
    public void testGetWarehouseDepot() throws CanNotAddResourceToDepotException, DepotOutOfBoundException {
        personalBoard.getWarehouseDepots().get(1).addResource(Resource.COIN);
        personalBoard.getWarehouseDepots().get(1).addResource(Resource.COIN);

        Depot depot = new Depot(virtualView, 2);
        depot.addResource(Resource.COIN);
        depot.addResource(Resource.COIN);

        assertEquals(depot, personalBoard.getWarehouseDepot(1));
    }

    @Test(expected = DepotOutOfBoundException.class)
    public void testGetWarehouseDepot_DepotOutOfBoundException() throws DepotOutOfBoundException {
        personalBoard.getWarehouseDepot(5);
    }

    @Test
    public void testGetStrongbox() throws CanNotAddResourceToStrongboxException {
        List<Resource> resources = new ArrayList<>();
        resources.add(Resource.COIN);
        resources.add(Resource.STONE);
        resources.add(Resource.SERVANT);
        resources.add(Resource.COIN);
        resources.add(Resource.SHIELD);

        personalBoard.addResourcesToStrongbox(resources);

        assertEquals(resources, personalBoard.getStrongbox());
    }


    @Test(expected = CanNotAddResourceToStrongboxException.class)
    public void testGetStrongbox_CanNotAddResourceToStrongboxException() throws CanNotAddResourceToStrongboxException {
        List<Resource> resources = new ArrayList<>();
        resources.add(Resource.COIN);
        resources.add(Resource.STONE);
        resources.add(Resource.EMPTY);
        resources.add(Resource.COIN);
        resources.add(Resource.SHIELD);

        personalBoard.addResourcesToStrongbox(resources);

        assertEquals(resources, personalBoard.getStrongbox());
    }

    @Test
    public void testGrabFromStrongbox() throws CanNotAddResourceToStrongboxException {
        List<Resource> resources = new ArrayList<>();
        resources.add(Resource.COIN);
        resources.add(Resource.STONE);
        resources.add(Resource.SERVANT);
        resources.add(Resource.COIN);
        resources.add(Resource.SHIELD);

        personalBoard.addResourcesToStrongbox(resources);
        resources.remove(Resource.COIN);
        resources.remove(Resource.COIN);
        personalBoard.grabResourcesFromStrongbox(Resource.COIN, 2);
        assertEquals(resources, personalBoard.getStrongbox());

    }

    @Test
    public void testBaseProductionPower() throws WrongBasicPowerResourceRequirementException {
        List<Resource> resources = new ArrayList<>();
        resources.add(Resource.COIN);
        resources.add(Resource.STONE);
        Resource resource = personalBoard.baseProductionPower(resources, Resource.SHIELD);
        assertEquals(resource, Resource.SHIELD);
    }

    @Test(expected = WrongBasicPowerResourceRequirementException.class)
    public void testBaseProductionPower_WrongBasicPowerResourceRequirementException() throws WrongBasicPowerResourceRequirementException {
        List<Resource> resources = new ArrayList<>();
        resources.add(Resource.COIN);
        resources.add(Resource.STONE);
        resources.add(Resource.STONE);
        Resource resource = personalBoard.baseProductionPower(resources, Resource.SHIELD);
    }

    @Test
    public void testGrabAllResourcesFromDepot() throws DepotOutOfBoundException, CanNotAddResourceToDepotException, NegativeNumberOfElementsToGrabException {
        personalBoard.getWarehouseDepot(0).addResource(Resource.COIN);
        assertEquals(new ArrayList<>() {{
            add(Resource.COIN);
        }}, personalBoard.getWarehouseDepot(0).grabAllResources());
    }

    @Test
    public void TestGrabResourcesFromWarehouse() throws DepotOutOfBoundException, CanNotAddResourceToDepotException, NegativeNumberOfElementsToGrabException {

        Depot depot = personalBoard.getWarehouseDepot(2);
        depot.addResource(Resource.COIN);
        depot.addResource(Resource.COIN);

        assertEquals(new ArrayList<>() {{
            add(Resource.COIN);
            add(Resource.COIN);
        }}, personalBoard.grabResourcesFromWarehouse(Resource.COIN, 2));
    }

    @Test
    public void testGrabResourcesFromWarehouseAndStrongbox() throws DepotOutOfBoundException, CanNotAddResourceToDepotException, NegativeNumberOfElementsToGrabException, CanNotAddResourceToStrongboxException {
        personalBoard.getWarehouseDepot(0).addResource(Resource.COIN);
        personalBoard.addResourceToStrongbox(Resource.COIN);
        assertEquals(new ArrayList<>() {{
            add(Resource.COIN);
            add(Resource.COIN);
        }}, personalBoard.grabResourcesFromWarehouseAndStrongbox(Resource.COIN, 2));
    }

}