package it.polimi.ingsw.psp26.controller.turns.actions.leader;

import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.leadercards.LeaderCard;

public abstract class LeaderAction {

    private Match match;
    private Player player;
    private LeaderCard leaderCard;

    public void play(Match match, Player player, LeaderCard leaderCard) {
    }
}
