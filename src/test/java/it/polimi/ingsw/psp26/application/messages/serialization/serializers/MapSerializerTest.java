package it.polimi.ingsw.psp26.application.messages.serialization.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class MapSerializerTest {

    private void testSerialization(Gson gson) {
        HashMapContainer hashMapContainer = new HashMapContainer();
        String json = gson.toJson(hashMapContainer, HashMapContainer.class);
        System.out.println(json);
        assertEquals(hashMapContainer, gson.fromJson(json, HashMapContainer.class));
    }

    @Test
    public void testHashMapSerialization_WithAdapter() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter((new TypeToken<Map<A, Integer>>() {
                }).getType(), new MapSerializer<>(A.class, Integer.class))
                .create();
        testSerialization(gson);
    }

    @Test(expected = Exception.class) // counter example
    public void testHashMapSerialization_WithoutAdapter() {
        Gson gson = new Gson();
        testSerialization(gson);
    }

    private class A {
        private final String a;

        private A(String a) {
            this.a = a;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            A a1 = (A) o;
            return Objects.equals(a, a1.a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a);
        }
    }

    private class HashMapContainer {
        private final Map<A, Integer> hashMap;

        public HashMapContainer() {
            hashMap = new HashMap<>() {{
                put(new A("a"), 1);
                put(new A("b"), 2);
                put(new A("c"), 3);
            }};
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HashMapContainer that = (HashMapContainer) o;
            return Objects.equals(hashMap, that.hashMap);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hashMap);
        }
    }
}