package it.polimi.ingsw.psp26.controller.turns;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.turns.states.BenefitsState;
import it.polimi.ingsw.psp26.controller.turns.states.TurnState;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;

public class Turn {

    private final MatchController matchController;
    private final int turnNumber;
    private final Player turnPlayer;

    private TurnState turnState;
    private Phase phase;

    public Turn(MatchController matchController, Player turnPlayer, int turnNumber) {
        this.matchController = matchController;
        this.turnPlayer = turnPlayer;
        this.turnNumber = turnNumber;

        // step 1: check first turn benefits // it can be performed every time
        // step 2: leader action (-> check vatican report -> check endgame)
        // step 3: normal action (-> check vatican report -> check endgame)
        // step 4: leader action (-> check vatican report -> check endgame)
        // step 5: next turn

        // starting from:
        // step 1: check first turn benefits // it can be performed every time
        turnState = new BenefitsState(this);

        phase = Phase.LEADER_ACTION_FIRST;
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

    public Match getMatch() {
        return matchController.getMatch();
    }

    private Player getNextPlayer() {
        int playerIndex = getMatch().getPlayers().indexOf(turnPlayer);
        return getMatch().getPlayers().get((playerIndex + 1) % getMatch().getPlayers().size());
    }

    private Turn createTurn(Player player) {
        int nextTurnNUmber = turnNumber + 1;
        return (getMatch().isMultiPlayerMode()) ?
                new Turn(matchController, player, nextTurnNUmber) :
                new SinglePlayerTurn(matchController, player, nextTurnNUmber);
    }

    public void initializeNextTurn() {
        matchController.updateCurrentTurn(createTurn(getNextPlayer()));
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}
