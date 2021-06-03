package it.polimi.ingsw.psp26.model.enums;

import java.util.Locale;

public enum Resource {

    COIN("    Coin    ", Color.YELLOW),
    STONE("   Stones   ", Color.GREY),
    SERVANT("  Servants  ", Color.PURPLE),
    SHIELD("   Shield   ", Color.BLUE),
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
     * @return The Resource name
     */
    public String getName() {
        return name;
    }


    /**
     * @return The Resource Color
     */
    public Color getColor() {
        return color;
    }


    /**
     * @return The name of the Resource without white spaces and in upper case letters
     */
    public String getUppercaseName() {
        return name.replaceAll("\\s+", "").toUpperCase(Locale.ROOT);
    }

}
