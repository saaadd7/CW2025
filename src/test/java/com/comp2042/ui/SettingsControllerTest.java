package com.comp2042.ui;

import com.comp2042.sounds.SoundManager;
import javafx.application.Platform;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class SettingsControllerTest {

    @BeforeAll
    static void initToolkit() {
        try { Platform.startup(() -> {}); } catch (IllegalStateException e) {}
    }

    @Test
    void testToggleSoundUpdatesManagerAndText() throws Exception {
        // Arrange
        SettingsController controller = new SettingsController();
        StubSoundManager soundManager = new StubSoundManager();
        Button sndBtn = new Button();
        Button bgmBtn = new Button();

        // Inject FXML mocks
        inject(controller, "soundToggleButton", sndBtn);
        inject(controller, "backgroundMusicToggleButton", bgmBtn);

        // Act 1: Initialize
        Platform.runLater(() -> {
            controller.setSoundManager(soundManager);

            // Assert Initial State (On)
            assertTrue(sndBtn.getText().contains("On"));

            // Act 2: Toggle Sound via reflection (simulate button click method)
            try {
                Method toggleMethod = SettingsController.class.getDeclaredMethod("toggleSounds");
                toggleMethod.setAccessible(true);
                toggleMethod.invoke(controller);
            } catch (Exception e) { fail(e); }

            // Assert Post-Toggle (Off)
            assertFalse(soundManager.isSoundsEnabled());
            assertTrue(sndBtn.getText().contains("Off"));
        });
    }

    @Test
    void testToggleMusicUpdatesManagerAndText() throws Exception {
        // Arrange
        SettingsController controller = new SettingsController();
        StubSoundManager soundManager = new StubSoundManager();
        Button sndBtn = new Button();
        Button bgmBtn = new Button();

        inject(controller, "soundToggleButton", sndBtn);
        inject(controller, "backgroundMusicToggleButton", bgmBtn);

        Platform.runLater(() -> {
            controller.setSoundManager(soundManager);

            // Act: Toggle Music
            try {
                Method toggleMethod = SettingsController.class.getDeclaredMethod("toggleBackgroundMusic");
                toggleMethod.setAccessible(true);
                toggleMethod.invoke(controller);
            } catch (Exception e) { fail(e); }

            // Assert
            assertFalse(soundManager.isBackgroundMusicEnabled());
            assertTrue(bgmBtn.getText().contains("Off"));
        });
    }

    // --- Helpers ---
    private void inject(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    static class StubSoundManager extends SoundManager {
        boolean sounds = true;
        boolean music = true;

        // We override the constructor to prevent loading real files
        public StubSoundManager() {
            super();
        }

        // Override logic methods to avoid Media usage
        @Override public void toggleSounds() { sounds = !sounds; }
        @Override public boolean isSoundsEnabled() { return sounds; }
        @Override public void toggleBackgroundMusic() { music = !music; }
        @Override public boolean isBackgroundMusicEnabled() { return music; }
        // Override play methods to do nothing
        @Override public void playClickSound() {}
    }
}