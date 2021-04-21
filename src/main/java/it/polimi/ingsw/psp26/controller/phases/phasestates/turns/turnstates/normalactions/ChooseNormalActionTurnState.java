package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;

import static it.polimi.ingsw.psp26.application.messages.MessageType.*;

public class ChooseNormalActionTurnState extends TurnState {
    public ChooseNormalActionTurnState(Turn turn) {
        super(turn);
        turn.setTurnPhase(TurnPhase.NORMAL_TO_LEADER_ACTION);
    }

    @Override
    public void play(SessionMessage message) {
        super.play(message);

        if (message.getMessageType().equals(CHOICE_NORMAL_ACTION)) {
            switch ((MessageType) message.getPayload()) {

                case ACTIVATE_PRODUCTION:
                    turn.changeState(new ActivateProductionNormalActionTurnState(turn));
                    turn.play(message);
                    break;

                case MARKET_RESOURCE:
                    turn.changeState(new MarketResourceNormalActionTurnState(turn));
                    turn.play(message);
                    break;

                case BUY_CARD:
                    turn.changeState(new BuyCardNormalActionTurnState(turn));
                    turn.play(message);
                    break;
            }

        } else {
            turn.getMatchController().notifyObservers(new MultipleChoicesMessage(turn.getTurnPlayer().getSessionToken(),
                    CHOICE_NORMAL_ACTION, 1, 1,
                    ACTIVATE_PRODUCTION, MARKET_RESOURCE, BUY_CARD));
        }

    }
}
