package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;

public class BlackCrossActionToken implements ActionToken {

    /**
     * Executes the action of the Action Token
     * In this case, it gives 2 points to the Black Cross marker
     *
     * @param faithTrack           The Faith Track where the Black Cross is contained
     * @param developmentCardsGrid The Development Grid where the Tokens can discard Development Cards
     */
    @Override
    public void execute(FaithTrack faithTrack, DevelopmentCardsGrid developmentCardsGrid) {
        faithTrack.moveBlackCrossPosition(2);
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
