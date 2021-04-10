package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.*;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BuyCardNormalActionTurnState extends TurnState {
    public BuyCardNormalActionTurnState(Turn turn) {
        super(turn);
    }

    public void play(Message message) {
        List<Resource> playerResources = getPlayerResources(turn.getTurnPlayer());
        List<DevelopmentCard> playerCards = turn.getTurnPlayer().getPersonalBoard().getVisibleDevelopmentCards();
        getAvailableCard(turn.getMatch(), playerResources, playerCards);
        //WaitPlayerAction
        //buyCard(Message); // State 1
        //placeCard(Message); // State 2

    }

    private List<Resource> getPlayerResources(Player player) {
        List<Resource> resources = new ArrayList<>();
        player.getPersonalBoard().getWarehouseDepots().stream().forEach(x -> resources.addAll(x.getResources()));
        resources.addAll(player.getPersonalBoard().getStrongbox());
        return resources;
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

    private void buyCard(Color color, Level level, Player player) throws NegativeNumberOfElementsToGrabException {
        DevelopmentCard drawnCard = null;
        int numberResources = 0;
        int i = 0;

        try {
            drawnCard = turn.getMatch().getDevelopmentGrid().drawCard(color, level);
        } catch (LevelDoesNotExistException | ColorDoesNotExistException | NoMoreDevelopmentCardsException e) {
            System.out.print("Error"); // To improve
        }
        for (Resource resource : drawnCard.getCost().keySet()) {
            numberResources = drawnCard.getCost().get(resource);
            numberResources -= player.getPersonalBoard().grabResourcesFromWarehouse(resource, numberResources).size();
            if (numberResources > 0) player.getPersonalBoard().grabResourcesFromStrongbox(resource, numberResources);
        }


    }

    private void placeCard(Message message, DevelopmentCard drawnCard) {
        int i = 0; //Message body give position information
        try {
            turn.getTurnPlayer().getPersonalBoard().addDevelopmentCard(i, drawnCard);
        } catch (CanNotAddDevelopmentCardToSlotException | DevelopmentCardSlotOutOfBoundsException e) {
            System.out.println("The position chosen is not correct, choose another one");
            //NotifyObserver();
        }

    }


}
