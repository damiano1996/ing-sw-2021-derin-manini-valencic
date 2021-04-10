package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.DepotOutOfBoundException;
import it.polimi.ingsw.psp26.exceptions.NegativeNumberOfElementsToGrabException;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//To test after message.class is completed

public class MarketResourceNormalActionTurnState extends TurnState {
    List<Resource> tempResources;

    public MarketResourceNormalActionTurnState(Turn turn) {
        super(turn);
    }

    public void play(Message message) {
        // TODO: to implement sub-states

        //Get message
        boolean choice = false; // Temporal fix of Player choice
        int i = 1;             // Temporal fix of Player choice
        //message su play che contiene informazioni sulla mossa
        //choose()

        if (choice) {
            tempResources = Arrays.asList(turn.getMatch().getMarketTray().getMarblesOnRow(i));
            turn.getMatch().getMarketTray().pushMarbleFromSlideToColumn(i);
        } else {
            tempResources = Arrays.asList(turn.getMatch().getMarketTray().getMarblesOnRow(i));
            turn.getMatch().getMarketTray().pushMarbleFromSlideToRow(i);
        }

        isRedMarblePresent(turn.getTurnPlayer());
        tempResources = parseResource();

        List<Depot> CurrentDepotsStatus = turn.getTurnPlayer().getPersonalBoard().getWarehouseDepots();
        try {
            organizeResource(CurrentDepotsStatus);
        } catch (DepotOutOfBoundException | NegativeNumberOfElementsToGrabException e) {
            e.printStackTrace();
        }
        discardResources(turn.getMatch(), turn.getTurnPlayer());

        // next state is...
        turn.changeState(new CheckVaticanReportTurnState(turn));
    }

    private List<Resource> parseResource() {
        return tempResources.stream().filter(x -> !x.equals(Resource.EMPTY)).filter(x -> !x.equals(Resource.FAITH_MARKER)).collect(Collectors.toList());
    }

    private void isRedMarblePresent(Player player) {
        tempResources.stream().filter(x -> x.equals(Resource.FAITH_MARKER)).forEach(x -> player.getPersonalBoard().getFaithTrack().addFaithPoints(1));
    }

    private void organizeResource(List<Depot> CurrentDepots) throws DepotOutOfBoundException, NegativeNumberOfElementsToGrabException {
        boolean IsPlayerNotFinished = true;
        while (IsPlayerNotFinished) {
            //Get message
            tempResources = turn.getTurnPlayer().getPersonalBoard().getWarehouseDepot(1).grabAllResources();
            moveResourceFromSlideToDepot(Resource.COIN, 1, CurrentDepots.get(1));
        }
    }

    private void moveResourceFromSlideToDepot(Resource resource, int resourceNum, Depot depot) {
        //if(depot.isAdmissible(resource) && ((depot.getResources().size() + resourceNum )<= depot.getMaxNumberOfResources() )){
        for (int i = 0; i < resourceNum; i++) {
            try {
                depot.addResource(resource);
            } catch (CanNotAddResourceToDepotException e) {
                System.out.println("Errore"); // To improve
            }
            tempResources.remove(resource);
        }


    }

    private void discardResources(Match match, Player player) {
        match.getPlayers().stream().filter(x -> !x.equals(player)).forEach(x -> x.getPersonalBoard().getFaithTrack().addFaithPoints(tempResources.size()));
        tempResources.clear();
    }

}
