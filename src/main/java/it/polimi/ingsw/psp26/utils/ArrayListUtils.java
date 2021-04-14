package it.polimi.ingsw.psp26.utils;

import it.polimi.ingsw.psp26.exceptions.NegativeNumberOfElementsToGrabException;

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
     * @throws NegativeNumberOfElementsToGrabException if the number of elements to draw is a negative number
     * @throws IndexOutOfBoundsException               if the index of the card to draw is out of bounds
     */
    public static <T> List<T> grabElements(List<T> list, int numberOfElements) throws NegativeNumberOfElementsToGrabException, IndexOutOfBoundsException {
        if (numberOfElements < 0) throw new NegativeNumberOfElementsToGrabException();

        List<T> drawnElements = new ArrayList<>(list.subList(0, numberOfElements));

        list.removeAll(drawnElements);
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
}
