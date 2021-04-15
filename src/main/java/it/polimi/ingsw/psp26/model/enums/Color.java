package it.polimi.ingsw.psp26.model.enums;

public enum Color {
    GREY("\033[38;5;242m", "Grey"),
    RED("\u001b[31m", "Red"),
    WHITE("\u001b[37m", "White"),
    GREEN("\u001b[32m", "Green"),
    BLUE("\u001b[34;1m", "Blue"),
    YELLOW("\u001b[33m", "Yellow"),
    PURPLE("\u001b[35m", "Purple"),
    BYELLOW("\u001b[33;1m", "BYellow"),
    BLACK("\u001b[30m", "Black"),

    RESET("\u001b[0m", "Reset");

    private final String ANSIcode;

    private final String name;

    Color(String ANSIcode, String name) {
        this.ANSIcode = ANSIcode;
        this.name = name;
    }

    public String setColor() {
        return ANSIcode;
    }

    public String toString() {
        return ANSIcode;
    }

    public String getName() {
        return name;
    }
}
