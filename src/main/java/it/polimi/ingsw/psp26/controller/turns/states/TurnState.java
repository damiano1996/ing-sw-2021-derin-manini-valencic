package it.polimi.ingsw.psp26.controller.turns.states;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.turns.Turn;

public abstract class TurnState {

    protected final Turn turn;

    public TurnState(Turn turn) {
        this.turn = turn;
    }

    public void play(Message message) {
    }
}
