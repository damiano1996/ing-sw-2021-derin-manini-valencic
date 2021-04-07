package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.Observer;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.controller.endgamecheckers.EndMatchChecker;
import it.polimi.ingsw.psp26.controller.endgamecheckers.MultiPlayerEndMatchChecker;
import it.polimi.ingsw.psp26.controller.turns.SinglePlayerTurn;
import it.polimi.ingsw.psp26.controller.turns.Turn;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.Collections;
import java.util.HashMap;


public class MatchController implements Observer<Message> {

    private final VirtualView virtualView;
    private Match match;
    private int maxNumberOfPlayers;

    private Turn currentTurn;
    private EndMatchChecker endMatchChecker;

    public MatchController(VirtualView virtualView, int matchId) {
        this.virtualView = virtualView;

        initializeMatch(matchId);
        initializeEndMatchChecker();
    }

    @Override
    public void update(Message message) {
        MessageType messageType = message.getMessageType();
        switch (messageType) {
            case ADD_PLAYER:
                addPlayer(message);
                break;
            case MAX_NUMBER_OF_PLAYERS:
                setMaxNumberOfPlayers(message);
                break;
            case NORMAL_ACTION_MARKET_RESOURCE:
            case NORMAL_ACTION_ACTIVATE_PRODUCTION:
            case NORMAL_ACTION_BUY_CARD:
            case LEADER_ACTION_PLAY:
            case LEADER_ACTION_DISCARD:
                playCurrentTurn(message);
                break;
        }
    }

    private void initializeMatch(int id) {
        match = new Match(virtualView, id);
    }

    private void addPlayer(Message message) {
        String nickname = (String) message.getPayload().get("nickname");
        String sessionToken = (String) message.getPayload().get("sessionToken");
        match.addPlayer(new Player(virtualView, nickname, sessionToken));

        if (match.getPlayers().size() == maxNumberOfPlayers)
            initializeTurn();
    }

    private void setMaxNumberOfPlayers(Message message) {
        maxNumberOfPlayers = (int) message.getPayload().get("maxNumberOfPlayers");
        if (maxNumberOfPlayers > 4) maxNumberOfPlayers = 4;
    }

    private void initializeTurn() {
        Collections.shuffle(match.getPlayers());
        currentTurn = createTurn(match.getPlayers().get(0));
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

    private void playCurrentTurn(Message message) {
        // to be implemented
        // currentTurn.play();
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
