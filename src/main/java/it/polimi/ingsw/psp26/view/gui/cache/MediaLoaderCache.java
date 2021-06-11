package it.polimi.ingsw.psp26.view.gui.cache;

import javafx.scene.media.Media;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class to keep, as local variables, media loaded from file,
 * to speed up the loading process, to avoid reading from file.
 */
public class MediaLoaderCache {

    private static MediaLoaderCache instance;

    private final Map<String, Media> medias;

    /**
     * Constructor of the class.
     */
    private MediaLoaderCache() {
        medias = new HashMap<>();
    }

    /**
     * Getter of the instance of the class.
     *
     * @return an instance associated to this class
     */
    public static MediaLoaderCache getInstance() {
        if (instance == null) instance = new MediaLoaderCache();
        return instance;
    }

    /**
     * Method to load media given the file name.
     * <p>
     * If file name was already loaded, it returns the media from a local variable.
     * Otherwise, it reads it from file, and then the media is returned.
     *
     * @param fileName file name of the media that must be loaded
     * @return the media
     */
    public Media loadMediaFromFile(String fileName) {
        if (medias.containsKey(fileName)) return medias.get(fileName);
        System.out.println("MediaLoaderCache - Loading media file: " + fileName);
        Media media = new Media(String.valueOf(getClass().getResource(fileName)));
        medias.put(fileName, media);
        return media;
    }
}
