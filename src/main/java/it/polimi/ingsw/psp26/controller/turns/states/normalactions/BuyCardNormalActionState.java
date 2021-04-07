package it.polimi.ingsw.psp26.controller.turns.states.normalactions;

import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.controller.turns.Turn;
import it.polimi.ingsw.psp26.controller.turns.states.TurnState;
import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCard;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.enums.Resource;

import java.util.ArrayList;
import java.util.List;

public class BuyCardNormalActionState extends TurnState {
    public BuyCardNormalActionState(Turn turn) {
        super(turn);
    }

    public void play(Message message) {
        List<Resource> playerResources = getPlayerResources(turn.getTurnPlayer());
        List<DevelopmentCard> playerCard = turn.getTurnPlayer().getPersonalBoard().getVisibleDevelopmentCards();
        getAvailableCard(turn.getMatch(), playerResources, playerCard);
        //WaitPlayerAction
        //buyCard() It needs player to specify if from depot or chest


    }

    private List<Resource> getPlayerResources(Player player) {
        List<Resource> resources = new ArrayList<>();
        player.getPersonalBoard().getWarehouseDepots().stream().forEach(x -> resources.addAll(x.getResources()));
        resources.addAll(player.getPersonalBoard().getStrongbox());
        return resources;
    }

    private List<DevelopmentCard> getAvailableCard(Match match, List<Resource> resources, List<DevelopmentCard> cards) {
        List<DevelopmentCard> availableCard = new ArrayList<>();

        boolean isAvailable = true;
        for (DevelopmentCard card : match.getDevelopmentGrid().getAllVisibleCards()) {
            for (Resource resource : card.getCost().keySet()) {
                if (isAvailable) {
                    if (resources.stream().filter(x -> x.equals(resource)).count() < card.getCost().get(resource).intValue())
                        isAvailable = false;
                }
            }
            if (isAvailable) availableCard.add(card);
            isAvailable = true;
        }
        return availableCard;

    }

    private void buyCard(Color color, Level level, Match match) {
        DevelopmentCard drawnCard = null;
        try {
            drawnCard = match.getDevelopmentGrid().drawCard(color, level);
        } catch (LevelDoesNotExistException | ColorDoesNotExistException | NoMoreDevelopmentCardsException e) {
            System.out.print("Error"); // To improve

        }
        for (Resource resource : drawnCard.getCost().keySet()) {

        }

    }

}
