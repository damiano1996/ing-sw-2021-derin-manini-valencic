package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.application.messages.MessageType;
import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.application.observer.Observable;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class modeling the player.
 */
public class Player extends Observable<SessionMessage> {

    private transient final VirtualView virtualView;

    private final String nickname;
    private final String sessionToken;
    private final PersonalBoard personalBoard;

    private boolean inkwell;
    private List<LeaderCard> leaderCards;

    /**
     * Constructor of the player.
     *
     * @param virtualView  virtual view to be added to the observers list
     * @param nickname     nickname of the player
     * @param sessionToken token assigned to the player to be uniquely distinguished
     */
    public Player(VirtualView virtualView, String nickname, String sessionToken) {
        super();
        addObserver(virtualView);
        this.virtualView = virtualView;

        this.nickname = nickname;
        this.sessionToken = sessionToken;
        personalBoard = new PersonalBoard(virtualView, this);
        inkwell = false;
        leaderCards = new ArrayList<>();

        // notifyObservers(new Message());
        notifyObservers(new SessionMessage(sessionToken, MessageType.PERSONAL_BOARD, this));
    }

    /**
     * Getter of the player's nickname.
     *
     * @return nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter of the session token that has been assigned to the player.
     *
     * @return the token of the player
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * Getter of the personal board of the player.
     *
     * @return the personal board of the player
     */
    public PersonalBoard getPersonalBoard() {
        return personalBoard;
    }

    /**
     * Getter of the leader cards that the players has.
     *
     * @return the list containing the leader cards of the player
     */
    public List<LeaderCard> getLeaderCards() {
        return Collections.unmodifiableList(leaderCards);
    }

    /**
     * Setter of the drawn leader cards.
     *
     * @param leaderCards list of leader cards that the player chose
     */
    public void setLeaderCards(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
        // notifyObservers(new Message()); // TODO: to be completed
    }

    /**
     * Method to discard a leader card.
     *
     * @param leaderCard leader card to discard
     */
    public void discardLeaderCard(LeaderCard leaderCard) {
        leaderCards.remove(leaderCard);
    }

    /**
     * To assign to the player the inkwell.
     */
    public void giveInkwell() {
        inkwell = true;
        // notifyObservers(new Message()); // TODO: to be completed
    }

    /**
     * To check if the player has the inkwell.
     *
     * @return true if the player has the inkwell, false otherwise
     */
    public boolean hasInkwell() {
        return inkwell;
    }

    /**
     * To check if the player has leader cards that can be used.
     *
     * @return true if the player has leader cards that can be played
     */
    public boolean isLeaderActionPlayable() {
        return leaderCards
                .stream()
                .anyMatch(x -> !x.isActive());
    }

    /**
     * Getter for the virtual view.
     *
     * @return virtual view
     */
    public VirtualView getVirtualView() {
        return virtualView;
    }
}
