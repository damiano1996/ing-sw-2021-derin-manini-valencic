package it.polimi.ingsw.psp26.application;

import com.google.gson.Gson;

public class GsonConverter {

    private static GsonConverter instance;

    private final Gson gson;

    private GsonConverter() {
        gson = new Gson();
    }

    public static GsonConverter getInstance() {
        if (instance == null)
            instance = new GsonConverter();
        return instance;
    }

    public Gson getGson() {
        return gson;
    }
}
