package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;

/**
 * Abstract class of the turn states. It is used to model the possible states in a turn.
 */
public abstract class TurnState {

    protected final Turn turn;

    /**
     * Class constructor.
     *
     * @param turn the turn which contains this state.
     */
    public TurnState(Turn turn) {
        this.turn = turn;
    }

    /**
     * Abstract method of play.
     *
     * @param message the message that is played
     */
    public void play(SessionMessage message) {
    }
}
