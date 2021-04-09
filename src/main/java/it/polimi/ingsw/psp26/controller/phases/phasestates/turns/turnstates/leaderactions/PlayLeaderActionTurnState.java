package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;

public class PlayLeaderActionTurnState extends TurnState {
    public PlayLeaderActionTurnState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(Message message) {
        super.play(message);

        // next state is...
        turn.changeState(new CheckVaticanReportTurnState(turn));
    }
}
