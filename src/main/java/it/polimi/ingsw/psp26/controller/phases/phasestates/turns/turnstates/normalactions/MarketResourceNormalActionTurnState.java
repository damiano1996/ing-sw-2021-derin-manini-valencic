package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
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

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;

//To test after message.class is completed


public class MarketResourceNormalActionTurnState extends TurnState {
    List<Resource> tempResources;
    Resource resourceChosen;
    MessageType lastMessage;

    public MarketResourceNormalActionTurnState(Turn turn) {
        super(turn);
    }

    public void play(SessionMessage message) {
        // TODO: to implement sub-states
        switch (message.getMessageType()) {
            case MARKET_RESOURCE:
                int[] rowColumnInts = {0, 1, 2, 3, 4, 5, 6};
                turn.getMatchController().notifyObservers(
                        new MultipleChoicesMessage(turn.getTurnPlayer().getSessionToken(),
                                MessageType.CHOICE_ROW_COLUMN, 1, 1, rowColumnInts));

                break;
            case ROW_COLUMN_CHOSEN:
                int RowColumnInt = (int) message.getPayload();
                if (RowColumnInt >= 4) {
                    tempResources = Arrays.asList(turn.getMatchController().getMatch().getMarketTray().getMarblesOnRow(RowColumnInt % 4));
                    turn.getMatchController().getMatch().getMarketTray().pushMarbleFromSlideToColumn(RowColumnInt % 4);
                } else {
                    tempResources = Arrays.asList(turn.getMatchController().getMatch().getMarketTray().getMarblesOnColumn(RowColumnInt));
                    turn.getMatchController().getMatch().getMarketTray().pushMarbleFromSlideToRow(RowColumnInt);
                }
                isRedMarblePresent(turn.getTurnPlayer());
                tempResources = parseResource();
                turn.getMatchController().notifyObservers(new MultipleChoicesMessage(turn.getTurnPlayer().getSessionToken(),
                        MessageType.CHOICE_ORGANIZATION_MOVE, 1, 1,
                        RESOURCE_POSITION_CHOSEN_ONE, GRAB_RESOURCES, MARKET_NEXT));
                break;
            case RESOURCE_POSITION_CHOSEN_ONE:
                turn.getMatchController().notifyObservers(new MultipleChoicesMessage(turn.getTurnPlayer().getSessionToken(),
                        CHOICE_RESOURCE, 1, 1,
                        Resource.COIN, Resource.STONE, Resource.SHIELD, Resource.SERVANT));
                break;
            case CHOICE_RESOURCE:
//                List<Depot> currentDepotsStatus = turn.getTurnPlayer().getPersonalBoard().getWarehouse();
//                moveResourceFromSlideToDepot(Resource.COIN, currentDepotsStatus.get(1));
//                turn.getMatchController().notifyObservers(new MultipleChoicesMessage(turn.getTurnPlayer().getSessionToken(),
//                        MessageType.CHOICE_ORGANIZATION_MOVE, 1, 1,
//                        RESOURCE_POSITION_CHOSEN_ONE, GRAB_RESOURCES, MARKET_NEXT));
                break;
            case GRAB_RESOURCES:
//                try {
//                    tempResources.addAll(turn.getTurnPlayer().getPersonalBoard().getWarehouseDepot((int) message.getPayload()).grabAllResources());
//                } catch (NegativeNumberOfElementsToGrabException | DepotOutOfBoundException e) {
//                    e.printStackTrace();
//                }
//                turn.getMatchController().notifyObservers(new MultipleChoicesMessage(turn.getTurnPlayer().getSessionToken(),
//                        MessageType.CHOICE_ORGANIZATION_MOVE, 1, 1,
//                        RESOURCE_POSITION_CHOSEN_ONE, GRAB_RESOURCES, MARKET_NEXT));

            case MARKET_NEXT:
                discardResources(turn.getMatchController().getMatch(), turn.getTurnPlayer());
                turn.changeState(new CheckVaticanReportTurnState(turn));
                turn.play(message);
                break;
        }
    }

    private List<Resource> parseResource() {
        return tempResources.stream().filter(x -> !x.equals(Resource.EMPTY)).filter(x -> !x.equals(Resource.FAITH_MARKER)).collect(Collectors.toList());
    }

    private void isRedMarblePresent(Player player) {
        tempResources.stream().filter(x -> x.equals(Resource.FAITH_MARKER)).forEach(x -> player.getPersonalBoard().getFaithTrack().addFaithPoints(1));
    }

    private void moveResourceFromSlideToDepot(Resource resource, Depot depot) {
//        List<Resource> DepotCopy = new ArrayList<>();
//        DepotCopy.addAll(depot.getResources());
//        for (int i = 0; i < Math.min((int) tempResources.stream().filter(x -> x.equals(resource)).count(), depot.getMaxNumberOfResources()); i++) {
//            try {
//                depot.addResource(resource);
//            } catch (CanNotAddResourceToDepotException e) {
//                try {
//                    depot.grabAllResources();
//                    for (int j = 0; j < DepotCopy.size(); j++) {
//                        depot.addResource(DepotCopy.get(0));
//                    }
//                    turn.getMatchController().notifyObservers(new MultipleChoicesMessage(turn.getTurnPlayer().getSessionToken(),
//                            MessageType.CHOICE_ORGANIZATION_MOVE, 1, 1,
//                            RESOURCE_POSITION_CHOSEN_ONE, GRAB_RESOURCES, MARKET_NEXT));
//                } catch (NegativeNumberOfElementsToGrabException | CanNotAddResourceToDepotException exc) {
//                    exc.printStackTrace();
//                }
//            }
//            tempResources.remove(resource);
//        }


    }

    private void discardResources(Match match, Player player) {
        match.getPlayers().stream().filter(x -> !x.equals(player)).forEach(x -> x.getPersonalBoard().getFaithTrack().addFaithPoints(tempResources.size()));
        tempResources.clear();
    }



}

