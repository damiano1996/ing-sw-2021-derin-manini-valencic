package it.polimi.ingsw.psp26.network;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.configurations.Configurations.SESSION_TOKEN_LENGTH;
import static it.polimi.ingsw.psp26.network.NetworkUtils.generateSessionToken;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class NetworkUtilsTest {

    @Test
    public void testGenerateSessionToken() {
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String newToken = generateSessionToken(SESSION_TOKEN_LENGTH);
            assertEquals(SESSION_TOKEN_LENGTH, newToken.length());
            assertFalse(tokens.contains(newToken));
            tokens.add(newToken);
        }
    }
}