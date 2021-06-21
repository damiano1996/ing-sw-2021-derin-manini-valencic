package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;

import java.util.Collections;
import java.util.List;

/**
 * Class that represent a DevelopmentGridCell.
 * A DevelopmentGridCell can contain up to 4 DevelopmentCards.
 */
public class DevelopmentGridCell {

    private final DevelopmentCardType developmentCardType;
    private final List<DevelopmentCard> developmentCards;

    /**
     * Constructor of the class.
     * It creates the DevelopmentCards using the DevelopmentCardsInitializer, then shuffles the obtained DevelopmentCards.
     *
     * @param developmentCardType the DevelopmentCardType wanted for that specific DevelopmentGridCell
     */
    public DevelopmentGridCell(DevelopmentCardType developmentCardType) {
        this.developmentCardType = developmentCardType;

        DevelopmentCardsInitializer initializer = DevelopmentCardsInitializer.getInstance();
        developmentCards = initializer.getByDevelopmentCardType(developmentCardType);
        Collections.shuffle(developmentCards);
    }


    /**
     * Removes and returns the DevelopmentCard on top of the DevelopmentGridCell.
     *
     * @return the DevelopmentCard on top of the DevelopmentGridCell
     * @throws NoMoreDevelopmentCardsException thrown if the DevelopmentGridCell is empty
     */
    public DevelopmentCard drawCard() throws NoMoreDevelopmentCardsException {
        try {
            return developmentCards.remove(0);
        } catch (Exception e) {
            throw new NoMoreDevelopmentCardsException();
        }
    }


    /**
     * Tells if the DevelopmentGridCell is empty.
     *
     * @return true if no DevelopmentCards are contained in the DevelopmentGridCell, false otherwise
     */
    public boolean isEmpty() {
        return developmentCards.size() == 0;
    }


    /**
     * Getter of the DevelopmentGridCell's DevelopmentCardsType.
     *
     * @return the DevelopmentCardType attribute of the DevelopmentGridCell
     */
    public DevelopmentCardType getDevelopmentCardType() {
        return developmentCardType;
    }


    /**
     * Getter of the first DevelopmentCard contained in the DevelopmentGridCell.
     *
     * @return the DevelopmentCard on top of the DevelopmentGridCell
     */
    public DevelopmentCard getFirstCard() {
        return developmentCards.get(0);
    }


    /**
     * Getter of the number of DevelopmentCards contained in the DevelopmentGridCell.
     *
     * @return the number of DevelopmentCards contained in the DevelopmentGridCell
     */
    public int getDevelopmentCardsSize() {
        return developmentCards.size();
    }

}
