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

    /**
     * Constructor of the class.
     *
     * @param turn current turn
     */
    public ChooseNormalActionTurnState(Turn turn) {
        super(turn);
        turn.setTurnPhase(TurnPhase.NORMAL_TO_LEADER_ACTION);
    }

    /**
     * Method to redirect the state of the turn in one of the three possible action
     *
     * @param message the message containing the action that the current player will perform
     */
    @Override
    public void play(SessionMessage message) {
        super.play(message);

        if (message.getMessageType().equals(CHOICE_NORMAL_ACTION)) {
            try {
                switch ((MessageType) message.getPayload()) {

                    case ACTIVATE_PRODUCTION:
                        turn.changeState(new ActivateProductionNormalActionTurnState(turn));
                        turn.play(message);
                        turn.notifyAllPlayers("The player " + turn.getTurnPlayer().getNickname() + " chose to activate their production");
                        break;

                    case MARKET_RESOURCE:
                        turn.changeState(new MarketResourceNormalActionTurnState(turn));
                        turn.play(message);
                        turn.notifyAllPlayers("The player " + turn.getTurnPlayer().getNickname() + " went to the market");
                        break;

                    case BUY_CARD:
                        turn.changeState(new BuyCardNormalActionTurnState(turn));
                        turn.play(message);
                        turn.notifyAllPlayers("The player " + turn.getTurnPlayer().getNickname() + " chose to buy a new development card");
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
