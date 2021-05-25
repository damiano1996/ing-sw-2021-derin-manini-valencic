package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.NotificationUpdateMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.SpecialToken;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendMessageToAllPlayerExceptOne;

public class PlayingPhaseState extends PhaseState {

    private Turn currentTurn;
    private boolean lastTurn;

    public PlayingPhaseState(Phase phase) {
        super(phase);

        initializeFirstTurn();
    }

    @Override
    public void execute(SessionMessage message) {
        currentTurn.play(message);
    }

    private void initializeFirstTurn() {
        phase.getMatchController().getMatch().shufflePlayers();
        currentTurn = new Turn(
                this,
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                0
        );
        lastTurn = false;
    }

    public void playFirstTurn() {
        try {
            sendNotificationMessageNewTurn();
            currentTurn.play(
                    new SessionMessage(currentTurn.getTurnPlayer().getSessionToken(), MessageType.GENERAL_MESSAGE)
            );
        } catch (InvalidPayloadException ignored) {
        }
    }

    public void updateCurrentTurn() {
        int nextTurnNUmber = currentTurn.getTurnNumber() + 1;
        currentTurn = new Turn(this,
                phase.getMatchController(),
                getNextPlayer(),
                nextTurnNUmber);
        try {
            sendNotificationMessageNewTurn();
            currentTurn.play(new SessionMessage(currentTurn.getTurnPlayer().getSessionToken(), MessageType.GENERAL_MESSAGE));
        } catch (InvalidPayloadException ignored) {
        }
    }

    private Player getNextPlayer() {
        int currentPlayerIndex = phase.getMatchController().getMatch().getPlayers().indexOf(currentTurn.getTurnPlayer());
        return phase.getMatchController().getMatch().getPlayers().get((currentPlayerIndex + 1) % phase.getMatchController().getMatch().getPlayers().size());
    }

    public void goToEndMatchPhaseState(SessionMessage message) {
        // next state is...
        phase.changeState(new EndMatchPhaseState(phase));
        phase.execute(message);
    }

    public boolean isLastTurn() {
        return lastTurn;
    }

    public void setLastTurn() {
        lastTurn = true;
    }

    private void sendNotificationMessageNewTurn() {
        try {
            String messagePayload = "Turn of " + currentTurn.getTurnPlayer().getNickname();
            phase.getMatchController().notifyObservers(
                    new NotificationUpdateMessage(
                            SpecialToken.BROADCAST.getToken(),
                            messagePayload
                    )
            );
            phase.getMatchController().notifyObservers(
                    new SessionMessage(
                            SpecialToken.BROADCAST.getToken(),
                            MessageType.GENERAL_MESSAGE,
                            messagePayload
                    )
            );
            sendMessageToAllPlayerExceptOne(
                    currentTurn, currentTurn.getTurnPlayer(), new Message(MessageType.OPPONENT_TURN));
        } catch (InvalidPayloadException e) {
            e.printStackTrace();
        }
    }

}
