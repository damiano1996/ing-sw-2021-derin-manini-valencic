package it.polimi.ingsw.psp26.model.enums;

/**
 * Enum used to contain Resource values.
 */
public enum Resource {

    COIN("Coin", Color.YELLOW),
    STONE("Stone", Color.GREY),
    SERVANT("Servant", Color.PURPLE),
    SHIELD("Shield", Color.BLUE),
    FAITH_MARKER("Faith Marker", Color.RED),
    EMPTY("Empty", Color.WHITE),
    UNKNOWN("Unknown", Color.WHITE);

    private final String name;
    private final Color color;

    /**
     * Constructor of the class.
     * It sets the name and the Color of the Resource.
     *
     * @param name  the name of the Resource
     * @param color the Color of the Resource
     */
    Resource(String name, Color color) {
        this.name = name;
        this.color = color;
    }


    /**
     * Getter of the Resource name.
     *
     * @return the Resource name
     */
    public String getName() {
        return name;
    }


    /**
     * Getter of the Resource Color.
     *
     * @return the Resource Color
     */
    public Color getColor() {
        return color;
    }

}
