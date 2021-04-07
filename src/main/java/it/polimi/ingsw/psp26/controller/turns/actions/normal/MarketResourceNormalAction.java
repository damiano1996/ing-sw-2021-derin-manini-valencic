package it.polimi.ingsw.psp26.controller.turns.actions.normal;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class MarketResourceNormalAction extends NormalAction {
    List<Resource> tempResources;

    @Override
    public void play(Match match, Player player) {
        //Get message
        boolean choice = false; // Temporal fix of Player choice
        int i = 1;             // Temporal fix of Player choice
        //message su play che contiene informazioni sulla mossa
        //choose()
        if (choice) {
            tempResources = Arrays.asList(match.getMarketTray().getMarblesOnRow(i));
            match.getMarketTray().pushMarbleFromSlideToColumn(i);
        } else {
            tempResources = Arrays.asList(match.getMarketTray().getMarblesOnRow(i));
            match.getMarketTray().pushMarbleFromSlideToRow(i);
        }
        isRedMarblePresent(player);
        tempResources = parseResource();

        List<Depot> CurrentDepotsStatus = player.getPersonalBoard().getWarehouseDepots();

        organizeResource(CurrentDepotsStatus);

        discardResources(match, player);
    }

    private List<Resource> parseResource() {
        return tempResources.stream().filter(x -> !x.equals(Resource.EMPTY)).filter(x -> !x.equals(Resource.FAITH_MARKER)).collect(Collectors.toList());
    }

    private void isRedMarblePresent(Player player) {
        tempResources.stream().filter(x -> x.equals(Resource.FAITH_MARKER)).forEach(x -> player.getPersonalBoard().getFaithTrack().addFaithPoints(1));
    }

    private void organizeResource(List<Depot> CurrentDepots) {
        boolean IsPlayerNotFinished = true;
        while (IsPlayerNotFinished) {
            //Get messagge
            getResourceFromDepot(CurrentDepots.get(1));
            moveAddResourceIntoDepot(Resource.COIN, CurrentDepots.get(1));
        }
    }

    private void moveAddResourceIntoDepot(Resource resource, Depot depot) {
        if (depot.getMaxNumberOfResources() <= (int) tempResources.stream().filter(x -> x.equals(resource)).count()) {
            getResourceFromDepot(depot);
            try {
                depot.addResource(tempResources.stream().filter(x -> x.equals(resource)).collect(Collectors.toList()).subList(0, depot.getMaxNumberOfResources()));
            } catch (CanNotAddResourceToDepotException e) {
                System.out.println("Errore"); // Da migliorare
            }
            //Misses the remove the resources added from tempResources to complete the action
        } else {
            System.out.println("Not enough resources"); // Temp solution
        }
    }

    private void getResourceFromDepot(Depot depot) {
        tempResources.addAll(depot.getResources());
        depot.removeResource();
    }

    private void discardResources(Match match, Player player) {
        match.getPlayers().stream().filter(x -> !x.equals(player)).forEach(x -> x.getPersonalBoard().getFaithTrack().addFaithPoints(tempResources.size()));
        tempResources.clear();
    }

}

