package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToWarehouse;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.MatchUtils.addFaithPointsToPlayers;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;

public class ResourcesWarehousePlacer extends TurnState {

    private final List<Resource> resourcesToAdd;

    public ResourcesWarehousePlacer(Turn turn, List<Resource> resourcesToAdd) {
        super(turn);
        this.resourcesToAdd = resourcesToAdd;
    }

    @Override
    public void play(SessionMessage message) {
        super.play(message);

        if (message.getMessageType().equals(MessageType.PLACE_IN_WAREHOUSE)) {

            try {
                // by protocol we receive a list of resources which represents the order to fill the warehouse
                List<Resource> resourceOrder = castElements(Resource.class, message.getListPayloads()); // TODO: case in leader depot
                // collecting all the resources of the player
                List<Resource> playerResources = turn.getTurnPlayer().getPersonalBoard().getWarehouse().grabAllResources();
                playerResources.addAll(resourcesToAdd);

                int discardedResources = 0;

                // filling the warehouse with the given order
                for (Resource resource : resourceOrder) {
                    // amount of this resource
                    int resourceQuantity = Collections.frequency(playerResources, resource);
                    // try to add
                    for (int i = 0; i < resourceQuantity; i++) {
                        try {
                            turn.getTurnPlayer().getPersonalBoard().getWarehouse().addResource(resource);
                        } catch (CanNotAddResourceToWarehouse canNotAddResourceToWarehouse) {
                            // if we reached the maximum we will add one point to opponents
                            discardedResources += 1;
                        }
                    }
                }

                // adding FP to other players
                addFaithPointsToPlayers(turn.getMatchController().getMatch(), turn.getTurnPlayer(), discardedResources);
                // checking vatican report
                turn.changeState(new CheckVaticanReportTurnState(turn));
                turn.play(message);
            } catch (EmptyPayloadException ignored) {
            }

        } else {
            sendMessage();
        }

    }

    private void sendMessage() {
        System.out.println("ResourcesWarehousePlacer - sending message to " + turn.getTurnPlayer().getNickname());

        Message msg1 = new Message(MessageType.PLACE_IN_WAREHOUSE, turn.getTurnPlayer().getPersonalBoard().getWarehouse());
        Message msg2 = new Message(MessageType.PLACE_IN_WAREHOUSE, resourcesToAdd.toArray());

        turn.getMatchController().notifyObservers(
                new SessionMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        MessageType.PLACE_IN_WAREHOUSE,
                        msg1, msg2
                )
        );

    }

}
