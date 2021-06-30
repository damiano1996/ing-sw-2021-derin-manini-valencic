package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;

public abstract class PhaseState {

    protected final Phase phase;

    /**
     * Class constructor.
     *
     * @param phase the phase which contains this state
     */
    protected PhaseState(Phase phase) {
        this.phase = phase;
    }

    /**
     * Abstract method of execute.
     *
     * @param message the message that is executed
     */
    public void execute(SessionMessage message) {
    }
}
