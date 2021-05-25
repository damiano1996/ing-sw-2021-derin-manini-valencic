package it.polimi.ingsw.psp26.view;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.client.cache.CachedModel;

import java.util.*;
import java.util.stream.Collectors;

public class ViewUtils {

    /**
     * Used to change the appearance of the given MessageType replacing the underscore characters
     *
     * @param messageType The MessageType to beautify
     * @return The beautified MessageType
     */
    public static String beautifyMessageType(MessageType messageType) {
        String message = messageType.name();
        message = message.replace('_', ' ');
        return toTitleStyle(message);
    }


    /**
     * Used in beautifyMessageType() to manipulate the MessageType
     *
     * @param string The MessageType name
     * @return The beautified MessageType
     */
    public static String toTitleStyle(String string) {
        StringBuilder newString = new StringBuilder();

        boolean space = true;
        for (Character character : string.toLowerCase(Locale.ROOT).toCharArray()) {
            newString.append((space) ? Character.toUpperCase(character) : character);
            space = character == ' ';
        }
        return newString.toString();
    }


    //-----------------------------------------//
    //          ENDING SCREEN METHODS          //
    //-----------------------------------------//    

    /**
     * Static method used to create the final leaderboard of Players-Points
     *
     * @param players The Players of the ended Match
     * @return The leaderboard to display as a Map
     */
    public static Map<String, Integer> createLeaderboard(List<Player> players) {
        Map<String, Integer> leaderboard = new HashMap<>();
        for (Player player : players) leaderboard.put(player.getNickname(), player.getPoints());
        return leaderboard;
    }


    /**
     * Static method used to create the ended Match Players List
     *
     * @param cachedModel The CachedModel from which get the Players information
     * @return The List of the ended Match Players
     */
    public static List<Player> createPlayersList(CachedModel cachedModel) {
        List<Player> players = new ArrayList<>();
        try {
            for (int i = 0; i < cachedModel.getNumberOfOpponents(); i++) {
                players.add(cachedModel.getOpponentCached(i).getObject());
            }
            players.add(cachedModel.getMyPlayerCached().getObject());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return players;
    }


    /**
     * Orders the Map given in a decreasing order considering the Integers
     *
     * @param leaderboard The Map containing the Players nicknames and their points
     * @return A List containing the Players nicknames in the order they have to be printed
     */
    public static List<String> getOrderedPlayersList(Map<String, Integer> leaderboard) {
        Set<Map.Entry<String, Integer>> entries = leaderboard.entrySet();
        return entries.stream().sorted((o1, o2) -> {
            if (o1.getValue() < o2.getValue()) return 1;
            if (o1.getValue().equals(o2.getValue())) return 0;
            return -1;
        }).map(Map.Entry::getKey).collect(Collectors.toList());
    }


    //--------------------------------------------//
    //          WAREHOUSE PLACER METHODS          //
    //--------------------------------------------//

    /**
     * If it's not possible to change Resources between two Depots, restores the original situation
     *
     * @param warehouse                The Warehouse that contains the depots
     * @param sourceDepot              The source Depot for the change
     * @param destinationDepot         The destination Depot for the change
     * @param tempResourcesSource      A temporary List of the source Depot's Resources
     * @param tempResourcesDestination A temporary List of the destination Depot's Resources
     */
    public static void restoreOriginalSituation(Warehouse warehouse, int sourceDepot, int destinationDepot, List<Resource> tempResourcesSource, List<Resource> tempResourcesDestination) {
        warehouse.getAllDepots().get(sourceDepot).grabAllResources();
        warehouse.getAllDepots().get(destinationDepot).grabAllResources();

        try {
            addMultipleResources(warehouse, sourceDepot, tempResourcesSource);
            addMultipleResources(warehouse, destinationDepot, tempResourcesDestination);
        } catch (CanNotAddResourceToDepotException e) {
            e.printStackTrace();
        }
    }


    /**
     * Add a list of resources to a depot
     *
     * @param warehouse    The warehouse from where the Depot is taken
     * @param depotIndex   The index of the Depot where the Resources are going to be added
     * @param resourceList The resources to add
     * @throws CanNotAddResourceToDepotException Thrown if the resources can't be added to the depot
     */
    public static void addMultipleResources(Warehouse warehouse, int depotIndex, List<Resource> resourceList) throws CanNotAddResourceToDepotException {
        for (Resource resource : resourceList) warehouse.addResourceToDepot(depotIndex, resource);
    }


    /**
     * Creates the List to send to the Server by taking the Resource's type contained in each Depot
     *
     * @param warehouse Resources to insert into the List are taken from here
     * @return The list to send
     */
    public static List<Resource> createListToSend(Warehouse warehouse) {
        List<Resource> resourcesInWarehouse = new ArrayList<>();
        for (int i = 0; i < warehouse.getAllDepots().size(); i++) {
            resourcesInWarehouse.add(warehouse.getAllDepots().get(i).getContainedResourceType());
        }
        return resourcesInWarehouse;
    }


    /**
     * Changes the positions of the Resources between two Depots
     *
     * @param warehouse        The Warehouse containing the Depots
     * @param sourceDepot      The Depot from which start the change
     * @param destinationDepot The Depot in which end the change
     */
    public static void changePosition(Warehouse warehouse, int sourceDepot, int destinationDepot) {
        List<Resource> tempResourcesSource = warehouse.getAllDepots().get(sourceDepot).grabAllResources();
        List<Resource> tempResourcesDestination = warehouse.getAllDepots().get(destinationDepot).grabAllResources();

        try {
            addMultipleResources(warehouse, sourceDepot, tempResourcesDestination);
            addMultipleResources(warehouse, destinationDepot, tempResourcesSource);
        } catch (CanNotAddResourceToDepotException e) {
            restoreOriginalSituation(warehouse, sourceDepot, destinationDepot, tempResourcesSource, tempResourcesDestination);
        }
    }


    //--------------------------------------------------------//
    //          DEVELOPMENT CARD BUY SCREEN  METHODS          //
    //--------------------------------------------------------//

    /**
     * Returns the desired Development Card without drawing it from the Development Grid
     *
     * @param developmentCardsGrid The Grid from where to get the Development Card
     * @param cardLevel            The desired Development Card Level
     * @param cardColor            The desired Development Card Color
     * @return The desired Development Card
     */
    public static DevelopmentCard getSelectedDevelopmentCard(DevelopmentCardsGrid developmentCardsGrid, String cardLevel, String cardColor) {
        return developmentCardsGrid.getAllVisibleCards().stream().filter(x -> x.getDevelopmentCardType().getColor().getName().equalsIgnoreCase(cardColor))
                .filter(x -> x.getDevelopmentCardType().getLevel().getLevelName().equalsIgnoreCase(cardLevel)).findFirst().get();
    }

}
