package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.DepotOutOfBoundException;
import it.polimi.ingsw.psp26.exceptions.NegativeNumberOfElementsToGrabException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.MatchUtils.addFaithPointsToPlayers;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.MatchUtils.getWarehouseResources;
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

            // by protocol we receive a list of resources, which represents the type of resources to allocate in the corresponding depot
            List<Resource> resourceOrder = castElements(Resource.class, message.getListPayloads());
            // collecting all the resources of the player
            List<Resource> playerResources = getWarehouseResources(turn.getTurnPlayer());
            playerResources.addAll(resourcesToAdd);

            int discardedResources = 0;

            // for each resource type, we place them in the corresponding depot
            for (int i = 0; i < resourceOrder.size(); i++) {

                // quantity of the resources of this kind
                int quantity = Collections.frequency(playerResources, resourceOrder.get(i));

                try {
                    // before to add, we have to clean the depot
                    turn.getTurnPlayer().getPersonalBoard().getWarehouseDepot(i).grabAllResources();

                    // now we can add the resources
                    for (int j = 0; j < quantity; j++) {
                        try {
                            // try to add
                            turn.getTurnPlayer().getPersonalBoard().getWarehouseDepot(i).addResource(resourceOrder.get(i));
                        } catch (CanNotAddResourceToDepotException e) {
                            // if not possible we discard the resource and later we will assign points to opponents
                            discardedResources += 1;
                        }
                    }
                } catch (NegativeNumberOfElementsToGrabException | DepotOutOfBoundException e) {
                    e.printStackTrace();
                }

            }

            // adding FP to other players
            addFaithPointsToPlayers(turn.getMatchController().getMatch(), turn.getTurnPlayer(), discardedResources);
            // checking vatican report
            turn.changeState(new CheckVaticanReportTurnState(turn));
            turn.play(message);

        } else {
            sendMessage();
        }

    }


    /**
     * Method to send resources and warehouse to view.
     * By protocol design, the first element of the payload represents the resources that the player has received.
     *
     */
    private void sendMessage() {
        System.out.println("ResourcesWarehousePlacer - sending message to " + turn.getTurnPlayer().getNickname());

        turn.getMatchController().notifyObservers(
                new SessionMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        MessageType.PLACE_IN_WAREHOUSE,
                        warehouseToLists(resourcesToAdd, turn.getTurnPlayer()).toArray()
                )
        );

    }

    /**
     * Method transforms the list of depots in a list of lists of resources.
     * Useful for message payload purposes.
     *
     * @param resourcesToAdd list of resource to add to the warehouse
     * @param player         player that is receiving new resources
     * @return list of lists containing resources
     */
    private List<List<Resource>> warehouseToLists(List<Resource> resourcesToAdd, Player player) {
        List<List<Resource>> listWarehouse = new ArrayList<>();
        listWarehouse.add(resourcesToAdd);

        for (Depot depot : player.getPersonalBoard().getWarehouseDepots())
            listWarehouse.add(depot.getResources());

        return listWarehouse;
    }


}
