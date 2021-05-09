package it.polimi.ingsw.psp26.controller.phases.phasestates.turns;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.MatchController;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions.ChooseLeaderActionTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.ChooseNormalActionTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.singleplayer.LorenzoMagnificoTurnState;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;

public class TurnUtils {

    public static void goToNextStateAfterLeaderAction(Turn turn, SessionMessage message) {
        System.out.println("goToNextStateAfterLeaderAction - current turn phase: " + turn.getTurnPhase());

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
                } else {
                    // After the second leader action, go to Lorenzo action
                    turn.changeState(new LorenzoMagnificoTurnState(turn));
                    turn.play(message);
                }
                break;
            // TODO: case Lorenzo
        }
    }

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

    public synchronized static void sendSessionMessageToAllPlayers(MatchController matchController, Message message) throws InvalidPayloadException {
        for (Player player : matchController.getMatch().getPlayers()) {
            matchController.notifyObservers(
                    new SessionMessage(
                            player.getSessionToken(),
                            message.getMessageType(),
                            message.getArrayPayloads()
                    )
            );
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
