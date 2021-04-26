package it.polimi.ingsw.psp26.application.messages.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.psp26.application.messages.serialization.serializers.AbstractClassSerializer;
import it.polimi.ingsw.psp26.application.messages.serialization.serializers.HashMapSerializer;
import it.polimi.ingsw.psp26.model.developmentgrid.DevelopmentCardType;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.model.leadercards.specialleaderabilities.SpecialAbility;

import java.util.Map;

/**
 * Singleton class for serialization.
 * It manages the Gson object that is in charge of serializing and deserializing the payloads of the messages.
 */
public class GsonConverter {

    private static GsonConverter instance;

    private final Gson gson;

    /**
     * Private constructor of the class.
     */
    private GsonConverter() {
        gson = new GsonBuilder()
                .registerTypeAdapter(SpecialAbility.class, new AbstractClassSerializer<SpecialAbility>())
                .registerTypeAdapter((new TypeToken<Map<Resource, Integer>>() {
                }).getType(), new HashMapSerializer<>(Resource.class, Integer.class))
                .registerTypeAdapter((new TypeToken<Map<DevelopmentCardType, Integer>>() {
                }).getType(), new HashMapSerializer<>(DevelopmentCardType.class, Integer.class))
                .create();
    }

    /**
     * Getter of the instance.
     *
     * @return an instance of the class
     */
    public static GsonConverter getInstance() {
        if (instance == null)
            instance = new GsonConverter();
        return instance;
    }

    /**
     * Getter of the gson object.
     *
     * @return gson object
     */
    public Gson getGson() {
        return gson;
    }

}
