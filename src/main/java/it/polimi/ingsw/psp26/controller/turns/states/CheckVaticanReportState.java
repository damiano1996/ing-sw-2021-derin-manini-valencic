package it.polimi.ingsw.psp26.controller.turns.states;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.turns.Turn;
import it.polimi.ingsw.psp26.controller.turns.states.endgamecheckers.EndMatchCheckerState;

public class CheckVaticanReportState extends TurnState {
    public CheckVaticanReportState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(Message message) {
        super.play(message);

        // next state is...
        turn.changeState(new EndMatchCheckerState(turn));
    }
}
