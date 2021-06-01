package it.polimi.ingsw.psp26.model.enums;

public enum Level {

    UNDEFINED("UNDEFINED", 0),
    FIRST("FIRST", 1),
    SECOND("SECOND", 2),
    THIRD("THIRD", 3);

    private final int levelNumber;
    private final String levelName;

    Level(String levelName, int levelNumber) {
        this.levelNumber = levelNumber;
        this.levelName = levelName;
    }


    /**
     * @return The Level number
     */
    public int getLevelNumber() {
        return levelNumber;
    }


    /**
     * @return The Level name
     */
    public String getLevelName() {
        return levelName;
    }

}
