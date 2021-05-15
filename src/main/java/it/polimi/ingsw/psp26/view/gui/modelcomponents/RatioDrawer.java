package it.polimi.ingsw.psp26.view.gui.modelcomponents;

import javafx.scene.layout.Pane;

import static it.polimi.ingsw.psp26.view.gui.GUIConfigurations.REFERENCE_WIDTH;

public abstract class RatioDrawer {

    protected final Pane pane;
    protected final int initMaxWidth;
    protected final float ratio;

    public RatioDrawer(int maxWidth) {
        pane = new Pane();
        this.initMaxWidth = maxWidth;
        ratio = this.initMaxWidth / REFERENCE_WIDTH;
    }

    public Pane draw() {
        return pane;
    }

}