package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;

public class RecoveringMatchPhaseState extends PhaseState {

    private final PhaseState actualPhaseState;

    public RecoveringMatchPhaseState(Phase phase, PhaseState actualPhaseState) {
        super(phase);

        this.actualPhaseState = actualPhaseState;
    }

    @Override
    public void execute(SessionMessage message) {

    }
}
