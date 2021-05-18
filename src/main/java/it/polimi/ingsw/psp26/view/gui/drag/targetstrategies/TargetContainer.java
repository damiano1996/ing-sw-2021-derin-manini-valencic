package it.polimi.ingsw.psp26.view.gui.drag.targetstrategies;

import javafx.scene.layout.Pane;

public abstract class TargetContainer<T> implements TargetPlacer<T> {

    private final Pane pane;

    protected TargetContainer(Pane pane) {
        this.pane = pane;
    }

    @Override
    public boolean place(T object) {
        return false;
    }

    public Pane getPane() {
        return pane;
    }
}
