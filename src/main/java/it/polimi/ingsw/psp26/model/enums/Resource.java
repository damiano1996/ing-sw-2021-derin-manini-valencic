package it.polimi.ingsw.psp26.model.enums;

public enum Resource {
    COIN("Coin", Color.YELLOW),
    STONE("Stone", Color.GREY),
    SERVANT("Servant", Color.PURPLE),
    SHIELD("Shield", Color.BLUE),
    FAITH_MARKER("Faith Marker", Color.RED),
    EMPTY("Empty", Color.WHITE);

    private final String name;
    private final Color color;

    Resource(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}
