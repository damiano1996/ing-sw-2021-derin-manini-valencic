package it.polimi.ingsw.psp26.model.enums;

public enum Color {
    GREY("\033[38;5;242m"),
    RED("\u001b[31m"),
    WHITE("\u001b[37m"),
    GREEN("\u001b[32m"),
    BLUE("\u001b[34;1m"),
    YELLOW("\u001b[33m"),
    PURPLE("\u001b[35m"),
    BYELLOW("\u001b[33;1m"),
    BLACK("\u001b[30m"), // TODO: check

    RESET("\u001b[0m");

    private final String ANSIcode;

    Color(String ANSIcode) {
        this.ANSIcode = ANSIcode;
    }

    public String setColor() {
        return ANSIcode;
    }

    public String toString() {
        return ANSIcode;
    }
}
