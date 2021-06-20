package it.polimi.ingsw.psp26.controller;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.application.observer.Observer;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.EndMatchPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.RecoveringMatchPhaseState;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.network.server.VirtualView;

/**
 * Class modelling the controller.
 * It extends the Observable abstract class (to be observed by the virtual view and by the model) and
 * it implements the Observer interface (to observe the virtual view).
 * <p>
 * It handles the top level of the state design pattern of the controller, executing the phases.
 */
public class MatchController extends Observable<SessionMessage> implements Observer<SessionMessage> {

    private final VirtualView virtualView;
    private final Phase phase;
    private Match match;

    private boolean isWaitingForPlayers;
    private int maxNumberOfPlayers;

    private boolean recoveryMode;

    /**
     * Class constructor.
     * <p>
     * This constructor can be used in classical situations,
     * when a match should be instantiated for the first time (differently by the second constructor).
     * It adds to the observers the virtual view and initializes a match object.
     *
     * @param virtualView virtual view associated to this match
     * @param matchId     identifier of the match
     */
    public MatchController(VirtualView virtualView, int matchId) {
        super();
        addObserver(virtualView);
        this.virtualView = virtualView;

        this.isWaitingForPlayers = true;

        initializeMatch(matchId);
        phase = new Phase(this);
        recoveryMode = false;

        System.out.println("MatchController - New match controller has been created.");
    }

    /**
     * Class constructor.
     * <p>
     * This is a second version that takes more parameters as input.
     * This constructor can be used to restore a saved match,
     * indeed it will be set in a recovery mode state.
     *
     * @param virtualView     virtual view associated to this match
     * @param match           match object (typically the restored one)
     * @param turnPlayerIndex the index of the player that has to play
     * @param turnNumber      the number of the turn of the player that has to play
     */
    public MatchController(VirtualView virtualView, Match match, int turnPlayerIndex, int turnNumber) {
        super();
        addObserver(virtualView);
        this.virtualView = virtualView;

        this.match = match;
        phase = new Phase(this);

        // Initializing the playing phase that must be recovered when all clients will be ready to resume the match.
        PlayingPhaseState playingPhaseState = new PlayingPhaseState(phase, false, turnNumber);
        playingPhaseState.getCurrentTurn().setTurnPlayer(match.getPlayers().get(turnPlayerIndex));

        phase.changeState(new RecoveringMatchPhaseState(phase, playingPhaseState));

        maxNumberOfPlayers = match.getPlayers().size();
        recoveryMode = true;
        System.out.println("MatchController - MatchController has been restored successfully!");
    }


    /**
     * Method to update the controller.
     * It executes the phase, forwarding the message.
     *
     * @param message session message
     */
    @Override
    public synchronized void update(SessionMessage message) {

        if (message.getMessageType() == MessageType.INDEFINITE_SUSPENSION ||
                message.getMessageType() == MessageType.HEARTBEAT_INDEFINITE_SUSPENSION) {
            phase.changeState(new EndMatchPhaseState(phase));
        }
        phase.execute(message);

    }

    /**
     * Method to initialize the match.
     *
     * @param id identifier of the match
     */
    private synchronized void initializeMatch(int id) {
        match = new Match(virtualView, id);
    }

    /**
     * Getter of the match.
     *
     * @return match object
     */
    public synchronized Match getMatch() {
        return match;
    }

    /**
     * Getter of the virtual view.
     *
     * @return virtual view object
     */
    public synchronized VirtualView getVirtualView() {
        return virtualView;
    }

    /**
     * Method to check if the controller is still waiting for incoming players.
     *
     * @return true if the match is waiting, false if not waiting
     */
    public synchronized boolean isWaitingForPlayers() {
        return isWaitingForPlayers;
    }

    /**
     * Method to stop to wait for players.
     */
    public synchronized void stopWaitingForPlayers() {
        isWaitingForPlayers = false;
    }

    /**
     * Getter of the maximum number of player for the current match.
     *
     * @return the maximum number of players
     */
    public synchronized int getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    /**
     * Setter of the maximum number of players.
     *
     * @param maxNumberOfPlayers maximum number of players
     */
    public synchronized void setMaxNumberOfPlayers(int maxNumberOfPlayers) {
        this.maxNumberOfPlayers = maxNumberOfPlayers;
        System.out.println("MatchController - Max number of players: " + this.maxNumberOfPlayers);
    }

    /**
     * Method to check if the controller is in recovery mode.
     *
     * @return true if it is in recovery mode, false otherwise
     */
    public boolean isRecoveryMode() {
        return recoveryMode;
    }

    /**
     * Method to update the recovery mode state. It changes the state to be no more in recovery mode.
     */
    public void setMatchCompletelyRecovered() {
        this.recoveryMode = false;
    }
}
