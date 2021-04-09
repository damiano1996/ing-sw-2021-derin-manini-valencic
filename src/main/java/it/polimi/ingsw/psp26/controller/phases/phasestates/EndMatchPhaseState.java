package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.model.Player;

import java.util.HashMap;

public class EndMatchPhaseState extends PhaseState {
    public EndMatchPhaseState(Phase phase) {
        super(phase);
    }

    private HashMap<Player, Integer> computePlayersPoints() {
        // to be implemented
        return null;
    }
}
