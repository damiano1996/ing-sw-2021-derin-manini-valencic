package it.polimi.ingsw.psp26.view;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.ModelUpdateMessage;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.LeaderDepot;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.client.cache.CachedModel;
import it.polimi.ingsw.psp26.network.server.VirtualView;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static it.polimi.ingsw.psp26.view.ViewUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ViewUtilsTest {

    VirtualView virtualView;

    @Before
    public void setUp() {
        virtualView = new VirtualView();
    }

    @Test
    public void testBeautifyMessageType() {
        assertEquals("General Message", beautifyMessageType(MessageType.GENERAL_MESSAGE));
    }

    @Test
    public void testToTitleStyle() {
        assertEquals("This Is A Title", toTitleStyle("this IS a TITLE"));
    }

    @Test
    public void testCreateLeaderboardAndGetOrderedPlayersList() {
        Map<String, Integer> leaderboardOne = new HashMap<>();
        List<Player> players = new ArrayList<>();

        players.add(new Player(virtualView, "nick1", "session1"));
        players.add(new Player(virtualView, "nick2", "session2"));
        players.add(new Player(virtualView, "nick3", "session3"));

        players.get(0).addPoints(5);
        players.get(1).addPoints(15);
        players.get(2).addPoints(10);

        for (Player player : players) leaderboardOne.put(player.getNickname(), player.getPoints());

        Map<String, Integer> leaderboardTwo = createLeaderboard(players);

        for (String nickname : leaderboardOne.keySet()) {
            assertEquals(leaderboardOne.get(nickname), leaderboardTwo.get(nickname));
        }

        List<Player> orderedPlayersOne = new ArrayList<>();
        orderedPlayersOne.add(players.get(1));
        orderedPlayersOne.add(players.get(2));
        orderedPlayersOne.add(players.get(0));

        List<String> orderedPlayersTwo = getOrderedPlayersList(leaderboardTwo);

        for (int i = 0; i < orderedPlayersOne.size(); i++) {
            assertEquals(orderedPlayersOne.get(i).getNickname(), orderedPlayersTwo.get(i));
        }
    }

    @Test
    public void testCreatePlayersList() throws InvalidPayloadException, EmptyPayloadException {
        CachedModel cachedModel = new CachedModel("myNickname");
        List<Player> players = new ArrayList<>();

        Player player1 = new Player(virtualView, "myNickname", "session1");
        Player player2 = new Player(virtualView, "opponent1", "session2");
        Player player3 = new Player(virtualView, "opponent2", "session3");
        Player player4 = new Player(virtualView, "opponent3", "session4");

        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);

        cachedModel.updateCachedModel(new ModelUpdateMessage("", MessageType.PLAYER_MODEL, players.get(0)));
        cachedModel.updateCachedModel(new ModelUpdateMessage("", MessageType.PLAYER_MODEL, players.get(1)));
        cachedModel.updateCachedModel(new ModelUpdateMessage("", MessageType.PLAYER_MODEL, players.get(2)));
        cachedModel.updateCachedModel(new ModelUpdateMessage("", MessageType.PLAYER_MODEL, players.get(3)));

        List<Player> createdPlayers = createPlayersList(cachedModel);

        Set<String> playersNicknames = new HashSet<>();
        for (Player player : players) playersNicknames.add(player.getNickname());

        Set<String> createdPlayersNicknames = new HashSet<>();
        for (Player player : createdPlayers) createdPlayersNicknames.add(player.getNickname());

        assertTrue(playersNicknames.containsAll(createdPlayersNicknames));
    }

    @Test
    public void testCreateListToSend() throws CanNotAddResourceToDepotException {
        Warehouse warehouse = new Warehouse(virtualView, 3, "sessionToken");
        warehouse.addLeaderDepot(new LeaderDepot(virtualView, Resource.SERVANT));

        List<Resource> resourceTypes = new ArrayList<>();
        resourceTypes.add(Resource.STONE);
        resourceTypes.add(Resource.EMPTY);
        resourceTypes.add(Resource.COIN);
        resourceTypes.add(Resource.SERVANT);

        warehouse.addResourceToDepot(0, Resource.STONE);
        warehouse.addResourceToDepot(2, Resource.COIN);
        warehouse.addResourceToDepot(3, Resource.SERVANT);

        List<Resource> createdList = createListToSend(warehouse);

        for (int i = 0; i < resourceTypes.size(); i++) {
            assertEquals(resourceTypes.get(i), createdList.get(i));
        }
    }

    @Test
    public void testChangePosition() throws CanNotAddResourceToDepotException {
        Warehouse warehouse = new Warehouse(virtualView, 3, "sessionToken");
        warehouse.addResourceToDepot(0, Resource.STONE);
        warehouse.addResourceToDepot(1, Resource.COIN);
        warehouse.addResourceToDepot(2, Resource.SERVANT);

        changePosition(warehouse, 2, 1);

        assertEquals(warehouse.getAllDepots().get(1).getContainedResourceType(), Resource.SERVANT);
        assertEquals(warehouse.getAllDepots().get(2).getContainedResourceType(), Resource.COIN);
    }

    @Test
    public void testChangePosition_RestoreOriginalSituation() throws CanNotAddResourceToDepotException {
        Warehouse warehouse = new Warehouse(virtualView, 3, "sessionToken");
        warehouse.addResourceToDepot(0, Resource.STONE);
        warehouse.addResourceToDepot(1, Resource.COIN);
        warehouse.addResourceToDepot(2, Resource.SERVANT);
        warehouse.addResourceToDepot(2, Resource.SERVANT);
        warehouse.addResourceToDepot(2, Resource.SERVANT);

        changePosition(warehouse, 2, 1);

        assertEquals(warehouse.getAllDepots().get(1).getContainedResourceType(), Resource.COIN);
        assertEquals(warehouse.getAllDepots().get(2).getContainedResourceType(), Resource.SERVANT);
    }

}