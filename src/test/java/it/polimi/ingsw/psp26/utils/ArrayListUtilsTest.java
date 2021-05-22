package it.polimi.ingsw.psp26.utils;

import it.polimi.ingsw.psp26.model.enums.Resource;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.psp26.model.enums.Resource.*;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.getElementsByIndices;
import static it.polimi.ingsw.psp26.utils.ArrayListUtils.grabElements;
import static org.junit.Assert.assertEquals;

public class ArrayListUtilsTest {

    @Test
    public void testGrabElements() {
        List<Resource> resources = new ArrayList<>() {{
            add(COIN);
            add(COIN);
            add(COIN);
            add(COIN);
        }};

        grabElements(resources, 2);
        assertEquals(2, resources.size());
        assertEquals(Arrays.asList(COIN, COIN), resources);
    }

    @Test
    public void testGetElementsByIndices() {
        List<Resource> resources = Arrays.asList(COIN, SERVANT, STONE, SHIELD);
        List<Integer> indices = Arrays.asList(0, 2);

        assertEquals(Arrays.asList(COIN, STONE), getElementsByIndices(resources, indices));
    }
}