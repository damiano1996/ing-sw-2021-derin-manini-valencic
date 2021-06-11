package it.polimi.ingsw.psp26.view.gui.cache;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getCompletePath;

/**
 * Singleton class to keep, as local variables, images and re-use them when necessary,
 * to avoid reading from file.
 */
public class ImageLoaderCache {

    private static ImageLoaderCache instance;

    private final Map<String, Image> images;

    /**
     * Constructor of the class.
     */
    private ImageLoaderCache() {
        images = new HashMap<>();
    }

    /**
     * Getter of the instance of the class.
     *
     * @return an instance of the object associated to this class
     */
    public static ImageLoaderCache getInstance() {
        if (instance == null) instance = new ImageLoaderCache();
        return instance;
    }

    /**
     * Method to load images from file.
     * <p>
     * It loads the requested image and returns it.
     * If file name was already loaded, it returns the image from a local variable.
     * Otherwise, it reads it from file, and then the image is returned.
     *
     * @param fileName file name of the image that must be loaded
     * @param width    desired with in pixel of the image
     * @return the image
     */
    public Image loadImageFromFile(String fileName, double width) {
        //noinspection SuspiciousNameCombination
        return loadImageFromFile(fileName, width, width, true, true);
    }

    /**
     * Method to load images from file.
     * <p>
     * It loads the requested image and returns it.
     * If file name was already loaded, it returns the image from a local variable.
     * Otherwise, it reads it from file, and then the image is returned.
     *
     * @param fileName file name of the image that must be loaded
     * @param width    desired with in pixel of the image
     * @param height   desired height in pixel of the image
     * @param var1     to keep aspect ratio (see javaFX documentation for Image())
     * @param var2     to smooth (see javaFX documentation for Image())
     * @return the image
     */
    public Image loadImageFromFile(String fileName, double width, double height, boolean var1, boolean var2) {
        String imageIdentifier = fileName + "__" + width + "_" + height + "_" + var1 + "_" + var2;
        if (images.containsKey(imageIdentifier)) return images.get(imageIdentifier);

        System.out.println("ImageLoaderCache - Loading image: " + fileName);
        Image image = new Image(getCompletePath(fileName), width, height, var1, var2);
        images.put(imageIdentifier, image);
        return image;
    }
}
