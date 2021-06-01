package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;

import java.util.Objects;


/**
 * This class is used to put in a single place both the Color and the Level of Development Cards
 */
public class DevelopmentCardType {

    private final Color color;
    private final Level level;

    public DevelopmentCardType(Color color, Level level) {
        this.color = color;
        this.level = level;
    }


    /**
     * @return The Color of the Development Card
     */
    public Color getColor() {
        return color;
    }


    /**
     * @return The Level of the Development Card
     */
    public Level getLevel() {
        return level;
    }


    /**
     * toString method
     *
     * @return A String representation of the Object
     */
    @Override
    public String toString() {
        return "DevelopmentCardType{" +
                "color=" + color.getName() +
                ", level=" + level +
                '}';
    }


    /**
     * Equals method
     *
     * @param o Object to be compared
     * @return True if equals, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevelopmentCardType that = (DevelopmentCardType) o;
        return color == that.color && level == that.level;
    }


    /**
     * hashCode method
     *
     * @return A hashCode of the Object
     */
    @Override
    public int hashCode() {
        return Objects.hash(color, level);
    }

}
