package it.polimi.ingsw.psp26.controller.turns.actions.normal;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//To test after message.class is completed

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
            //Get message
            getResourceFromDepot(CurrentDepots.get(1));
            addResourceIntoDepot(Resource.COIN, 1,  CurrentDepots.get(1));

        }
    }

    private void addResourceIntoDepot(Resource resource, int resourceNum, Depot depot) {
      //if(depot.isAdmissible(resource) && ((depot.getResources().size() + resourceNum )<= depot.getMaxNumberOfResources() )){
          for(int i = 0; i < resourceNum ; i++){
              try {
                  depot.addResource(resource);
              }catch (CanNotAddResourceToDepotException e) {
                      System.out.println("Errore"); // To improve
              }
              tempResources.remove(resource);
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

