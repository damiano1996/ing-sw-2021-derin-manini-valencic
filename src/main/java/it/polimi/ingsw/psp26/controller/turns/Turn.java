package it.polimi.ingsw.psp26.controller.turns;

import it.polimi.ingsw.psp26.controller.turns.states.TurnState;
import it.polimi.ingsw.psp26.model.Match;
import it.polimi.ingsw.psp26.model.Player;

public class Turn {

    private final Match match;
    private final int turnNumber;
    private final Player turnPlayer;

    private TurnState turnState;

    public Turn(Match match, Player turnPlayer, int turnNumber) {
        this.match = match;
        this.turnPlayer = turnPlayer;
        this.turnNumber = turnNumber;
    }

    public void firstTurnBenefits() {
        switch (turnNumber) {
            case 0:
                turnPlayer.giveInkwell();
                break;
            case 1:
                // TODO: choice 1 extra resource
                break;
            case 2:
                // TODO choice 1 extra resource + 1 FP
                break;
            case 3:
                // TODO choice 2 extra resources + 1 FP
                break;
        }
    }

    public void checkVaticanReport() {
        // to be implemented
    }

    public Player getTurnPlayer() {
        return turnPlayer;
    }

    public int getTurnNumber() {
        return turnNumber;
    }
}
