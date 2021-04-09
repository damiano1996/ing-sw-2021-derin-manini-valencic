package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.ExcessiveResourceRequestedException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.ArrayList;
import java.util.List;

public class ActivateProductionNormalActionTurnState extends TurnState {
    public ActivateProductionNormalActionTurnState(Turn turn) {
        super(turn);
    }

    public void play(Message message) {
        List<Resource> playerResources = getPlayerResources(turn.getTurnPlayer());
        List<DevelopmentCard> playerCards = turn.getTurnPlayer().getPersonalBoard().getVisibleDevelopmentCards();
        List<Resource> resourcesProduced = turn.getTurnPlayer().getPersonalBoard().getStrongbox();
        //ShowCards() and getMessage()
        for (DevelopmentCard card : playerCards) { // Instead of player cards there should be chosenCards
            ActivateProduction(card, turn.getTurnPlayer());
            resourcesProduced.addAll(getProductionResources(card));
        }


    }

    private List<Resource> getPlayerResources(Player player) {
        List<Resource> resources = new ArrayList<>();
        player.getPersonalBoard().getWarehouseDepots().stream().forEach(x -> resources.addAll(x.getResources()));
        resources.addAll(player.getPersonalBoard().getStrongbox());
        return resources;
    }

    private void ActivateProduction(DevelopmentCard chosenCard, Player player) {
        int resourceCost = 0;
        for (Resource resource : chosenCard.getProductionCost().keySet()) {
            resourceCost = chosenCard.getProductionCost().get(resource);
            try {
                resourceCost -= player.getPersonalBoard().grabMinResourceFromDepots(resource, resourceCost).size();
            } catch (ExcessiveResourceRequestedException e) {
                e.printStackTrace();
            }
            try {
                if (resourceCost > 0) player.getPersonalBoard().removeResourceFromStrongbox(resource, resourceCost);
            } catch (ExcessiveResourceRequestedException e) {
                System.out.println("Error");
            }

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

}
