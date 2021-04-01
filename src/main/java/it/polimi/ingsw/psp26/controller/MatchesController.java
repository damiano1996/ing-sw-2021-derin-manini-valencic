package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.controller.endgamecheckers.EndMatchChecker;
import it.polimi.ingsw.psp26.controller.endgamecheckers.MultiPlayerEndMatchChecker;
import it.polimi.ingsw.psp26.controller.turns.SinglePlayerTurn;
import it.polimi.ingsw.psp26.controller.turns.Turn;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class MatchesController {

    private final Match match;

    private Turn currentTurn;
    private EndMatchChecker endMatchChecker;

    public MatchesController(Match match) {
        this.match = match;

        initializeEndMatchChecker();
    }

    public void initializeMatch(int id, List<Player> players) {
        Collections.shuffle(players);
        Match NewMatch = new Match(id, players);
    }

    private void initializeTurn() {
        //it could be called initializeGame
        Player firstPlayer = match.getPlayers().get(0);
        firstPlayer.giveInkwell();
        if (match.isMultiPlayerMode()) DistributeResource();
        for (Player player : match.getPlayers()) {
            ChooseLeaders();
            // player.setLeaderCards(); Da mettere i Leader scelti nel ChooseLeaders
        }
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
        return match.getPlayers().get((playerIndex + 1) % match.getPlayers().size());
    }

    private void DistributeResource() {
        // to be implemented
        //It should give to each player the resource they want + faith point (established by rule table)
    }

    private void ChooseLeaders() {
        //to be implemented
        //Display first 4 cards of the deck and return List<2Leader> and discard the rest of them
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
