package it.polimi.ingsw.psp26.view.gui.drag.targetstrategies;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Generic class to model a feasible target for a draggable object.
 *
 * @param <T> generic object type
 */
public abstract class TargetContainer<T> implements TargetPlacer<T> {

    private final GridPane draggableObjectsContainer;
    private final Pane targetContainerPane;

    /**
     * Constructor of the class.
     *
     * @param draggableObjectsContainer grid pane that contains the draggable objects after release of the mouse button
     * @param targetContainerPane       pane that acts as target box.
     *                                  It defines the location, associated to the draggableObjectsContainer,
     *                                  in which the object can be released and moved in the draggableObjectsContainer
     */
    protected TargetContainer(GridPane draggableObjectsContainer, Pane targetContainerPane) {
        this.draggableObjectsContainer = draggableObjectsContainer;
        this.targetContainerPane = targetContainerPane;
    }

    /**
     * Method to place the object
     *
     * @param object object that must be placed somewhere
     * @return true if object placed successfully, false otherwise
     */
    @Override
    public boolean place(T object) {
        return false;
    }

    /**
     * Getter of the draggableObjectsContainer.
     *
     * @return GridPane
     */
    public GridPane getDraggableObjectsContainer() {
        return draggableObjectsContainer;
    }

    /**
     * Getter of the targetContainerPane.
     *
     * @return Pane
     */
    public Pane getTargetContainerPane() {
        return targetContainerPane;
    }
}
