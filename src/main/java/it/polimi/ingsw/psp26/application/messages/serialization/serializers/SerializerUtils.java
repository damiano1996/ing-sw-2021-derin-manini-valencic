package it.polimi.ingsw.psp26.application.messages.serialization.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class SerializerUtils {

    public static Type typeForName(final JsonElement typeElem) {
        try {
            return Class.forName(typeElem.getAsString());
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}
