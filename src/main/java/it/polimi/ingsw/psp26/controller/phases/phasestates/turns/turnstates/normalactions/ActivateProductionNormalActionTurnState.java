package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.DepotOutOfBoundException;
import it.polimi.ingsw.psp26.exceptions.NegativeNumberOfElementsToGrabException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;

import java.util.ArrayList;
import java.util.List;

public class ActivateProductionNormalActionTurnState extends TurnState {
    public ActivateProductionNormalActionTurnState(Turn turn) {
        super(turn);
    }

    public void play(Message message) {
        List<Resource> playerStrongBoxCopy = new ArrayList<>();
        List<List<Resource>> playerDepotsCopy = new ArrayList<List<Resource>>();

        List<Resource> playerResources = getPlayerResources(turn.getTurnPlayer());
        List<DevelopmentCard> playerCards = turn.getTurnPlayer().getPersonalBoard().getVisibleDevelopmentCards();
        List<Resource> resourcesProduced = new ArrayList<>();
        //ShowCards() and getMessage()

        for (DevelopmentCard card : playerCards) { // Instead of player cards there should be chosenCards
            try {
                activateCardProduction(card, turn.getTurnPlayer());
            } catch (NegativeNumberOfElementsToGrabException e) {
                System.out.println("Number of resources insufficient to activate all development card");
                System.out.println("Choose another action or choose a different set of development cards");
                try {
                    Reverse(playerStrongBoxCopy, playerDepotsCopy, turn.getTurnPlayer());
                } catch (DepotOutOfBoundException | NegativeNumberOfElementsToGrabException | CanNotAddResourceToDepotException exc) {
                    exc.printStackTrace();
                }
            }
            resourcesProduced.addAll(getProductionResources(card));
        }
        turn.getTurnPlayer().getPersonalBoard().getStrongbox().addAll(resourcesProduced);
    }

    private List<Resource> getPlayerResources(Player player) {
        List<Resource> resources = new ArrayList<>();
        player.getPersonalBoard().getWarehouseDepots().stream().forEach(x -> resources.addAll(x.getResources()));
        resources.addAll(player.getPersonalBoard().getStrongbox());
        return resources;
    }

    private void activateCardProduction(DevelopmentCard chosenCard, Player player) throws NegativeNumberOfElementsToGrabException {
        int resourceCost = 0;
        for (Resource resource : chosenCard.getProductionCost().keySet()) {
            resourceCost = chosenCard.getProductionCost().get(resource);
            player.getPersonalBoard().grabResourcesFromWarehouseAndStrongbox(resource, resourceCost);
        }
    }

    private List<Resource> getProductionResources(DevelopmentCard chosenCard) {
        List<Resource> resourcesProduced = new ArrayList<>();
        for (Resource resource : chosenCard.getProductionReturn().keySet()) {
            for (int i = 0; i < chosenCard.getProductionReturn().get(resource); i++) {
                resourcesProduced.add(resource);
            }
        }
        return resourcesProduced;
    }

    private void takePlayerResourcesSnapShot(Player player, List<Resource> StrongBoxCopy, List<List<Resource>> DepotsCopy) {
        StrongBoxCopy.addAll(player.getPersonalBoard().getStrongbox());
        for (Depot depot : player.getPersonalBoard().getWarehouseDepots()) {
            List<Resource> DepotCopy1 = new ArrayList<>();
            DepotCopy1.addAll(depot.getResources());
            DepotsCopy.add(DepotCopy1);
        }
    }


    private void Reverse(List<Resource> StrongBoxCopy, List<List<Resource>> DepotsCopy, Player player) throws DepotOutOfBoundException, CanNotAddResourceToDepotException, NegativeNumberOfElementsToGrabException {
        player.getPersonalBoard().getStrongbox().clear();
        player.getPersonalBoard().getStrongbox().addAll(StrongBoxCopy);
        for (int i = 0; i < player.getPersonalBoard().getWarehouseDepots().size(); i++) {
            player.getPersonalBoard().getWarehouseDepot(i).grabAllResources();
            for(int j = 0; j < DepotsCopy.get(i).size(); j++){
                player.getPersonalBoard().getWarehouseDepot(i).addResource(DepotsCopy.get(i).get(0));
            }

        }


    }

}
