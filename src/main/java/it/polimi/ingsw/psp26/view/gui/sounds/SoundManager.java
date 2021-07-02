package it.polimi.ingsw.psp26.view.gui.sounds;

import it.polimi.ingsw.psp26.view.gui.cache.MediaLoaderCache;
import javafx.scene.media.AudioClip;

/**
 * Singleton class to handle music and sound effects.
 */
public class SoundManager {

    //Songs
    public static final String INTRO_SONG = "main_theme_03.wav";
    public static final String MAIN_THEME_SONG = "main_theme_04.wav";

    //Sound effects
    public static final String DIALOG_SOUND = "button_click_01.wav";
    public static final String RESOURCE_SOUND = "button_click_02.wav";
    public static final String DIALOG_SOUND_TWO = "button_click_03.wav";
    private static final String MUSIC_DIRECTORY = "/gui/music/";

    private static SoundManager instance;
    private final double MAX_MUSIC_VOLUME = 1.0d;
    private final double MAX_SOUND_EFFECTS_VOLUME = 0.7d;
    private AudioClip musicAudio;
    private AudioClip effectAudio;
    private double volumeMusic = MAX_MUSIC_VOLUME;
    private double volumeEffect = MAX_SOUND_EFFECTS_VOLUME;

    /**
     * Getter of the instance of the class.
     *
     * @return instance of the class
     */
    public static SoundManager getInstance() {
        if (instance == null)
            instance = new SoundManager();

        return instance;
    }

    /**
     * Method to set the background music.
     * It loads the music file and plays it in infinite loop.
     *
     * @param musicPath path of the file to play
     */
    public void setMusic(String musicPath) {
        if (musicAudio != null)
            musicAudio.stop();
        musicAudio = new AudioClip(
                MediaLoaderCache.getInstance().loadMediaFromFile(MUSIC_DIRECTORY + musicPath).getSource()
        );
        musicAudio.setCycleCount(AudioClip.INDEFINITE);

        musicAudio.play(volumeMusic);
    }

    /**
     * Method to play a sound effect only one time.
     *
     * @param soundEffectPath path of the file to play
     */
    public void setSoundEffect(String soundEffectPath) {
        effectAudio = new AudioClip(
                MediaLoaderCache.getInstance().loadMediaFromFile(MUSIC_DIRECTORY + soundEffectPath).getSource()
        );
        effectAudio.setCycleCount(1);

        effectAudio.play(volumeEffect);
    }

    /**
     * Method to mute the music.
     */
    public void muteMusic() {
        if (musicAudio != null)
            musicAudio.stop();
        volumeMusic = 0;
    }

    /**
     * Method to restart the music.
     */
    public void unmuteMusic() {
        volumeMusic = MAX_MUSIC_VOLUME;
        musicAudio.play(volumeMusic);
    }

    /**
     * Method to mute the effects sounds.
     */
    public void muteEffect() {
        if (effectAudio != null)
            effectAudio.stop();
        volumeEffect = 0;
    }

    /**
     * Method to unmute the sound effects.
     */
    public void unmuteEffect() {
        volumeEffect = MAX_SOUND_EFFECTS_VOLUME;
        effectAudio.play(volumeEffect);
    }

    /**
     * Getter of the volume of the music.
     *
     * @return volume of the music
     */
    public double getVolumeMusic() {
        return volumeMusic;
    }

    /**
     * Getter of the volume of the sound effects.
     *
     * @return volume of the sound effects
     */
    public double getVolumeEffect() {
        return volumeEffect;
    }
}
