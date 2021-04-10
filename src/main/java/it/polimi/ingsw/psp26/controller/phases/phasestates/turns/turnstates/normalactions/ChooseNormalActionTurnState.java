package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;

public class ChooseNormalActionTurnState extends TurnState {
    public ChooseNormalActionTurnState(Turn turn) {
        super(turn);
        turn.setTurnPhase(TurnPhase.NORMAL_TO_LEADER_ACTION);
    }

    @Override
    public void play(Message message) {
        super.play(message);

        // TODO: to implement

        // ... switch cases

        // e.g
        // next state is...
        turn.changeState(new MarketResourceNormalActionTurnState(turn));
    }
}
