package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.exceptions.NegativeNumberOfCardsToDrawException;
import it.polimi.ingsw.psp26.exceptions.NotEnoughCardsToDrawException;
import it.polimi.ingsw.psp26.exceptions.PlayerDoesNotExistException;

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
        actionTokenStack = new ArrayList<>(); // To be initialized

        initializeLeaderDeck();
    }

    private void initializeLeaderDeck() {
        // Temporary solution
        for (int i = 0; i < 16; i++)
            leaderDeck.add(new LeaderCard(new ArrayList<>(), new ArrayList<>(), 0, new SpecialAbility()));
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

    public List<LeaderCard> getLeaderDeck() {
        return leaderDeck;
    }

    public List<LeaderCard> drawLeaders(int numberOfCards) throws NegativeNumberOfCardsToDrawException, NotEnoughCardsToDrawException {
        if (numberOfCards < 0) throw new NegativeNumberOfCardsToDrawException();

        List<LeaderCard> drawnLeaderCards = leaderDeck.subList(0, numberOfCards);
        if (drawnLeaderCards.size() < numberOfCards) throw new NotEnoughCardsToDrawException();

        leaderDeck.removeAll(drawnLeaderCards);
        return drawnLeaderCards;
    }

    public void shuffleLeaderDeck() {
        Collections.shuffle(leaderDeck);
    }

    public List<ActionToken> getActionTokenStack() {
        return actionTokenStack;
    }

    public void shuffleActionTokenStack() {
        Collections.shuffle(actionTokenStack);
    }
}
