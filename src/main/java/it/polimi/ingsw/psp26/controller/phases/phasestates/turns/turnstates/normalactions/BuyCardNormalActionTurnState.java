package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.MultipleChoicesMessage;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.CheckVaticanReportTurnState;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.psp26.application.messages.MessageType.CHOICE_POSITION;
import static it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.commons.MatchUtils.getAllPlayerResources;

public class BuyCardNormalActionTurnState extends TurnState {
    DevelopmentCard boughtCard = null;

    public BuyCardNormalActionTurnState(Turn turn) {
        super(turn);
    }

    public void play(SessionMessage message) {
        super.play(message);

        switch (message.getMessageType()) {
            case BUY_CARD:
                List<Resource> playerResources = getAllPlayerResources(turn.getTurnPlayer());
                List<DevelopmentCard> playerCards = turn.getTurnPlayer().getPersonalBoard().getVisibleDevelopmentCards();
                List<DevelopmentCard> gettableCards = getAvailableCard(turn.getMatchController().getMatch(), playerResources, playerCards);
                turn.getMatchController().notifyObservers(new MultipleChoicesMessage(turn.getTurnPlayer().getSessionToken(),
                        MessageType.CHOICE_CARD_TO_BUY, 1, 1,
                        gettableCards.toArray(new Object[0])
                ));
                break;
            case CARD_TO_BUY_CHOSEN:
                try {
                    boughtCard = buyCard((DevelopmentCard) message.getPayload(), turn.getTurnPlayer());
                } catch (NegativeNumberOfElementsToGrabException e) {
                    e.printStackTrace();
                }
                turn.getMatchController().notifyObservers(new MultipleChoicesMessage(turn.getTurnPlayer().getSessionToken(),
                        CHOICE_POSITION, 1, 1, positionsForCard().toArray(new Object[0])
                ));
                break;

            case POSITION_CHOSEN:
                placeCard((int) message.getPayload());
                turn.changeState(new CheckVaticanReportTurnState(turn));
                turn.play(message);
                break;

            default:
                new SessionMessage(
                        turn.getTurnPlayer().getSessionToken(),
                        MessageType.CHOICE_NORMAL_ACTION);

        }
    }


    private List<DevelopmentCard> getAvailableCard(Match match, List<Resource> resources, List<DevelopmentCard> playerCard) {
        List<DevelopmentCard> availableCard = new ArrayList<>();
        List<Integer> feasibleLevels = playerCard.stream().map(x -> x.getDevelopmentCardType().getLevel().getLevelNumber() + 1).distinct().collect(Collectors.toList());
        boolean isAvailable = true;
        for (DevelopmentCard card : match.getDevelopmentGrid().getAllVisibleCards()) {
            for (Resource resource : card.getCost().keySet()) {
                if (isAvailable) {
                    if (resources.stream().filter(x -> x.equals(resource)).count() < card.getCost().get(resource).intValue())
                        isAvailable = false;
                }
            }
            if (isAvailable && feasibleLevels.contains(card.getDevelopmentCardType().getLevel().getLevelNumber()))
                availableCard.add(card);
            isAvailable = true;
        }
        return availableCard;

    }

    private DevelopmentCard buyCard(DevelopmentCard playerCard, Player player) throws NegativeNumberOfElementsToGrabException {
        DevelopmentCard drawnCard = null;
        int numberResources = 0;
        int i = 0;

        try {
            drawnCard = turn.getMatchController().getMatch().getDevelopmentGrid().drawCard(playerCard.getDevelopmentCardType().getColor(), playerCard.getDevelopmentCardType().getLevel());
        } catch (LevelDoesNotExistException | ColorDoesNotExistException | NoMoreDevelopmentCardsException e) {
        }
        for (Resource resource : drawnCard.getCost().keySet()) {
            numberResources = drawnCard.getCost().get(resource);
            numberResources -= player.getPersonalBoard().grabResourcesFromWarehouse(resource, numberResources).size();
            if (numberResources > 0) player.getPersonalBoard().grabResourcesFromStrongbox(resource, numberResources);
        }
        return drawnCard;


    }

    private List<Integer> positionsForCard() {
        List<Integer> CorrectPositions = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            CorrectPositions.add(i);
            try {
                turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(i, boughtCard);
            } catch (CanNotAddDevelopmentCardToSlotException | DevelopmentCardSlotOutOfBoundsException e) {
                CorrectPositions.remove(i);
            }
        }
        return CorrectPositions;
    }

    private void placeCard(int position) {
        try {
            turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(position, boughtCard);
        } catch (CanNotAddDevelopmentCardToSlotException | DevelopmentCardSlotOutOfBoundsException e) {
            System.out.println("The position chosen is not correct, choose another one");
            turn.getMatchController().notifyObservers(new MultipleChoicesMessage(turn.getTurnPlayer().getSessionToken(),
                    CHOICE_POSITION, 1, 1, positionsForCard().toArray(new Object[0])
            ));
        }

    }


}
