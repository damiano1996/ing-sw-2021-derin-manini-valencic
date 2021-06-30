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
    private int turnNumber;
    private Player turnPlayer;

    private TurnState turnState;
    private TurnPhase turnPhase;

    /**
     * Class constructor.
     *
     * @param playingPhaseState The playing phase state in which this turn is created
     * @param matchController The associated match controller
     * @param turnPlayer the player which turn is it
     * @param turnNumber the new turn number
     */
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

    /**
     * Method that checks if the turn is to be skipped, or to be played then it notifies the other players.
     * <p>
     * The method checks if the player is disconnected by looking if the message type is DEATH, if they are, it notifies
     * all players about it and  if it is multiplayer mode, it updates the turn to the one of the following player. If
     * the player is not disconnected it plays their turn.
     *
     * @param message the session message
     */
    public void play(SessionMessage message) {
        if (message.getSessionToken().equals(turnPlayer.getSessionToken())) {

            if (message.getMessageType().equals(MessageType.DEATH)) {
                System.out.println("Turn - Receiving DEATH message, we should notify the other clients.");
                try {
                    getMatchController().notifyObservers(
                            new NotificationUpdateMessage(
                                    SpecialToken.BROADCAST.getToken(),
                                    turnPlayer.getNickname() + " lost the connection. He skips the turn."
                            )
                    );

                    // updating turn skipping for current player
                    System.out.println("Turn - Updating current turn, since player is disconnected!");
                    if (matchController.getMatch().isMultiPlayerMode()) {
                        playingPhaseState.updateCurrentTurn();
                        // Then playing with the new player:
                        turnState.play(new SessionMessage(turnPlayer.getSessionToken(), MessageType.GENERAL_MESSAGE));
                    }

                } catch (InvalidPayloadException ignored) {
                }

            } else {
                turnState.play(message);
            }
        } else {

            // Communicating that the turn is of one opponent
            try {
                matchController.notifyObservers(new SessionMessage(message.getSessionToken(), MessageType.OPPONENT_TURN));
            } catch (InvalidPayloadException ignored) {
            }
        }
    }

    /**
     * Setter of the turn state.
     *
     * @param newTurnState the turn state in which this turn is going to be in
     */
    public void changeState(TurnState newTurnState) {
        turnState = newTurnState;
    }

    /**
     * Getter of the turn state.
     *
     * @return the turn state in which this turn is in
     */
    public TurnState getTurnState() {
        return turnState;
    }

    /**
     * Getter of the turn player.
     *
     * @return the player whose turn is it
     */
    public Player getTurnPlayer() {
        return turnPlayer;
    }

    /**
     * Setter of the turn player.
     *
     * @param turnPlayer the player whose turn is going to be
     */
    public void setTurnPlayer(Player turnPlayer) {
        this.turnPlayer = turnPlayer;
    }

    /**
     * Getter of the turn number.
     *
     * @return the number of the current turn
     */
    public int getTurnNumber() {
        return turnNumber;
    }

    /**
     *  Getter of the turn phase.
     *
     * @return the turn phase in which this turn is in
     */
    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    /**
     * Setter of the turn phase.
     *
     * @param turnPhase the turn phase in which this turn is going to be in
     */
    public void setTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }

    /**
     * Getter of the current phase state.
     *
     * @return The current phase state that is playing phase state
     */
    public PlayingPhaseState getPlayingPhaseState() {
        return playingPhaseState;
    }

    /**
     * Getter of the match controller.
     *
     * @return the associated match controller
     */
    public MatchController getMatchController() {
        return matchController;
    }

    /**
     * Method to send a notification message, broadcast to all players.
     *
     * @param messageText the message that is sent to all players
     */
    public void notifyAllPlayers(String messageText) {
        try {
            getMatchController().notifyObservers(
                    new NotificationUpdateMessage(
                            SpecialToken.BROADCAST.getToken(),
                            messageText)
            );
        } catch (InvalidPayloadException e) {
            e.printStackTrace();
        }
    }
}
