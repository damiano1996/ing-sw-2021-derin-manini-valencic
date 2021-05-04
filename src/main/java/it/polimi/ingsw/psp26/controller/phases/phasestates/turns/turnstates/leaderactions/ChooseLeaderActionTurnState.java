package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.goToNextStateAfterLeaderAction;

/**
 * Class to models the state in which player can chose the leader action to perform.
 */
public class ChooseLeaderActionTurnState extends TurnState {


    /**
     * Constructor of the class.
     *
     * @param turn      current turn
     * @param turnPhase phase of the turn
     */
    public ChooseLeaderActionTurnState(Turn turn, TurnPhase turnPhase) {
        super(turn);
        System.out.println("ChooseLeaderActionTurnState - turn phase:" + turnPhase);
        turn.setTurnPhase(turnPhase);
    }

    /**
     * Method to ask to the client which is the leader action to perform.
     * After having received the response, it forwards the message to the selection option.
     *
     * @param message session message
     */
    @Override
    public void play(SessionMessage message) {
        super.play(message);

        System.out.println("ChooseLeaderActionTurnState - message received: " + message.toString());

        List<MessageType> playableLeaderActions = getPlayableLeaderActions();

        if (playableLeaderActions.size() == 0) {
            goToNextStateAfterLeaderAction(turn, message);

        } else {

            if (message.getMessageType().equals(CHOICE_LEADER_ACTION)) {

                try {
                    System.out.println("ChooseLeaderActionTurnState - action selected: " + message.getPayload());

                    switch ((MessageType) message.getPayload()) {

                        case ACTIVATE_LEADER:
                        case DISCARD_LEADER:
                            turn.changeState(new ActivateOrDiscardLeaderTurnState(turn));
                            turn.play(message);
                            break;

                        case SKIP_LEADER_ACTION:
                            goToNextStateAfterLeaderAction(turn, message);
                            break;

                    }
                } catch (EmptyPayloadException ignored) {
                }

            } else {

                System.out.println("ChooseLeaderActionTurnState - sending choices to player.");

                // if message type doesn't match with any of previous cases, we have to display the choice to the client!
                try {
                    turn.getMatchController().notifyObservers(
                            new SessionMessage(
                                    turn.getTurnPlayer().getSessionToken(),
                                    PERSONAL_BOARD,
                                    turn.getTurnPlayer()
                            )
                    );
                    turn.getMatchController().notifyObservers(
                            new MultipleChoicesMessage(
                                    turn.getTurnPlayer().getSessionToken(),
                                    CHOICE_LEADER_ACTION,
                                    "Choice leader action to perform:",
                                    1, 1,
                                    false,
                                    playableLeaderActions.toArray()
                            )
                    );
                } catch (InvalidPayloadException ignored) {
                }
            }


        }
    }

    private List<MessageType> getPlayableLeaderActions() {
        ActivateOrDiscardLeaderTurnState activateOrDiscardLeaderTurnState = new ActivateOrDiscardLeaderTurnState(turn);
        List<MessageType> choices = new ArrayList<>();
        if (activateOrDiscardLeaderTurnState.isAtLeastOneLeaderActivatable()) choices.add(ACTIVATE_LEADER);
        if (activateOrDiscardLeaderTurnState.isAtLeastOneLeaderDiscardable()) choices.add(DISCARD_LEADER);
        if (choices.size() > 0) choices.add(SKIP_LEADER_ACTION);
        return choices;
    }
}
