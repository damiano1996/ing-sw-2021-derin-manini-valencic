package it.polimi.ingsw.psp26.controller.turns.states.leaderactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.turns.Turn;
import it.polimi.ingsw.psp26.controller.turns.states.CheckVaticanReportState;
import it.polimi.ingsw.psp26.controller.turns.states.TurnState;

public class DiscardLeaderActionState extends TurnState {
    public DiscardLeaderActionState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(Message message) {
        super.play(message);

        // next state is...
        turn.changeState(new CheckVaticanReportState(turn));
    }
}
