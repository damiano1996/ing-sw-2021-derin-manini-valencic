package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.Player;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendSessionMessageToAllPlayers;

public class InitializationPhaseState extends PhaseState {

    public InitializationPhaseState(Phase phase) {
        super(phase);
    }

    @Override
    public void execute(SessionMessage message) {
        super.execute(message);

        if (message.getMessageType() == MessageType.ADD_PLAYER) {

            try {
                addPlayer(message);
            } catch (EmptyPayloadException e) {
                e.printStackTrace();
            }

            // next state is...
            if (phase.getMatchController().getMatch().getPlayers().size() == phase.getMatchController().getMaxNumberOfPlayers()) {
                // Communicating to match controller that we reached the maximum number of players.
                phase.getMatchController().stopWaitingForPlayers();

                System.out.println("Initialization phase - sending stop message");
                try {
                    sendSessionMessageToAllPlayers(phase.getMatchController(), new Message(MessageType.STOP_WAITING, "Stop waiting..."));
                } catch (InvalidPayloadException ignored) {
                }
                
                try {
                    sendSessionMessageToAllPlayers(phase.getMatchController(), new Message(MessageType.GENERAL_MESSAGE, "The match can begin!"));
                } catch (InvalidPayloadException e) {
                    e.printStackTrace();
                }
                
                // Updating the state. The match can begin!
                phase.changeState(new PlayingPhaseState(phase));
                phase.execute(message);
            }
        }
    }

    private void addPlayer(SessionMessage message) throws EmptyPayloadException {
        String nickname = (String) message.getPayload();
        String sessionToken = message.getSessionToken();
        System.out.println("Initialization phase - new player - nickname: " + nickname + " - sessionToken: " + sessionToken);
        phase.getMatchController().getMatch().addPlayer(new Player(phase.getMatchController().getVirtualView(), nickname, sessionToken));

        System.out.println("Initialization phase  - sending start waiting message");
        try {
            phase.getMatchController().notifyObservers(new SessionMessage(sessionToken, MessageType.START_WAITING, "Please wait for other Players to join..."));
        } catch (InvalidPayloadException ignored) {
        }
    }

}
