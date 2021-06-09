package it.polimi.ingsw.psp26.view.gui.cache.snapshots;

import it.polimi.ingsw.psp26.view.gui.cache.snapshots.imagecache.ImageCache;
import it.polimi.ingsw.psp26.view.gui.cache.snapshots.imagecache.RoundedImageCache;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.areImagesEqual;

public class SnapshotCache {

    private static final int MAX_CACHE_SIZE = 300;
    private static SnapshotCache instance;
    private final List<RoundedImageCache> roundedSnapshots;
    private List<ImageCache> shadowedSnapshots;


    private SnapshotCache() {
        shadowedSnapshots = new ArrayList<>();
        roundedSnapshots = new ArrayList<>();
    }

    public static SnapshotCache getInstance() {
        if (instance == null) instance = new SnapshotCache();
        return instance;
    }

    public Image getShadowedImage(Image image, DropShadow dropShadow) {
        if (shadowedSnapshots.size() > MAX_CACHE_SIZE) shadowedSnapshots.clear();

        ImageCache imageCache = shadowedSnapshots
                .stream()
                .filter(x -> areImagesEqual(x.getOriginalImage(), image))
                .findFirst()
                .orElse(null);

        if (imageCache != null) {

            System.out.println(
                    "SnapshotCache - Snapshot reloaded from local memory. " +
                            "Saved images: " + shadowedSnapshots.size()
            );
            return imageCache.getEditedImage();

        } else {

            System.out.println("SnapshotCache - Snapshot not found, creating a new one.");
            ImageView imageView = new ImageView(image);
            imageView.setEffect(dropShadow);

            Image imageWithShadow = getSnapshot(imageView);
            shadowedSnapshots.add(new ImageCache(image, imageWithShadow));

            return imageWithShadow;
        }
    }

    public Image getRoundedCornerImage(Image image, float ratio, int arcSize) {
        if (roundedSnapshots.size() > MAX_CACHE_SIZE) roundedSnapshots.clear();

        RoundedImageCache roundedImageCache = roundedSnapshots
                .stream()
                .filter(x -> areImagesEqual(
                        x.getOriginalImage(), image) &&
                        x.getRatio() == ratio && x.getArcSize() == arcSize)
                .findFirst()
                .orElse(null);

        if (roundedImageCache != null) {

            System.out.println(
                    "SnapshotCache - Snapshot reloaded from local memory. " +
                            "Saved rounded images: " + roundedSnapshots.size()
            );
            return roundedImageCache.getEditedImage();

        } else {

            System.out.println("SnapshotCache - Snapshot not found, creating a new one.");
            ImageView imageView = new ImageView(image);

            Rectangle clip = new Rectangle(image.getWidth(), image.getHeight());
            clip.setArcWidth(arcSize * ratio);
            clip.setArcHeight(arcSize * ratio);
            imageView.setClip(clip);

            Image roundedImage = getSnapshot(imageView);
            roundedSnapshots.add(new RoundedImageCache(image, roundedImage, ratio, arcSize));
            return roundedImage;
        }
    }

    private Image getSnapshot(ImageView imageView) {
        System.gc();

        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        return imageView.snapshot(snapshotParameters, null);
    }
}
