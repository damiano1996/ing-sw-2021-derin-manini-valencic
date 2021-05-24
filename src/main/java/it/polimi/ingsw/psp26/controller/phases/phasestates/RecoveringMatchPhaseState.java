package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.exceptions.PlayerDoesNotExistException;
import it.polimi.ingsw.psp26.network.SpecialToken;

public class RecoveringMatchPhaseState extends PhaseState {

    private final PhaseState actualPhaseState;

    public RecoveringMatchPhaseState(Phase phase, PhaseState actualPhaseState) {
        super(phase);

        this.actualPhaseState = actualPhaseState;
    }

    @Override
    public void execute(SessionMessage message) {

        try {

            if (message.getMessageType().equals(MessageType.DEATH)) {
                String disconnectedPlayerNickname = phase.getMatchController().getMatch().getPlayerBySessionToken((String) message.getPayload()).getNickname();
                String payloadMessage = disconnectedPlayerNickname + " has left the match, waiting few seconds for reconnection...";
                System.out.println("DisconnectionPhaseState - " + payloadMessage);
                phase.getMatchController().notifyObservers(new SessionMessage(SpecialToken.BROADCAST.getToken(), MessageType.START_WAITING, payloadMessage));
            }

        } catch (PlayerDoesNotExistException | EmptyPayloadException | InvalidPayloadException ignored) {
        }
    }
}
