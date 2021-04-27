package it.polimi.ingsw.psp26.application.messages.serialization.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Class to collect common method used for serialization purposes.
 */
public class SerializerUtils {

    /**
     * Method that returns the type of a give json element.
     *
     * @param jsonElement element type
     * @return type of the element
     * @throws JsonParseException if json element can not be parsed
     */
    public static Type typeFromJsonElement(JsonElement jsonElement) throws JsonParseException {
        try {
            return Class.forName(jsonElement.getAsString());
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}
