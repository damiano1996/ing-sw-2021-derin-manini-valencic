package it.polimi.ingsw.psp26.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayListUtils {

    /**
     * Allows to draw elements from a list, removing them from it.
     *
     * @param list             list containing the elements to draw
     * @param numberOfElements number of elements to draw
     * @param <T>              generic object
     * @return list containing the drawn elements
     * @throws IndexOutOfBoundsException if the index of the card to draw is out of bounds
     */
    public static <T> List<T> grabElements(List<T> list, int numberOfElements) throws IndexOutOfBoundsException {
        List<T> drawnElements = new ArrayList<>(list.subList(0, numberOfElements));
        for (T elem : drawnElements) {
            list.remove(elem);
        }
        return drawnElements;
    }

    /**
     * Method returns a sublist containing objects in the position indicated by the list of indices.
     *
     * @param list    list containing objects to select
     * @param indices list containing the indices
     * @param <T>     generic type
     * @return sublist of list
     */
    public static <T> List<T> getElementsByIndices(List<T> list, List<Integer> indices) {
        return indices.stream().map(list::get).collect(Collectors.toList());
    }

    public static <M, N> List<M> castElements(Class<M> targetClassType, List<N> list) {
        List<M> castedList = new ArrayList<>();
        for (N item : list) castedList.add((M) item);
        return castedList;
    }
}
