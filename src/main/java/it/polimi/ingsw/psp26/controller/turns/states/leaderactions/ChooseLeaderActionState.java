package it.polimi.ingsw.psp26.controller.turns.states.leaderactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.turns.Phase;
import it.polimi.ingsw.psp26.controller.turns.Turn;
import it.polimi.ingsw.psp26.controller.turns.states.TurnState;
import it.polimi.ingsw.psp26.controller.turns.states.normalactions.ChooseNormalActionState;

public class ChooseLeaderActionState extends TurnState {
    public ChooseLeaderActionState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(Message message) {
        super.play(message);
        // TODO: to implement

        // .... switch cases

        // if no leader action playable:
        // next state is...
        turn.setPhase(Phase.NORMAL_ACTION);
        turn.changeState(new ChooseNormalActionState(turn)); // TODO: creates the correct instance considering the player choice
    }
}
