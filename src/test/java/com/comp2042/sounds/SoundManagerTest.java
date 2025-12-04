package com.comp2042.sounds;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SoundManagerTest {

    // This is crucial: It starts the JavaFX engine so 'Media' objects don't crash the test
    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit might already be running from another test, which is fine
        }
    }

    @Test
    void testInitialState() {
        // Arrange & Act
        SoundManager soundManager = new SoundManager();

        // Assert
        assertTrue(soundManager.isSoundsEnabled(), "Sounds should be ON by default");
        assertTrue(soundManager.isBackgroundMusicEnabled(), "Music should be ON by default");
    }

    @Test
    void testToggleSoundEffects() {
        // Arrange
        SoundManager soundManager = new SoundManager();

        // Act 1: Turn OFF
        soundManager.toggleSounds();
        assertFalse(soundManager.isSoundsEnabled(), "Sounds should be OFF after toggle");

        // Act 2: Turn ON
        soundManager.toggleSounds();
        assertTrue(soundManager.isSoundsEnabled(), "Sounds should be ON after second toggle");
    }

    @Test
    void testToggleBackgroundMusic() {
        // Arrange
        SoundManager soundManager = new SoundManager();

        // Act 1: Turn OFF
        soundManager.toggleBackgroundMusic();
        assertFalse(soundManager.isBackgroundMusicEnabled(), "Music should be OFF after toggle");

        // Act 2: Turn ON
        soundManager.toggleBackgroundMusic();
        assertTrue(soundManager.isBackgroundMusicEnabled(), "Music should be ON after second toggle");
    }

    @Test
    void testSafeAudioCalls() {
        // This is a "Smoke Test". We can't easily hear the sound in a unit test,
        // but we can ensure calling the methods doesn't crash the game.
        SoundManager soundManager = new SoundManager();

        assertDoesNotThrow(() -> soundManager.playClickSound());
        assertDoesNotThrow(() -> soundManager.playThudSound());
        assertDoesNotThrow(() -> soundManager.playSwooshSound());
    }
}