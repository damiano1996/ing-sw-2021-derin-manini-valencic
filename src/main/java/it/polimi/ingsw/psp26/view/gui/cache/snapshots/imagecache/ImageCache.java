package it.polimi.ingsw.psp26.view.gui.cache.snapshots.imagecache;

import javafx.scene.image.Image;

/**
 * Class used to keep track of images and edited version of them.
 */
public class ImageCache {

    private final Image originalImage;
    private final Image editedImage;

    /**
     * Constructor of the class.
     *
     * @param originalImage original image
     * @param editedImage   edited version of the original image
     */
    public ImageCache(Image originalImage, Image editedImage) {
        this.originalImage = originalImage;
        this.editedImage = editedImage;
    }

    /**
     * Getter of the original image.
     *
     * @return original image
     */
    public Image getOriginalImage() {
        return originalImage;
    }

    /**
     * Getter of the edited image.
     *
     * @return edited image
     */
    public Image getEditedImage() {
        return editedImage;
    }
}
