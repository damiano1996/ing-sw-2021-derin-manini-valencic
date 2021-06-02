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
import static it.polimi.ingsw.psp26.network.server.MessageUtils.getPlayerModelUpdateMessage;


public class InitializationPhaseState extends PhaseState {

    public InitializationPhaseState(Phase phase) {
        super(phase);
    }

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
                    phase.getMatchController().notifyObservers(new SessionMessage(SpecialToken.BROADCAST.getToken(), MessageType.STOP_WAITING, "Stop waiting..."));
                    phase.getMatchController().notifyObservers(new SessionMessage(SpecialToken.BROADCAST.getToken(), MessageType.GENERAL_MESSAGE, "The match can start!"));
                } catch (InvalidPayloadException e) {
                    e.printStackTrace();
                }

                // Send the initial version of the Market and Development Grid to all Players
                //notifyMarketAndGridCreation();

                // Updating the state. The match can begin!
                PlayingPhaseState playingPhaseState = new PlayingPhaseState(phase);
                phase.changeState(playingPhaseState);
                // phase.execute(message); // is performed by the PlayingPhaseState() at the initialization of the turn
                playingPhaseState.playFirstTurn();
            }
        }
    }

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
            // Sending to all players the board of the new player.
            phase.getMatchController().notifyObservers(getPlayerModelUpdateMessage(sessionToken));
        } catch (InvalidPayloadException ignored) {
        }
    }

    /**
     * Used to send the first version of the Market and Development Grid to all Players
     */
//    private synchronized void notifyMarketAndGridCreation() {
//        phase.getMatchController().notifyObservers(getMarketTrayModelUpdateMessage());
//        phase.getMatchController().notifyObservers(getDevelopmentGridModelUpdateMessage());
//    }

}
