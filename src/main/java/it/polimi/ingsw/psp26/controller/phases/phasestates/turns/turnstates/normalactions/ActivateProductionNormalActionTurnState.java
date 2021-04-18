package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToStrongboxException;
import it.polimi.ingsw.psp26.exceptions.DepotOutOfBoundException;
import it.polimi.ingsw.psp26.exceptions.NegativeNumberOfElementsToGrabException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;

public class ActivateProductionNormalActionTurnState extends TurnState {
    List<DevelopmentCard> ChosenCards = new ArrayList<>();

    public ActivateProductionNormalActionTurnState(Turn turn) {
        super(turn);
    }

    public void play(Message message) { // TO BE FINISHED

        if (!IsBasePowerPresent(castElements(DevelopmentCard.class, message.getPayloads()))) {
            List<Resource> resourcesProduced = new ArrayList<>(); //
            resourcesProduced = ActivateProduction(castElements(DevelopmentCard.class, message.getPayloads()));

            try {
                turn.getTurnPlayer().getPersonalBoard().addResourcesToStrongbox(resourcesProduced);
            } catch (CanNotAddResourceToStrongboxException ex) {
                ex.printStackTrace();
            }
        } else {

            switch (message.getMessageType()) {
                case RESOURCE_CHOSEN:

                    List<Resource> ChosenResources = castElements(Resource.class, message.getPayloads());
                    turn.changeState(new ActivateProductionNormalActionTurnState(turn));
                    turn.play(message);
                    break;

                default:
                    ChosenCards = castElements(DevelopmentCard.class, message.getPayloads());
                    turn.getMatchController().notifyObservers(
                            new Message(turn.getTurnPlayer().getSessionToken(),
                                    MessageType.CHOICE_RESOURCE_IN_RESOURCE_OUT)
                    );
                    break;
            }
        }
    }

    private void activateACardProduction(DevelopmentCard chosenCard, Player player) throws NegativeNumberOfElementsToGrabException {
        int resourceCost = 0;
        for (Resource resource : chosenCard.getProduction().getProductionCost().keySet()) {
            resourceCost = getCleanResources(chosenCard.getProduction().getProductionCost()).get(resource);
            player.getPersonalBoard().grabResourcesFromWarehouseAndStrongbox(resource, resourceCost);
        }
    }

    private List<Resource> getProductionResources(DevelopmentCard chosenCard) {
        List<Resource> resourcesProduced = new ArrayList<>();
        for (Resource resource : chosenCard.getProduction().getProductionReturn().keySet()) {
            for (int i = 0; i < chosenCard.getProduction().getProductionReturn().get(resource); i++) {
                resourcesProduced.add(resource);
            }
        }
        return resourcesProduced;
    }

    private void takePlayerResourcesSnapShot(Player player, List<Resource> strongBoxCopy, List<List<Resource>> depotsCopy) {
        strongBoxCopy.addAll(player.getPersonalBoard().getStrongbox());
        for (Depot depot : player.getPersonalBoard().getWarehouseDepots()) {
            List<Resource> depotCopy1 = new ArrayList<>();
            depotCopy1.addAll(depot.getResources());
            depotsCopy.add(depotCopy1);
        }
    }


    private void Reverse(List<Resource> strongBoxCopy, List<List<Resource>> depotsCopy, Player player) throws DepotOutOfBoundException, CanNotAddResourceToDepotException, NegativeNumberOfElementsToGrabException {
        player.getPersonalBoard().getStrongbox().clear();
        player.getPersonalBoard().getStrongbox().addAll(strongBoxCopy);
        for (int i = 0; i < player.getPersonalBoard().getWarehouseDepots().size(); i++) {
            player.getPersonalBoard().getWarehouseDepot(i).grabAllResources();
            for (int j = 0; j < depotsCopy.get(i).size(); j++) {
                player.getPersonalBoard().getWarehouseDepot(i).addResource(depotsCopy.get(i).get(0));
            }

        }
    }

    private List<Resource> ActivateProduction(List<DevelopmentCard> chosenCards) {
        List<Resource> playerStrongBoxCopy = new ArrayList<>();
        List<List<Resource>> playerDepotsCopy = new ArrayList<List<Resource>>();
        takePlayerResourcesSnapShot(turn.getTurnPlayer(), playerStrongBoxCopy, playerDepotsCopy);
        List<Resource> resourcesProduced = new ArrayList<>();
        for (DevelopmentCard card : chosenCards) {
            try {
                activateACardProduction(card, turn.getTurnPlayer());
            } catch (NegativeNumberOfElementsToGrabException e) {
                turn.getMatchController().notifyObservers(new Message(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_NORMAL_ACTION));
                try {
                    Reverse(playerStrongBoxCopy, playerDepotsCopy, turn.getTurnPlayer());
                } catch (DepotOutOfBoundException | NegativeNumberOfElementsToGrabException | CanNotAddResourceToDepotException exc) {
                    exc.printStackTrace();
                }
            }
            resourcesProduced.addAll(getProductionResources(card));
        }
        return resourcesProduced;
    }

    private boolean IsBasePowerPresent(List<DevelopmentCard> cards) {
        for (DevelopmentCard card : cards) {
            if (card.getCost().get(Resource.EMPTY) == 0) return true;
        }
        return false;
    }

    /**
     * Methods resolves the unknown resources asking to the client the desired one.
     *
     * @param rawResources cost or production return of a Production object
     * @return cost or productions cleaned replacing unknown resources with the chosen one
     */
    private Map<Resource, Integer> getCleanResources(Map<Resource, Integer> rawResources) {
        Map<Resource, Integer> cleanResources = new HashMap<>();
        for (Resource rawResource : rawResources.keySet()) {
            int quantity = rawResources.get(rawResource);

            if (!rawResource.equals(Resource.UNKNOWN)) {
                cleanResources.put(rawResource, quantity);
            } else {

                UnknownResourceResolver unknownResourceResolver = UnknownResourceResolver.getInstance(
                        turn.getMatchController().getVirtualView());

                try {
                    cleanResources.put(unknownResourceResolver.getResourceDefined(turn.getTurnPlayer()), quantity);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return cleanResources;
    }
}

