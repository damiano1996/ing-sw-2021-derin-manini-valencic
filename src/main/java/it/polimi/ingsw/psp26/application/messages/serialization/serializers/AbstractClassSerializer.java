package it.polimi.ingsw.psp26.application.messages.serialization.serializers;

import com.google.gson.*;

import java.lang.reflect.Type;

import static it.polimi.ingsw.psp26.application.messages.serialization.serializers.SerializerUtils.typeFromJsonElement;

/**
 * Class to serialize abstract classes.
 *
 * @param <T> generic abstract class
 */
public class AbstractClassSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    /**
     * Method to serialize an abstract class defined at runtime.
     * It adds a parameter to the json indicating the runtime class type, and the data contained in the object.
     *
     * @param object                   generic object to be serialized
     * @param type                     serialization type
     * @param jsonSerializationContext serialization context
     * @return the serialized object
     */
    @Override
    public JsonElement serialize(T object, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("class", object.getClass().getName());
        jsonObject.add("data", jsonSerializationContext.serialize(object));

        return jsonObject;
    }

    /**
     * Method to deserialize the abstract class.
     * It reads the parameter that is referring to the class type and re-assemble the object.
     *
     * @param jsonElement                json to deserialize
     * @param type                       deserialization type
     * @param jsonDeserializationContext deserialization context
     * @return deserialized object
     * @throws JsonParseException if class type not found, or if json is not parsed correctly
     */
    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) jsonElement;

        JsonElement typeString = jsonObject.get("class");
        JsonElement data = jsonObject.get("data");

        Type actualType = typeFromJsonElement(typeString);

        return jsonDeserializationContext.deserialize(data, actualType);
    }

}
