package it.polimi.ingsw.psp26.view.gui.cache.snapshots.imagecache;

import javafx.scene.image.Image;

public class RoundedImageCache extends ImageCache {

    private final float ratio;
    private final int arcSize;


    public RoundedImageCache(Image originalImage, Image editedImage, float ratio, int arcSize) {
        super(originalImage, editedImage);

        this.ratio = ratio;
        this.arcSize = arcSize;
    }

    public float getRatio() {
        return ratio;
    }

    public int getArcSize() {
        return arcSize;
    }
}
