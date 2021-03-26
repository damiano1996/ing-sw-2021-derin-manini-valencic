package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.exceptions.NegativeNumberOfElementsToDrawException;
import it.polimi.ingsw.psp26.exceptions.PlayerDoesNotExistException;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.actiontokens.DiscardActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.ProductionAbility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Match extends Observable {

    private final int id;
    private final List<Player> players;
    private final ResourceSupply resourceSupply;
    private final DevelopmentGrid developmentGrid;
    private final MarketTray marketTray;
    private final List<LeaderCard> leaderDeck;
    private final List<ActionToken> actionTokenStack;

    public Match(int id, List<Player> players) {
        this.id = id;
        this.players = players;
        resourceSupply = new ResourceSupply();
        developmentGrid = new DevelopmentGrid();
        marketTray = new MarketTray();
        leaderDeck = new ArrayList<>();
        actionTokenStack = new ArrayList<>();

        initializeLeaderDeck();
        initializeActionTokenStack();
    }

    private void initializeLeaderDeck() {
        // Temporary solution
        for (int i = 0; i < 16; i++)
            leaderDeck.add(new LeaderCard(new ArrayList<>(), new ArrayList<>(), 0, new ProductionAbility()));

        shuffleLeaderDeck();
    }

    private void initializeActionTokenStack() {
        // Temporary solution
        for (int i = 0; i < 7; i++) {
            actionTokenStack.add(new DiscardActionToken());
        }

        shuffleActionTokenStack();
    }

    public int getId() {
        return id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isMultiPlayerMode() {
        return players.size() > 1;
    }

    public Player getPlayerByNickname(String nickname) throws PlayerDoesNotExistException {
        return players.stream()
                .filter(x -> x.getNickname().equals(nickname))
                .reduce((a, b) -> b)
                .orElseThrow(PlayerDoesNotExistException::new);
    }

    public ResourceSupply getResourceSupply() {
        return resourceSupply;
    }

    public DevelopmentGrid getDevelopmentGrid() {
        return developmentGrid;
    }

    public MarketTray getMarketTray() {
        return marketTray;
    }

    private <T> List<T> drawElements(List<T> list, int numberOfElements) throws NegativeNumberOfElementsToDrawException, IndexOutOfBoundsException {
        if (numberOfElements < 0) throw new NegativeNumberOfElementsToDrawException();

        List<T> drawnElements = new ArrayList<>(list.subList(0, numberOfElements));

        list.removeAll(drawnElements);
        return drawnElements;
    }

    public List<LeaderCard> drawLeaders(int numberOfCards) throws NegativeNumberOfElementsToDrawException, IndexOutOfBoundsException {
        return drawElements(leaderDeck, numberOfCards);
    }

    public List<ActionToken> drawActionTokens(int numberOfTokens) throws NegativeNumberOfElementsToDrawException, IndexOutOfBoundsException {
        return drawElements(actionTokenStack, numberOfTokens);
    }

    private void shuffleLeaderDeck() {
        Collections.shuffle(leaderDeck);
    }

    private void shuffleActionTokenStack() {
        Collections.shuffle(actionTokenStack);
    }
}
