package it.polimi.ingsw.psp26.view.gui;

import it.polimi.ingsw.psp26.view.gui.cache.MediaLoaderCache;
import javafx.scene.media.AudioClip;

public class SoundManager {

    private static final String MUSIC_DIRECTORY = "/gui/music/";

    private static SoundManager instance;

    private AudioClip musicAudio;
    private AudioClip effectAudio;
    private double volume = 100;

    public static SoundManager getInstance() {
        if (instance == null)
            instance = new SoundManager();

        return instance;
    }

    public void setMusic(String musicPath) {
        if (musicAudio != null)
            musicAudio.stop();
        musicAudio = new AudioClip(
                MediaLoaderCache.getInstance().loadMediaFromFile(MUSIC_DIRECTORY + musicPath).getSource()
        );
        musicAudio.setCycleCount(10);

        musicAudio.play(volume);
    }

    public void setSoundEffect(String soundEffect) {
        effectAudio = new AudioClip(
                MediaLoaderCache.getInstance().loadMediaFromFile(MUSIC_DIRECTORY + soundEffect).getSource()
        );
        effectAudio.setCycleCount(1);

        effectAudio.play(150);
    }

    public void muteMusic() {
        if (musicAudio != null)
            musicAudio.stop();
        volume = 0;
    }

    public void unmuteMusic() {
        volume = 100;
        musicAudio.play(volume);
    }
}
