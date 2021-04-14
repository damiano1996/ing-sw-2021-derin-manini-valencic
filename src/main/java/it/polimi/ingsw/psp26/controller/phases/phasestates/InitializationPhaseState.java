package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.model.Player;

import java.util.HashMap;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;

public class InitializationPhaseState extends PhaseState {

    private int maxNumberOfPlayers;

    public InitializationPhaseState(Phase phase) {
        super(phase);
    }

    @Override
    public void execute(Message message) {
        super.execute(message);

        switch (message.getMessageType()) {

            case MULTI_OR_SINGLE_PLAYER_MODE:
                setMultiOrSinglePlayer(message);
                break;

            case MAX_NUMBER_OF_PLAYERS:
                setMaxNumberOfPlayers(message);
                break;

            case ADD_PLAYER:
                addPlayer(message);

                // next state is...
                if (phase.getMatchController().getMatch().getPlayers().size() == maxNumberOfPlayers) {
                    phase.changeState(new PlayingPhaseState(phase));
                    phase.execute(new Message());
                }
                break;
            default:

                phase.getMatchController().getVirtualView().update(new Message(
                        MULTI_OR_SINGLE_PLAYER_MODE, // message type
                        MULTIPLAYER_MODE, SINGLE_PLAYER_MODE)); // choices
        }
    }

    private void setMultiOrSinglePlayer(Message message) {
        switch ((MessageType) message.getPayload()) {
            case MULTIPLAYER_MODE:
                // TODO:
                System.out.println("Multiplayer mode has been selected.");
                break;
            case SINGLE_PLAYER_MODE:
                maxNumberOfPlayers = 1;
                System.out.println("Single player mode has been selected.");
                break;
        }
    }

    private void setMaxNumberOfPlayers(Message message) {
        maxNumberOfPlayers = (int) message.getPayload();
        if (maxNumberOfPlayers > 4) maxNumberOfPlayers = 4;
    }

    private void addPlayer(Message message) {
        String nickname = (String) message.getPayload();
        String sessionToken = generateAccessToken();
        phase.getMatchController().getMatch().addPlayer(new Player(phase.getMatchController().getVirtualView(), nickname, sessionToken));
    }

    private String generateAccessToken() {
        // TODO: to be implemented
        return null;
    }
}
