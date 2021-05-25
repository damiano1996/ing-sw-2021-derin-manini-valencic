package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnPhase;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions.ChooseLeaderActionTurnState;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendErrorMessage;
import static it.polimi.ingsw.psp26.utils.CollectionsUtils.castElements;

/**
 * Class that models the leader cards assignment.
 */
public class LeaderCardsAssignmentTurnState extends TurnState {

    private final List<LeaderCard> drawnLeaders;

    /**
     * Constructor of the class.
     *
     * @param turn current turn
     */
    public LeaderCardsAssignmentTurnState(Turn turn) {
        super(turn);

        drawnLeaders = new ArrayList<>();
    }

    /**
     * Method to assign to the player the leader cards.
     * If this is not the first turn for the turn player,
     * it changes the state of the turn, going to ChooseLeaderActionTurnState.
     *
     * @param message session message
     */
    @Override
    public void play(SessionMessage message) {
        super.play(message);

        // skipping if first turn played by all players
        if (turn.getTurnNumber() > turn.getMatchController().getMatch().getPlayers().size() - 1) {
            turn.changeState(new ChooseLeaderActionTurnState(turn, TurnPhase.LEADER_TO_NORMAL_ACTION));
            turn.play(message);

        } else {

            if (drawnLeaders.size() == 0)
                drawnLeaders.addAll(turn.getMatchController().getMatch().drawLeaders(4));

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

                } catch (IndexOutOfBoundsException e) {
                    sendErrorMessage(turn, "You have to choose two leader cards.");
                    sendLeaderCardsChoiceMessage();
                }

            } else {
                sendLeaderCardsChoiceMessage();
            }
        }
    }

    /**
     * Method sends a message containing the drawn leaders.
     */
    private void sendLeaderCardsChoiceMessage() {
        try {
            turn.getMatchController().notifyObservers(
                    new MultipleChoicesMessage(
                            turn.getTurnPlayer().getSessionToken(),
                            MessageType.CHOICE_LEADERS,
                            "Choose two leader cards:",
                            2, 2,
                            false,
                            drawnLeaders.toArray()
                    )
            );
        } catch (InvalidPayloadException e) {
            e.printStackTrace();
        }
    }

}
