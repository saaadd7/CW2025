package com.comp2042.sounds;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    private MediaPlayer thudPlayer;
    private MediaPlayer swooshPlayer;
    private MediaPlayer backgroundMusicPlayer;
    private AudioClip clickPlayer;

    private boolean soundsEnabled = true;
    private boolean backgroundMusicEnabled = true;

    public SoundManager() {
        // 1. Load thud.wav
        java.net.URL thudResource = getClass().getResource("/sounds/thud.wav");
        if (thudResource != null) {
            Media thudSound = new Media(thudResource.toExternalForm());
            thudPlayer = new MediaPlayer(thudSound);
            thudPlayer.setVolume(0.5);
            System.out.println("Thud Sound Loaded");
        } else {
            System.err.println("ERROR: Sound file 'thud.wav' not found.");
        }

        // 2. Load swoosh.wav
        java.net.URL swooshResource = getClass().getResource("/sounds/swoosh.wav");
        if (swooshResource != null) {
            Media swooshSound = new Media(swooshResource.toExternalForm());
            swooshPlayer = new MediaPlayer(swooshSound);
            swooshPlayer.setVolume(1.0);
            System.out.println("Swoosh Sound Loaded");
        } else {
            System.err.println("ERROR: Sound file 'swoosh.wav' not found.");
        }

        // 3. Load click.mp3
        // I assume you kept the .mp3 extension. If you converted to .wav, change this to "click.wav"
        java.net.URL clickResource = getClass().getResource("/sounds/click.mp3");
        if (clickResource != null) {
            clickPlayer = new AudioClip(clickResource.toExternalForm());
            clickPlayer.setVolume(0.5);
            System.out.println("Click Sound Loaded");
        } else {
            System.err.println("ERROR: 'click.mp3' not found. Check if the file is in src/main/resources/sounds/");
        }

        java.net.URL backgroundMusicResource = getClass().getResource("/sounds/gamebgm.m4a");
        if (backgroundMusicResource != null) {
            Media backgroundMusic = new Media(backgroundMusicResource.toExternalForm());
            backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music
            System.out.println("Background Music Loaded");
        } else {
            System.err.println("ERROR: Sound file 'gamebgm.m4a' not found.");
        }
    }

    public void playThudSound() {
        if (soundsEnabled && thudPlayer != null) {
            thudPlayer.seek(thudPlayer.getStartTime());
            thudPlayer.play();
        }
    }

    public void playSwooshSound() {
        if (soundsEnabled && swooshPlayer != null) {
            swooshPlayer.seek(swooshPlayer.getStartTime());
            swooshPlayer.play();
        }
    }

    // Play Click Sound
    public void playClickSound() {
        if (soundsEnabled && clickPlayer != null) {
            clickPlayer.play();
        }
    }

    public void playBackgroundMusic() {
        if (backgroundMusicEnabled && backgroundMusicPlayer != null) {
            backgroundMusicPlayer.play();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
        backgroundMusicEnabled = false;
    }

    public void toggleSounds() {
        soundsEnabled = !soundsEnabled;
        System.out.println("Sounds Enabled: " + soundsEnabled);
    }

    public boolean isSoundsEnabled() {
        return soundsEnabled;
    }

    public void toggleBackgroundMusic() {
        backgroundMusicEnabled = !backgroundMusicEnabled;
        if (backgroundMusicEnabled) {
            playBackgroundMusic();
        } else {
            stopBackgroundMusic();
        }
    }

    public boolean isBackgroundMusicEnabled() {
        return backgroundMusicEnabled;
    }
}