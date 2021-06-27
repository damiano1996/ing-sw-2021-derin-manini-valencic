package it.polimi.ingsw.psp26.utils;

import it.polimi.ingsw.psp26.exceptions.ValueDoesNotExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class containing general methods related to Collections.
 */
public class CollectionsUtils {

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

    /**
     * Method to cast all elements of a list.
     *
     * @param targetClassType target cast type
     * @param list            list of objects that must be casted
     * @param <M>             target class type
     * @param <N>             source class type
     * @return casted list
     */
    @SuppressWarnings("unchecked") // to suppress the cast to M
    public static <M, N> List<M> castElements(Class<M> targetClassType, List<N> list) {
        List<M> castedList = new ArrayList<>();
        for (N item : list) castedList.add((M) item);
        return castedList;
    }

    /**
     * Getter of the index of the value in the map
     *
     * @param kvMap map containing the searched value
     * @param value value for which we are looking for the index
     * @param <K>   generic key type
     * @param <V>   generic value type
     * @return index of the value in the map
     * @throws ValueDoesNotExistsException if value does not exist
     */
    public static <K, V> int getIndexOf(Map<K, V> kvMap, V value) throws ValueDoesNotExistsException {
        List<V> vs = new ArrayList<>(kvMap.values());
        for (int i = 0; i < vs.size(); i++) if (vs.get(i).equals(value)) return i;
        throw new ValueDoesNotExistsException();
    }
}
