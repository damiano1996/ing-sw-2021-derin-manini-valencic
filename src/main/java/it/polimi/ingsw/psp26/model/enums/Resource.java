package it.polimi.ingsw.psp26.model.enums;

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

    Resource(String name, Color color) {
        this.name = name;
        this.color = color;
    }


    /**
     * Getter of the Resource Name
     *
     * @return The Resource name
     */
    public String getName() {
        return name;
    }


    /**
     * Getter of the Resource Color
     *
     * @return The Resource Color
     */
    public Color getColor() {
        return color;
    }

}
