package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;
import it.polimi.ingsw.psp26.model.personalboard.PersonalBoard;

import java.util.List;

/**
 * Class modeling the player.
 */
public class Player {

    private final String nickname;
    private final String sessionToken;
    private final PersonalBoard personalBoard;

    private boolean inkwell;
    private List<LeaderCard> leaderCards;

    /**
     * Constructor of the player.
     *
     * @param nickname     nickname of the player
     * @param sessionToken token assigned to the player to be uniquely distinguished
     */
    public Player(String nickname, String sessionToken) {
        this.nickname = nickname;
        this.sessionToken = sessionToken;
        personalBoard = new PersonalBoard();
        inkwell = false;
    }

    /**
     * To get the player's nickname.
     *
     * @return nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * To get the session token that has been assigned to the player.
     *
     * @return the token of the player
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * To get the personal board of the player.
     *
     * @return the personal board of the player
     */
    public PersonalBoard getPersonalBoard() {
        return personalBoard;
    }

    /**
     * To get the leader cards that the players has.
     *
     * @return the list containing the leader cards of the player
     */
    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    /**
     * To set the drawn leader cards.
     *
     * @param leaderCards list of leader cards that the player chose
     */
    public void setLeaderCards(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    /**
     * To assign to the player the inkwell.
     */
    public void giveInkwell() {
        inkwell = true;
    }

    /**
     * To check if the player has the inkwell.
     *
     * @return true if the player has the inkwell, false otherwise
     */
    public boolean isInkwell() {
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
}
