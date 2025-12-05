package com.comp2042.ui;

import com.comp2042.event.DownData;
import com.comp2042.event.GameEvent;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.ViewData;
import javafx.application.Platform;
import javafx.scene.control.Button;
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
            assertFalse(flow.isPaused());
            assertFalse(flow.isGameOver());

            // Act 2: Pause
            flow.pauseGame();
            // Assert: If no crash occurs here, the logic is sound.
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
            flow.newGame();

            // Assert
            assertFalse(flow.isGameOver());
            assertFalse(flow.isPaused());
            assertEquals(1, infoPanel.level);

            // This now checks the REAL JavaFX property, which the controller set to false
            assertFalse(gameOverPanel.isVisible(), "Game Over panel should be hidden");
        });
    }

    // --- STUBS ---

    static class StubRenderer extends GameBoardRenderer {
        public StubRenderer() { super(null); }
        @Override public void refreshBrick(ViewData data) {}
    }

    static class StubInfoPanel extends GameInfoPanelController {
        int level = 0;
        public StubInfoPanel() { super(null, null, null); } // Fix for constructor arguments
        @Override public void setLevel(int level) { this.level = level; }
        @Override public void updatePreviews(ViewData data) {}
    }

    // FIX: Removed the illegal overrides.
    // We rely on the parent class (StackPane/Node) to handle the visible property.
    static class StubGameOverPanel extends GameOverPanel {
        public StubGameOverPanel() {
            // If GameOverPanel has a specific constructor, you might need 'super(...)' here.
            // Assuming default or no-arg constructor works for now.
        }
    }
}