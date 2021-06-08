package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;

import java.util.Collections;
import java.util.List;

public class DevelopmentGridCell {

    private final DevelopmentCardType developmentCardType;
    private final List<DevelopmentCard> developmentCards;

    /**
     * Constructor of the class
     * It creates the Development Cards using the DevelopmentCardsInitializer, then shuffles the obtained Cards
     *
     * @param developmentCardType The Type wanted for that specific Cell
     */
    public DevelopmentGridCell(DevelopmentCardType developmentCardType) {
        this.developmentCardType = developmentCardType;

        DevelopmentCardsInitializer initializer = DevelopmentCardsInitializer.getInstance();
        developmentCards = initializer.getByDevelopmentCardType(developmentCardType);
        Collections.shuffle(developmentCards);
    }


    /**
     * Removes and returns the Card on top of the Development Grid Cell
     *
     * @return The Card on top of the Development Grid Cell
     * @throws NoMoreDevelopmentCardsException Thrown if the cell is empty
     */
    public DevelopmentCard drawCard() throws NoMoreDevelopmentCardsException {
        try {
            return developmentCards.remove(0);
        } catch (Exception e) {
            throw new NoMoreDevelopmentCardsException();
        }
    }


    /**
     * Tells if the Development Grid Cell is empty
     *
     * @return True if no Cards are contained in the Cell, false otherwise
     */
    public boolean isEmpty() {
        return developmentCards.size() == 0;
    }


    /**
     * Getter of the Development Grid Cell Development Cards type
     *
     * @return The developmentCardType attribute of the Development Grid Cell
     */
    public DevelopmentCardType getDevelopmentCardType() {
        return developmentCardType;
    }


    /**
     * Getter of the first Development Card contained in the Development Grid Cell
     *
     * @return The Card on top of the Cell
     */
    public DevelopmentCard getFirstCard() {
        return developmentCards.get(0);
    }


    /**
     * Getter of the number of Development Cards contained in the Development Grid Cell
     *
     * @return The number of Development Cards contained in the Development Grid Cell
     */
    public int getDevelopmentCardsSize() {
        return developmentCards.size();
    }

}
