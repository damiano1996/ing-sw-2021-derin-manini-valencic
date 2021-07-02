package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.network.SpecialToken;

/**
 * Class used to recover a match when all player inside it disconnected or exited.
 * This phase state is set when a player connects back to the game, and choose the recovery option.
 */
public class RecoveringMatchPhaseState extends PhaseState {

    private final PlayingPhaseState nextPlayingPhaseState;

    /**
     * Class constructor.
     *
     * @param phase                 the phase of the game that has called it
     * @param nextPlayingPhaseState the phase following the recover
     */
    public RecoveringMatchPhaseState(Phase phase, PlayingPhaseState nextPlayingPhaseState) {
        super(phase);

        this.nextPlayingPhaseState = nextPlayingPhaseState;
    }

    /**
     * Methods that waits for all player in a match and when they are all present it recovers the match
     * <p>
     * It notifies each player that calls this method to wait. When the number of waiting player is equal to the number
     * of player that was in the match, it recovers the match and sends to each player the items components needed to
     * play the game. In the end, it changes phase state and starts a new turn.
     *
     * @param message the message that is executed
     */
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
