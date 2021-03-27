package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.exceptions.NoMoreDevelopmentCardsException;
import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;

import java.util.Collections;
import java.util.List;

public class DevelopmentGridCell {

    private final Color color;
    private final Level level;
    private final List<DevelopmentCard> developmentCards;

    public DevelopmentGridCell(Color color, Level level) {
        this.color = color;
        this.level = level;

        DevelopmentCardsInitializer initializer = DevelopmentCardsInitializer.getInstance();
        developmentCards = initializer.getByColorAndLevel(color, level);
        Collections.shuffle(developmentCards);
    }

    public Color getColor() {
        return color;
    }

    public Level getLevel() {
        return level;
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
}
