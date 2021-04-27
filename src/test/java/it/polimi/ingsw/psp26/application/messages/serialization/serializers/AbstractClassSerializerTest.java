package it.polimi.ingsw.psp26.application.messages.serialization.serializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class AbstractClassSerializerTest {

    private void testSerialization(Gson gson) {
        B expectedB = new B();
        String json = gson.toJson(expectedB, A.class);
        System.out.println(json);
        B actualB = gson.fromJson(json, (Type) A.class);
        assertEquals(expectedB, actualB);
    }

    @Test
    public void testAbstractClassSerialization_WithAdapter() {
        Gson gson = new GsonBuilder().registerTypeAdapter(A.class, new AbstractClassSerializer<A>()).create();
        testSerialization(gson);
    }

    @Test(expected = Exception.class) // counter example
    public void testAbstractClassSerialization_WithoutAdapter() {
        Gson gson = new Gson();
        testSerialization(gson);
    }

    private abstract class A {
        private int a = 0;
    }

    private class B extends A {
        private int b = 0;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            B b1 = (B) o;
            return b == b1.b;
        }

        @Override
        public int hashCode() {
            return Objects.hash(b);
        }
    }
}