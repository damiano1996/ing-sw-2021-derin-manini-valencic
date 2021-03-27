package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.exceptions.MustShuffleActionTokenStackException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;

public class BlackCrossShuffleActionToken implements ActionToken {
    @Override
    public void execute(FaithTrack faithTrack, DevelopmentGrid developmentGrid) throws MustShuffleActionTokenStackException {
        faithTrack.moveBlackCrossPosition(1);
        throw new MustShuffleActionTokenStackException();
    }
}
