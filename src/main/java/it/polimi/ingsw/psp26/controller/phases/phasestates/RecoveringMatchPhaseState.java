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
            phase.getMatchController().notifyObservers(
                    new SessionMessage(
                            message.getSessionToken(),
                            MessageType.START_WAITING,
                            "Waiting all players to resume the match..."
                    )
            );

            if (phase.getMatchController().getMatch().getPlayers().size() ==
                    phase.getMatchController().getVirtualView().getNumberOfNodeClients()) {
                // If the number of players is equal to the number of node clients we can resume the match since
                // this means that all the clients (associated the the players) are connected
                phase.getMatchController().setMatchCompletelyRecovered();

                phase.getMatchController().notifyObservers(new SessionMessage(SpecialToken.BROADCAST.getToken(), MessageType.STOP_WAITING));
                phase.getMatchController().getVirtualView().sendingMainMatchComponents(SpecialToken.BROADCAST.getToken());

                // notifying players to remember who is the turn player
                nextPlayingPhaseState.sendNotificationMessageNewTurn();

                phase.changeState(nextPlayingPhaseState);
                // Message just to trigger the match controller
                phase.getMatchController().update(
                        new SessionMessage(
                                nextPlayingPhaseState.getCurrentTurn().getTurnPlayer().getSessionToken(),
                                MessageType.GENERAL_MESSAGE
                        )
                );
            }

        } catch (InvalidPayloadException ignored) {
        }
    }
}
