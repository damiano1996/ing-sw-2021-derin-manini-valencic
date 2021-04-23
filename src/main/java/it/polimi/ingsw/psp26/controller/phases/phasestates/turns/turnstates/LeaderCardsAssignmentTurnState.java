package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions.ChooseLeaderActionTurnState;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendErrorMessage;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.castElements;

public class LeaderCardsAssignmentTurnState extends TurnState {

    private final List<LeaderCard> drawnLeaders;

    public LeaderCardsAssignmentTurnState(Turn turn) {
        super(turn);

        drawnLeaders = new ArrayList<>();
        drawnLeaders.addAll(turn.getMatchController().getMatch().drawLeaders(4));
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

                try {

                    List<LeaderCard> selectedLeaderCards = castElements(LeaderCard.class, message.getListPayloads());
                    // checking if cards are among the drawn.
                    if (selectedLeaderCards.size() != 2 ||
                            !drawnLeaders.contains(selectedLeaderCards.get(0)) ||
                            !drawnLeaders.contains(selectedLeaderCards.get(1))) {

                        System.out.println("Error, asking again!");
                        sendErrorMessage(turn, "You have to choose two different leader cards among those drawn.");
                        sendLeaderCardsChoiceMessage();

                    } else {

                        turn.getTurnPlayer().setLeaderCards(selectedLeaderCards);
                        turn.changeState(new BenefitsTurnState(turn));
                        turn.play(message);
                    }

                } catch (EmptyPayloadException | IndexOutOfBoundsException e) {
                    sendErrorMessage(turn, "You have to choose two leader cards.");
                    sendLeaderCardsChoiceMessage();
                }

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
                        "Choice two leader cards:",
                        2, 2,
                        drawnLeaders.toArray()
                )
        );
    }

}
