package it.polimi.ingsw.psp26.controller.turns;

import it.polimi.ingsw.psp26.controller.turns.actions.leader.LeaderAction;
import it.polimi.ingsw.psp26.controller.turns.actions.normal.NormalAction;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;

public class Turn {

    private final Match match;
    private final Player turnPlayer;
    private NormalAction normalAction;
    private LeaderAction leaderAction;

    public Turn(Match match, Player turnPlayer) {
        this.match = match;
        this.turnPlayer = turnPlayer;
    }

    public void chooseNormalAction() {
        // temporary solution
        normalAction = null;
    }

    public void chooseLeaderAction() {
        // temporary solution
        leaderAction = null;
    }

    public void playNormalAction() {
        // to be implemented
    }

    public void playLeaderAction() {
        // to be implemented
    }

    public void checkVaticanReport() {
        // to be implemented
    }

    public Player getTurnPlayer() {
        return turnPlayer;
    }
}
