package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.Phase;

public abstract class PhaseState {

    protected final Phase phase;

    protected PhaseState(Phase phase) {
        this.phase = phase;
    }

    public void execute(Message message) {
    }
}
