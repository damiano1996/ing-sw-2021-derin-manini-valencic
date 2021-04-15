package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.Observer;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.network.server.VirtualView;


public class MatchController implements Observer<Message> {

    private final VirtualView virtualView;
    private final Phase phase;
    private Match match;

    private boolean isWaitingForPlayers;

    public MatchController(VirtualView virtualView, int matchId) {
        this.virtualView = virtualView;
        this.isWaitingForPlayers = true;

        initializeMatch(matchId);
        phase = new Phase(this);
    }

    @Override
    public void update(Message message) {
        phase.execute(message);
    }

    private void initializeMatch(int id) {
        match = new Match(virtualView, id);
    }

    public Match getMatch() {
        return match;
    }

    public VirtualView getVirtualView() {
        return virtualView;
    }

    public boolean isWaitingForPlayers() {
        return isWaitingForPlayers;
    }

    public void stopWaitingForPlayers() {
        isWaitingForPlayers = false;
    }
}
