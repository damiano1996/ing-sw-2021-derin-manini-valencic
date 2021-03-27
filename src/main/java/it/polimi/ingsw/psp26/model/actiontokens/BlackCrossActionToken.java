package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;

public class BlackCrossActionToken implements ActionToken {
    @Override
    public void execute(FaithTrack faithTrack, DevelopmentGrid developmentGrid) {
        faithTrack.moveBlackCrossPosition(2);
    }
}
