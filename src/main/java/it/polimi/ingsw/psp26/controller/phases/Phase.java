package it.polimi.ingsw.psp26.controller.phases;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.phasestates.InitializationPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PhaseState;

public class Phase {

    private final MatchController matchController;
    private PhaseState phaseState;

    public Phase(MatchController matchController) {
        this.matchController = matchController;

        // step 1: waiting for match setup
        // step 2: play game
        // step 3: end game

        phaseState = new InitializationPhaseState(this);
    }

    public void execute(Message message) {
        phaseState.execute(message);
    }

    public void changeState(PhaseState newPhaseState) {
        phaseState = newPhaseState;
    }

    public MatchController getMatchController() {
        return matchController;
    }
}
