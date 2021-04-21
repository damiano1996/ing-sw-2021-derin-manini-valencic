package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.OneResourceTurnState;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToStrongboxException;
import it.polimi.ingsw.psp26.exceptions.DepotOutOfBoundException;
import it.polimi.ingsw.psp26.exceptions.NegativeNumberOfElementsToGrabException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_CARDS_TO_ACTIVATE;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;

public class ActivateProductionNormalActionTurnState extends TurnState {

    private List<Production> productionActivated = new ArrayList<>();
    private List<Resource> unknownSwapResources;
    private int numOfUnknownCost;
    private int numOfUnknownProd;

    public ActivateProductionNormalActionTurnState(Turn turn) {
        super(turn);
    }

    public void play(SessionMessage message) { // TO BE FINISHED

        switch (message.getMessageType()) {
            case ACTIVATE_PRODUCTION:
                turn.getMatchController().notifyObservers(new MultipleChoicesMessage(turn.getTurnPlayer().getSessionToken(),
                        CHOICE_CARDS_TO_ACTIVATE, 1,
                        turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().size(),
                        turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().toArray(new Object[0])
                ));
                break;
            case CARDS_TO_ACTIVATE_CHOSEN:
                productionActivated = castElements(Production.class, message.getListPayloads());

                for (Production production : productionActivated) {
                    if (production.getProductionCost().containsKey(Resource.UNKNOWN))
                        numOfUnknownCost += production.getProductionCost().get(Resource.UNKNOWN);
                    if (production.getProductionReturn().containsKey(Resource.UNKNOWN))
                        numOfUnknownProd += production.getProductionReturn().get(Resource.UNKNOWN);
                }
                turn.changeState(new OneResourceTurnState(turn, this, numOfUnknownProd + numOfUnknownCost));
                break;

            case CHOICE_RESOURCE:
                List<Resource> unknownSwapResources = castElements(Resource.class, message.getListPayloads());
                activateProduction(message);
                break;
        }
    }

    private void takePlayerResourcesSnapShot(List<Resource> strongBoxCopy, List<List<Resource>> depotsCopy, Player player) {
        strongBoxCopy.addAll(player.getPersonalBoard().getStrongbox());
        for (Depot depot : player.getPersonalBoard().getWarehouse().getAllDepots()) {
            List<Resource> depotCopy1 = new ArrayList<>();
            depotCopy1.addAll(depot.getResources());
            depotsCopy.add(depotCopy1);
        }
    }


    private void reverse(List<Resource> strongBoxCopy, List<List<Resource>> depotsCopy, Player player) throws DepotOutOfBoundException, CanNotAddResourceToDepotException, NegativeNumberOfElementsToGrabException {
        player.getPersonalBoard().getStrongbox().clear();
        player.getPersonalBoard().getStrongbox().addAll(strongBoxCopy);
        for (int i = 0; i < player.getPersonalBoard().getWarehouse().getAllDepots().size(); i++) {
            player.getPersonalBoard().getWarehouse().getAllDepots().get(i).grabAllResources();
            for (int j = 0; j < depotsCopy.get(i).size(); j++) {
                player.getPersonalBoard().getWarehouse().addResourceToDepot(i, depotsCopy.get(i).get(0));
            }

        }
        turn.getMatchController().notifyObservers(new MultipleChoicesMessage(turn.getTurnPlayer().getSessionToken(),
                CHOICE_CARDS_TO_ACTIVATE, 1,
                turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().size(),
                turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().toArray(new Object[0])
        ));
    }


    private void activateProduction(SessionMessage message) {
        List<Resource> playerStrongBoxCopy = new ArrayList<>();
        List<List<Resource>> playerDepotsCopy = new ArrayList<List<Resource>>();
        takePlayerResourcesSnapShot(playerStrongBoxCopy, playerDepotsCopy, turn.getTurnPlayer());
        for (Production production : productionActivated) {
            for (Resource resource : production.getProductionCost().keySet()) {
                if (resource == Resource.UNKNOWN) {
                    for (int i = 0; i < production.getProductionCost().get(Resource.UNKNOWN); i++) {
                        if (turn.getTurnPlayer().getPersonalBoard().grabResourcesFromStrongbox(unknownSwapResources.get(0), 1) != null) {
                            unknownSwapResources.remove(0);
                        } else {
                            try {
                                reverse(playerStrongBoxCopy, playerDepotsCopy, turn.getTurnPlayer());
                            } catch (CanNotAddResourceToDepotException | DepotOutOfBoundException | NegativeNumberOfElementsToGrabException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }
                } else {
                    if (turn.getTurnPlayer().getPersonalBoard().grabResourcesFromWarehouseAndStrongbox(resource, production.getProductionCost().get(resource)).size()
                            != production.getProductionCost().get(resource)) {
                        try {
                            reverse(playerStrongBoxCopy, playerDepotsCopy, turn.getTurnPlayer());
                        } catch (CanNotAddResourceToDepotException | DepotOutOfBoundException | NegativeNumberOfElementsToGrabException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                }
            }
        }
        collectProduction();
        turn.changeState(new CheckVaticanReportTurnState(turn));
        turn.play(message);
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
            resourcesProduced.addAll(unknownSwapResources);
            try {
                turn.getTurnPlayer().getPersonalBoard().addResourcesToStrongbox(resourcesProduced);
            } catch (CanNotAddResourceToStrongboxException e) {
                e.printStackTrace();

            }
        }
    }

}

