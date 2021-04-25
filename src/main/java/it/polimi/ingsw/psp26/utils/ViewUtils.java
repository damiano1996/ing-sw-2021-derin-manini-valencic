package it.polimi.ingsw.psp26.utils;

public class ViewUtils {

    /**
     * Checks if the inserted char is a number or a character
     *
     * @param c The char to examine
     * @return False if c is a number, true if it's not a number
     */
    public static boolean checkAsciiRange(char c) {
        return (48 > (int) c || (int) c > 57);
    }
    
}
