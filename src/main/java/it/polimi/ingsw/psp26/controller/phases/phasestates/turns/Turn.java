package it.polimi.ingsw.psp26.controller.phases.phasestates.turns;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.NotificationUpdateMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.phasestates.PlayingPhaseState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.LeaderCardsAssignmentTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.SpecialToken;

public class Turn {

    private final PlayingPhaseState playingPhaseState;
    private final MatchController matchController;
    private final int turnNumber;
    private final Player turnPlayer;

    private TurnState turnState;
    private TurnPhase turnPhase;

    public Turn(PlayingPhaseState playingPhaseState, MatchController matchController, Player turnPlayer, int turnNumber) {
        this.playingPhaseState = playingPhaseState;
        this.matchController = matchController;
        this.turnPlayer = turnPlayer;
        this.turnNumber = turnNumber;

        // step 0: assign leaders
        // step 1: check first turn benefits // it can be performed every time
        // step 2: leader action (-> check vatican report -> check endgame)
        // step 3: normal action (-> check vatican report -> check endgame)
        // step 4: leader action (-> check vatican report -> check endgame)
        // step 5: next turn

        turnPhase = TurnPhase.RESOURCE_PLACER_TO_LEADER_ACTION;
        // starting from:
        turnState = new LeaderCardsAssignmentTurnState(this);
    }

    public void play(SessionMessage message) {
        if (message.getSessionToken().equals(turnPlayer.getSessionToken())) {

            if (message.getMessageType().equals(MessageType.DEATH)) {
                try {
                    getMatchController().notifyObservers(new NotificationUpdateMessage(SpecialToken.BROADCAST.getToken(), turnPlayer.getNickname() + " lost the connection. He skips the turn."));
                } catch (InvalidPayloadException e) {
                    e.printStackTrace();
                }
                // updating turn skipping for current player
                if (matchController.getMatch().isMultiPlayerMode()) playingPhaseState.updateCurrentTurn();

            } else {
                turnState.play(message);
            }
        }
    }

    public void changeState(TurnState newTurnState) {
        turnState = newTurnState;
    }

    public Player getTurnPlayer() {
        return turnPlayer;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public void setTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }

    public PlayingPhaseState getPlayingPhaseState() {
        return playingPhaseState;
    }

    public MatchController getMatchController() {
        return matchController;
    }
}
