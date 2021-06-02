package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.SpecialToken;

public class RecoveringMatchPhaseState extends PhaseState {

    private final PlayingPhaseState nextPlayingPhaseState;

    public RecoveringMatchPhaseState(Phase phase, PlayingPhaseState nextPlayingPhaseState) {
        super(phase);

        this.nextPlayingPhaseState = nextPlayingPhaseState;
    }

    @Override
    public void execute(SessionMessage message) {

        try {

            System.out.println("RecoveringMatchPhaseState - Player wants to resume the match.");
            phase.getMatchController().notifyObservers(new SessionMessage(message.getSessionToken(), MessageType.START_WAITING, "Waiting all players to resume the match..."));


            if (phase.getMatchController().getMatch().getPlayers().size() ==
                    phase.getMatchController().getVirtualView().getNumberOfNodeClients()) {
                // If the number of players is equal to the number of node clients we can resume the match since
                // this means that all the clients (associated the the players) are connected
                phase.getMatchController().setMatchCompletelyRecovered();

                phase.getMatchController().notifyObservers(new SessionMessage(SpecialToken.BROADCAST.getToken(), MessageType.STOP_WAITING));

                phase.changeState(nextPlayingPhaseState);
                // TODO: Needs of message to trigger the match controller to notify players that the turn can be of an opponent!
            }

        } catch (InvalidPayloadException ignored) {

        }
    }
}
