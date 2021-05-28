package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.OneResourceTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.ResourcesWarehousePlacerTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions.ChooseLeaderActionTurnState;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.utils.CollectionsUtils.castElements;


public class MarketResourceNormalActionTurnState extends TurnState {

    private List<Resource> tempResources;

    /**
     * Constructor of the class.
     *
     * @param turn current turn
     */
    public MarketResourceNormalActionTurnState(Turn turn) {
        super(turn);
    }

    /**
     * Method that checks the messages of the current player and redirects to the right sub-phase.
     * <p>
     * The first sub-phase is CHOICE_NORMAL_ACTION: It checks if it is the message that arrived from
     * ChooseNormalActionTurnState, and then it sends to the current player the list of their available resources.
     * <p>
     * The second sub-phase is CHOICE_ROW_COLUMN: It receives the answer of the previous message, it checks if the
     * player choose a row or a column and which and takes it. Then it push the marble on the corresponding column/row.
     * If the resources taken contain a empty resource it calls the method that execute the leader power. If two
     * leaders are activated ask for the resources otherwise calls the method to parse the resource and to redirect to
     * the next phase.
     * <p>
     * The third sub-phase is CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY: This message type is received only when two leaders
     * are executed with success. The message contains the choice for each empty resource. The empty resources are
     * changed accordingly, and then it calls the method to parse the resource and to redirect to the next phase.
     * <p>
     * The fourth sub-phase is UNDO_OPTION_SELECTED/default: It checks if an undo message is sent, and redirects the
     * player to the ChooseNormalActionTurnState.
     *
     * @param message The message sent by the current player, that carries his choices during the turn.
     */

    public void play(SessionMessage message) {

        try {
            switch (message.getMessageType()) {
                case CHOICE_NORMAL_ACTION:
                    turn.getMatchController().notifyObservers(
                            new SessionMessage(
                                    turn.getTurnPlayer().getSessionToken(),
                                    MessageType.CHOICE_ROW_COLUMN
                            ));

                    break;
                case CHOICE_ROW_COLUMN:

                    boolean doubleActivation = false;
                    int RowColumnInt = (int) message.getPayload();
                    if (RowColumnInt <= 2) {

                        tempResources = Arrays.asList(turn.getMatchController().getMatch().getMarketTray().getMarblesOnRow((RowColumnInt)));
                        turn.getMatchController().getMatch().getMarketTray().pushMarbleFromSlideToRow((RowColumnInt));

                    } else {

                        tempResources = Arrays.asList(turn.getMatchController().getMatch().getMarketTray().getMarblesOnColumn((RowColumnInt + 1) % 4));
                        turn.getMatchController().getMatch().getMarketTray().pushMarbleFromSlideToColumn((RowColumnInt + 1) % 4);

                    }

                    if (tempResources.contains(Resource.EMPTY)) doubleActivation = applyMarbleLeaderEffect(message);

                    if (!doubleActivation) {
                        refactorResourceAndChangeTurn(message);

                    }
                    break;

                case CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY:
                    List<Resource> playerResourceChoice = castElements(Resource.class, message.getListPayloads());

                    for (int i = 0; i < tempResources.size(); i++) {
                        if (tempResources.get(i).equals(Resource.EMPTY)) {
                            tempResources.set(i, playerResourceChoice.get(0));
                            playerResourceChoice.remove(0);
                        }
                    }

                    refactorResourceAndChangeTurn(message);
                    break;

                default:
                case UNDO_OPTION_SELECTED:
                    turn.changeState(new ChooseNormalActionTurnState(turn));
                    turn.play(message);
                    break;
            }
        } catch (EmptyPayloadException | InvalidPayloadException ignored) {

        }
    }


    /**
     * Method that filters the resource of white and red marbles and redirects to the next phase.
     *
     * @param message the message that is forward to the next phase.
     */
    private void refactorResourceAndChangeTurn(SessionMessage message) {

        isRedMarblePresent();
        tempResources = tempResources.stream()
                .filter(x -> !x.equals(Resource.EMPTY))
                .filter(x -> !x.equals(Resource.FAITH_MARKER))
                .collect(Collectors.toList());

        if (tempResources.size() > 0) {

            turn.changeState(new ResourcesWarehousePlacerTurnState(turn, tempResources));

        } else {

            turn.changeState(new ChooseLeaderActionTurnState(turn, TurnPhase.LEADER_ACTION_TO_END));
        }
        turn.play(message);
    }

    /**
     * Method that checks if there is a red marble in the selected row or column and in case gives one point.
     */

    private void isRedMarblePresent() {

        tempResources.stream()
                .filter(x -> x.equals(Resource.FAITH_MARKER))
                .forEach(x -> turn.getTurnPlayer().getPersonalBoard().getFaithTrack().addFaithPoints(1));
    }

    //public List<Resource> getTempResources() { return tempResources;}

    /**
     * Method that for each leader active, tries to execute their power and in case of one or more marble leader it
     * apply their effect.
     * <p>
     * It creates two empty lists, in one list it is copied the original list of the column/row.Then for each leader
     * it tries to execute the power, if the result list has a different number of empty resources of the original one,
     * the list is saved.Then it checks the lists to see if both are different from the original one, if they are for
     * each empty resources it request to the player with which resource between the two they want to substitute it.
     *
     * @param message the message that is forwarded to the next phase.
     * @return true if two leaders executed their effect, false otherwise.
     */

    private boolean applyMarbleLeaderEffect(SessionMessage message) {

        List<Resource> tempResourceLeader = new ArrayList<>();

        List<Resource> tempResourceBeforeLeaders = new ArrayList<>(tempResources);

        for (LeaderCard leader : turn.getTurnPlayer().getLeaderCards()) {

            leader.execute(tempResources);

            if (tempResources.size() != tempResourceBeforeLeaders.size()) {

                tempResources = new ArrayList<>();
                tempResources.addAll(tempResourceBeforeLeaders);

            }

            if (tempResourceLeader.size() == 0 && !tempResources.contains(Resource.EMPTY)) {

                tempResourceLeader.addAll(tempResources);
                tempResources = new ArrayList<>();
                tempResources.addAll(tempResourceBeforeLeaders);

            }
        }

        if (tempResourceLeader.size() != 0 && !tempResources.contains(Resource.EMPTY)) {

            List<Resource> leaderResources = new ArrayList<>();

            leaderResources.add(tempResources.get(tempResourceBeforeLeaders.indexOf(Resource.EMPTY)));
            leaderResources.add(tempResourceLeader.get(tempResourceBeforeLeaders.indexOf(Resource.EMPTY)));
            tempResources = tempResourceBeforeLeaders;

            turn.changeState(
                    new OneResourceTurnState(turn,
                            this,
                            (int) tempResources.stream().filter(x -> x.equals(Resource.EMPTY)).count(),
                            false,
                            leaderResources
                    )
            );
            turn.play(message);

            return true;

        } else {

            if (tempResourceLeader.size() != 0) tempResources = tempResourceLeader;

        }
        return false;

    }
}

