package it.polimi.ingsw.psp26.model;

enum Resource {
    COIN("Coin", Color.YELLOW),
    STONE("Stone", Color.GREY),
    SERVANT("Servant", Color.PURPLE),
    SHIELD("Shield", Color.BLUE),
    FAITH_MARKERS("Faith Markers", Color.RED),
    EMPTY("Empty", Color.WHITE);

    private final String name;
    private final Color color;

    Resource(String name, Color color) {
        this.name = name;
        this.color = color;
    }
}
