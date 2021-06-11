package it.polimi.ingsw.psp26.view.gui.cache.snapshots.imagecache;

import javafx.scene.image.Image;

/**
 * Class to keep track of images and rounded version of them.
 */
public class RoundedImageCache extends ImageCache {

    private final float ratio;
    private final int arcSize;

    /**
     * Constructor of the class.
     *
     * @param originalImage original image
     * @param editedImage   edited version of the original image
     * @param ratio         ratio used to obtain the edited image
     * @param arcSize       arc size used to obtain the edited image
     */
    public RoundedImageCache(Image originalImage, Image editedImage, float ratio, int arcSize) {
        super(originalImage, editedImage);

        this.ratio = ratio;
        this.arcSize = arcSize;
    }

    /**
     * Getter of the ratio.
     *
     * @return ratio used to obtain the edited image
     */
    public float getRatio() {
        return ratio;
    }

    /**
     * Getter of the arc size.
     *
     * @return arc size used to obtain the edited image
     */
    public int getArcSize() {
        return arcSize;
    }
}
