package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.normalactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.TurnState;
import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.personalboard.Depot;

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
        //buyCard() It needs player to specify if from depot or chest?


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

    private void buyCard(Color color, Level level, Player player) {
        DevelopmentCard drawnCard = null;
        try {
            drawnCard = turn.getMatch().getDevelopmentGrid().drawCard(color, level);
        } catch (LevelDoesNotExistException | ColorDoesNotExistException | NoMoreDevelopmentCardsException e) {
            System.out.print("Error"); // To improve
        }
        int numberResources = 0;
        for (Resource resource : drawnCard.getCost().keySet()) {
            numberResources = drawnCard.getCost().get(resource);
            for (Depot depot : player.getPersonalBoard().getWarehouseDepots()) {
                if (!depot.getResources().isEmpty() && depot.getResources().get(0).equals(resource)) {
                    try {
                        depot.removeResource(Math.min(depot.getResources().size(), numberResources));
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Tried to remove to many resources");
                    }
                    numberResources = numberResources - Math.min(depot.getResources().size(), numberResources);
                }
            }
            for (int i = 0; i < numberResources; i++) {
                player.getPersonalBoard().getStrongbox().remove(resource);
            }
        }


    }

}
