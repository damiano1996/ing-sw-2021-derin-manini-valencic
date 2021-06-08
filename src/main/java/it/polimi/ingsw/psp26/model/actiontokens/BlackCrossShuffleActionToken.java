package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.exceptions.MustShuffleActionTokenStackException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;

public class BlackCrossShuffleActionToken implements ActionToken {

    /**
     * Executes the action of the Action Token
     * In this case, it gives 1 point to the Black Cross marker and throws an exception telling that
     * the Action Tokens stack must be refilled and shuffled
     *
     * @param faithTrack           The Faith Track where the Black Cross is contained
     * @param developmentCardsGrid The Development Grid where the Tokens can discard Development Cards
     * @throws MustShuffleActionTokenStackException The Action Token stack must be refilled and shuffled
     */
    @Override
    public void execute(FaithTrack faithTrack, DevelopmentCardsGrid developmentCardsGrid) throws MustShuffleActionTokenStackException {
        faithTrack.moveBlackCrossPosition(1);
        throw new MustShuffleActionTokenStackException();
    }


    /**
     * Getter of the Token's name
     * 
     * @return The Token's name
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
