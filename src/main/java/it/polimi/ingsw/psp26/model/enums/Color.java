package it.polimi.ingsw.psp26.model.enums;

public enum Color {
    GREY("\033[38;5;242m", "GREY"),
    RED("\u001b[31m", "RED"),
    WHITE("\u001b[37m", "WHITE"),
    GREEN("\u001b[32m", "GREEN"),
    BLUE("\u001b[34;1m", "BLUE"),
    YELLOW("\u001b[33m", "YELLOW"),
    PURPLE("\u001b[35m", "PURPLE"),
    BYELLOW("\u001b[33;1m", "BYELLOW"),
    BLACK("\u001b[30m", "BLACK"),

    RESET("\u001b[0m", "Reset");

    private final String ANSIcode;

    private final String name;

    Color(String ANSICode, String name) {
        this.ANSIcode = ANSICode;
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
