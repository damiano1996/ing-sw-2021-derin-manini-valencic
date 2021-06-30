package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.endgamecheckers.EndMatchCheckerTurnState;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_DEVELOPMENT_CARD_SLOT_POSITION;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendGeneralMessage;

public class BuyCardNormalActionTurnState extends TurnState {
    private DevelopmentCard boughtCard = null;
    private List<Resource> tempResources = new ArrayList<>();

    /**
     * Constructor of the class.
     *
     * @param turn current turn
     */
    public BuyCardNormalActionTurnState(Turn turn) {
        super(turn);
    }

    /**
     * Method that checks the messages of the current player and redirects to the right sub-phase.
     * <p>
     * The first sub-phase is CHOICE_NORMAL_ACTION: It checks if it is the message that arrived from
     * ChooseNormalActionTurnState, it also checks if there are leader discount power to activate and in case it stores
     * the discount amount.In the sends to the current player the list of their available resources.
     * <p>
     * The second sub-phase is CHOICE_CARD_TO_BUY: It checks if the player can buy at least one card and if it the
     * player choice of the development card they want to buy is affordable. If it does not pass the first check
     * it sends an error message and redirect to the ChooseNormalActionTurnState, if it does not pass the second check
     * send them a message to choose another card. If it pass both checks, it call the method to draw the card player
     * and pay the cost resources. It then send a message to the player with the card slot available.
     * <p>
     * The third sub-phase is CHOICE_DEVELOPMENT_CARD_SLOT_POSITION: It takes the player answer of the previous
     * message(the card slot), and calls a method to put the bought card in that card slot.
     * <p>
     * The fourth sub-phase is UNDO_OPTION_SELECTED/default: It checks if an undo message is sent, and redirects the
     * player to the ChooseNormalActionTurnState.
     *
     * @param message The message sent by the current player, that carries his choices during the turn.
     */
    @Override
    public void play(SessionMessage message) {
        super.play(message);

        try {
            switch (message.getMessageType()) {
                case CHOICE_NORMAL_ACTION:

                    for (LeaderCard leader : turn.getTurnPlayer().getLeaderCards()) {
                        leader.execute(tempResources);
                    }

                    turn.getMatchController().notifyObservers(
                            new SessionMessage(
                                    turn.getTurnPlayer().getSessionToken(),
                                    MessageType.CHOICE_CARD_TO_BUY
                            ));

                    break;
                case CHOICE_CARD_TO_BUY:
                    if (getAvailableCard().size() > 0) {
                        if (getAvailableCard().contains((DevelopmentCard) message.getPayload())) {

                            boughtCard = (DevelopmentCard) message.getPayload();
                            buyCard();

                            turn.getMatchController().notifyObservers(
                                    new MultipleChoicesMessage(
                                            turn.getTurnPlayer().getSessionToken(),
                                            CHOICE_DEVELOPMENT_CARD_SLOT_POSITION,
                                            "Choose where to place the new card:",
                                            1, 1,
                                            false,
                                            positionsForCard().toArray(new Object[0])
                                    ));
                        } else {
                            sendGeneralMessage(turn, "The selected card cannot be bought...");
                            turn.getMatchController().notifyObservers(
                                    new SessionMessage(
                                            turn.getTurnPlayer().getSessionToken(),
                                            MessageType.CHOICE_CARD_TO_BUY
                                    ));

                        }
                    } else {

                        sendGeneralMessage(turn, "There are no card that can be bought...");
                        turn.changeState(new ChooseNormalActionTurnState(turn));
                        turn.play(message);

                    }

                    break;

                case CHOICE_DEVELOPMENT_CARD_SLOT_POSITION:
                    turn.notifyAllPlayers("The player " + turn.getTurnPlayer().getNickname() + " bought the following card: "
                            + boughtCard);
                    placeCard((String) message.getPayload());
                    turn.changeState(new EndMatchCheckerTurnState(turn));
                    turn.play(message);
                    break;

                case UNDO_OPTION_SELECTED:
                default:
                    turn.notifyAllPlayers("The player " + turn.getTurnPlayer().getNickname() + " went back on their steps");
                    turn.changeState(new ChooseNormalActionTurnState(turn));
                    turn.play(message);

                    break;

            }
        } catch (EmptyPayloadException | InvalidPayloadException ignored) {
        }
    }

    /**
     * Method that filters the visible cards of the development grid in base if player can afford and place in one of
     * their slot or not.
     *
     * @return List of development card that the player has enough resources to buy or is able to position.
     */
    private List<DevelopmentCard> getAvailableCard() {
        List<DevelopmentCard> availableCard = new ArrayList<>();
        List<Integer> feasibleLevels = new ArrayList<>();

        if (turn.getTurnPlayer().getPersonalBoard().getVisibleDevelopmentCards().size() < 3) feasibleLevels.add(1);
        feasibleLevels.addAll(turn.getTurnPlayer().getPersonalBoard().getVisibleDevelopmentCards().stream().map(x -> x.getDevelopmentCardType().getLevel().getLevelNumber() + 1).distinct().collect(Collectors.toList()));

        boolean isAvailable = true;
        for (DevelopmentCard card : turn.getMatchController().getMatch().getDevelopmentGrid().getAllVisibleCards()) {
            for (Resource resource : card.getCost().keySet()) {
                if (isAvailable) {
                    if (turn.getTurnPlayer().getPersonalBoard().getAllAvailableResources().stream().filter(x -> x.equals(resource)).count()
                            < (card.getCost().get(resource) - tempResources.stream().filter(x -> x.equals(resource)).count()))
                        isAvailable = false;
                }
            }

            if (isAvailable && feasibleLevels.contains(card.getDevelopmentCardType().getLevel().getLevelNumber()))
                availableCard.add(card);
            isAvailable = true;
        }
        return availableCard;

    }

    /**
     * Method that given a card, takes the resource cost from the warehouse and depot of the current player.
     */
    private void buyCard() {
        DevelopmentCard drawnCard = boughtCard;
        int numberResources;
        int i = 0;

        try {
            drawnCard = turn.getMatchController().getMatch().getDevelopmentGrid().drawCard(drawnCard.getDevelopmentCardType().getColor(), drawnCard.getDevelopmentCardType().getLevel());

        } catch (LevelDoesNotExistException | ColorDoesNotExistException | NoMoreDevelopmentCardsException ignored) {
        }

        for (Resource resource : drawnCard.getCost().keySet()) {

            numberResources = drawnCard.getCost().get(resource) - (int) tempResources.stream().filter(x -> x.equals(resource)).count();
            numberResources -= turn.getTurnPlayer().getPersonalBoard().getWarehouse().grabResources(resource, numberResources).size();

            if (numberResources > 0)
                turn.getTurnPlayer().getPersonalBoard().grabResourcesFromStrongbox(resource, numberResources);

        }
    }

    /**
     * Method that checks which position the boughtCard can be placed
     *
     * @return List of string that represent the indexes of the position that are allowed.
     */
    private List<String> positionsForCard() {
        List<String> CorrectPositions = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            if (turn.getTurnPlayer().getPersonalBoard().isCardPlaceable(i, boughtCard))
                CorrectPositions.add("Slot " + (i + 1));
        }
        return CorrectPositions;
    }

    /**
     * Method that place a development card in the pointed slot of the current player.
     *
     * @param slotIndex the index of the slot in which the card is placed.
     */
    private void placeCard(String slotIndex) {
        try {
            int position = Integer.parseInt(slotIndex.substring(5, 6)) - 1;
            turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(position, boughtCard);

        } catch (CanNotAddDevelopmentCardToSlotException | DevelopmentCardSlotOutOfBoundsException e) {

            try {
                turn.getMatchController().notifyObservers(
                        new MultipleChoicesMessage(
                                turn.getTurnPlayer().getSessionToken(),
                                CHOICE_DEVELOPMENT_CARD_SLOT_POSITION,
                                "Choose where to place the new card:",
                                1, 1,
                                false,
                                positionsForCard().toArray(new Object[0])
                        ));

            } catch (InvalidPayloadException invalidPayloadException) {
                invalidPayloadException.printStackTrace();
            }
        }

    }

}
