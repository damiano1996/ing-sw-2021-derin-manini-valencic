package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.controller.endgamecheckers.EndMatchChecker;
import it.polimi.ingsw.psp26.controller.endgamecheckers.MultiPlayerEndMatchChecker;
import it.polimi.ingsw.psp26.controller.turns.SinglePlayerTurn;
import it.polimi.ingsw.psp26.controller.turns.Turn;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;

import java.util.HashMap;

public class MatchesController {

    private final Match match;

    private Turn currentTurn;
    private EndMatchChecker endMatchChecker;

    public MatchesController(Match match) {
        this.match = match;

        initializeEndMatchChecker();
    }

    public void initializeMatch() {
        // to be implemented
    }

    private void initializeTurn() {
        Player firstPlayer = match.getPlayers().get(0);
        currentTurn = createTurn(firstPlayer);
    }

    private void initializeEndMatchChecker() {
        if (match.isMultiPlayerMode())
            endMatchChecker = new MultiPlayerEndMatchChecker();
        else
            endMatchChecker = new EndMatchChecker();
    }

    private Turn createTurn(Player player) {
        return (match.isMultiPlayerMode()) ?
                new Turn(match, player) :
                new SinglePlayerTurn(match, player);
    }

    public Match getMatch() {
        return match;
    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public EndMatchChecker getEndMatchChecker() {
        return endMatchChecker;
    }

    public void updateCurrentTurn() {
        currentTurn = createTurn(getNextPlayer());
    }

    public void playCurrentTurn() {
        // to be implemented
    }

    public Player getNextPlayer() {
        Player currentPlayer = currentTurn.getTurnPlayer();
        int playerIndex = match.getPlayers().indexOf(currentPlayer);
        return match.getPlayers().get(playerIndex + 1);
    }

    public void endMatch() {
    }

    public HashMap<Player, Integer> computePlayersPoints() {
        // to be implemented
        return null;
    }

    public void checkEndMatch() {
    }
}
