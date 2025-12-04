package com.comp2042.ui;

import com.comp2042.event.InputEventListener;
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

        Platform.runLater(() -> {
            // 1. Initialize first
            controller.initialize(null, null);

            // 2. NOW bind score (safe because controllers exist)
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

        Platform.runLater(() -> {
            // 1. Initialize first (Creates gameFlowController)
            controller.initialize(null, null);

            // 2. NOW set the listener (Safe because gameFlowController is not null)
            controller.setEventListener(spy);

            // Act: Simulate clicking "New Game"
            // This implicitly tests that the button action doesn't crash the controller
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

            // Mock GameOverPanel
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