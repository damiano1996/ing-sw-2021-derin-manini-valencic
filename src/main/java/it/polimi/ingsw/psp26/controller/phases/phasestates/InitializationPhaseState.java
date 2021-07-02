package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.NotificationUpdateMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.exceptions.SessionTokenDoesNotExistsException;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.network.SpecialToken;
import it.polimi.ingsw.psp26.network.server.memory.Users;

import static it.polimi.ingsw.psp26.application.messages.MessageType.SET_NUMBER_OF_PLAYERS;

/**
 * Class that is used for the first phase of the game. It works as lobby, it adds users to the match as player and when
 * the lobby is full it starts the match.
 */
public class InitializationPhaseState extends PhaseState {

    /**
     * Constructor of the class.
     *
     * @param phase which this state is in
     */
    public InitializationPhaseState(Phase phase) {
        super(phase);
    }

    /**
     * Methods that waits for all player in a match and when they are all present it starts the match
     * <p>
     * When the number of waiting player is equal to the maximum number of player of the match, it sends to each player
     * the items components needed to play the game and notify them that the match is starting.In the end, it changes
     * phase state and starts a new turn.
     *
     * @param message the message that is executed
     */
    @Override
    public synchronized void execute(SessionMessage message) {
        super.execute(message);

        if (message.getMessageType().equals(MessageType.ADD_PLAYER)) {

            try {
                addPlayer(message);
            } catch (EmptyPayloadException | SessionTokenDoesNotExistsException ignored) {
            }

            // next state is...
            if (phase.getMatchController().getMatch().getPlayers().size() == phase.getMatchController().getMaxNumberOfPlayers()) {
                // Communicating to match controller that we reached the maximum number of players.
                phase.getMatchController().stopWaitingForPlayers();

                System.out.println("InitializationPhaseState - sending stop message");
                try {

                    // Sending all the components to the players
                    phase.getMatchController().getVirtualView().sendingMainMatchComponents(SpecialToken.BROADCAST.getToken());
                    phase.getMatchController().notifyObservers(new SessionMessage(SpecialToken.BROADCAST.getToken(), MessageType.STOP_WAITING, "Stop waiting..."));
                    phase.getMatchController().notifyObservers(new SessionMessage(SpecialToken.BROADCAST.getToken(), MessageType.GENERAL_MESSAGE, "The match can start!"));

                } catch (InvalidPayloadException ignored) {
                }

                // Updating the state. The match can begin!
                PlayingPhaseState playingPhaseState = new PlayingPhaseState(phase);
                phase.changeState(playingPhaseState);
                playingPhaseState.playFirstTurn();
            }
        }
    }

    /**
     * Method that retrieve the user, creates the player and adds it to the match. It notifies them to wait and the
     * other players that they joined the game.
     *
     * @param message that contains the session token of the player to add
     * @throws EmptyPayloadException the message payload is empty
     * @throws SessionTokenDoesNotExistsException if does not find the user relative to the session token.
     */
    private synchronized void addPlayer(SessionMessage message) throws EmptyPayloadException, SessionTokenDoesNotExistsException {
        String sessionToken = message.getSessionToken();
        String nickname = Users.getInstance().getNickname(sessionToken);
        Player newPlayer = new Player(phase.getMatchController().getVirtualView(), nickname, sessionToken);

        System.out.println("InitializationPhaseState - New player - nickname: " + nickname + " - sessionToken: " + sessionToken);
        phase.getMatchController().getMatch().addPlayer(newPlayer);
        System.out.println("InitializationPhaseState - Number of players in the match: " + phase.getMatchController().getMatch().getPlayers().size());

        System.out.println("InitializationPhaseState - Sending start waiting message");
        try {
            phase.getMatchController().notifyObservers(new SessionMessage(sessionToken, SET_NUMBER_OF_PLAYERS, phase.getMatchController().getMaxNumberOfPlayers()));
            phase.getMatchController().notifyObservers(new NotificationUpdateMessage(SpecialToken.BROADCAST.getToken(), nickname + " joined the game!"));
            phase.getMatchController().notifyObservers(new SessionMessage(sessionToken, MessageType.START_WAITING, "Waiting for opponents to connect..."));
        } catch (InvalidPayloadException ignored) {
        }
    }
}
