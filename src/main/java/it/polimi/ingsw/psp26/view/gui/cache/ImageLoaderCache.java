package it.polimi.ingsw.psp26.view.gui.cache;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.psp26.view.gui.GUIUtils.getCompletePath;

public class ImageLoaderCache {

    private static ImageLoaderCache instance;

    private final Map<String, Image> images;

    private ImageLoaderCache() {
        images = new HashMap<>();
    }

    public static ImageLoaderCache getInstance() {
        if (instance == null) instance = new ImageLoaderCache();
        return instance;
    }

    public Image loadImageFromFile(String fileName, double width) {
        //noinspection SuspiciousNameCombination
        return loadImageFromFile(fileName, width, width, true, true);
    }

    public Image loadImageFromFile(String fileName, double width, double height, boolean var1, boolean var2) {
        String imageIdentifier = fileName + "__" + width + "_" + height + "_" + var1 + "_" + var2;
        if (images.containsKey(imageIdentifier)) return images.get(imageIdentifier);

        System.out.println("ImageLoaderCache - Loading image: " + fileName);
        Image image = new Image(getCompletePath(fileName), width, height, var1, var2);
        images.put(imageIdentifier, image);
        return image;
    }
}
