package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.model.Player;

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
    }

}
