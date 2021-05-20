package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.exceptions.MustShuffleActionTokenStackException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;

public class BlackCrossShuffleActionToken implements ActionToken {

    @Override
    public void execute(FaithTrack faithTrack, DevelopmentCardsGrid developmentCardsGrid) throws MustShuffleActionTokenStackException {
        faithTrack.moveBlackCrossPosition(1);
        throw new MustShuffleActionTokenStackException();
    }

    /**
     * Returns the Token's name
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
