package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LorenzoWinException;
import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;
import it.polimi.ingsw.psp26.view.cli.CliUtils;

import java.io.PrintWriter;

public class DiscardActionToken implements ActionToken {

    private final Color colorToDiscard;
    private final CliUtils cliUtils; //TODO se si trova un metodo per leggere i colori direttamente da file andrà tolta

    public DiscardActionToken(Color colorToDiscard) {
        this.colorToDiscard = colorToDiscard;
        this.cliUtils = new CliUtils(new PrintWriter(System.out));

    }


    @Override
    public void execute(FaithTrack faithTrack, DevelopmentGrid developmentGrid) throws ColorDoesNotExistException, LevelDoesNotExistException, LorenzoWinException {
        int cardsToRemove = 2;

        //Try removing cards from first level slot
        cardsToRemove = removeFirstAndSecondLevel(cardsToRemove, developmentGrid, Level.FIRST);

        //If there are cards left to remove, proceed to the second level slot
        if (cardsToRemove > 0) {
            cardsToRemove = removeFirstAndSecondLevel(cardsToRemove, developmentGrid, Level.SECOND);

            //If there are cards left to remove, proceed to the third level slot
            if (cardsToRemove > 0) {
                removeThirdLevel(cardsToRemove, developmentGrid);
            }
        }
    }

    /**
     * Used by the token to remove cards from First, Second or both DevelopmentGrid levels
     *
     * @param cardsToRemove   the number of cards to remove
     * @param developmentGrid the grid where the cards are removed
     * @param level           the desired level of cards to be removed
     * @return the number of cards left to remove
     * @throws ColorDoesNotExistException There are no longer Development Cards of the Color the Token tried to remove
     * @throws LevelDoesNotExistException There are no longer Development Cards of the Level the Token tried to remove
     */
    private int removeFirstAndSecondLevel(int cardsToRemove, DevelopmentGrid developmentGrid, Level level) throws ColorDoesNotExistException, LevelDoesNotExistException {
        int removedCards = 2 - cardsToRemove;
        try {
            for (int i = 0; i < cardsToRemove; i++) {
                developmentGrid.drawCard(colorToDiscard, level);
                removedCards++;
            }
            cardsToRemove = 0;
        } catch (NoMoreDevelopmentCardsException e) {
            cardsToRemove -= removedCards;
        }
        return cardsToRemove;
    }

    /**
     * Used by the token to remove cards from the Third DevelopmentGrid levels
     *
     * @param cardsToRemove   the number of cards to remove
     * @param developmentGrid the grid where the cards are removed
     * @throws ColorDoesNotExistException There are no longer Development Cards of the Color the Token tried to remove
     * @throws LevelDoesNotExistException There are no longer Development Cards of the Level the Token tried to remove
     * @throws LorenzoWinException        The token draws all the cards from Third Level. You loose
     */
    private void removeThirdLevel(int cardsToRemove, DevelopmentGrid developmentGrid) throws ColorDoesNotExistException, LevelDoesNotExistException, LorenzoWinException {
        try {
            for (int i = 0; i < cardsToRemove; i++) {
                developmentGrid.drawCard(colorToDiscard, Level.THIRD);
            }
            if (!developmentGrid.isAvailable(colorToDiscard, Level.THIRD)) throw new LorenzoWinException();
        } catch (NoMoreDevelopmentCardsException e) {
            throw new LorenzoWinException();
        }
    }

    /**
     * @return The Token's name
     */
    @Override
    public String getTokenName() { //TODO modificare questo se si vuole mettere un metodo per stampare i token direttamente nelle singole classi
        cliUtils.printFigure("BlankToken", 20, 141);
        cliUtils.pPCS("▄▄▄▄▄", this.colorToDiscard, 24, 161);
        cliUtils.pPCS("█   █", this.colorToDiscard, 25, 161);
        cliUtils.pPCS("█   █", this.colorToDiscard, 26, 161);
        cliUtils.pPCS("█████", this.colorToDiscard, 27, 161);
        cliUtils.pPCS("▀▀▀▀▀", this.colorToDiscard, 28, 161);

        return "DiscardActionToken";
    }
}
