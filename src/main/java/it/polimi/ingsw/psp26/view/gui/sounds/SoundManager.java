package it.polimi.ingsw.psp26.view.gui.sounds;

import it.polimi.ingsw.psp26.view.gui.cache.MediaLoaderCache;
import javafx.scene.media.AudioClip;

public class SoundManager {

    //Songs
    public static final String INTROSONG = "main_theme_03.wav";
    public static final String MAINTHEMESONG = "main_theme_04.wav";
    //Sound effects
    public static final String DIALOGSOUND = "button_click_01.wav";
    public static final String RESOURCESOUND = "button_click_02.wav";
    public static final String DIALOGSOUND2 = "button_click_03.wav";
    private static final String MUSIC_DIRECTORY = "/gui/music/";
    private static SoundManager instance;
    private AudioClip musicAudio;
    private AudioClip effectAudio;
    private double volumeMusic = 100;
    private double volumeEffect = 100;

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

        musicAudio.play(volumeMusic);
    }

    public void setSoundEffect(String soundEffect) {
        effectAudio = new AudioClip(
                MediaLoaderCache.getInstance().loadMediaFromFile(MUSIC_DIRECTORY + soundEffect).getSource()
        );
        effectAudio.setCycleCount(1);

        effectAudio.play(volumeEffect);
    }

    public void muteMusic() {
        if (musicAudio != null)
            musicAudio.stop();
        volumeMusic = 0;
    }

    public void unmuteMusic() {
        volumeMusic = 100;
        musicAudio.play(volumeMusic);
    }

    public void muteEffect() {
        if (effectAudio != null)
            effectAudio.stop();
        volumeEffect = 0;
    }

    public void unmuteEffect() {
        volumeEffect = 100;
        effectAudio.play(volumeEffect);
    }

    public double getVolumeMusic() {
        return volumeMusic;
    }

    public double getVolumeEffect() {
        return volumeEffect;
    }
}
