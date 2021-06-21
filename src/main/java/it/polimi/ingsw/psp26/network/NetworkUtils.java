package it.polimi.ingsw.psp26.network;

import java.util.Random;

/**
 * Utility class containing utility method for the Network package.
 */
public class NetworkUtils {

    /**
     * Method to generate a random token.
     *
     * @param length length of the generated token
     * @return session token
     */
    public static String generateSessionToken(int length) {
        Random random = new Random();
        String charSet = "0123456789abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sessionToken = new StringBuilder();
        for (int i = 0; i < length; i++) sessionToken.append(charSet.charAt(random.nextInt(charSet.length())));
        return sessionToken.toString();
    }
}
