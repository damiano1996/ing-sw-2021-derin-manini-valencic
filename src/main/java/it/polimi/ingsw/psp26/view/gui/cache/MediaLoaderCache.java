package it.polimi.ingsw.psp26.view.gui.cache;

import javafx.scene.media.Media;

import java.util.HashMap;
import java.util.Map;

public class MediaLoaderCache {

    private static MediaLoaderCache instance;

    private final Map<String, Media> medias;

    private MediaLoaderCache() {
        medias = new HashMap<>();
    }

    public static MediaLoaderCache getInstance() {
        if (instance == null) instance = new MediaLoaderCache();
        return instance;
    }

    public Media loadMediaFromFile(String fileName) {
        if (medias.containsKey(fileName)) return medias.get(fileName);
        System.out.println("MediaLoaderCache - Loading media file: " + fileName);
        Media media = new Media(String.valueOf(getClass().getResource(fileName)));
        medias.put(fileName, media);
        return media;
    }
}
