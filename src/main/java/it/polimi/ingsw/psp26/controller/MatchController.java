package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.network.server.VirtualView;


public class MatchController extends Observable<SessionMessage> implements Observer<SessionMessage> {

    private final VirtualView virtualView;
    private final Phase phase;
    private Match match;

    private boolean isWaitingForPlayers;
    private int maxNumberOfPlayers;

    public MatchController(VirtualView virtualView, int matchId) {
        super();
        addObserver(virtualView);
        this.virtualView = virtualView;

        System.out.println("MatchController - New match controller has been created.");
        this.isWaitingForPlayers = true;

        initializeMatch(matchId);
        phase = new Phase(this);
    }
    
    public MatchController(VirtualView virtualView, Match match, int turnPlayerIndex, int turnNumber) {
        super();
        addObserver(virtualView);
        this.virtualView = virtualView;

        this.match = match;
        phase = new Phase(this);
        
        PlayingPhaseState playingPhaseState = new PlayingPhaseState(phase, false);
        playingPhaseState.getCurrentTurn().setTurnPlayer(match.getPlayers().get(turnPlayerIndex));
        playingPhaseState.getCurrentTurn().setTurnNumber(turnNumber);
        phase.changeState(playingPhaseState);

        maxNumberOfPlayers = match.getPlayers().size();
        System.out.println("MatchController - MatchController has been restored.");
    }


    @Override
    public synchronized void update(SessionMessage message) {
        phase.execute(message);
    }

    private synchronized void initializeMatch(int id) {
        match = new Match(virtualView, id);
    }

    public synchronized Match getMatch() {
        return match;
    }

    public synchronized VirtualView getVirtualView() {
        return virtualView;
    }

    public synchronized boolean isWaitingForPlayers() {
        return isWaitingForPlayers;
    }

    public synchronized void stopWaitingForPlayers() {
        isWaitingForPlayers = false;
    }

    public synchronized int getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    public synchronized void setMaxNumberOfPlayers(int maxNumberOfPlayers) {
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        System.out.println("MatchController - Max number of players: " + this.maxNumberOfPlayers);
    }
    
}
