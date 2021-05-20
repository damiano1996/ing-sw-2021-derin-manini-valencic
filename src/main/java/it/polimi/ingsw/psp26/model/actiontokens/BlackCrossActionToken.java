package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;

public class BlackCrossActionToken implements ActionToken {

    @Override
    public void execute(FaithTrack faithTrack, DevelopmentCardsGrid developmentCardsGrid) {
        faithTrack.moveBlackCrossPosition(2);
    }

    /**
     * Returns the Token's name
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
