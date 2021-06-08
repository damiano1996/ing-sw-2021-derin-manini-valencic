package it.polimi.ingsw.psp26.view.gui.cache.snapshots.imagecache;

import javafx.scene.image.Image;

public class ImageCache {

    private final Image originalImage;
    private final Image editedImage;

    public ImageCache(Image originalImage, Image editedImage) {
        this.originalImage = originalImage;
        this.editedImage = editedImage;
    }

    public Image getOriginalImage() {
        return originalImage;
    }

    public Image getEditedImage() {
        return editedImage;
    }
}
