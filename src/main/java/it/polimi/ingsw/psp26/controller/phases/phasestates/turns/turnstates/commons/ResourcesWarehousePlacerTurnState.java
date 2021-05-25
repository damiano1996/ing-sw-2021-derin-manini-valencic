package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToWarehouse;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendErrorMessage;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.MatchUtils.addFaithPointsToPlayers;
import static it.polimi.ingsw.psp26.utils.CollectionsUtils.castElements;

/**
 * Class to model the turn state in which players pace resources in the warehouse.
 */
public class ResourcesWarehousePlacerTurnState extends TurnState {

    private final List<Resource> resourcesToAdd;

    /**
     * Constructor of the class.
     *
     * @param turn           current turn
     * @param resourcesToAdd resources to add in the warehouse
     */
    public ResourcesWarehousePlacerTurnState(Turn turn, List<Resource> resourcesToAdd) {
        super(turn);
        this.resourcesToAdd = resourcesToAdd;
    }

    /**
     * Method to place resources to warehouse.
     * It sends to the player the current warehouse with the list of resources that must be placed in.
     * As response, it expected a list of resources that indicates the order to fill the warehouse.
     * <p>
     * E.g. {COIN, STONE, SHIELD, COIN, STONE} is the received list.
     * The method has to place in the first row, of the warehouse, the COINs,
     * in the second the STONEs, in the third the SHIELDs, and so on.
     * The list does NOT referer to the quantity of the available resources, but only on the order!
     * If the corresponding depot doesn't have sufficient slots, the resources will be discarded, or placed in the leader depot, if available.
     * <p>
     * For user experience purposes, if there is a duplicate (e.g. {COIN, SERVANT, COIN}) between the base depots,
     * the method notifies the player with an error message, also re-sending the initial message.
     * <p>
     * Duplicates, in the received list, are admitted in case of leader depots.
     * The method has to fill in order the warehouse and then the leader depot.
     * <p>
     * E.g.
     * resourceOrder = {COIN, STONE, SHIELD, COIN, STONE}
     * 3 base depots and 2 leader depot (COIN and STONE)
     * All available COINs will be placed filling the first row of the warehouse and then the leader (COIN) depot if there are more than 1 COINs.
     * All available STONEs will be placed filling the second row of the warehouse and then the leader (STONE) depot if there are more than 2 STONEs.
     * all available SHIELDs will be placed in the third row of the warehouse.
     * All un-placeable resources will be discarded. Also in case of resources that the user has, but he decided to not insert in the list.
     * <p>
     * After having completed the action, the turn state change to check the vatican report.
     *
     * @param message session message
     */
    @Override
    public void play(SessionMessage message) {
        super.play(message);

        if (message.getMessageType().equals(MessageType.PLACE_IN_WAREHOUSE)) {

            // by protocol we receive a list of resources which represents the order to fill the warehouse
            List<Resource> resourceOrder = castElements(Resource.class, message.getListPayloads());

            // checking if there is a duplicate in the base depots. If so we send an error message,
            // otherwise we can fill the warehouse with the given order of resources
            if (isDuplicate(
                    resourceOrder.subList(0,
                            Math.min(
                                    turn.getTurnPlayer().getPersonalBoard().getWarehouse().getBaseDepots().size(),
                                    resourceOrder.size())))) {
                sendErrorMessage(turn, "Each depot of the warehouse must contains different resources.");
                sendWarehouseMessage();

            } else {

                // Checking if player has switched resource between depots and if switch was admissible given the constrains of the rules
                if (isAdmissibleSwitch(removeDuplicates(resourceOrder))) {

                    int discardedResources = fillWarehouse(resourceOrder);
                    System.out.println("ResourcesWarehousePlacerTurnState - resources to discard: " + discardedResources);
                    // adding FP to other players
                    addFaithPointsToPlayers(turn.getMatchController().getMatch(), turn.getTurnPlayer(), discardedResources);
                    // checking vatican report
                    System.out.println("ResourcesWarehousePlacerTurnState - going to vatican report");
                    turn.changeState(new CheckVaticanReportTurnState(turn));
                    turn.play(message);
                } else {

                    sendErrorMessage(turn, "Bad depots switch. Fill the warehouse again.");
                    sendWarehouseMessage();
                }
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
        // removing duplicates: to avoid to refill two times with the same resource
        resourceOrder = removeDuplicates(resourceOrder);

        // collecting all the resources of the player
        List<Resource> playerResources = turn.getTurnPlayer().getPersonalBoard().getWarehouse().grabAllResources();
        playerResources.addAll(resourcesToAdd); // adding the new obtained resources

        int discardedResources = 0; // counter of the resources that cannot be placed
        // tryToAllocate: counter of the resources that we tried to allocate.
        // To check if in the resourceOrder list there are resources completely ignored, but the client has.
        int tryToAllocate = 0;

        // filling the warehouse with the given order
        for (int i = 0; i < resourceOrder.size(); i++) {
            Resource resourceToAdd = resourceOrder.get(i);
            // amount of this resource
            int resourceQuantity = Collections.frequency(playerResources, resourceToAdd);
            tryToAllocate += resourceQuantity;

            // try to add
            for (int j = 0; j < resourceQuantity; j++) {
                try {
                    turn.getTurnPlayer().getPersonalBoard().getWarehouse().addResource(resourceToAdd, i);
                } catch (CanNotAddResourceToWarehouse canNotAddResourceToWarehouse) {
                    discardedResources += 1;
                }
            }
        }
        int ignoredResources = playerResources.size() - tryToAllocate;
        return discardedResources + ignoredResources;
    }

    /**
     * Method removes duplicates from list preserving the order of the elements.
     * EMPTY resources are not filtered.
     *
     * @param resources list of resources
     * @return list of resources without duplicates
     */
    private List<Resource> removeDuplicates(List<Resource> resources) {
        List<Resource> newResources = new ArrayList<>();
        for (Resource resource : resources)
            if (!newResources.contains(resource) || resource.equals(Resource.EMPTY)) newResources.add(resource);
        return newResources;
    }

    /**
     * Method to check if there are duplicated resources in the list.
     *
     * @param resources list of resources
     * @return true if there is a duplicate, false otherwise
     */
    private boolean isDuplicate(List<Resource> resources) {
        List<Integer> multiplicity = resources
                .stream()
                .map(x -> (!x.equals(Resource.EMPTY) ? Collections.frequency(resources, x) : 1))
                .collect(Collectors.toList());
        System.out.println(multiplicity);
        return multiplicity.stream().anyMatch(x -> x != 1);
    }

    /**
     * Method to send to the client the warehouse and the list of resource that he has received.
     * The method sends two consecutive messages with same message type. the first with warehouse as payload,
     * the second with the list of resources to add to it.
     */
    private void sendWarehouseMessage() {
        System.out.println("ResourcesWarehousePlacer - sending message to " + turn.getTurnPlayer().getNickname());
        try {

            turn.getMatchController().notifyObservers(
                    new SessionMessage(
                            turn.getTurnPlayer().getSessionToken(),
                            MessageType.PLACE_IN_WAREHOUSE,
                            turn.getTurnPlayer().getPersonalBoard().getWarehouse()
                    )
            );
            turn.getMatchController().notifyObservers(
                    new SessionMessage(
                            turn.getTurnPlayer().getSessionToken(),
                            MessageType.PLACE_IN_WAREHOUSE,
                            resourcesToAdd.toArray()
                    )
            );
        } catch (InvalidPayloadException ignored) {
        }
    }

    /**
     * Method to check if the new resource order, to fill the warehouse,
     * is admissible under the constraints imposed by the rules in case of switch between resources of different depots
     *
     * @param resourceFillOrder list of resources
     * @return true if constraints are satisfied, false otherwise
     */
    private boolean isAdmissibleSwitch(List<Resource> resourceFillOrder) {
        for (Depot sourceDepot : turn.getTurnPlayer().getPersonalBoard().getWarehouse().getBaseDepots()) {
            if (!sourceDepot.getContainedResourceType().equals(Resource.EMPTY)) {

                Depot targetDepot = turn.getTurnPlayer().getPersonalBoard().getWarehouse().getBaseDepots().get(
                        resourceFillOrder.subList(0, turn.getTurnPlayer().getPersonalBoard().getWarehouse().getBaseDepots().size())
                                .indexOf(sourceDepot.getContainedResourceType())
                );

                if (sourceDepot.getResources().size() > targetDepot.getMaxNumberOfResources())
                    return false;
            }

        }
        return true;
    }
}
