package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;

public class ChooseNormalActionTurnState extends TurnState {
    public ChooseNormalActionTurnState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(Message message) {
        super.play(message);

        // TODO: to implement

        // ... switch cases

        // e.g
        // next state is...
        turn.setTurnPhase(TurnPhase.LEADER_ACTION_SECOND_TIME);
        turn.changeState(new MarketResourceNormalActionTurnState(turn));
    }
}
