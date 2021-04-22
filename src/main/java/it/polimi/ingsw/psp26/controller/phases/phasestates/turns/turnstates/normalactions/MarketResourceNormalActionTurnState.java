package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.OneResourceTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.ResourcesWarehousePlacer;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Warehouse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//To test after message.class is completed


public class MarketResourceNormalActionTurnState extends TurnState {
    List<Resource> tempResources;


    public MarketResourceNormalActionTurnState(Turn turn) {
        super(turn);
    }

    public void play(SessionMessage message) {
        // TODO: to implement sub-states
        try {
            switch (message.getMessageType()) {
                case MARKET_RESOURCE:
                    int[] rowColumnInts = {0, 1, 2, 3, 4, 5, 6};
                    turn.getMatchController().notifyObservers(
                            new MultipleChoicesMessage(
                                    turn.getTurnPlayer().getSessionToken(),
                                    MessageType.CHOICE_ROW_COLUMN,
                                    "Choose a number between 0 to 6 where 0-2 refers to columns and 3-6 refers to rows",
                                    1, 1,
                                    rowColumnInts
                            ));

                    break;
                case CHOICE_ROW_COLUMN:
                    int RowColumnInt = (int) message.getPayload();
                    if (RowColumnInt >= 3) {
                        tempResources = Arrays.asList(turn.getMatchController().getMatch().getMarketTray().getMarblesOnRow((RowColumnInt + 1) % 4));
                        turn.getMatchController().getMatch().getMarketTray().pushMarbleFromSlideToColumn((RowColumnInt + 1) % 4);
                    } else {
                        tempResources = Arrays.asList(turn.getMatchController().getMatch().getMarketTray().getMarblesOnColumn(RowColumnInt));
                        turn.getMatchController().getMatch().getMarketTray().pushMarbleFromSlideToRow(RowColumnInt);
                    }
                    isRedMarblePresent(turn.getTurnPlayer());
                    tempResources = parseResource();
                    turn.changeState(new OneResourceTurnState(turn, this, turn.getTurnPlayer().getPersonalBoard().getWarehouse().getAllDepots().size()));
                    break;

                case CHOICE_RESOURCE:
                    turn.changeState(new ResourcesWarehousePlacer(turn, tempResources));
                    play(message);

                    break;
            }
        } catch (EmptyPayloadException ignored) {
        }
    }

    private List<Resource> parseResource() {
        return tempResources.stream().filter(x -> !x.equals(Resource.EMPTY)).filter(x -> !x.equals(Resource.FAITH_MARKER)).collect(Collectors.toList());
    }

    private void isRedMarblePresent(Player player) {
        tempResources.stream().filter(x -> x.equals(Resource.FAITH_MARKER)).forEach(x -> player.getPersonalBoard().getFaithTrack().addFaithPoints(1));
    }


    private void discardResources(Match match, Player player) {
        match.getPlayers().stream().filter(x -> !x.equals(player)).forEach(x -> x.getPersonalBoard().getFaithTrack().addFaithPoints(tempResources.size()));
        tempResources.clear();
    }


}

