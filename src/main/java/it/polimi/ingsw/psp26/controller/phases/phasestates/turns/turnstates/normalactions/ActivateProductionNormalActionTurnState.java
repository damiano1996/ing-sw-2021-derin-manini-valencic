package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.OneResourceTurnState;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;

public class ActivateProductionNormalActionTurnState extends TurnState {

    private List<Production> productionActivated = new ArrayList<>();
    private List<Resource> unknownCostResources;
    private List<Resource> unknownProdResources;
    private int numOfUnknownCost;
    private int numOfUnknownProd;

    public ActivateProductionNormalActionTurnState(Turn turn) {
        super(turn);
    }

    public void play(SessionMessage message) { // TO BE FINISHED

        try {
            switch (message.getMessageType()) {
                case ACTIVATE_PRODUCTION:
                    turn.getMatchController().notifyObservers(
                            new MultipleChoicesMessage(
                                    turn.getTurnPlayer().getSessionToken(),
                                    CHOICE_CARDS_TO_ACTIVATE,
                                    "Choose the development cards to activate:",
                                    1, turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().size(),
                                    turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().toArray(new Object[0])
                            ));
                    break;

                case CHOICE_CARDS_TO_ACTIVATE:
                    productionActivated = castElements(Production.class, message.getListPayloads());
                    numOfUnknownCost = 0;
                    numOfUnknownProd = 0;
                    for (Production production : productionActivated) {
                        if (production.getProductionCost().containsKey(Resource.UNKNOWN))
                            numOfUnknownCost += production.getProductionCost().get(Resource.UNKNOWN);
                        if (production.getProductionReturn().containsKey(Resource.UNKNOWN))
                            numOfUnknownProd += production.getProductionReturn().get(Resource.UNKNOWN);
                    }
                    if(numOfUnknownCost != 0) {
                        new SessionMessage(
                                turn.getTurnPlayer().getSessionToken(),
                                GENERAL_MESSAGE,
                                "Choose the resource to pay that replaces the unknown resource in the production:"
                        );
                        turn.changeState(new OneResourceTurnState(turn, this, numOfUnknownCost));
                    }else if(numOfUnknownProd != 0){
                        new SessionMessage(
                                turn.getTurnPlayer().getSessionToken(),
                                GENERAL_MESSAGE,
                                "Choose the resource that you want back of the unknown resource in the production:"
                        );
                        turn.changeState(new OneResourceTurnState(turn, this, numOfUnknownProd));
                    }else{
                        play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE));
                    }
                    break;

                case CHOICE_RESOURCE:
                    if(unknownCostResources.size() == numOfUnknownCost){
                        if(numOfUnknownProd != 0) unknownProdResources = castElements(Resource.class, message.getListPayloads());
                        activateProduction(message);
                    }else {
                        unknownCostResources = castElements(Resource.class, message.getListPayloads());
                        if(numOfUnknownProd != 0) {
                            new SessionMessage(
                                    turn.getTurnPlayer().getSessionToken(),
                                    GENERAL_MESSAGE,
                                    "Choose the resource that you want back of the unknown resource in the production:"
                            );
                            turn.changeState(new OneResourceTurnState(turn, this, numOfUnknownProd));
                        }else{
                            play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE));
                        }

                    }
                    break;
            }
        } catch (EmptyPayloadException ignored) {
        }
    }

    private boolean isProductionFeasible(){
        List<Resource> availableResource = turn.getTurnPlayer().getPersonalBoard().getAllAvailableResources();
        for (Production production : productionActivated) {
            for (Resource resource : production.getProductionCost().keySet()) {
                if (resource == Resource.UNKNOWN) {
                    for (int i = 0; i < production.getProductionCost().get(Resource.UNKNOWN); i++) {
                        if (!availableResource.remove((unknownCostResources.get(i)))) {
                            return false;
                        }
                    }
                }
                for(int i = 0; i < production.getProductionCost().get(resource); i++){
                    if(!availableResource.remove(resource)){
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private void activateProduction(SessionMessage message) {
        if(isProductionFeasible()) {
            for (Production production : productionActivated) {
                for (Resource resource : production.getProductionCost().keySet()) {
                    if (resource == Resource.UNKNOWN) {
                        for (int i = 0; i < production.getProductionCost().get(Resource.UNKNOWN); i++) {
                            if (turn.getTurnPlayer().getPersonalBoard().grabResourcesFromStrongbox(unknownCostResources.get(0), 1) != null) {
                                unknownCostResources.remove(0);
                            }
                            turn.getTurnPlayer().getPersonalBoard().grabResourcesFromWarehouseAndStrongbox(resource,
                                    production.getProductionCost().get(resource));
                        }
                    }
                }
            }
            collectProduction();
            turn.changeState(new CheckVaticanReportTurnState(turn));
            turn.play(message);
        }else{
            turn.getMatchController().notifyObservers(
                    new MultipleChoicesMessage(
                            turn.getTurnPlayer().getSessionToken(),
                            CHOICE_CARDS_TO_ACTIVATE,
                            "Choose the development cards to activate:",
                            1, turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().size(),
                            turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().toArray(new Object[0])
                    ));
        }

    }


    private void collectProduction() {
        List<Resource> resourcesProduced = new ArrayList<>();
        for (Production production : productionActivated) {
            for (Resource resourceProd : production.getProductionReturn().keySet()) {
                if (resourceProd != Resource.UNKNOWN) {
                    for (int j = 0; j < production.getProductionReturn().get(resourceProd); j++)
                        resourcesProduced.add(resourceProd);
                }
            }
            resourcesProduced.addAll(unknownProdResources);
            try {
                turn.getTurnPlayer().getPersonalBoard().addResourcesToStrongbox(resourcesProduced);
            } catch (CanNotAddResourceToStrongboxException e) {
                e.printStackTrace();

            }
        }
    }

}

