package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.application.Observer;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.network.server.VirtualView;


public class MatchController extends Observable<Message> implements Observer<Message> {

    private final VirtualView virtualView;
    private final Phase phase;
    private Match match;

    private boolean isWaitingForPlayers;
    private int maxNumberOfPlayers;

    public MatchController(VirtualView virtualView, int matchId) {
        super();
        addObserver(virtualView);
        this.virtualView = virtualView;

        System.out.println("New match controller has been created.");
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

    public int getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    public void setMaxNumberOfPlayers(int maxNumberOfPlayers) {
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        System.out.println("Number of players: " + this.maxNumberOfPlayers);
    }
}
