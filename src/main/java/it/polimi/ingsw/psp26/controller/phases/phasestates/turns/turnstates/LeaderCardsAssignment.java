package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions.ChooseLeaderActionTurnState;
import it.polimi.ingsw.psp26.exceptions.NegativeNumberOfElementsToGrabException;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;

public class LeaderCardsAssignment extends TurnState {

    private final List<LeaderCard> drawnLeaders;

    public LeaderCardsAssignment(Turn turn) {
        super(turn);

        drawnLeaders = new ArrayList<>();
        try {
            drawnLeaders.addAll(turn.getMatchController().getMatch().drawLeaders(4));
        } catch (NegativeNumberOfElementsToGrabException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play(SessionMessage message) {
        super.play(message);

        // skipping if first turn played by all players
        if (turn.getTurnNumber() > turn.getMatchController().getMatch().getPlayers().size() - 1) {
            turn.changeState(new ChooseLeaderActionTurnState(turn, TurnPhase.LEADER_TO_NORMAL_ACTION));
            turn.play(message);

        } else {

            if (message.getMessageType().equals(MessageType.CHOICE_LEADERS)) {

                List<LeaderCard> selectedLeaderCards = castElements(LeaderCard.class, message.getListPayloads());
                // checking if cards are among the drawn.
                if (selectedLeaderCards.size() > 2 ||
                        !drawnLeaders.contains(selectedLeaderCards.get(0)) ||
                        !drawnLeaders.contains(selectedLeaderCards.get(1))) {
                    sendErrorMessage();
                    sendLeaderCardsChoiceMessage();
                }

                turn.getTurnPlayer().setLeaderCards(selectedLeaderCards);

                turn.changeState(new BenefitsTurnState(turn));
                turn.play(message);

            } else {

                sendLeaderCardsChoiceMessage();
            }
        }
    }

    private void sendLeaderCardsChoiceMessage() {
        turn.getMatchController().notifyObservers(
                new MultipleChoicesMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        MessageType.CHOICE_LEADERS,
                        2, 2,
                        drawnLeaders.toArray()
                )
        );
    }

    private void sendErrorMessage() {
        turn.getMatchController().notifyObservers(
                new SessionMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        MessageType.ERROR_MESSAGE,
                        "You have to choose two different leader cards among those drawn."
                )
        );
    }

}
