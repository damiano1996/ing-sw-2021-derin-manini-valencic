package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;

public abstract class TurnState {

    protected final Turn turn;

    public TurnState(Turn turn) {
        this.turn = turn;
    }

    public void play(SessionMessage message) {
    }
}
