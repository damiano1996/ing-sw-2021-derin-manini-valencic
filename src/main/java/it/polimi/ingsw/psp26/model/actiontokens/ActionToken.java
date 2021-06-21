package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LorenzoWinException;
import it.polimi.ingsw.psp26.exceptions.MustShuffleActionTokenStackException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;

/**
 * Interface that contains the method that executes the ActionTokens action.
 */
public interface ActionToken {

    /**
     * Executes the action of the calling ActionToken.
     *
     * @param faithTrack           the FaithTrack where the ActionToken will take effect
     * @param developmentCardsGrid the DevelopmentCardsGrid where the ActionToken will take effect
     * @throws MustShuffleActionTokenStackException the ActionToken stack must be refilled and shuffled
     * @throws ColorDoesNotExistException           the Color of the Development Card doesn't exist
     * @throws LevelDoesNotExistException           the Level of the Development Card doesn't exist
     * @throws LorenzoWinException                  all the cards of a Color have been discarded (e.g. all the yellow cards have been
     *                                              discarded: Lorenzo wins the game)
     */
    void execute(FaithTrack faithTrack, DevelopmentCardsGrid developmentCardsGrid) throws MustShuffleActionTokenStackException, ColorDoesNotExistException, LevelDoesNotExistException, LorenzoWinException;

}
