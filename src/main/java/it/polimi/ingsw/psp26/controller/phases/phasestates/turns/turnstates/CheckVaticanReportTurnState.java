package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.endgamecheckers.EndMatchCheckerTurnState;

public class CheckVaticanReportTurnState extends TurnState {
    public CheckVaticanReportTurnState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(SessionMessage message) {
        super.play(message);

        // next state is...
        turn.changeState(new EndMatchCheckerTurnState(turn));
        turn.play(message);
    }
}
