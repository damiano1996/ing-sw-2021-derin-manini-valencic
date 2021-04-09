package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.model.Player;

public class InitializationPhaseState extends PhaseState {

    private int maxNumberOfPlayers;

    public InitializationPhaseState(Phase phase) {
        super(phase);
    }

    @Override
    public void execute(Message message) {
        super.execute(message);

        // TODO: to be implemented

        // next state is...
        // if (phase.getMatchController().getMatch().getPlayers().size() == maxNumberOfPlayers)
        phase.changeState(new PlayingPhaseState(phase));
    }

    private void setMaxNumberOfPlayers(Message message) {
        maxNumberOfPlayers = (int) message.getPayload().get("maxNumberOfPlayers");
        if (maxNumberOfPlayers > 4) maxNumberOfPlayers = 4;
    }

    private void addPlayer(Message message) {
        String nickname = (String) message.getPayload().get("nickname");
        String sessionToken = (String) message.getPayload().get("sessionToken"); // TODO: to be randomly generated
        phase.getMatchController().getMatch().addPlayer(new Player(phase.getMatchController().getVirtualView(), nickname, sessionToken));
    }
}
