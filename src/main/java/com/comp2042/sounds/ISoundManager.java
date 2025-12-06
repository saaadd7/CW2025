package com.comp2042.sounds;

/**
 * Defines the contract for a sound manager.
 * Implementations of this interface are responsible for playing sound effects and background music.
 */
public interface ISoundManager {
    /**
     * Plays a thud sound effect.
     */
    void playThudSound();

    /**
     * Plays a swoosh sound effect.
     */
    void playSwooshSound();

    /**
     * Plays a click sound effect.
     */
    void playClickSound();

    /**
     * Plays the background music.
     */
    void playBackgroundMusic();

    /**
     * Stops the background music.
     */
    void stopBackgroundMusic();

    /**
     * Toggles the sound effects on or off.
     */
    void toggleSounds();

    /**
     * Checks if sound effects are enabled.
     *
     * @return true if sound effects are enabled, false otherwise.
     */
    boolean isSoundsEnabled();

    /**
     * Toggles the background music on or off.
     */
    void toggleBackgroundMusic();

    /**
     * Checks if background music is enabled.
     *
     * @return true if background music is enabled, false otherwise.
     */
    boolean isBackgroundMusicEnabled();
}