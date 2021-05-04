package it.polimi.ingsw.psp26.model.developmentgrid;

import it.polimi.ingsw.psp26.model.enums.Color;
import it.polimi.ingsw.psp26.model.enums.Level;

import java.util.Objects;

public class DevelopmentCardType {

    private final Color color;
    private final Level level;

    public DevelopmentCardType(Color color, Level level) {
        this.color = color;
        this.level = level;
    }

    public Color getColor() {
        return color;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "DevelopmentCardType{" +
                "color=" + color.getName() +
                ", level=" + level +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DevelopmentCardType that = (DevelopmentCardType) o;
        return color == that.color && level == that.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, level);
    }
}
