package com.comp2042.ui;

import com.comp2042.GameMode; // <--- 1. IMPORT ADDED
import com.comp2042.event.InputEventListener;
import com.comp2042.event.ViewData;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameFlowControllerTest {

    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit is already running, which is fine
        }
    }

    @Test
    void testStartAndPauseLogic() {
        // Arrange
        Button pauseBtn = new Button();
        StackPane notificationPane = new StackPane();

        StubRenderer renderer = new StubRenderer();
        StubInfoPanel infoPanel = new StubInfoPanel();
        StubGameOverPanel gameOverPanel = new StubGameOverPanel();

        GameFlowController flow = new GameFlowController(renderer, infoPanel, notificationPane, pauseBtn, gameOverPanel);

        Platform.runLater(() -> {
            // Act 1: Start
            flow.start();

            // Assert
            assertFalse(flow.isPaused());
            assertFalse(flow.isGameOver());

            // Act 2: Pause
            flow.pauseGame();

            // Assert
            assertTrue(flow.isPaused()); // Validated pause state
        });
    }

    @Test
    void testNewGameResetsState() {
        // Arrange
        Button pauseBtn = new Button();
        StubRenderer renderer = new StubRenderer();
        StubInfoPanel infoPanel = new StubInfoPanel();
        StubGameOverPanel gameOverPanel = new StubGameOverPanel();

        GameFlowController flow = new GameFlowController(renderer, infoPanel, new StackPane(), pauseBtn, gameOverPanel);

        flow.setEventListener(new InputEventListener() {
            @Override
            public Object onGameEvent(com.comp2042.event.GameEvent event) {
                return null;
            }
        });

        Platform.runLater(() -> {
            // Act
            // <--- 2. FIX: Passed GameMode.CLASSIC argument
            flow.newGame(GameMode.CLASSIC);

            // Assert
            assertFalse(flow.isGameOver());
            assertFalse(flow.isPaused());
            assertEquals(1, infoPanel.level);

            // This checks the JavaFX property on the stub
            assertFalse(gameOverPanel.isVisible(), "Game Over panel should be hidden");
        });
    }

    // --- STUBS ---

    static class StubRenderer extends GameBoardRenderer {
        // <--- 3. FIX: Pass a real GridPane to avoid NPE in parent constructor
        public StubRenderer() { super(new GridPane()); }
        @Override public void refreshBrick(ViewData data) {}
    }

    static class StubInfoPanel extends GameInfoPanelController {
        int level = 0;
        // <--- 3. FIX: Pass nulls is usually okay here if parent doesn't use them immediately
        public StubInfoPanel() { super(null, null, null); }
        @Override public void setLevel(int level) { this.level = level; }
        @Override public void updatePreviews(ViewData data) {}
    }

    static class StubGameOverPanel extends GameOverPanel {
        public StubGameOverPanel() {
            // Default constructor
        }
    }
}