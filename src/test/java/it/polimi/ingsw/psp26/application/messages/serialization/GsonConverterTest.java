package it.polimi.ingsw.psp26.application.messages.serialization;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GsonConverterTest {

    @Test
    public void getInstance() {
        assertNotNull(GsonConverter.getInstance());
    }

    @Test
    public void getGson() {
        assertNotNull(GsonConverter.getInstance().getGson());
    }
}