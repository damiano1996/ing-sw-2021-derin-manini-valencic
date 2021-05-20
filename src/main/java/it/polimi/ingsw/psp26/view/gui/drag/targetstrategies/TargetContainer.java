package it.polimi.ingsw.psp26.view.gui.drag.targetstrategies;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public abstract class TargetContainer<T> implements TargetPlacer<T> {

    private final GridPane draggableObjectsContainer;
    private final Pane targetContainerPane;

    protected TargetContainer(GridPane draggableObjectsContainer, Pane targetContainerPane) {
        this.draggableObjectsContainer = draggableObjectsContainer;
        this.targetContainerPane = targetContainerPane;
    }

    @Override
    public boolean place(T object) {
        return false;
    }

    public GridPane getDraggableObjectsContainer() {
        return draggableObjectsContainer;
    }

    public Pane getTargetContainerPane() {
        return targetContainerPane;
    }
}
