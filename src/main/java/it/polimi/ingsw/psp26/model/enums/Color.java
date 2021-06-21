package it.polimi.ingsw.psp26.model.enums;

/**
 * Enum used to contain Color values.
 */
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

    /**
     * Constructor of the class.
     * It sets the Color ANSICode and name.
     *
     * @param ANSICode the AnsiCode of the Color
     * @param name     the name of the Color
     */
    Color(String ANSICode, String name) {
        this.ANSICode = ANSICode;
        this.name = name;
    }


    /**
     * Used to set the desired Color in the console screen.
     * Must call a setColor() on RESET to bo back to normal Color when needed.
     *
     * @return the ANSICode that will set the Color on the console screen
     */
    public String setColor() {
        return ANSICode;
    }


    /**
     * Getter of the Color ANSICode.
     *
     * @return a String representation of the ANSICode of the Color
     */
    public String toString() {
        return ANSICode;
    }


    /**
     * Getter of the Color name.
     *
     * @return the name of the Color
     */
    public String getName() {
        return name;
    }

}
