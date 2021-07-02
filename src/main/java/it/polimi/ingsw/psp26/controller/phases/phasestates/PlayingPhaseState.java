package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.NotificationUpdateMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.SpecialToken;
import it.polimi.ingsw.psp26.network.server.memory.GameSaver;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.getNextPlayer;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendMessageToAllPlayerExceptOne;

/**
 * Class used as the container and manager of all sub-phase related to the game. It contains methods to manage the turns.
 * It is called after the initialization phase state and calls the end match phase state when the game ends.
 */
public class PlayingPhaseState extends PhaseState {

    private Turn currentTurn;
    private boolean lastTurn;

    /**
     * Constructor of the class.
     *
     * @param phase which this state is in
     */
    public PlayingPhaseState(Phase phase) {
        super(phase);

        initializeFirstTurn(true, 0);
    }

    /**
     * Constructor of the class.
     *
     * @param phase           which this state is in
     * @param shuffle         if true it shuffles the player in the match, otherwise it does not
     * @param firstTurnNumber The turn number that is set for the first turn
     */
    public PlayingPhaseState(Phase phase, boolean shuffle, int firstTurnNumber) {
        super(phase);

        initializeFirstTurn(shuffle, firstTurnNumber);
    }

    /**
     * Method that calls the play method of turn with message as input.
     *
     * @param message the message that is forwarded
     */
    @Override
    public void execute(SessionMessage message) {
        currentTurn.play(message);
    }

    /**
     * Getter of turn
     *
     * @return the current turn
     */
    public Turn getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Method that shuffle the player, creates the first turn and saves the backup of the match.
     *
     * @param shuffle         if true it shuffles the player in the match, otherwise it does not
     * @param firstTurnNumber The turn number that is set for the first turn
     */
    private void initializeFirstTurn(boolean shuffle, int firstTurnNumber) {
        if (shuffle) phase.getMatchController().getMatch().shufflePlayers();

        currentTurn = new Turn(
                this,
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                firstTurnNumber
        );

        // Saving match with initialization data. Since we are saving before playing the turn,
        // we have to set current player and current turn number.
        // Different condition w.r.t. when we save at the end of the turn!
        GameSaver.getInstance().backupMatch(
                currentTurn.getMatchController().getMatch(),
                currentTurn.getMatchController().getMatch().getPlayers().indexOf(currentTurn.getTurnPlayer()),
                currentTurn.getTurnNumber()
        );

        lastTurn = false;
    }

    /**
     * Method that notify the first player that it is their turn, and starts it.
     */
    public void playFirstTurn() {
        try {
            sendNotificationMessageNewTurn();
            currentTurn.play(
                    new SessionMessage(currentTurn.getTurnPlayer().getSessionToken(), MessageType.GENERAL_MESSAGE)
            );
        } catch (InvalidPayloadException ignored) {
        }
    }

    /**
     * Method that update the turn number, saves the turn information in the backup and creates and starts the turn for
     * the following player.
     */
    public void updateCurrentTurn() {
        int nextTurnNumber = currentTurn.getTurnNumber() + 1;

        GameSaver.getInstance().backupMatch(
                currentTurn.getMatchController().getMatch(),
                currentTurn.getMatchController().getMatch().getPlayers().indexOf(getNextPlayer(currentTurn)),
                nextTurnNumber
        );

        currentTurn = new Turn(this,
                phase.getMatchController(),
                getNextPlayer(currentTurn),
                nextTurnNumber);
        try {
            sendNotificationMessageNewTurn();
            currentTurn.play(new SessionMessage(currentTurn.getTurnPlayer().getSessionToken(), MessageType.GENERAL_MESSAGE));
        } catch (InvalidPayloadException ignored) {
        }
    }

    /**
     * Method to change state to the end match one.
     *
     * @param message that is forwarded to the end match phase state
     */
    public void goToEndMatchPhaseState(SessionMessage message) {
        // next state is...
        phase.changeState(new EndMatchPhaseState(phase));
        phase.execute(message);
    }

    /**
     * Getter of the last turn.
     *
     * @return It returns true if it is the last turn, false otherwise
     */
    public boolean isLastTurn() {
        return lastTurn;
    }

    /**
     * Setter of the last turn.
     */
    public void setLastTurn() {
        lastTurn = true;
    }

    /**
     * Method that notify the player of the current turn that the turn started and also notify all other player the
     * same information both as a message and in the notification stack.
     */
    public void sendNotificationMessageNewTurn() {
        try {
            String messageToTurnPlayer = "It's your turn!";
            String messageToOpponents = "Turn of " + currentTurn.getTurnPlayer().getNickname();
            // Same message for all in the notification stack
            phase.getMatchController().notifyObservers(
                    new NotificationUpdateMessage(SpecialToken.BROADCAST.getToken(), messageToOpponents)
            );
            // Only opponents receive messageToOpponents as general message
            sendMessageToAllPlayerExceptOne(currentTurn, currentTurn.getTurnPlayer(),
                    new Message(MessageType.GENERAL_MESSAGE, messageToOpponents)
            );
            sendMessageToAllPlayerExceptOne(currentTurn, currentTurn.getTurnPlayer(),
                    new Message(MessageType.OPPONENT_TURN)
            );
            // Turn player receives messageToTurnPlayer
            phase.getMatchController().notifyObservers(
                    new SessionMessage(
                            currentTurn.getTurnPlayer().getSessionToken(),
                            MessageType.GENERAL_MESSAGE,
                            messageToTurnPlayer)
            );
        } catch (InvalidPayloadException e) {
            e.printStackTrace();
        }
    }

}
