package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.leaderactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.EmptyPayloadException;
import it.polimi.ingsw.psp26.exceptions.InvalidPayloadException;
import it.polimi.ingsw.psp26.exceptions.LeaderCannotBeActivatedException;
import it.polimi.ingsw.psp26.exceptions.LeaderCannotBeDiscardedException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.goToNextStateAfterLeaderAction;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendErrorMessage;

/**
 * Class to handle the leader actions, which are: activate and discard a leader.
 */
public class ActivateOrDiscardLeaderTurnState extends TurnState {

    private MessageType lastMessage;

    /**
     * Constructor of the class.
     *
     * @param turn current turn
     */
    public ActivateOrDiscardLeaderTurnState(Turn turn) {
        super(turn);
    }

    /**
     * Method receives the leader action to perform (activate or discard a leader).
     * It sends to the user the leader cards that can be activated or discarded, and waits for selection.
     * After that, it checks if the selected leader card can be activated (or discarded) and modify the model consequently.
     *
     * @param message session message
     */
    @Override
    public void play(SessionMessage message) {
        super.play(message);

        if (message.getMessageType().equals(MessageType.UNDO_OPTION_SELECTED)) {
            turn.changeState(new ChooseLeaderActionTurnState(turn, turn.getTurnPhase()));
            turn.play(message);
        } else {
            try {

                // since this state is called by ChooseLeaderActionTurnState, we can assume that the first message has one of the following payloads
                if (message.getPayload().equals(MessageType.ACTIVATE_LEADER) || message.getPayload().equals(MessageType.DISCARD_LEADER))
                    lastMessage = (MessageType) message.getPayload();

                if (message.getMessageType().equals(MessageType.CHOICE_LEADERS)) {

                    LeaderCard leaderCard = (LeaderCard) message.getPayload();
                    playAction(leaderCard, message);

                } else {
                    sendLeaderChoicesMessage(message);
                }

            } catch (EmptyPayloadException emptyPayloadException) {
                sendLeaderChoicesMessage(message);
            }
        }
    }

    /**
     * Method to sends, to the player, the leader cards that can be activated or discarded.
     * It collects all the leader cards that can be used in the request action type.
     * If no leader can be played, it change the turn states, going to next state, and send an error message to the client.
     *
     * @param message message to forward to the next state in case of error
     */
    private void sendLeaderChoicesMessage(SessionMessage message) {

        List<LeaderCard> playableLeaders = (lastMessage.equals(MessageType.ACTIVATE_LEADER)) ? // as payload we can send only leaders
                turn.getTurnPlayer().getLeaderCards().stream().filter(this::isActivatable).collect(Collectors.toList()) :
                turn.getTurnPlayer().getLeaderCards().stream().filter(x -> !x.isActive()).collect(Collectors.toList());

        if (playableLeaders.size() == 0) {
            sendErrorMessage(turn, "You do not have leader cards that can be " + ((lastMessage.equals(MessageType.ACTIVATE_LEADER)) ? "activated." : "discarded"));

            goToNextStateAfterLeaderAction(turn, message);

        } else {
            try {
                turn.getMatchController().notifyObservers(
                        new MultipleChoicesMessage(
                                turn.getTurnPlayer().getSessionToken(),
                                MessageType.CHOICE_LEADERS,
                                "Choose the leader " + ((lastMessage.equals(MessageType.ACTIVATE_LEADER)) ? " to activate:" : " to discard:"),
                                1, 1,
                                true,
                                playableLeaders.toArray()
                        )
                );
            } catch (InvalidPayloadException ignored) {
            }
        }
    }

    /**
     * Method activates or discards a leader card.
     *
     * @param leaderCard leader card to activate or discard
     * @param message    message to be forwarded to the next turn states
     */
    private void playAction(LeaderCard leaderCard, SessionMessage message) {
        try {
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

        } catch (LeaderCannotBeActivatedException | LeaderCannotBeDiscardedException leaderCannotBeActivatedException) {
            // if we arrive here means that no leader has been activated
            // sending an error message
            sendErrorMessage(turn, "Leader card cannot be " + (lastMessage.equals(MessageType.ACTIVATE_LEADER) ? "activated!" : "discarded!"));
            // resend the choices
            sendLeaderChoicesMessage(message);
        }
    }

    /**
     * Method to check if there is at least one leader that can be activated.
     *
     * @return true if there is, false otherwise
     */
    public boolean isAtLeastOneLeaderActivatable() {
        for (LeaderCard leaderCard : turn.getTurnPlayer().getLeaderCards()) {
            if (isActivatable(leaderCard)) return true;
        }
        return false;
    }

    /**
     * Method to check if there is at least one leader that can be discarded.
     *
     * @return true if there is, false otherwise
     */
    public boolean isAtLeastOneLeaderDiscardable() {
        for (LeaderCard leaderCard : turn.getTurnPlayer().getLeaderCards()) {
            if (!leaderCard.isActive()) return true;
        }
        return false;
    }

    /**
     * Method to discard a leader card.
     * Given the leader card, it checks if it can be discarded. If so, it discards it.
     *
     * @param leaderCard leader card under examination
     * @throws LeaderCannotBeDiscardedException if leader card cannot be discarded
     */
    private void discardLeader(LeaderCard leaderCard) throws LeaderCannotBeDiscardedException {
        // since the leader is just a copy of the original one, we should found the original
        boolean discarded = false;
        for (LeaderCard playerLeaderCard : turn.getTurnPlayer().getLeaderCards()) {
            if (playerLeaderCard.equals(leaderCard)) {

                if (!playerLeaderCard.isActive()) {
                    turn.getTurnPlayer().discardLeaderCard(leaderCard);
                    turn.getTurnPlayer().getPersonalBoard().getFaithTrack().addFaithPoints(1);
                    discarded = true;
                    break;
                } else {
                    throw new LeaderCannotBeDiscardedException();
                }

            }
        }
        if (!discarded) throw new LeaderCannotBeDiscardedException();
    }

    /**
     * Method to activate the leader card.
     * Given the leader card, it checks if it can be activated. If so, it activates it.
     *
     * @param leaderCard leader card to activate
     * @throws LeaderCannotBeActivatedException if leader cannot be activated
     */
    private void activateLeader(LeaderCard leaderCard) throws LeaderCannotBeActivatedException {

        boolean leaderHasBeenActivated = false;
        // since the input leader is a copy of the original one, we have to retrieve the original
        for (LeaderCard playerLeaderCard : turn.getTurnPlayer().getLeaderCards()) {

            if (playerLeaderCard.equals(leaderCard)) {

                // checks if leader card can be activated
                if (isActivatable(playerLeaderCard)) {

                    turn.getTurnPlayer().activateLeaderCard(playerLeaderCard);
                    leaderHasBeenActivated = true;

                } else {
                    throw new LeaderCannotBeActivatedException();
                }
            }
        }

        if (!leaderHasBeenActivated) throw new LeaderCannotBeActivatedException();
    }

    /**
     * Method that checks if a leader card can be activated.
     *
     * @param leaderCard leader to activate
     * @return true if it can be activated, false otherwise
     */
    private boolean isActivatable(LeaderCard leaderCard) {
        return !leaderCard.isActive() &&
                isResourcesRequirementSatisfied(leaderCard) &&
                isDevelopmentCardTypeRequirementsSatisfied(leaderCard);
    }

    /**
     * Method to check if requirement of the resources is satisfied.
     *
     * @param leaderCard leader card under examination
     * @return true if requirements are satisfied, false otherwise
     */
    private boolean isResourcesRequirementSatisfied(LeaderCard leaderCard) {
        for (Resource resource : leaderCard.getResourcesRequirements().keySet())
            // if there are less resources than required we return false
            if (Collections.frequency(
                    turn.getTurnPlayer().getPersonalBoard().getAllAvailableResources(), resource) <
                    leaderCard.getResourcesRequirements().get(resource))
                return false;
        // if all requirements are satisfied we can return true
        return true;
    }

    /**
     * Method to check if the requirement of the development card type is satisfied.
     *
     * @param leaderCard leader card under examination
     * @return true if requirements are satisfied, false otherwise
     */
    private boolean isDevelopmentCardTypeRequirementsSatisfied(LeaderCard leaderCard) {
        for (DevelopmentCardType developmentCardType : leaderCard.getDevelopmentCardRequirements().keySet()) {
            // extract requirements
            Color requiredColor = developmentCardType.getColor();
            Level requiredLevel = developmentCardType.getLevel();
            int requiredMultiplicity = leaderCard.getDevelopmentCardRequirements().get(developmentCardType);

            if (requiredLevel.equals(Level.UNDEFINED)) {
                // if undefined, we have just to check the multiplicity, not the level
                if (getPlayerDevelopmentCards(requiredColor).size() < requiredMultiplicity) return false;
            } else {
                // if level is defined we have to check it
                if (getPlayerDevelopmentCards(requiredColor).stream().filter(x -> x.getDevelopmentCardType().getLevel().equals(requiredLevel)).count() < requiredMultiplicity)
                    return false;
            }
        }
        // if all requirements are satisfied we can return true
        return true;
    }

    /**
     * Getter of the development cards of the current player by color.
     *
     * @param color color of the desired cards
     * @return list of development cards
     */
    private List<DevelopmentCard> getPlayerDevelopmentCards(Color color) {
        List<DevelopmentCard> sameColorDevelopmentCards = new ArrayList<>();
        for (DevelopmentCard developmentCard : turn.getTurnPlayer().getPersonalBoard().getDevelopmentCardsSlots().stream().flatMap(List::stream).collect(Collectors.toList())) {
            if (developmentCard.getDevelopmentCardType().getColor().equals(color))
                sameColorDevelopmentCards.add(developmentCard);
        }
        return sameColorDevelopmentCards;
    }
}
