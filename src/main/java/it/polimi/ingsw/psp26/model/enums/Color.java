package it.polimi.ingsw.psp26.model.enums;

public enum Color {

    GREY("\033[38;5;242m", "GREY"),
    RED("\u001b[31m", "RED"),
    WHITE("\u001b[37m", "WHITE"),
    GREEN("\u001b[32m", "GREEN"),
    BLUE("\u001b[34;1m", "BLUE"),
    YELLOW("\u001b[33m", "YELLOW"),
    PURPLE("\u001b[35m", "PURPLE"),

    RESET("\u001b[0m", "Reset");

    private final String ANSICode;
    private final String name;

    Color(String ANSICode, String name) {
        this.ANSICode = ANSICode;
        this.name = name;
    }


    /**
     * Used to set the desired Color in the console screen
     * Must call a setColor() on RESET to bo back to normal Color when needed
     *
     * @return The ANSICode that will set the Color on the console screen
     */
    public String setColor() {
        return ANSICode;
    }


    /**
     * Getter of the Color ANSICode
     *
     * @return A String representation of the ANSICode of the Color
     */
    public String toString() {
        return ANSICode;
    }


    /**
     * Getter of the Color name
     *
     * @return The name of the Color
     */
    public String getName() {
        return name;
    }

}
