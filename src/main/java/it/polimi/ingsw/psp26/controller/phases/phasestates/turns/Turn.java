package it.polimi.ingsw.psp26.controller.phases.phasestates.turns;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.BenefitsTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;

public class Turn {

    private final PlayingPhaseState playingPhaseState;
    private final Match match;
    private final int turnNumber;
    private final Player turnPlayer;

    private TurnState turnState;
    private TurnPhase turnPhase;

    public Turn(PlayingPhaseState playingPhaseState, Match match, Player turnPlayer, int turnNumber) {
        this.playingPhaseState = playingPhaseState;
        this.match = match;
        this.turnPlayer = turnPlayer;
        this.turnNumber = turnNumber;

        // step 1: check first turn benefits // it can be performed every time
        // step 2: leader action (-> check vatican report -> check endgame)
        // step 3: normal action (-> check vatican report -> check endgame)
        // step 4: leader action (-> check vatican report -> check endgame)
        // step 5: next turn

        // starting from:
        // step 1: check first turn benefits // it can be performed every time
        turnState = new BenefitsTurnState(this);
    }

    public void play(Message message) {
        turnState.play(message);
    }

    public void changeState(TurnState newTurnState) {
        turnState = newTurnState;
    }

    public Player getTurnPlayer() {
        return turnPlayer;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public void setTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }

    public Match getMatch() {
        return match;
    }

    public PlayingPhaseState getPlayingPhaseState() {
        return playingPhaseState;
    }
}
