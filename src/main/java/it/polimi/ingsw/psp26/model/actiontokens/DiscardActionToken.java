package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LorenzoWinException;
import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardsGrid;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;

/**
 * The class representing the Lorenzo's DiscardActionToken.
 */
public class DiscardActionToken implements ActionToken {

    private final Color colorToDiscard;

    /**
     * Constructor of the class.
     * It sets the Color of the DevelopmentCards the ActionToken will discard when executed.
     *
     * @param colorToDiscard the Color of the DevelopmentCards the ActionToken will discard when executed
     */
    public DiscardActionToken(Color colorToDiscard) {
        this.colorToDiscard = colorToDiscard;
    }


    /**
     * Executes the action of the ActionToken.
     * In this case, discards 2 cards from the DevelopmentCardGrid. The attribute colorToDiscard will tell what
     * Color of the DevelopmentCards will be discarded.
     * The method tries to discard from the first Level; if no cards are available, proceeds to the higher Levels, up to the third.
     * The ColorDoesNotExistException and LevelDoesNotExistException are thrown since drawCards() is used in the auxiliary methods.
     *
     * @param faithTrack           the FaithTrack where the BlackCross is contained
     * @param developmentCardsGrid the DevelopmentGrid where the ActionTokens can discard DevelopmentCards
     * @throws ColorDoesNotExistException the Color of the DevelopmentCard doesn't exist
     * @throws LevelDoesNotExistException the Level of the DevelopmentCard doesn't exist
     * @throws LorenzoWinException        all the cards of a Color have been discarded (e.g. all the yellow cards have been
     *                                    discarded: Lorenzo wins the game)
     */
    @Override
    public void execute(FaithTrack faithTrack, DevelopmentCardsGrid developmentCardsGrid) throws ColorDoesNotExistException, LevelDoesNotExistException, LorenzoWinException {
        int cardsToRemove = 2;

        //Try removing cards from first level slot
        cardsToRemove = removeFirstAndSecondLevel(cardsToRemove, developmentCardsGrid, Level.FIRST);

        //If there are cards left to remove, proceed to the second level slot
        if (cardsToRemove > 0) {
            cardsToRemove = removeFirstAndSecondLevel(cardsToRemove, developmentCardsGrid, Level.SECOND);

            //If there are cards left to remove, proceed to the third level slot
            if (cardsToRemove > 0) {
                removeThirdLevel(cardsToRemove, developmentCardsGrid);
            }
        }
    }


    /**
     * Used by the ActionToken to remove cards from First, Second or both DevelopmentGrid levels.
     *
     * @param cardsToRemove        the number of cards to remove
     * @param developmentCardsGrid the grid where the cards are removed
     * @param level                the desired level of cards to be removed
     * @return the number of cards left to remove
     * @throws ColorDoesNotExistException there are no longer DevelopmentCards of the Color the ActionToken tried to remove
     * @throws LevelDoesNotExistException there are no longer DevelopmentCards of the Level the ActionToken tried to remove
     */
    private int removeFirstAndSecondLevel(int cardsToRemove, DevelopmentCardsGrid developmentCardsGrid, Level level) throws ColorDoesNotExistException, LevelDoesNotExistException {
        int removedCards = 2 - cardsToRemove;
        try {
            for (int i = 0; i < cardsToRemove; i++) {
                developmentCardsGrid.drawCard(colorToDiscard, level);
                removedCards++;
            }
            cardsToRemove = 0;
        } catch (NoMoreDevelopmentCardsException e) {
            cardsToRemove -= removedCards;
        }
        return cardsToRemove;
    }


    /**
     * Used by the ActionToken to remove cards from the Third DevelopmentGrid levels.
     *
     * @param cardsToRemove        the number of cards to remove
     * @param developmentCardsGrid the grid where the cards are removed
     * @throws ColorDoesNotExistException there are no longer DevelopmentCards of the Color the ActionToken tried to remove
     * @throws LevelDoesNotExistException there are no longer DevelopmentCards of the Level the ActionToken tried to remove
     * @throws LorenzoWinException        the token draws all the cards from Third Level. Lorenzo wins the game
     */
    private void removeThirdLevel(int cardsToRemove, DevelopmentCardsGrid developmentCardsGrid) throws ColorDoesNotExistException, LevelDoesNotExistException, LorenzoWinException {
        try {
            for (int i = 0; i < cardsToRemove; i++) {
                developmentCardsGrid.drawCard(colorToDiscard, Level.THIRD);
            }
            if (!developmentCardsGrid.isAvailable(colorToDiscard, Level.THIRD)) throw new LorenzoWinException();
        } catch (NoMoreDevelopmentCardsException e) {
            throw new LorenzoWinException();
        }
    }


    /**
     * Getter of the ActionToken's name.
     *
     * @return the Token's name
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "-" + colorToDiscard.getName();
    }

}
