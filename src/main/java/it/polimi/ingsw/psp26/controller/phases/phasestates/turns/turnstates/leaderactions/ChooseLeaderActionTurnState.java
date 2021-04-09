package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.ChooseNormalActionTurnState;

public class ChooseLeaderActionTurnState extends TurnState {


    public ChooseLeaderActionTurnState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(Message message) {
        super.play(message);

        turn.setTurnPhase(TurnPhase.NORMAL_ACTION);

        if (!turn.getTurnPlayer().isLeaderActionPlayable()) {

            // if leader action is not playable we can go directly to the choose normal action state:
            turn.changeState(new ChooseNormalActionTurnState(turn));
            // and play it directly forwarding the message:
            turn.play(message);

        } else {

            switch (message.getMessageType()) {
                case ACTIVATE_LEADER:
                case DISCARD_LEADER:
                    turn.changeState(new ActivateOrDiscardLeaderTurnState(turn));
                    turn.play(message);
                    break;
                case SKIP_LEADER_ACTION:
                    turn.changeState(new ChooseLeaderActionTurnState(turn));
                    turn.play(message);
                    break;

                default:
                    // if message type doesn't match with any of previous cases, we have to display the choice to the client!
                    // client.displayChooseLeaderAction();
                    break;
            }
        }
    }
}
