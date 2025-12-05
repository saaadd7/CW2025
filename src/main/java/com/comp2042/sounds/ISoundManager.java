package com.comp2042.sounds;

public interface ISoundManager {
    void playThudSound();
    void playSwooshSound();
    void playClickSound();
    void playBackgroundMusic();
    void stopBackgroundMusic();
    void toggleSounds();
    boolean isSoundsEnabled();
    void toggleBackgroundMusic();
    boolean isBackgroundMusicEnabled();
}