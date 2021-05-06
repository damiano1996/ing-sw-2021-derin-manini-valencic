package it.polimi.ingsw.psp26.view.gui.modelcomponents;

import javafx.scene.layout.Pane;

public abstract class RatioDrawer {

    protected static final float WIDTH = 2867;

    protected final Pane pane;
    protected final float ratio;

    public RatioDrawer(int maxWidth) {
        pane = new Pane();
        ratio = maxWidth / WIDTH;
    }

    public Pane draw() {
        return pane;
    }

}
