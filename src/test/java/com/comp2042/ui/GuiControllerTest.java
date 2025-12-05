package com.comp2042.ui;

import com.comp2042.event.GameEvent;
import com.comp2042.event.InputEventListener;
import com.comp2042.sounds.ISoundManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class GuiControllerTest {

    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {}
    }

    @Test
    void testInitializeSetsUpControllers() {
        GuiController controller = new GuiController();
        injectMockFXML(controller);

        // FIX: Inject a Fake SoundManager so the real one doesn't crash the JVM
        StubSoundManager stubSound = new StubSoundManager();
        try { setPrivateField(controller, "soundManager", stubSound); } catch (Exception e) { fail(e); }

        Platform.runLater(() -> {
            controller.initialize(null, null);

            assertNotNull(controller.getGameBoardRenderer(), "Renderer should be created in initialize()");
            assertNotNull(controller.getGameInfoPanelController(), "Info Panel should be created in initialize()");
        });
    }

    @Test
    void testBindScore() {
        GuiController controller = new GuiController();
        injectMockFXML(controller);

        // FIX: Inject Fake SoundManager
        try { setPrivateField(controller, "soundManager", new StubSoundManager()); } catch (Exception e) { fail(e); }

        Platform.runLater(() -> {
            controller.initialize(null, null);

            SimpleIntegerProperty score = new SimpleIntegerProperty(0);
            controller.bindScore(score);
            score.set(100);

            assertNotNull(controller.getGameInfoPanelController());
        });
    }

    @Test
    void testNewGameAction() {
        // Arrange
        GuiController controller = new GuiController();
        injectMockFXML(controller);
        SpyListener spy = new SpyListener();

        // FIX: Inject Fake SoundManager
        try { setPrivateField(controller, "soundManager", new StubSoundManager()); } catch (Exception e) { fail(e); }

        Platform.runLater(() -> {
            controller.initialize(null, null);
            controller.setEventListener(spy);

            assertDoesNotThrow(() -> controller.newGame(new ActionEvent()));
        });
    }

    // --- HELPER METHODS ---

    private void injectMockFXML(GuiController controller) {
        try {
            setPrivateField(controller, "scoreLabel", new Label());
            setPrivateField(controller, "levelLabel", new Label());
            setPrivateField(controller, "pauseButton", new Button());
            setPrivateField(controller, "startButton", new Button());
            setPrivateField(controller, "settingsButton", new Button());
            setPrivateField(controller, "helpButton", new Button());
            setPrivateField(controller, "gamePanel", new GridPane());
            setPrivateField(controller, "nextGrid", new GridPane());
            setPrivateField(controller, "groupNotification", new StackPane());
            setPrivateField(controller, "particlePane", new Pane());

            GameOverPanel mockOverPanel = new GameOverPanel();
            setPrivateField(controller, "gameOverPanel", mockOverPanel);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to inject mocks: " + e.getMessage());
        }
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    // --- STUBS ---

    // A safe SoundManager that does NOT load real audio files
    static class StubSoundManager implements ISoundManager {
        public void playThudSound() {}
        public void playSwooshSound() {}
        public void playClickSound() {}
        public void playBackgroundMusic() {}
        public void stopBackgroundMusic() {}
        public void toggleSounds() {}
        public boolean isSoundsEnabled() { return true; }
        public void toggleBackgroundMusic() {}
        public boolean isBackgroundMusicEnabled() { return true; }
    }

    static class SpyListener implements InputEventListener {
        @Override
        public Object onGameEvent(com.comp2042.event.GameEvent event) {
            return null;
        }
    }
}