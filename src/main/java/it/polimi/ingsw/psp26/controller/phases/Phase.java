package it.polimi.ingsw.psp26.controller.phases;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.phasestates.InitializationPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PhaseState;

/**
 * Class that models the phase.
 */
public class Phase {

    private final MatchController matchController;
    private PhaseState phaseState;

    /**
     * Class constructor.
     *
     * @param matchController the match controller that is set
     */

    public Phase(MatchController matchController) {
        this.matchController = matchController;

        // step 1: waiting for match setup
        // step 2: play game
        // step 3: end game

        phaseState = new InitializationPhaseState(this);
    }

    /**
     * Method that calls the phase state execution of message.
     *
     * @param message the message that is passed on
     */

    public void execute(SessionMessage message) {
        phaseState.execute(message);
    }

    /**
     * Setter of the phase state.
     *
     * @param newPhaseState the phase state that is set
     */
    public void changeState(PhaseState newPhaseState) {
        phaseState = newPhaseState;
    }

    /**
     * Getter of the phase state.
     *
     * @return the phase state
     */
    public PhaseState getPhaseState() {
        return phaseState;
    }

    /**
     * Getter of the match controller.
     *
     * @return the match controller
     */

    public MatchController getMatchController() {
        return matchController;
    }
}
