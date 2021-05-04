package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.endgamecheckers.EndMatchCheckerTurnState;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_POSITION;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.goToNextStateAfterLeaderAction;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.TurnUtils.sendGeneralMessage;

public class BuyCardNormalActionTurnState extends TurnState {
    DevelopmentCard boughtCard = null;
    List<Resource> tempResources = new ArrayList<>();

    public BuyCardNormalActionTurnState(Turn turn) {
        super(turn);
    }

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
                                    MessageType.CHOICE_CARD_TO_BUY,
                                    turn.getMatchController().getMatch().getDevelopmentGrid()
                            ));

                    turn.getMatchController().notifyObservers(
                            new SessionMessage(
                                    turn.getTurnPlayer().getSessionToken(),
                                    MessageType.CHOICE_CARD_TO_BUY,
                                    turn.getTurnPlayer().getPersonalBoard().getAllAvailableResources().toArray(new Object[0])
                            ));

                    break;
                case CHOICE_CARD_TO_BUY:
                    if (getAvailableCard().size() > 0) {
                        if (getAvailableCard().contains((DevelopmentCard) message.getPayload())) {

                            try {

                                boughtCard = buyCard((DevelopmentCard) message.getPayload());

                            } catch (NegativeNumberOfElementsToGrabException e) {
                                e.printStackTrace();
                            }

                            turn.getMatchController().notifyObservers(
                                    new MultipleChoicesMessage(
                                            turn.getTurnPlayer().getSessionToken(),
                                            CHOICE_POSITION,
                                            "Choose where to place the new card:",
                                            1, 1,
                                            false,
                                            positionsForCard().toArray(new Object[0])
                                    ));
                        } else {

                            turn.getMatchController().notifyObservers(
                                    new SessionMessage(
                                            turn.getTurnPlayer().getSessionToken(),
                                            MessageType.CHOICE_CARD_TO_BUY,
                                            turn.getMatchController().getMatch().getDevelopmentGrid()
                                    ));
                            turn.getMatchController().notifyObservers(
                                    new SessionMessage(
                                            turn.getTurnPlayer().getSessionToken(),
                                            MessageType.CHOICE_CARD_TO_BUY,
                                            turn.getTurnPlayer().getPersonalBoard().getAllAvailableResources().toArray(new Object[0])
                                    ));

                        }
                    } else {

                        sendGeneralMessage(turn, "No available cards to buy - sending to choose action:");
                        turn.changeState(new ChooseNormalActionTurnState(turn));
                        turn.play(message);

                    }

                    break;

                case CHOICE_POSITION:

                    placeCard((int) message.getPayload());
                    turn.changeState(new EndMatchCheckerTurnState(turn));
                    turn.play(message);
                    break;

                case QUIT_OPTION_SELECTED:
                default:

                    turn.changeState(new ChooseNormalActionTurnState(turn));
                    turn.play(message);

                    break;

            }
        } catch (EmptyPayloadException | InvalidPayloadException ignored) {
            // TODO: handle exception
        }
    }


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
                            < (card.getCost().get(resource).intValue() - tempResources.stream().filter(x -> x.equals(resource)).count()))
                        isAvailable = false;
                }
            }

            if (isAvailable && feasibleLevels.contains(card.getDevelopmentCardType().getLevel().getLevelNumber()))
                availableCard.add(card);
            isAvailable = true;
        }
        return availableCard;

    }

    private DevelopmentCard buyCard(DevelopmentCard playerCard) throws NegativeNumberOfElementsToGrabException {
        DevelopmentCard drawnCard = null;
        int numberResources = 0;
        int i = 0;

        try {
            drawnCard = turn.getMatchController().getMatch().getDevelopmentGrid().drawCard(playerCard.getDevelopmentCardType().getColor(), playerCard.getDevelopmentCardType().getLevel());

        } catch (LevelDoesNotExistException | ColorDoesNotExistException | NoMoreDevelopmentCardsException e) { }

        for (Resource resource : drawnCard.getCost().keySet()) {

            numberResources = drawnCard.getCost().get(resource) - (int) tempResources.stream().filter(x -> x.equals(resource)).count();
            numberResources -= turn.getTurnPlayer().getPersonalBoard().getWarehouse().grabResources(resource, numberResources).size();
            if (numberResources > 0)
                turn.getTurnPlayer().getPersonalBoard().grabResourcesFromStrongbox(resource, numberResources);

        }
        return drawnCard;


    }

    private List<Integer> positionsForCard() {
        List<Integer> CorrectPositions = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            if (turn.getTurnPlayer().getPersonalBoard().isCardPlaceable(i, boughtCard)) CorrectPositions.add(i);
        }
        return CorrectPositions;
    }

    private void placeCard(int position) {
        try {

            turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(position, boughtCard);

        } catch (CanNotAddDevelopmentCardToSlotException | DevelopmentCardSlotOutOfBoundsException e) {

            try {
                turn.getMatchController().notifyObservers(
                        new MultipleChoicesMessage(
                                turn.getTurnPlayer().getSessionToken(),
                                CHOICE_POSITION,
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

    public List<Resource> getTempResources() {
        return tempResources;
    }


}
