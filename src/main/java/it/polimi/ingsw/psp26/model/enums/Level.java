package it.polimi.ingsw.psp26.model.enums;

/**
 * Enum used to contain Level values.
 */
public enum Level {

    UNDEFINED("UNDEFINED", 0),
    FIRST("FIRST", 1),
    SECOND("SECOND", 2),
    THIRD("THIRD", 3);

    private final int levelNumber;
    private final String levelName;

    /**
     * Constructor of the class.
     * It sets the levelName and levelNumber of the Level.
     * 
     * @param levelName the name of the Level
     * @param levelNumber the Number of the Level
     */
    Level(String levelName, int levelNumber) {
        this.levelNumber = levelNumber;
        this.levelName = levelName;
    }


    /**
     * Getter of the Level number.
     *
     * @return the Level number
     */
    public int getLevelNumber() {
        return levelNumber;
    }


    /**
     * Getter of the Level name.
     *
     * @return the Level name
     */
    public String getLevelName() {
        return levelName;
    }

}
