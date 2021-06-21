package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.exceptions.MustShuffleActionTokenStackException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;

/**
 * The class representing the Lorenzo's BlackCrossShuffleActionToken.
 */
public class BlackCrossShuffleActionToken implements ActionToken {

    /**
     * Executes the action of the ActionToken.
     * In this case, it gives 1 point to the BlackCross marker and throws an exception telling that
     * the ActionTokens stack must be refilled and shuffled.
     *
     * @param faithTrack           the FaithTrack where the BlackCross is contained
     * @param developmentCardsGrid the DevelopmentGrid where the ActionTokens can discard DevelopmentCards
     * @throws MustShuffleActionTokenStackException the ActionToken stack must be refilled and shuffled
     */
    @Override
    public void execute(FaithTrack faithTrack, DevelopmentCardsGrid developmentCardsGrid) throws MustShuffleActionTokenStackException {
        faithTrack.moveBlackCrossPosition(1);
        throw new MustShuffleActionTokenStackException();
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
