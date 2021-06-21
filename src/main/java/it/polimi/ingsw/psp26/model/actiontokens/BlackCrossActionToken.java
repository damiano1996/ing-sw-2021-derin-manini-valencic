package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;

/**
 * The class representing the Lorenzo's BlackCrossActionToken.
 */
public class BlackCrossActionToken implements ActionToken {

    /**
     * Executes the action of the BlackCrossActionToken.
     * In this case, it gives 2 points to the BlackCross marker.
     *
     * @param faithTrack           the FaithTrack where the BlackCross is contained
     * @param developmentCardsGrid the DevelopmentGrid where the Tokens can discard DevelopmentCards
     */
    @Override
    public void execute(FaithTrack faithTrack, DevelopmentCardsGrid developmentCardsGrid) {
        faithTrack.moveBlackCrossPosition(2);
    }


    /**
     * Getter of the ActionToken's name.
     *
     * @return the ActionToken's name
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
