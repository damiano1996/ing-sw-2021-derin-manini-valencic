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
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendErrorMessage;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.MatchUtils.addFaithPointsToPlayers;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;

public class ResourcesWarehousePlacerTurnState extends TurnState {

    private final List<Resource> resourcesToAdd;

    public ResourcesWarehousePlacerTurnState(Turn turn, List<Resource> resourcesToAdd) {
        super(turn);
        this.resourcesToAdd = resourcesToAdd;
    }

    @Override
    public void play(SessionMessage message) {
        super.play(message);

        if (message.getMessageType().equals(MessageType.PLACE_IN_WAREHOUSE)) {

            try {
                // by protocol we receive a list of resources which represents the order to fill the warehouse
                List<Resource> resourceOrder = castElements(Resource.class, message.getListPayloads());

                // checking if there is a duplicate in the base depots. If so we send an error message,
                // otherwise we can fill the warehouse with the given order of resources
                if (isDuplicate(resourceOrder.subList(0, turn.getTurnPlayer().getPersonalBoard().getWarehouse().getBaseDepots().size()))) {
                    sendErrorMessage(turn, "Each depot of the warehouse must contains different resources.");
                } else {

                    int discardedResources = fillWarehouse(resourceOrder);
                    // adding FP to other players
                    addFaithPointsToPlayers(turn.getMatchController().getMatch(), turn.getTurnPlayer(), discardedResources);
                    // checking vatican report
                    turn.changeState(new CheckVaticanReportTurnState(turn));
                    turn.play(message);
                }
            } catch (EmptyPayloadException ignored) {
            }

        } else {
            sendWarehouseMessage();
        }

    }

    /**
     * Method to fill the warehouse given the order in which the resources must be inserted.
     * The the order is related to the order of the depots.
     * The amount of resources is computed considering the old and the new resources.
     * The method computes the number of discarded resources and returns it.
     *
     * @param resourceOrder list of resources
     * @return number of discarded resources
     */
    private int fillWarehouse(List<Resource> resourceOrder) {
        // collecting all the resources of the player
        List<Resource> playerResources = turn.getTurnPlayer().getPersonalBoard().getWarehouse().grabAllResources();
        playerResources.addAll(resourcesToAdd); // adding the new obtained resources

        int discardedResources = 0; // counter of the resources that cannot be placed

        // filling the warehouse with the given order
        for (Resource resourceToAdd : resourceOrder) {
            // amount of this resource
            int resourceQuantity = Collections.frequency(playerResources, resourceToAdd);

            // try to add
            for (int j = 0; j < resourceQuantity; j++) {
                try {
                    turn.getTurnPlayer().getPersonalBoard().getWarehouse().addResource(resourceToAdd);
                } catch (CanNotAddResourceToWarehouse canNotAddResourceToWarehouse) {
                    discardedResources += 1;
                }
            }
        }
        return discardedResources;
    }

    /**
     * Method to check if there are duplicated resources in the list.
     *
     * @param resources list of resources
     * @return true if there is a duplicate, false otherwise
     */
    private boolean isDuplicate(List<Resource> resources) {
        List<Integer> multiplicity = resources.stream().map(x -> Collections.frequency(resources, x)).collect(Collectors.toList());
        return !multiplicity.stream().allMatch(x -> x == 1);
    }

    /**
     * Method to send to the client the warehouse and the list of resource that he has received.
     * The method sends a session message that contains two sub messages as payload.
     * The first message contains the warehouse of the turn player,
     * the second contains the list of new resources.
     */
    private void sendWarehouseMessage() {
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
