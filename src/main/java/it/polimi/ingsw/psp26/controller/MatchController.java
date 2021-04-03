package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.Observer;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.endgamecheckers.EndMatchChecker;
import it.polimi.ingsw.psp26.controller.endgamecheckers.MultiPlayerEndMatchChecker;
import it.polimi.ingsw.psp26.controller.turns.SinglePlayerTurn;
import it.polimi.ingsw.psp26.controller.turns.Turn;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.HashMap;


public class MatchController implements Observer<Message> {

    private final VirtualView virtualView;
    private Match match;

    private Turn currentTurn;
    private EndMatchChecker endMatchChecker;

    public MatchController(VirtualView virtualView, int matchId) {
        this.virtualView = virtualView;

        initializeMatch(matchId);
        initializeEndMatchChecker();
    }

    @Override
    public void update(Message message) {

    }

    private void initializeMatch(int id) {
        match = new Match(virtualView, id);
    }

    private void addPlayer(String nickname, String sessionToken) {
        match.addPlayer(new Player(virtualView, nickname, sessionToken));
    }

    private void initializeTurn() {
        //it could be called initializeGame
        Player firstPlayer = match.getPlayers().get(0);
        firstPlayer.giveInkwell();
        currentTurn = createTurn(firstPlayer);
    }

    private void initializeEndMatchChecker() {
        endMatchChecker = (match.isMultiPlayerMode()) ?
                new MultiPlayerEndMatchChecker() :
                new EndMatchChecker();
    }

    private Turn createTurn(Player player) {
        int turnNumber = (currentTurn == null) ? 0 : currentTurn.getTurnNumber() + 1;
        return (match.isMultiPlayerMode()) ?
                new Turn(match, player, turnNumber) :
                new SinglePlayerTurn(match, player, turnNumber);
    }

    private EndMatchChecker getEndMatchChecker() {
        return endMatchChecker;
    }

    private void updateCurrentTurn() {
        currentTurn = createTurn(getNextPlayer());
    }

    private void playCurrentTurn() {
        // to be implemented
    }

    private Player getNextPlayer() {
        Player currentPlayer = currentTurn.getTurnPlayer();
        int playerIndex = match.getPlayers().indexOf(currentPlayer);
        return match.getPlayers().get((playerIndex + 1) % match.getPlayers().size());
    }

    private void endMatch() {
    }

    private HashMap<Player, Integer> computePlayersPoints() {
        // to be implemented
        return null;
    }

    private void checkEndMatch() {
    }
}
