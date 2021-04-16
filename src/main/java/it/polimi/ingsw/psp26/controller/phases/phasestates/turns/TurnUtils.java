package it.polimi.ingsw.psp26.controller.phases.phasestates.turns;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions.ChooseLeaderActionTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.ChooseNormalActionTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.singleplayer.LorenzoMagnificoTurnState;

public class TurnUtils {

    public static void goToNextStateAfterLeaderAction(Turn turn, Message message) {
        switch (turn.getTurnPhase()) {

            case LEADER_TO_NORMAL_ACTION:
                // If first leader action has been played, go to normal action
                turn.changeState(new ChooseNormalActionTurnState(turn));
                turn.play(message);
                break;

            case NORMAL_TO_LEADER_ACTION:
                // After normal action, go to leader action
                turn.changeState(new ChooseLeaderActionTurnState(turn, TurnPhase.NORMAL_TO_LEADER_ACTION));
                turn.play(message);
                break;

            case LEADER_ACTION_TO_END:
                if (turn.getMatchController().getMatch().isMultiPlayerMode()) {
                    // After the second leader action, go to next player turn
                    turn.getPlayingPhaseState().updateCurrentTurn();
                } else {
                    // After the second leader action, go to Lorenzo action
                    turn.changeState(new LorenzoMagnificoTurnState(turn));
                    turn.play(message);
                }
                break;
        }
    }
}
