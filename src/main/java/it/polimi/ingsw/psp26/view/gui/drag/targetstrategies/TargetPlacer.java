package it.polimi.ingsw.psp26.view.gui.drag.targetstrategies;

/**
 * Generic interface used to place a generic object.
 *
 * @param <T> generic object type
 */
public interface TargetPlacer<T> {

    /**
     * Method to place the given object.
     *
     * @param object object that must be placed somewhere
     * @return true if object has been placed successfully, false otherwise
     */
    boolean place(T object);
}
