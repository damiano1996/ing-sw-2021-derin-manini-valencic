package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.RecoveringMatchPhaseState;
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

        System.out.println("MatchController - new match controller has been created.");
        this.isWaitingForPlayers = true;

        initializeMatch(matchId);
        phase = new Phase(this);
    }

    @Override
    public void update(SessionMessage message) {
        if (!message.getMessageType().equals(MessageType.HEARTBEAT)) {

            if (message.getMessageType().equals(MessageType.DEATH))
                phase.changeState(new RecoveringMatchPhaseState(phase, phase.getPhaseState()));

            phase.execute(message);
        }
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
        System.out.println("MatchController - number of players: " + this.maxNumberOfPlayers);
    }
}
