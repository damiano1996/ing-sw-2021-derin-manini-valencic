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
        // TODO: to implement

        // .... switch cases

        // if no leader action playable:
        // next state is...
        turn.setTurnPhase(TurnPhase.NORMAL_ACTION);
        turn.changeState(new ChooseNormalActionTurnState(turn)); // TODO: creates the correct instance considering the player choice
    }
}
