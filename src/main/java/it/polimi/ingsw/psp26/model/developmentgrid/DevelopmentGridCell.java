package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;

import java.util.Collections;
import java.util.List;

public class DevelopmentGridCell {

    private final DevelopmentCardType developmentCardType;
    private final List<DevelopmentCard> developmentCards;

    public DevelopmentGridCell(DevelopmentCardType developmentCardType) {
        this.developmentCardType = developmentCardType;

        DevelopmentCardsInitializer initializer = DevelopmentCardsInitializer.getInstance();
        developmentCards = initializer.getByDevelopmentCardType(developmentCardType);
        Collections.shuffle(developmentCards);
    }

    public DevelopmentCard drawCard() throws NoMoreDevelopmentCardsException {
        try {
            return developmentCards.remove(0);
        } catch (Exception e) {
            throw new NoMoreDevelopmentCardsException();
        }
    }

    public boolean isEmpty() {
        return developmentCards.size() == 0;
    }

    public DevelopmentCardType getDevelopmentCardType() {
        return developmentCardType;
    }

    public DevelopmentCard getFirstCard() {
        return developmentCards.get(0);
    }

    public int getDevelopmentCardsSize() {
        return developmentCards.size();
    }
}
