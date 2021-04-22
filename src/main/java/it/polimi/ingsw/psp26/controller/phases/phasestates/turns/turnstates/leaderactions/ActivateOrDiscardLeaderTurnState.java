package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.goToNextStateAfterLeaderAction;

public class ActivateOrDiscardLeaderTurnState extends TurnState {

    private MessageType lastMessage;

    public ActivateOrDiscardLeaderTurnState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(SessionMessage message) {
        super.play(message);

        try {

            if (message.getPayload().equals(MessageType.ACTIVATE_LEADER) || message.getPayload().equals(MessageType.DISCARD_LEADER))
                lastMessage = (MessageType) message.getPayload();

            // step: choose which leader to activate/discard
            // step: activate/discard it

            if (message.getMessageType().equals(MessageType.CHOICE_LEADERS)) {

                LeaderCard leaderCard = (LeaderCard) message.getPayload();
                playAction(leaderCard, message);

            } else {

                turn.getMatchController().notifyObservers(
                        new MultipleChoicesMessage(
                                turn.getTurnPlayer().getSessionToken(),
                                MessageType.CHOICE_LEADERS,
                                "Choice the leader " + ((lastMessage.equals(MessageType.ACTIVATE_LEADER)) ? " to activate:" : " to discard:"),
                                1, 1,
                                turn.getTurnPlayer().getLeaderCards()
                        )
                );
            }
        } catch (EmptyPayloadException emptyPayloadException) {
            emptyPayloadException.printStackTrace();
        }
    }

    private void playAction(LeaderCard leaderCard, SessionMessage message) {
        switch (lastMessage) {
            case ACTIVATE_LEADER:
                activateLeader(leaderCard); // no needs to check vatican report after activation
                goToNextStateAfterLeaderAction(turn, message);
                break;

            case DISCARD_LEADER:
                discardLeader(leaderCard);
                // only in case of discard we have to check the vatican report!
                turn.changeState(new CheckVaticanReportTurnState(turn));
                turn.play(message);
                break;
        }
    }

    private void discardLeader(LeaderCard leaderCard) {
        turn.getTurnPlayer().discardLeaderCard(leaderCard);
        turn.getTurnPlayer().getPersonalBoard().getFaithTrack().addFaithPoints(1);
    }

    private void activateLeader(LeaderCard leaderCard) {
        for (LeaderCard playerLeaderCard : turn.getTurnPlayer().getLeaderCards()) {
            if (playerLeaderCard.equals(leaderCard))
                playerLeaderCard.activate(turn.getTurnPlayer());
        }
        turn.getMatchController().notifyObservers(
                new SessionMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        MessageType.LEADER_ACTIVATED,
                        leaderCard
                )
        );
    }
}
