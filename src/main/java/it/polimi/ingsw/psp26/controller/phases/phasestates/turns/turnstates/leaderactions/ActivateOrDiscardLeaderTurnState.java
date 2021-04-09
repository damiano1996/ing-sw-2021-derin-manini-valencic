package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions.ChooseNormalActionTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.singleplayer.LorenzoMagnificoTurnState;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

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

                if (turn.getTurnPhase().equals(TurnPhase.LEADER_ACTION_FIRST_TIME)) {
                    // go to normal action
                    turn.changeState(new ChooseNormalActionTurnState(turn));
                    turn.play(message);

                } else {

                    if (turn.getMatch().isMultiPlayerMode()) {
                        turn.getPlayingPhaseState().updateCurrentTurn(); // next turn
                    } else {
                        turn.changeState(new LorenzoMagnificoTurnState(turn)); // Lorenzo's turn
                        turn.play(message);
                    }

                }
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
        turn.getTurnPlayer().getLeaderCards().remove(leaderCard);
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
