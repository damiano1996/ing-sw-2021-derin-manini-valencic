package it.polimi.ingsw.psp26.controller.turns.states.endgamecheckers;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.turns.Phase;
import it.polimi.ingsw.psp26.controller.turns.Turn;
import it.polimi.ingsw.psp26.controller.turns.states.TurnState;
import it.polimi.ingsw.psp26.controller.turns.states.leaderactions.ChooseLeaderActionState;
import it.polimi.ingsw.psp26.controller.turns.states.normalactions.ChooseNormalActionState;

public class EndMatchCheckerState extends TurnState {

    public EndMatchCheckerState(Turn turn) {
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

        if (turn.getPhase().equals(Phase.LEADER_ACTION_FIRST))
            turn.changeState(new ChooseNormalActionState(turn));
        else if (turn.getPhase().equals(Phase.NORMAL_ACTION))
            turn.changeState(new ChooseLeaderActionState(turn));
        else
            turn.initializeNextTurn();
    }

    private void seventhCardDrawn() {
    }

    private void finalTilePosition() {
    }

    private void noMoreCards() { // Multiplayer
    }
}
