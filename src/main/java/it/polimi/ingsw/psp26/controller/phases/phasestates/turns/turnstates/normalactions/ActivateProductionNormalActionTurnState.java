package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.OneResourceTurnState;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToStrongboxException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.developmentgrid.Production;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_CARDS_TO_ACTIVATE;
import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_RESOURCE_FROM_WAREHOUSE;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendGeneralMessage;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;

public class ActivateProductionNormalActionTurnState extends TurnState {

    private List<Production> productionActivated = new ArrayList<>();
    private List<Resource> unknownCostResources = new ArrayList<>();
    private List<Resource> unknownProdResources = new ArrayList<>();
    private int numOfUnknownCost = 0;
    private int numOfUnknownProd = 0;

    public ActivateProductionNormalActionTurnState(Turn turn) {
        super(turn);
    }

    public void play(SessionMessage message) {

        try {
            switch (message.getMessageType()) {
                case CHOICE_NORMAL_ACTION:

                    turn.getMatchController().notifyObservers(
                            new SessionMessage(
                                    turn.getTurnPlayer().getSessionToken(),
                                    CHOICE_CARDS_TO_ACTIVATE,
                                    turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().toArray(new Object[0])
                            ));
                    turn.getMatchController().notifyObservers(
                            new SessionMessage(
                                    turn.getTurnPlayer().getSessionToken(),
                                    CHOICE_CARDS_TO_ACTIVATE,
                                    turn.getTurnPlayer().getPersonalBoard().getAllAvailableResources().toArray(new Object[0])
                            ));
                    break;

                case CHOICE_CARDS_TO_ACTIVATE:

                    if (isProductionPlayable()) {

                        productionActivated = castElements(Production.class, message.getListPayloads());

                        for (Production production : productionActivated) {

                            if (production.getProductionCost().containsKey(Resource.UNKNOWN))
                                numOfUnknownCost += production.getProductionCost().get(Resource.UNKNOWN);

                            if (production.getProductionReturn().containsKey(Resource.UNKNOWN))
                                numOfUnknownProd += production.getProductionReturn().get(Resource.UNKNOWN);
                        }
                        if (numOfUnknownCost != 0) {
                            sendGeneralMessage(turn, "Choose the resource to pay that replaces the unknown resource in the production:");

                            turn.changeState(new OneResourceTurnState(turn, this, numOfUnknownCost, true));
                            turn.play(message);

                        } else if (numOfUnknownProd != 0) {
                            sendGeneralMessage(turn, "Choose the resource that you want back of the unknown resource in the production:");

                            turn.changeState(new OneResourceTurnState(turn, this, numOfUnknownProd, false));
                            turn.play(message);

                        } else {

                            play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE_FROM_WAREHOUSE));

                        }
                    } else {
                        sendGeneralMessage(turn, "No enough resources to activate any card - sending to choose action:");
                        turn.changeState(new ChooseNormalActionTurnState(turn));
                        turn.play(message);

                    }
                    break;

                case CHOICE_RESOURCE_FROM_WAREHOUSE:
                case CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY:

                    System.out.println(message.getMessageType());
                    if (unknownCostResources == null || unknownCostResources.size() == numOfUnknownCost) {

                        if (numOfUnknownProd != 0)
                            unknownProdResources = castElements(Resource.class, message.getListPayloads());

                        activateProduction(message);

                    } else {
                        unknownCostResources = castElements(Resource.class, message.getListPayloads());

                        if (numOfUnknownProd != 0) {
                            sendGeneralMessage(turn, "Choose the resource that you want back of the unknown resource in the production:");

                            turn.changeState(new OneResourceTurnState(turn, this, numOfUnknownProd, false));
                            turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_CARDS_TO_ACTIVATE));

                        } else {

                            // TODO: add error message
                            play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE_FROM_WAREHOUSE));

                        }

                    }
                    break;
                default:
                case QUIT_OPTION_SELECTED:
                    turn.changeState(new ChooseNormalActionTurnState(turn));
                    turn.play(message);
                    break;


            }
        } catch (InvalidPayloadException ignored) {
        }
    }

    private boolean isProductionFeasible() {

        List<Resource> availableResource = new ArrayList<>(turn.getTurnPlayer().getPersonalBoard().getAllAvailableResources());

        for (Production production : productionActivated) {
            for (Resource resource : production.getProductionCost().keySet()) {
                if (resource == Resource.UNKNOWN) {
                    for (int i = 0; i < (production.getProductionCost().get(Resource.UNKNOWN)); i++) {
                        if (!availableResource.remove((unknownCostResources.get(i))))
                            return false;
                    }
                } else {
                    for (int i = 0; i < (production.getProductionCost().get(resource)); i++) {
                        if (!availableResource.remove(resource)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    private void activateProduction(SessionMessage message) {
        if (isProductionFeasible()) {
            for (Production production : productionActivated) {
                for (Resource resource : production.getProductionCost().keySet()) {
                    if (resource == Resource.UNKNOWN) {
                        for (int i = 0; i < production.getProductionCost().get(Resource.UNKNOWN); i++) {
                            if (turn.getTurnPlayer().getPersonalBoard().grabResourcesFromWarehouseAndStrongbox(unknownCostResources.get(0), 1).get(0) != null) {
                                unknownCostResources.remove(0);
                            }
                        }
                    }
                    turn.getTurnPlayer().getPersonalBoard().grabResourcesFromWarehouseAndStrongbox(resource,
                            production.getProductionCost().get(resource));
                }
            }
            collectProduction();
            turn.changeState(new CheckVaticanReportTurnState(turn));
            turn.play(message);
        } else {
            try {
                turn.getMatchController().notifyObservers(
                        new SessionMessage(
                                turn.getTurnPlayer().getSessionToken(),
                                CHOICE_CARDS_TO_ACTIVATE,
                                turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().toArray(new Object[0])
                        ));
                turn.getMatchController().notifyObservers(
                        new SessionMessage(
                                turn.getTurnPlayer().getSessionToken(),
                                CHOICE_CARDS_TO_ACTIVATE,
                                turn.getTurnPlayer().getPersonalBoard().getAllAvailableResources().toArray(new Object[0])
                        ));
            } catch (InvalidPayloadException ignored) {
            }
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
            if (unknownProdResources != null) resourcesProduced.addAll(unknownProdResources);
            resourcesProduced.stream()
                    .filter(x -> x.equals(Resource.FAITH_MARKER))
                    .forEach(x -> turn.getTurnPlayer().getPersonalBoard().getFaithTrack().addFaithPoints(1));
            resourcesProduced = resourcesProduced.stream()
                    .filter(x -> !x.equals(Resource.FAITH_MARKER))
                    .collect(Collectors.toList());

            try {
                turn.getTurnPlayer().getPersonalBoard().addResourcesToStrongbox(resourcesProduced);
            } catch (CanNotAddResourceToStrongboxException e) {
                e.printStackTrace();

            }
        }
    }

    private boolean isProductionPlayable() {
        if (turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().size() >= 2) {
            for (int i = 0; i < turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().size(); i++) {

                productionActivated.add(turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().get(i));
                if (!productionActivated.get(0).getProductionCost().containsKey(Resource.UNKNOWN)) {
                    if (isProductionFeasible()) return true;
                }
                productionActivated.remove(0);

            }

        }

        return turn.getTurnPlayer().getPersonalBoard().getAllAvailableResources().size() >= 2;
    }

}