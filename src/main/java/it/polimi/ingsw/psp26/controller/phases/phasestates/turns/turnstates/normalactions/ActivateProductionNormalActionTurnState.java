package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
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

import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_PRODUCTIONS_TO_ACTIVATE;
import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_RESOURCE_FROM_WAREHOUSE;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendErrorMessage;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendGeneralMessage;
import static it.polimi.ingsw.psp26.utils.CollectionsUtils.castElements;


public class ActivateProductionNormalActionTurnState extends TurnState {

    private List<Production> productionActivated = new ArrayList<>();
    private List<Resource> unknownCostResources = new ArrayList<>();
    private List<Resource> unknownProdResources = new ArrayList<>();
    private int numOfUnknownCost = 0;
    private int numOfUnknownProd = 0;

    /**
     * Constructor of the class.
     *
     * @param turn current turn
     */
    public ActivateProductionNormalActionTurnState(Turn turn) {
        super(turn);
    }

    /**
     *  Method that checks the messages of the current player and redirects to the right sub-phase.
     *
     *  The first sub-phase is CHOICE_NORMAL_ACTION: It checks if it is the message that arrived from
     *  ChooseNormalActionTurnState, and then it sends to the current player the message to choose the list of
     *  production that they want to activate.
     *
     *  The second sub-phase is CHOICE_PRODUCTIONS_TO_ACTIVATE: It receives the answer of the previous message, it calls
     *  the method to check if at least one production is actionable, if it is not it sends an error message and a
     *  redirection to the ChooseNormalActionTurnState. Then it calls the method to check and handle unknown resources
     *  and redirects to the next phase.
     *
     *  The third sub-phase is CHOICE_RESOURCE_FROM_WAREHOUSE/CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY: The message type
     *  depends on the unknown resource on the production list. It checks if there are unresolved unknown resources and
     *  so if it has to ask the player the unknown resource for the production result. If all unknown resources are
     *  defined, it calls the method to activate the productions and to go to the next phase.
     *
     *  The fourth sub-phase is QUIT_OPTION_SELECTED/default: It checks if an undo message is sent, and redirects the
     *  player to the ChooseNormalActionTurnState.
     *
     * @param message The message sent by the current player, that carries his choices during the turn.
     */

    public void play(SessionMessage message) {

        try {
            switch (message.getMessageType()) {
                case CHOICE_NORMAL_ACTION:

                    sendProductionMultipleChoiceMessage();

                    break;

                case CHOICE_PRODUCTIONS_TO_ACTIVATE:

                    if (isAtLeastOneProductionPlayable()) {

                        productionActivated = castElements(Production.class, message.getListPayloads());

                        checkForUnknownAndRedirect(message);

                    } else {

                        sendGeneralMessage(turn, "No enough resources to activate any card - sending to choose action:");
                        turn.changeState(new ChooseNormalActionTurnState(turn));
                        turn.play(message);

                    }
                    break;

                case CHOICE_RESOURCE_FROM_WAREHOUSE:
                case CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY:

                    if (unknownCostResources == null || unknownCostResources.size() == numOfUnknownCost) {

                        if (numOfUnknownProd != 0)
                            unknownProdResources = castElements(Resource.class, message.getListPayloads());

                        activateProduction(message);

                    } else {

                        unknownCostResources = castElements(Resource.class, message.getListPayloads());

                        if (numOfUnknownProd != 0) {
                            sendGeneralMessage(turn, "Choose the resource that you want back of the unknown resource in the production:");

                            turn.changeState(new OneResourceTurnState(turn, this, numOfUnknownProd, false));
                            turn.play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.CHOICE_PRODUCTIONS_TO_ACTIVATE));

                        } else {

                            sendErrorMessage(turn, "Error in the choice of resources");
                            play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE_FROM_WAREHOUSE));

                        }

                    }
                    break;

                default:
                case UNDO_OPTION_SELECTED:
                    turn.changeState(new ChooseNormalActionTurnState(turn));
                    turn.play(message);
                    break;


            }
        } catch (InvalidPayloadException ignored) {
        }
    }

    /**
     * Method that checks if the list of productions that the current player sent, in their totality, are actionable
     * by their current available resources.
     *
     * It takes a copy of all player resources and removes from them the resources required for the production, when it
     * meets unknown resources it substitute them with the player choices. If it cannot remove a resource from the copy
     * list it returns false, return true otherwise.
     *
     *
     * @return true if the whole production list is actionable, false if it is not.
     */

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

    /**
     * Method that check if the player production list proposed is possible, in case it takes the required resources
     * from the current player, gives them back the production resulting resources and then it takes them to the
     * following turn state
     *
     * It calls the isProductionFeasible to check if the player has enough resources to activate the production, If not
     * it sends an error message to the player and redirect them to ChooseNormalActionTurnState.
     * Instead if they have, it takes the resources required to activate the production first from the warehouse then
     * from the strongbox. Then it adds to the player the resources resulting by the production and redirect them to
     * next phase, CheckVaticanReportTurnState
     *
     *
     * @param message
     */


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

        } else {

            sendErrorMessage(turn, "Too many production activated or wrong use of the resources");
            turn.changeState(new ChooseNormalActionTurnState(turn));
        }

        turn.play(message);

    }

    /**
     * Method that sends to the current player a message to ask which production they want to activate between
     * their available production.
     *
     */

    private void sendProductionMultipleChoiceMessage() {
        try {
            turn.getMatchController().notifyObservers(
                    new MultipleChoicesMessage(
                            turn.getTurnPlayer().getSessionToken(),
                            CHOICE_PRODUCTIONS_TO_ACTIVATE,
                            "Choose the production that you want to activate:",
                            1, turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().size(),
                            true,
                            turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().toArray(new Object[0])
                    ));
        } catch (InvalidPayloadException ignored) {
        }
    }

    /**
     *  Method that adds to the current player strongbox the list of resources produced by the production list that
     *  they activated
     *
     *  For each production that the player activated it adds all the resource produced that are not unknown to a
     *  temporary list, then it checks if there were unknown resources and in case it adds up also the resources that
     *  the player decided to substitute the unknown resources with. The list is parsed and then added to the player
     *  strongbox
     *
     */

    private void collectProduction() {
        List<Resource> resourcesProduced = new ArrayList<>();
        for (Production production : productionActivated) {
            for (Resource resourceProd : production.getProductionReturn().keySet()) {
                if (resourceProd != Resource.UNKNOWN) {
                    for (int j = 0; j < production.getProductionReturn().get(resourceProd); j++)
                        resourcesProduced.add(resourceProd);
                }
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


    /**
     *  Method that checks if the current player has enough resources to activate at least one production of the
     *  productions they possess.
     *
     *  If there is only one visible production it means that it is the base one, and so it is enough to check that
     *  player has at least two resources. If there are more than one, it means that there is at least one development
     *  card (that could require only one resource), and so for each one of them it checks if its requirements are
     *  satisfiable.
     *
     * @return true if there is at least one production that is actionable by the player, false if there is none.
     */
    private boolean isAtLeastOneProductionPlayable() {
        if (turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().size() >= 2) {
            for (int i = 0; i < turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().size(); i++) {

                productionActivated.add(turn.getTurnPlayer().getPersonalBoard().getAllVisibleProductions().get(i));
                if (!productionActivated.get(0).getProductionCost().containsKey(Resource.UNKNOWN)) {
                    if (isProductionFeasible()){
                        productionActivated.remove(0);
                        return true;
                    }
                }
                productionActivated.remove(0);

            }

        }

        return turn.getTurnPlayer().getPersonalBoard().getAllAvailableResources().size() >= 2;
    }

    /**
     *  Method that checks the presence of unknown resources in the production list sent by the current player. If there
     *  are, it let them pick the substitute resources for them. Then it redirects the player to the next phase.
     *
     *  For each production, it checks the presence of unknown resources both in the production cost and in the return,
     *  for each unknown resource found it increase the relative counter. First it checks the cost counter if it is not
     *  zero and in case ask for the resource to substitute them with. If the cost counter is zero it does the same for
     *  the return. And if both are zero it directly goes to the next phase.
     *
     * @param message
     */

    private void checkForUnknownAndRedirect(SessionMessage message){
        for (Production production : productionActivated) {

            if (production.getProductionCost().containsKey(Resource.UNKNOWN))
                numOfUnknownCost += production.getProductionCost().get(Resource.UNKNOWN);

            if (production.getProductionReturn().containsKey(Resource.UNKNOWN))
                numOfUnknownProd += production.getProductionReturn().get(Resource.UNKNOWN);
        }
        if(numOfUnknownCost != 0 & turn.getTurnPlayer().getPersonalBoard().getAllAvailableResources().size() <= 1){
            sendGeneralMessage(turn, "Not enough resources to activate base power");
            turn.changeState(new ChooseNormalActionTurnState(turn));
            turn.play(message);
            return;
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

            try {
                play(new SessionMessage(turn.getTurnPlayer().getSessionToken(), CHOICE_RESOURCE_FROM_WAREHOUSE));

            } catch (InvalidPayloadException e) {

                e.printStackTrace();

            }

        }

    }

}