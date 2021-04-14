package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
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

import java.util.ArrayList;
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
        List<Integer> rowColumn = (List<Integer>) message.getPayload();
        switch (message.getMessageType()) {
            case MARKET_RESOURCE:
                if (rowColumn.get(0) == 0) {
                    tempResources = Arrays.asList(turn.getMatch().getMarketTray().getMarblesOnRow(rowColumn.get(1)));
                    turn.getMatch().getMarketTray().pushMarbleFromSlideToColumn(rowColumn.get(1));
                } else {
                    tempResources = Arrays.asList(turn.getMatch().getMarketTray().getMarblesOnRow(rowColumn.get(1)));
                    turn.getMatch().getMarketTray().pushMarbleFromSlideToRow(rowColumn.get(1));
                }
                isRedMarblePresent(turn.getTurnPlayer());
                tempResources = parseResource();
                new Message(turn.getTurnPlayer().getSessionToken(),
                        MessageType.CHOICE_ORGANIZATION_MOVE);
                break;
            case RESOURCE_POSITION_CHOSEN:
                List<Depot> currentDepotsStatus = turn.getTurnPlayer().getPersonalBoard().getWarehouseDepots();
                moveResourceFromSlideToDepot(Resource.COIN, 1, currentDepotsStatus.get(1));
                new Message(turn.getTurnPlayer().getSessionToken(),
                        MessageType.CHOICE_ORGANIZATION_MOVE);
                break;
            case GRAB_RESOURCES:
                try {
                    tempResources.addAll(turn.getTurnPlayer().getPersonalBoard().getWarehouseDepot((int) message.getPayload()).grabAllResources());
                } catch (NegativeNumberOfElementsToGrabException | DepotOutOfBoundException e) {
                    e.printStackTrace();
                }
                new Message(turn.getTurnPlayer().getSessionToken(),
                        MessageType.CHOICE_ORGANIZATION_MOVE);

            case MARKET_NEXT:
                discardResources(turn.getMatch(), turn.getTurnPlayer());
                turn.changeState(new CheckVaticanReportTurnState(turn));
                turn.play(message);
                break;

            default:
                new Message(turn.getTurnPlayer().getSessionToken(),
                        MessageType.CHOICE_NORMAL_ACTION);
        }
    }

    private List<Resource> parseResource() {
        return tempResources.stream().filter(x -> !x.equals(Resource.EMPTY)).filter(x -> !x.equals(Resource.FAITH_MARKER)).collect(Collectors.toList());
    }

    private void isRedMarblePresent(Player player) {
        tempResources.stream().filter(x -> x.equals(Resource.FAITH_MARKER)).forEach(x -> player.getPersonalBoard().getFaithTrack().addFaithPoints(1));
    }

    private void moveResourceFromSlideToDepot(Resource resource, int resourceNum, Depot depot) {
        List<Resource> DepotCopy = new ArrayList<>();
        DepotCopy.addAll(depot.getResources());
        for (int i = 0; i < resourceNum; i++) {
            try {
                depot.addResource(resource);
            } catch (CanNotAddResourceToDepotException e) {
                try {
                    depot.grabAllResources();
                    for (int j = 0; j < DepotCopy.size(); j++) {
                        depot.addResource(DepotCopy.get(0));
                    }
                    System.out.println("Error move not allowed");
                    new Message(turn.getTurnPlayer().getSessionToken(),
                            MessageType.CHOICE_ORGANIZATION_MOVE);
                } catch (NegativeNumberOfElementsToGrabException | CanNotAddResourceToDepotException exc) {
                    exc.printStackTrace();
                }
            }
            tempResources.remove(resource);
        }


    }

    private void discardResources(Match match, Player player) {
        match.getPlayers().stream().filter(x -> !x.equals(player)).forEach(x -> x.getPersonalBoard().getFaithTrack().addFaithPoints(tempResources.size()));
        tempResources.clear();
    }

}

