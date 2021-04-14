package it.polimi.ingsw.psp26.network;

import java.util.Random;

public class NetworkUtils {

    public static String generateSessionToken(int length) {
        Random random = new Random();
        String charSet = "0123456789abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sessionToken = new StringBuilder();
        for (int i = 0; i < length; i++) sessionToken.append(charSet.charAt(random.nextInt(charSet.length())));
        return sessionToken.toString();
    }
}
