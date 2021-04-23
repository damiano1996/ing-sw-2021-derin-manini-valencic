package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.ChooseNormalActionTurnState;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;

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

        if (!turn.getTurnPlayer().isLeaderActionPlayable()) {
            // if leader action is not playable we can go directly to the choose normal action state:
            turn.changeState(new ChooseNormalActionTurnState(turn));
            // and play it directly, forwarding the message:
            turn.play(message);

        } else {

            if (message.getMessageType().equals(CHOICE_LEADER_ACTION)) {

                try {
                    switch ((MessageType) message.getPayload()) {

                        case ACTIVATE_LEADER:
                        case DISCARD_LEADER:
                            turn.changeState(new ActivateOrDiscardLeaderTurnState(turn));
                            turn.play(message);
                            break;

                        case SKIP_LEADER_ACTION:
                            turn.changeState(new ChooseNormalActionTurnState(turn));
                            turn.play(message);
                            break;

                    }
                } catch (EmptyPayloadException ignored) {
                }

            } else {

                // if message type doesn't match with any of previous cases, we have to display the choice to the client!
                turn.getMatchController().notifyObservers(
                        new MultipleChoicesMessage(
                                turn.getTurnPlayer().getSessionToken(),
                                CHOICE_LEADER_ACTION,
                                "Choice leader action to perform:",
                                1, 1,
                                ACTIVATE_LEADER, DISCARD_LEADER, SKIP_LEADER_ACTION
                        )
                );
            }


        }
    }
}
