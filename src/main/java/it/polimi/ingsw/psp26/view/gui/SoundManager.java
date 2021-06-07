package it.polimi.ingsw.psp26.view.gui;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

public class SoundManager {

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
        Media media = new Media(String.valueOf(getClass().getResource("/gui/music/" + musicPath)));
        musicAudio = new AudioClip(media.getSource());
        musicAudio.setCycleCount(10);

        musicAudio.play(volume);
    }

    public void setSoundEffect(String soundEffect) {
        Media media = new Media(String.valueOf(getClass().getResource("/gui/music/" + soundEffect)));
        effectAudio = new AudioClip(media.getSource());
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
