package it.polimi.ingsw.psp26.application.serialization.serializers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.psp26.application.serialization.GsonConverter;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapSerializer<K, V> implements JsonSerializer<Map<K, V>>, JsonDeserializer<Map<K, V>> {

    private final Class<K> keysClassType;
    private final Class<V> valuesClassType;

    public HashMapSerializer(Class<K> keysClassType, Class<V> valuesClassType) {
        this.keysClassType = keysClassType;
        this.valuesClassType = valuesClassType;
    }


    @Override
    public JsonElement serialize(Map<K, V> kvMap, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("keys", jsonSerializationContext.serialize(kvMap.keySet(), (new TypeToken<List<K>>() {
        }).getType()));

        jsonObject.add("values", jsonSerializationContext.serialize(kvMap.values(), (new TypeToken<List<V>>() {
        }).getType()));

        return jsonObject;
    }

    @Override
    public Map<K, V> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Gson gson = GsonConverter.getInstance().getGson();

        JsonObject jsonObject = (JsonObject) jsonElement;

        JsonArray keys = jsonObject.get("keys").getAsJsonArray();
        JsonArray values = jsonObject.get("values").getAsJsonArray();

        Map<K, V> reAssembledHashMap = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {

            reAssembledHashMap.put(
                    gson.fromJson(keys.get(i), keysClassType),
                    gson.fromJson(values.get(i), valuesClassType)
            );
        }
        return reAssembledHashMap;
    }

}
