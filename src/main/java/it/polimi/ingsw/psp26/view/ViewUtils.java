package it.polimi.ingsw.psp26.view;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;
import it.polimi.ingsw.psp26.network.client.cache.CachedModel;

import java.util.*;

public class ViewUtils {

    public static String beautifyMessageType(MessageType messageType) {
        String message = messageType.name();
        message = message.replace('_', ' ');
        return toTitleStyle(message);
    }

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
                players.add(cachedModel.getOpponentCached(i).getObsoleteObject());
            }
            players.add(cachedModel.getMyPlayerCached().getObsoleteObject());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return players;
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
     * @return The Resources to put back in the Resource to insert List
     * @throws CanNotAddResourceToDepotException Thrown if the resources can't be added to the depot
     */
    public static List<Resource> addMultipleResources(Warehouse warehouse, int depotIndex, List<Resource> resourceList) throws CanNotAddResourceToDepotException {
        List<Resource> resources = new ArrayList<>();
        int maxNumberOfResources = warehouse.getAllDepots().get(depotIndex).getMaxNumberOfResources();

        //If the Resource to insert in a Depot are greater than the maxNumberOfResources the Depot can contain,
        //puts the remaining Resources in the Resource to insert List
        if (maxNumberOfResources < resourceList.size()) {

            for (int i = 0; i < maxNumberOfResources; i++)
                warehouse.addResourceToDepot(depotIndex, resourceList.get(i));

            for (int i = maxNumberOfResources; i < resourceList.size(); i++)
                resources.add(resourceList.get(i));

        }

        //Otherwise add the Resources normally
        else {
            for (Resource resource : resourceList) {
                warehouse.addResourceToDepot(depotIndex, resource);
            }
        }

        return resources;
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
     * @param resources        The Resource to insert List
     * @param sourceDepot      The Depot from which start the change
     * @param destinationDepot The Depot in which end the change
     */
    public static void changePosition(Warehouse warehouse, List<Resource> resources, int sourceDepot, int destinationDepot) {
        List<Resource> tempResourcesSource = warehouse.getAllDepots().get(sourceDepot).grabAllResources();
        List<Resource> tempResourcesDestination = warehouse.getAllDepots().get(destinationDepot).grabAllResources();

        try {
            resources.addAll(addMultipleResources(warehouse, sourceDepot, tempResourcesDestination));
            resources.addAll(addMultipleResources(warehouse, destinationDepot, tempResourcesSource));
        } catch (CanNotAddResourceToDepotException e) {
            restoreOriginalSituation(warehouse, sourceDepot, destinationDepot, tempResourcesSource, tempResourcesDestination);
        }
    }

}