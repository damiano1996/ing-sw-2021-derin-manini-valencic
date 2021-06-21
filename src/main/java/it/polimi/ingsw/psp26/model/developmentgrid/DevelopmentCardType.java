package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;

import java.util.Objects;

/**
 * This class is used to put in a single place both the Color and the Level of DevelopmentCards.
 */
public class DevelopmentCardType {

    private final Color color;
    private final Level level;

    /**
     * Constructor of the class.
     * It sets the Color and Level for the DevelopmentCardType.
     *
     * @param color the Color of the DevelopmentCardType
     * @param level the Level of the DevelopmentCardType
     */
    public DevelopmentCardType(Color color, Level level) {
        this.color = color;
        this.level = level;
    }


    /**
     * Getter of the DevelopmentCardType Color.
     *
     * @return the Color of the DevelopmentCard
     */
    public Color getColor() {
        return color;
    }


    /**
     * Getter of the DevelopmentCardType Level.
     *
     * @return the Level of the DevelopmentCard
     */
    public Level getLevel() {
        return level;
    }


    /**
     * toString method.
     *
     * @return a String representation of the Object
     */
    @Override
    public String toString() {
        return "DevelopmentCardType{" +
                "color=" + color.getName() +
                ", level=" + level +
                '}';
    }


    /**
     * Equals method.
     *
     * @param o Object to be compared.
     * @return true if equals, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevelopmentCardType that = (DevelopmentCardType) o;
        return color == that.color && level == that.level;
    }


    /**
     * hashCode method.
     *
     * @return a hashCode of the Object
     */
    @Override
    public int hashCode() {
        return Objects.hash(color, level);
    }

}
