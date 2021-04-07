package it.polimi.ingsw.psp26.model.enums;

public enum Level {
    UNDEFINED(0),
    FIRST(1),
    SECOND(2),
    THIRD(3);

    private final int levelNumber;

    Level(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getLevelNumber() {
        return levelNumber;
    }
}
