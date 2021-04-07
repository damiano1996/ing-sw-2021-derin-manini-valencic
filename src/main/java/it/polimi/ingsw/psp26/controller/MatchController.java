package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.Observer;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
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

    public MatchController(VirtualView virtualView, int matchId) {
        this.virtualView = virtualView;

        initializeMatch(matchId);
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
            case NORMAL_ACTION:
            case LEADER_ACTION:
                playCurrentTurn(message);
                break;
        }
    }

    private void initializeMatch(int id) {
        match = new Match(virtualView, id);
    }

    private void addPlayer(Message message) {
        String nickname = (String) message.getPayload().get("nickname");
        String sessionToken = (String) message.getPayload().get("sessionToken"); // TODO: to be randomly generated
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
        currentTurn = new Turn(this, match.getPlayers().get(0), 0);
    }

    public void updateCurrentTurn(Turn newTurn) {
        currentTurn = newTurn;
    }

    private void playCurrentTurn(Message message) {
        currentTurn.play(message);
    }

    private void endMatch() {
    }

    private HashMap<Player, Integer> computePlayersPoints() {
        // to be implemented
        return null;
    }

    private void checkEndMatch() {
    }

    public Match getMatch() {
        return match;
    }
}
