package com.comp2042.sounds;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.*;

public class SoundManager {

    private MediaPlayer thudPlayer;
    private MediaPlayer swooshPlayer;

    // Track whether sounds are enabled
    private boolean soundsEnabled = true;

    // Initialize sound players
    public SoundManager() {
        // --- Constructor remains the same (assuming your files are now .wav and exist) ---

        // 1. Correct Path for thud.wav
        java.net.URL thudResource = getClass().getResource("/sounds/thud.wav");
        if (thudResource != null) {
            Media thudSound = new Media(thudResource.toExternalForm());
            thudPlayer = new MediaPlayer(thudSound);
            thudPlayer.setVolume(0.5); // Set volume between 0.0 and 1.0 for proper audio level
            System.out.println("Thud Sound Loaded: " + thudResource.toExternalForm());
        } else {
            System.err.println("ERROR: Sound file 'thud.wav' not found. Check path in SoundManager.java");
        }

        // 2. Correct Path for swoosh.wav
        java.net.URL swooshResource = getClass().getResource("/sounds/swoosh.wav");
        if (swooshResource != null) {
            Media swooshSound = new Media(swooshResource.toExternalForm());
            swooshPlayer = new MediaPlayer(swooshSound);
            swooshPlayer.setVolume(1.0); // Set volume between 0.0 and 1.0 for proper audio level
            System.out.println("Swoosh Sound Loaded: " + swooshResource.toExternalForm());
        } else {
            System.err.println("ERROR: Sound file 'swoosh.wav' not found. Check path in SoundManager.java");
        }
    }

    // Method to play the thud sound effect when a brick falls into place
    public void playThudSound() {
        if (soundsEnabled && thudPlayer != null) {
            // Reset the player to the beginning before playing the sound
            thudPlayer.seek(thudPlayer.getStartTime());
            thudPlayer.play();
            System.out.println("Playing Thud Sound");
        } else {
            System.err.println("Thud Sound not played. Either sound is disabled or player is null.");
        }
    }

    // Method to play the swoosh sound effect when a row clears
    public void playSwooshSound() {
        if (soundsEnabled && swooshPlayer != null) {
            // Reset the player to the beginning before playing the sound
            swooshPlayer.seek(swooshPlayer.getStartTime());
            swooshPlayer.play();
            System.out.println("Playing Swoosh Sound");
        } else {
            System.err.println("Swoosh Sound not played. Either sound is disabled or player is null.");
        }
    }

    // Method to toggle sound on/off
    public void toggleSounds() {
        soundsEnabled = !soundsEnabled;
        System.out.println("Sounds Enabled: " + soundsEnabled);
    }

    // Method to get whether sounds are enabled
    public boolean isSoundsEnabled() {
        return soundsEnabled;
    }
}
