package it.polimi.ingsw.psp26.controller.phases.phasestates.turns;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions.ChooseLeaderActionTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.ChooseNormalActionTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.singleplayer.LorenzoMagnificoTurnState;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;

/**
 * Class that works as crossroad of turn states. It changes the turn state that ended to the following one.
 */
public class TurnUtils {

    /**
     * Method used to define the next state after a leader action.
     * It performs checks on the end of the match.
     * The game can stop if the end match phase has been activated and all the players have played their last turn.
     * <p>
     * In other cases it changes the state of the automaton according to the rules.
     * From leader action goes to normal action.
     * From normal action to the second leader action.
     * To the second leader action to the next turn.
     *
     * @param turn    turn object
     * @param message received message
     */
    public static void goToNextStateAfterLeaderAction(Turn turn, SessionMessage message) {
        System.out.println("goToNextStateAfterLeaderAction - current turn phase: " + turn.getTurnPhase());

        try {
            if (turn.getPlayingPhaseState().isLastTurn() && // true if end game has been activated
                    getNextPlayer(turn).hasInkwell() && // true if current player is the last of the table
                    turn.getTurnPhase().equals(TurnPhase.LEADER_ACTION_TO_END) // true if the last player has played the entire turn
            ) {

                turn.getPlayingPhaseState().goToEndMatchPhaseState(
                        new SessionMessage(turn.getTurnPlayer().getSessionToken(), MessageType.SEVENTH_CARD_DRAWN));

            } else {

                playNextState(turn, message);

            }

        } catch (InvalidPayloadException ignored) {
        }
    }

    /**
     * Method used to change the turn state after a leader action and play it.
     * It handles all the cases:
     * - from leader action to normal action;
     * - from normal action to leader action;
     * - from the second leader action to the next turn (single player or multi player cases).
     *
     * @param turn    turn object
     * @param message received message
     */
    private static void playNextState(Turn turn, SessionMessage message) {
        switch (turn.getTurnPhase()) {

            case RESOURCE_PLACER_TO_LEADER_ACTION:
                turn.changeState(new ChooseLeaderActionTurnState(turn, TurnPhase.LEADER_TO_NORMAL_ACTION));
                turn.play(message);
                break;

            case LEADER_TO_NORMAL_ACTION:
                // If first leader action has been played, go to normal action
                turn.changeState(new ChooseNormalActionTurnState(turn));
                turn.play(message);
                break;

            case NORMAL_TO_LEADER_ACTION:
                // After normal action, go to leader action
                turn.changeState(new ChooseLeaderActionTurnState(turn, TurnPhase.LEADER_ACTION_TO_END));
                turn.play(message);
                break;

            case LEADER_ACTION_TO_END:
                if (turn.getMatchController().getMatch().isMultiPlayerMode()) {
                    // After the second leader action, go to next player turn
                    turn.getPlayingPhaseState().updateCurrentTurn();
                    turn.notifyAllPlayers(turn.getTurnPlayer().getNickname() + " finished his turn.");

                } else {
                    // After the second leader action, go to Lorenzo action
                    turn.changeState(new LorenzoMagnificoTurnState(turn, TurnPhase.LORENZO_TO_END));
                    turn.play(message);

                }
                break;

            case LORENZO_TO_END:
                //After Lorenzo action, go to next turn
                turn.getPlayingPhaseState().updateCurrentTurn();
                break;
        }
    }

    /**
     * Method to get the next player that will play the turn.
     *
     * @param currentTurn turn object
     * @return next player that will play
     */
    public static Player getNextPlayer(Turn currentTurn) {
        int currentPlayerIndex = currentTurn.getMatchController().getMatch().getPlayers().indexOf(currentTurn.getTurnPlayer());
        return currentTurn.getMatchController().getMatch().getPlayers().get((currentPlayerIndex + 1) % currentTurn.getMatchController().getMatch().getPlayers().size());
    }

    /**
     * Method to send a message to all the players except one.
     *
     * @param turn          turn object
     * @param playerToAvoid player that mustn't receive the message
     * @param message       message to send
     */
    public static void sendMessageToAllPlayerExceptOne(Turn turn, Player playerToAvoid, Message message) {
        for (Player player : turn.getMatchController().getMatch().getPlayers()) {
            if (!player.getSessionToken().equals(playerToAvoid.getSessionToken())) {
                try {
                    turn.getMatchController().notifyObservers(
                            new SessionMessage(
                                    player.getSessionToken(),
                                    message.getMessageType(),
                                    message.getArrayPayloads()
                            )
                    );
                } catch (InvalidPayloadException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Method to send a choice normal action to the turn player.
     *
     * @param turn turn object
     */
    public static void sendChoiceNormalActionMessage(Turn turn) {
        try {

            turn.getMatchController().notifyObservers(
                    new MultipleChoicesMessage(
                            turn.getTurnPlayer().getSessionToken(),
                            CHOICE_NORMAL_ACTION,
                            "Choice normal action to perform:",
                            1, 1,
                            false,
                            ACTIVATE_PRODUCTION, MARKET_RESOURCE, BUY_CARD
                    )
            );
        } catch (InvalidPayloadException ignored) {
        }
    }

    /**
     * Method to send a error message to the player of the turn.
     *
     * @param turn         turn
     * @param errorMessage payload of the message
     */
    public static void sendErrorMessage(Turn turn, String errorMessage) {
        System.out.println("ERROR_MESSAGE: " + errorMessage);
        sendMessageToTurnPlayer(turn, ERROR_MESSAGE, errorMessage);
    }

    /**
     * Method to send a general message to the player of the turn.
     *
     * @param turn           turn
     * @param generalMessage payload of the message
     */
    public static void sendGeneralMessage(Turn turn, String generalMessage) {
        System.out.println("GENERAL_MESSAGE: " + generalMessage);
        sendMessageToTurnPlayer(turn, GENERAL_MESSAGE, generalMessage);
    }

    /**
     * Method to send a message to the player of the turn.
     *
     * @param turn        turn
     * @param messageType type of the message
     * @param message     payload of the message
     */
    private static void sendMessageToTurnPlayer(Turn turn, MessageType messageType, String message) {
        try {
            turn.getMatchController().notifyObservers(
                    new SessionMessage(
                            turn.getTurnPlayer().getSessionToken(),
                            messageType,
                            message
                    )
            );
        } catch (InvalidPayloadException ignored) {
        }
    }

}
