package it.polimi.ingsw.psp26.view.gui.maincomponents;

import javafx.scene.layout.Pane;

import static it.polimi.ingsw.psp26.view.gui.GUIWindowConfigurations.REFERENCE_WIDTH;

/**
 * Abstract class that can be used to draw javaFX components considering a ratio parameter.
 * Multiplying components sizes (widths and heights) by the ratio parameter,
 * panes can be scaled to adapt properly to the different screen resolutions.
 * <p>
 * Given a maxWidth, for the root Pane, it computes a ratio between the maxWidth and a REFERENCE_WIDTH.
 */
public abstract class RatioDrawer {

    protected final Pane pane;
    protected final int initMaxWidth;
    protected final float ratio;

    /**
     * Constructor of the class.
     *
     * @param maxWidth maxWidth for the root Pane
     */
    public RatioDrawer(int maxWidth) {
        pane = new Pane();
        this.initMaxWidth = maxWidth;
        ratio = this.initMaxWidth / REFERENCE_WIDTH;
    }

    /**
     * Method to draw components inside the root Pane.
     *
     * @return the root Pane
     */
    public Pane draw() {
        return pane;
    }

    /**
     * Getter of the computed ratio factor.
     *
     * @return ratio
     */
    public float getRatio() {
        return ratio;
    }
}
