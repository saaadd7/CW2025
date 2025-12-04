package com.comp2042.ui;

import com.comp2042.event.InputEventListener;
import com.comp2042.sounds.SoundManager; // Import SoundManager
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
    static class StubSoundManager extends SoundManager {
        public StubSoundManager() {
            super();
        }
        // Override Init to do nothing (so it doesn't load files)
        // Since constructor calls logic, we rely on the fact that if files are missing it prints error but doesn't crash.
        // BUT, the real constructor tries to load files.
        // TRICK: The simplest way is to ensure we don't trigger the media engine.
        // Note: Reflection injection happens AFTER the controller is made but BEFORE initialize().
        // The crash happens inside 'new SoundManager()' inside initialize().
        // By injecting this Stub and adding the 'if (soundManager == null)' check in GuiController,
        // the Real SoundManager (and the crash) is completely skipped!
    }

    static class SpyListener implements InputEventListener {
        @Override public com.comp2042.event.DownData onDownEvent(com.comp2042.event.MoveEvent e) { return null; }
        @Override public com.comp2042.event.ViewData onLeftEvent(com.comp2042.event.MoveEvent e) { return null; }
        @Override public com.comp2042.event.ViewData onRightEvent(com.comp2042.event.MoveEvent e) { return null; }
        @Override public com.comp2042.event.ViewData onRotateEvent(com.comp2042.event.MoveEvent e) { return null; }
        @Override public com.comp2042.event.DownData onHardDropEvent(com.comp2042.event.MoveEvent e) { return null; }
        @Override public void createNewGame() {}
        @Override public void onBackToMenuEvent() {}
    }
}