package com.comp2042.sounds;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Manages all sound effects and background music for the game.
 * Provides methods to play specific sounds, toggle sound effects and background music on/off.
 */
public class SoundManager implements ISoundManager {

    private static SoundManager instance;

    private MediaPlayer thudPlayer;
    private MediaPlayer swooshPlayer;
    private MediaPlayer backgroundMusicPlayer;
    private AudioClip clickPlayer;

    private boolean soundsEnabled = true;
    private boolean backgroundMusicEnabled = true;

    /**
     * Constructs a SoundManager and loads all necessary audio files.
     * Initializes MediaPlayers and AudioClips for various game sounds.
     * Prints error messages to System.err if any audio files are not found.
     */
    private SoundManager() {
        java.net.URL thudResource = getClass().getResource("/sounds/thud.wav");
        if (thudResource != null) {
            Media thudSound = new Media(thudResource.toExternalForm());
            thudPlayer = new MediaPlayer(thudSound);
            thudPlayer.setVolume(0.5);
        } else {
            System.err.println("ERROR: Sound file 'thud.wav' not found.");
        }

        java.net.URL swooshResource = getClass().getResource("/sounds/swoosh.wav");
        if (swooshResource != null) {
            Media swooshSound = new Media(swooshResource.toExternalForm());
            swooshPlayer = new MediaPlayer(swooshSound);
            swooshPlayer.setVolume(1.0);
        } else {
            System.err.println("ERROR: Sound file 'swoosh.wav' not found.");
        }

        java.net.URL clickResource = getClass().getResource("/sounds/click.mp3");
        if (clickResource != null) {
            clickPlayer = new AudioClip(clickResource.toExternalForm());
            clickPlayer.setVolume(0.5);
        } else {
            System.err.println("ERROR: 'click.mp3' not found. Check if the file is in src/main/resources/sounds/");
        }

        java.net.URL backgroundMusicResource = getClass().getResource("/sounds/gamebgm.m4a");
        if (backgroundMusicResource != null) {
            Media backgroundMusic = new Media(backgroundMusicResource.toExternalForm());
            backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        } else {
            System.err.println("ERROR: Sound file 'gamebgm.m4a' not found.");
        }
    }

    /**
     * Plays the 'thud' sound effect if sounds are enabled.
     */
    public void playThudSound() {
        if (soundsEnabled && thudPlayer != null) {
            thudPlayer.seek(thudPlayer.getStartTime());
            thudPlayer.play();
        }
    }

    /**
     * Plays the 'swoosh' sound effect if sounds are enabled.
     */
    public void playSwooshSound() {
        if (soundsEnabled && swooshPlayer != null) {
            swooshPlayer.seek(swooshPlayer.getStartTime());
            swooshPlayer.play();
        }
    }

    /**
     * Plays the 'click' sound effect if sounds are enabled.
     */
    public void playClickSound() {
        if (soundsEnabled && clickPlayer != null) {
            clickPlayer.play();
        }
    }

    /**
     * Plays the background music if background music is enabled.
     * The music is set to loop indefinitely.
     */
    public void playBackgroundMusic() {
        if (backgroundMusicEnabled && backgroundMusicPlayer != null) {
            backgroundMusicPlayer.play();
        }
    }

    /**
     * Stops the background music.
     * Sets `backgroundMusicEnabled` to false.
     */
    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
        backgroundMusicEnabled = false;
    }

    /**
     * Toggles the state of all sound effects (on/off).
     */
    public void toggleSounds() {
        soundsEnabled = !soundsEnabled;
    }

    /**
     * Checks if sound effects are currently enabled.
     * @return true if sounds are enabled, false otherwise.
     */
    public boolean isSoundsEnabled() {
        return soundsEnabled;
    }

    /**
     * Toggles the state of background music (on/off).
     * If enabled, it starts playing the background music; if disabled, it stops it.
     */
    public void toggleBackgroundMusic() {
        backgroundMusicEnabled = !backgroundMusicEnabled;
        if (backgroundMusicEnabled) {
            playBackgroundMusic();
        } else {
            stopBackgroundMusic();
        }
    }

    /**
     * Checks if background music is currently enabled.
     * @return true if background music is enabled, false otherwise.
     */
    public boolean isBackgroundMusicEnabled() {
        return backgroundMusicEnabled;
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
}