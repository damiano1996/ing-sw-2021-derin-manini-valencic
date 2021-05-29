package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.RecoveringMatchPhaseState;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.network.server.VirtualView;


public class MatchController extends Observable<SessionMessage> implements Observer<SessionMessage> {

    private final VirtualView virtualView;
    private final Phase phase;
    private Match match;

    private boolean isWaitingForPlayers;
    private int maxNumberOfPlayers;

    private boolean recoveryMode;

    public MatchController(VirtualView virtualView, int matchId) {
        super();
        addObserver(virtualView);
        this.virtualView = virtualView;

        System.out.println("MatchController - New match controller has been created.");
        this.isWaitingForPlayers = true;

        initializeMatch(matchId);
        phase = new Phase(this);
        recoveryMode = false;
    }

    public MatchController(VirtualView virtualView, Match match, int turnPlayerIndex, int turnNumber) {
        super();
        addObserver(virtualView);
        this.virtualView = virtualView;

        this.match = match;
        phase = new Phase(this);

        // Initializing the playing phase that must be recovered when all clients will be ready to resume the match.
        PlayingPhaseState playingPhaseState = new PlayingPhaseState(phase, false);
        playingPhaseState.getCurrentTurn().setTurnPlayer(match.getPlayers().get(turnPlayerIndex));
        playingPhaseState.getCurrentTurn().setTurnNumber(turnNumber);

        phase.changeState(new RecoveringMatchPhaseState(phase, playingPhaseState));

        maxNumberOfPlayers = match.getPlayers().size();
        recoveryMode = true;
        System.out.println("MatchController - MatchController has been restored successfully!");
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

    public boolean isRecoveryMode() {
        return recoveryMode;
    }

    public void setMatchCompletelyRecovered() {
        this.recoveryMode = false;
    }
}
