package it.polimi.ingsw.psp26.controller.phases.phasestates;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.Phase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.model.Player;

public class PlayingPhaseState extends PhaseState {

    private Turn currentTurn;

    public PlayingPhaseState(Phase phase) {
        super(phase);

        initializeFirstTurn();
    }

    @Override
    public void execute(Message message) {
        super.execute(message);
        currentTurn.play(message);
    }

    private void initializeFirstTurn() {
        phase.getMatchController().getMatch().shufflePlayers();
        currentTurn = new Turn(
                this,
                phase.getMatchController(),
                phase.getMatchController().getMatch().getPlayers().get(0),
                0
        );
    }

    public void updateCurrentTurn() {
        int nextTurnNUmber = currentTurn.getTurnNumber() + 1;
        currentTurn = new Turn(this,
                phase.getMatchController(),
                getNextPlayer(),
                nextTurnNUmber);
    }

    private Player getNextPlayer() {
        int currentPlayerIndex = phase.getMatchController().getMatch().getPlayers().indexOf(currentTurn.getTurnPlayer());
        return phase.getMatchController().getMatch().getPlayers().get((currentPlayerIndex + 1) % phase.getMatchController().getMatch().getPlayers().size());
    }

    public void goToEndMatchPhaseState(Message message) {
        // next state is...
        phase.changeState(new EndMatchPhaseState(phase));
        phase.execute(message);
    }

}
