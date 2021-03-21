package it.polimi.ingsw.psp26.model;

import java.util.List;

public class Player {

    private final String nickname;
    private final String sessionToken;
    private final PersonalBoard personalBoard;
    private boolean inkwell;

    private List<LeaderCard> leaderCards;

    public Player(String nickname, String sessionToken) {
        this.nickname = nickname;
        this.sessionToken = sessionToken;
        personalBoard = new PersonalBoard();
        inkwell = false;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public PersonalBoard getPersonalBoard() {
        return personalBoard;
    }

    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public void giveInkwell() {
        inkwell = true;
    }

    public boolean isInkwell() {
        return inkwell;
    }

    public boolean isLeaderActionPlayable() {
        boolean isLeaderNotActive = leaderCards.stream().anyMatch(x -> !x.isActive());
        return leaderCards.size() > 1 || isLeaderNotActive;
    }
}
