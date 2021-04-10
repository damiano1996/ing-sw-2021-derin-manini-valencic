package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Utils.goToNextStateAfterLeaderAction;

public class ActivateOrDiscardLeaderTurnState extends TurnState {

    private MessageType lastMessage;

    public ActivateOrDiscardLeaderTurnState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(Message message) {
        super.play(message);

        if (message.getMessageType().equals(MessageType.ACTIVATE_LEADER) || message.getMessageType().equals(MessageType.DISCARD_LEADER))
            lastMessage = message.getMessageType();

        // step: choose which leader to activate/discard
        // step: activate/discard it

        switch (message.getMessageType()) {
            case LEADER_CHOSEN:

                LeaderCard leaderCard = (LeaderCard) message.getPayload().get("leader");
                playAction(leaderCard, message);
                break;

            default:
                // TODO: client.displayLeaderToActivate();
                break;
        }
    }

    private void playAction(LeaderCard leaderCard, Message message) {
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
                playerLeaderCard.activate();
        }
        // TODO: client.displayActiveLeader()
    }
}
