package it.polimi.ingsw.psp26.model.actiontokens;

import it.polimi.ingsw.psp26.exceptions.ColorDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LevelDoesNotExistException;
import it.polimi.ingsw.psp26.exceptions.LorenzoWinException;
import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentGrid;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;
import it.polimi.ingsw.psp26.model.personalboard.FaithTrack;

public class DiscardActionToken implements ActionToken {

    private final Color colorToDiscard;

    public DiscardActionToken(Color colorToDiscard) {
        this.colorToDiscard = colorToDiscard;
    }


    @Override
    public void execute(FaithTrack faithTrack, DevelopmentGrid developmentGrid) throws ColorDoesNotExistException, LevelDoesNotExistException, LorenzoWinException {

        int cardsToRemove = 2;
        int removedCards = 0;

        //Try removing cards from first level slot
        try {
            for (int i = 0; i < cardsToRemove; i++) {
                developmentGrid.drawCard(colorToDiscard, Level.FIRST);
                removedCards++;
            }
            cardsToRemove = 0;
        } catch (NoMoreDevelopmentCardsException e) {
            cardsToRemove -= removedCards;
        }

        //If there are cards left to remove, proceed to the second level slot
        if (cardsToRemove > 0) {
            try {
                for (int i = 0; i < cardsToRemove; i++) {
                    developmentGrid.drawCard(colorToDiscard, Level.SECOND);
                    removedCards++;
                }
                cardsToRemove = 0;
            } catch (NoMoreDevelopmentCardsException e) {
                cardsToRemove -= removedCards;
            }

            //If there are cards left to remove, proceed to the third level slot
            if (cardsToRemove > 0) {
                try {
                    for (int i = 0; i < cardsToRemove; i++) {
                        developmentGrid.drawCard(colorToDiscard, Level.THIRD);
                    }
                    if (!developmentGrid.isAvailable(colorToDiscard, Level.THIRD)) throw new LorenzoWinException();
                } catch (NoMoreDevelopmentCardsException e) {
                    throw new LorenzoWinException();
                }
            }
        }
    }

}
