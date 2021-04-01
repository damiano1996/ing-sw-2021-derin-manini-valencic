package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.exceptions.NegativeNumberOfElementsToDrawException;
import it.polimi.ingsw.psp26.exceptions.PlayerDoesNotExistException;
import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.model.actiontokens.BlackCrossActionToken;
import it.polimi.ingsw.psp26.model.actiontokens.BlackCrossShuffleActionToken;
import it.polimi.ingsw.psp26.model.actiontokens.DiscardActionToken;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCardsInitializer;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that models the whole game
 */
public class Match extends Observable<Message> {

    private final int id;
    private final List<Player> players;
    private final ResourceSupply resourceSupply;
    private final DevelopmentGrid developmentGrid;
    private final MarketTray marketTray;
    private final List<LeaderCard> leaderDeck;
    private final List<ActionToken> actionTokenStack;

    /**
     * Class constructor.
     *
     * @param virtualView virtual view to be added to the observers list
     * @param id          match identifier
     * @param players     list of players in the match
     */
    public Match(VirtualView virtualView, int id, List<Player> players) {
        super();
        addObserver(virtualView);

        this.id = id;
        this.players = players;
        resourceSupply = new ResourceSupply();
        developmentGrid = new DevelopmentGrid(virtualView);
        marketTray = new MarketTray(virtualView);
        leaderDeck = new ArrayList<>();
        actionTokenStack = new ArrayList<>();

        initializeLeaderDeck();
        initializeActionTokenStack();

        notifyObservers(new Message()); // TODO: to be completed properly
    }

    /**
     * Initialization of the deck containing the leader cards and shuffling.
     */
    private void initializeLeaderDeck() {
        LeaderCardsInitializer initializer = LeaderCardsInitializer.getInstance();
        leaderDeck.addAll(initializer.getLeaderCards());
        shuffleLeaderDeck();
    }

    /**
     * Initialization of the stack containing the action tokens and shuffling.
     */
    private void initializeActionTokenStack() {
        actionTokenStack.add(new BlackCrossActionToken());
        actionTokenStack.add(new BlackCrossActionToken());
        actionTokenStack.add(new BlackCrossShuffleActionToken());
        actionTokenStack.add(new DiscardActionToken(Color.GREEN));
        actionTokenStack.add(new DiscardActionToken(Color.YELLOW));
        actionTokenStack.add(new DiscardActionToken(Color.BLUE));
        actionTokenStack.add(new DiscardActionToken(Color.PURPLE));
        shuffleActionTokenStack();
    }

    /**
     * Getter of the identifier of the match.
     *
     * @return match identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Getter of the players in the match.
     *
     * @return list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * To check if the match is in multi or single player mode.
     *
     * @return true if the match has more than one player.
     */
    public boolean isMultiPlayerMode() {
        return players.size() > 1;
    }

    /**
     * Getter of the player object by nickname.
     *
     * @param nickname nickname of the player
     * @return the player object with the corresponding nickname
     * @throws PlayerDoesNotExistException if there aren't players with this nickname
     */
    public Player getPlayerByNickname(String nickname) throws PlayerDoesNotExistException {
        return players
                .stream()
                .filter(x -> x.getNickname().equals(nickname))
                .reduce((a, b) -> b)
                .orElseThrow(PlayerDoesNotExistException::new);
    }

    /**
     * Getter of the resource supply object.
     *
     * @return the resource supply object
     */
    public ResourceSupply getResourceSupply() {
        return resourceSupply;
    }

    /**
     * Getter of the development grid object.
     *
     * @return the development grid object
     */
    public DevelopmentGrid getDevelopmentGrid() {
        return developmentGrid;
    }

    /**
     * Getter of the market tray object.
     *
     * @return the market tray object
     */
    public MarketTray getMarketTray() {
        return marketTray;
    }

    /**
     * Allows to draw elements from a list, removing them from it.
     *
     * @param list             list containing the elements to draw
     * @param numberOfElements number of elements to draw
     * @param <T>              generic object
     * @return list containing the drawn elements
     * @throws NegativeNumberOfElementsToDrawException if the number of elements to draw is a negative number
     * @throws IndexOutOfBoundsException               if the index of the card to draw is out of bounds
     */
    private <T> List<T> drawElements(List<T> list, int numberOfElements) throws NegativeNumberOfElementsToDrawException, IndexOutOfBoundsException {
        if (numberOfElements < 0) throw new NegativeNumberOfElementsToDrawException();

        List<T> drawnElements = new ArrayList<>(list.subList(0, numberOfElements));

        list.removeAll(drawnElements);
        return drawnElements;
    }

    /**
     * To draw a given number of cards from the leader deck.
     *
     * @param numberOfCards number of leader cards to draw
     * @return list containing the drawn leaders
     * @throws NegativeNumberOfElementsToDrawException if the number of cards to draw is negative
     * @throws IndexOutOfBoundsException               if the index of the card to draw is out of bounds
     */
    public List<LeaderCard> drawLeaders(int numberOfCards) throws NegativeNumberOfElementsToDrawException, IndexOutOfBoundsException {
        return drawElements(leaderDeck, numberOfCards);
    }

    /**
     * To draw a given number of cards from the action token stack.
     *
     * @param numberOfTokens number of tokens to draw
     * @return list containing the drawn tokens
     * @throws NegativeNumberOfElementsToDrawException if the number of cards to draw is negative
     * @throws IndexOutOfBoundsException               if the index of the card to draw is out of bounds
     */
    public List<ActionToken> drawActionTokens(int numberOfTokens) throws NegativeNumberOfElementsToDrawException, IndexOutOfBoundsException {
        return drawElements(actionTokenStack, numberOfTokens);
    }

    /**
     * Shuffles the leader deck.
     */
    private void shuffleLeaderDeck() {
        Collections.shuffle(leaderDeck);
    }

    /**
     * Shuffle the action token stack.
     */
    private void shuffleActionTokenStack() {
        Collections.shuffle(actionTokenStack);
    }
}
