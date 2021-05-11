package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
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
import java.util.Objects;

import static it.polimi.ingsw.psp26.utils.ArrayListUtils.grabElements;

/**
 * Class that models the whole game
 */
public class Match extends Observable<SessionMessage> {

    private final int id;
    private final List<Player> players;
    private final ResourceSupply resourceSupply;
    private final DevelopmentGrid developmentGrid;
    private final MarketTray marketTray;
    private final List<LeaderCard> leaderDeck;
    private final List<ActionToken> actionTokenStack;

    /**
     * Class constructor.
     * It initializes the components of the match. Moreover, it shuffles the leader deck and the token stack.
     *
     * @param virtualView virtual view to be added to the observers list
     * @param id          match identifier
     */
    public Match(VirtualView virtualView, int id) {
        super();
        addObserver(virtualView);

        this.id = id;
        this.players = new ArrayList<>();
        resourceSupply = new ResourceSupply();
        developmentGrid = new DevelopmentGrid(virtualView);
        marketTray = new MarketTray(virtualView);
        leaderDeck = new ArrayList<>();
        actionTokenStack = new ArrayList<>();

        initializeLeaderDeck();
        initializeActionTokenStack();
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
    public void initializeActionTokenStack() {
        actionTokenStack.clear();
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
     * Method to add a player to the match.
     *
     * @param player player to add
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Methods to shuffle players.
     */
    public void shufflePlayers() {
        Collections.shuffle(players);
    }

    /**
     * Getter of the players in the match.
     *
     * @return list of players
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
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
     * Getter of the player object by sessionToken.
     *
     * @param sessionToken sessionToken of the player
     * @return the player object with the corresponding nickname
     * @throws PlayerDoesNotExistException if there aren't players with this nickname
     */
    public Player getPlayerBySessionToken(String sessionToken) throws PlayerDoesNotExistException {
        return players
                .stream()
                .filter(x -> x.getSessionToken().equals(sessionToken))
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
     * To draw a given number of cards from the leader deck.
     *
     * @param numberOfCards number of leader cards to draw
     * @return list containing the drawn leaders
     * @throws IndexOutOfBoundsException if the index of the card to draw is out of bounds
     */
    public List<LeaderCard> drawLeaders(int numberOfCards) throws IndexOutOfBoundsException {
        System.out.println("leader deck size: " + leaderDeck.size());
        return grabElements(leaderDeck, numberOfCards);
    }

    /**
     * To draw a given number of cards from the action token stack.
     *
     * @param numberOfTokens number of tokens to draw
     * @return list containing the drawn tokens
     * @throws IndexOutOfBoundsException if the index of the card to draw is out of bounds
     */
    public List<ActionToken> drawActionTokens(int numberOfTokens) throws IndexOutOfBoundsException {
        return grabElements(actionTokenStack, numberOfTokens);
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

    /**
     * @return The List of ActionTokens
     */
    public List<ActionToken> getActionTokens() {
        return Collections.unmodifiableList(actionTokenStack);
    }

    /**
     * Equals method.
     *
     * @param o object to be compared
     * @return true if the given object is equal to this, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return id == match.id &&
                Objects.equals(players, match.players) &&
                Objects.equals(resourceSupply, match.resourceSupply) &&
                Objects.equals(developmentGrid, match.developmentGrid) &&
                Objects.equals(marketTray, match.marketTray) &&
                Objects.equals(leaderDeck, match.leaderDeck) &&
                Objects.equals(actionTokenStack, match.actionTokenStack);
    }

    /**
     * Hashing method.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, players, resourceSupply, developmentGrid, marketTray, leaderDeck, actionTokenStack);
    }
}
