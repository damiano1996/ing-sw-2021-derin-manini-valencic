package it.polimi.ingsw.psp26.controller.turns.states;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.turns.Turn;
import it.polimi.ingsw.psp26.controller.turns.states.leaderactions.ChooseLeaderActionState;

public class BenefitsState extends TurnState {
    public BenefitsState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(Message message) {

        switch (turn.getTurnNumber()) {
            case 0:
                turn.getTurnPlayer().giveInkwell();
                break;
            case 1:
                // TODO: choice 1 extra resource
                break;
            case 2:
                // TODO choice 1 extra resource + 1 FP
                break;
            case 3:
                // TODO choice 2 extra resources + 1 FP
                break;
        }

        // next state is...
        // step 2: leader action (-> check vatican report -> check endgame)
        turn.changeState(new ChooseLeaderActionState(turn));
    }
}
