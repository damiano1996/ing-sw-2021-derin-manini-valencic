package it.polimi.ingsw.psp26.application.serialization.serializers;

import com.google.gson.*;

import java.lang.reflect.Type;

import static it.polimi.ingsw.psp26.application.serialization.serializers.SerializerUtils.typeForName;

/**
 * // TODO: discuss about this...
 * References: https://stackoverflow.com/questions/16000163/using-gson-and-abstract-classes
 *
 * @param <T>
 */
public class AbstractClassSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    public JsonElement serialize(T object, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("class", object.getClass().getName());
        jsonObject.add("data", jsonSerializationContext.serialize(object));

        return jsonObject;
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) jsonElement;

        JsonElement typeString = jsonObject.get("class");
        JsonElement data = jsonObject.get("data");

        Type actualType = typeForName(typeString);

        return jsonDeserializationContext.deserialize(data, actualType);
    }

}
