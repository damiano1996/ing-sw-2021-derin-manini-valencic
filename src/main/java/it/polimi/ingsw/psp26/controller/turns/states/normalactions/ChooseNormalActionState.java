package it.polimi.ingsw.psp26.controller.turns.states.normalactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.turns.Phase;
import it.polimi.ingsw.psp26.controller.turns.Turn;
import it.polimi.ingsw.psp26.controller.turns.states.TurnState;

public class ChooseNormalActionState extends TurnState {
    public ChooseNormalActionState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(Message message) {
        super.play(message);

        // TODO: to implement

        // ... switch cases

        // e.g
        // next state is...
        turn.setPhase(Phase.LEADER_ACTION_SECOND);
        turn.changeState(new MarketResourceNormalActionState(turn));
    }
}
