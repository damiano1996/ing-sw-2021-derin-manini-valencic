package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;

import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_NORMAL_ACTION;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendChoiceNormalActionMessage;

public class ChooseNormalActionTurnState extends TurnState {
    public ChooseNormalActionTurnState(Turn turn) {
        super(turn);
        turn.setTurnPhase(TurnPhase.NORMAL_TO_LEADER_ACTION);
    }

    @Override
    public void play(SessionMessage message) {
        super.play(message);

        if (message.getMessageType().equals(CHOICE_NORMAL_ACTION)) {
            try {
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

            } catch (EmptyPayloadException ignored) {
                sendChoiceNormalActionMessage(turn);
            }

        } else {
            sendChoiceNormalActionMessage(turn);
        }

    }

}
