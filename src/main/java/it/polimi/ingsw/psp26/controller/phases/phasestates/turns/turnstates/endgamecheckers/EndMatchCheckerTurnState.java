package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.endgamecheckers;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions.ChooseLeaderActionTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.ChooseNormalActionTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.singleplayer.LorenzoMagnificoTurnState;

public class EndMatchCheckerTurnState extends TurnState {

    public EndMatchCheckerTurnState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(Message message) {
        super.play(message);

        if (turn.getMatch().isMultiPlayerMode()) {
            seventhCardDrawn();
            finalTilePosition();
        } else {
            // ...
        }

        if (turn.getTurnPhase().equals(TurnPhase.LEADER_ACTION_FIRST_TIME)) // If first leader action has been played, go to normal action
            turn.changeState(new ChooseNormalActionTurnState(turn));
        else if (turn.getTurnPhase().equals(TurnPhase.NORMAL_ACTION)) // After normal action, go to leader action
            turn.changeState(new ChooseLeaderActionTurnState(turn));
        else {
            if (turn.getMatch().isMultiPlayerMode())
                turn.getPlayingPhaseState().updateCurrentTurn(); // After the second leader action, go to next player turn
            else
                turn.changeState(new LorenzoMagnificoTurnState(turn)); // After the second leader action, go to Lorenzo action
        }
    }

    private void seventhCardDrawn() {
    }

    private void finalTilePosition() {
    }

    private void noMoreCards() { // Multiplayer
    }
}
