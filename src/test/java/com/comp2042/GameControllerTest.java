package com.comp2042;

import com.comp2042.event.DownData;
import com.comp2042.event.GameEvent;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.MoveEvent;
import com.comp2042.event.ViewData;
import com.comp2042.sounds.SoundManager;
import com.comp2042.ui.GameBoardRenderer;
import com.comp2042.ui.GuiController;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @BeforeAll
    static void initToolkit() {
        // Initialize JavaFX Toolkit to prevent "Toolkit not initialized" errors
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit is already running, which is fine
        }
    }

    @Test
    void testGameInitialization() {
        // Arrange
        StubGuiController gui = new StubGuiController();
        StubRenderer renderer = new StubRenderer();

        // Act
        GameController controller = new GameController(gui, renderer, SoundManager.getInstance(), null, 10, 20);

        // Assert
        assertTrue(gui.initCalled, "Controller should call initGameView on startup");
        assertTrue(gui.bindScoreCalled, "Controller should bind the score property");
    }

    @Test
    void testOnRightEvent() {
        // Arrange
        StubGuiController gui = new StubGuiController();
        StubRenderer renderer = new StubRenderer();
        GameController controller = new GameController(gui, renderer, SoundManager.getInstance(), null, 10, 20);

        // Act
        Object result = controller.onGameEvent(new com.comp2042.event.RightEvent());

        // Assert
        assertTrue(result instanceof ViewData);
        ViewData viewData = (ViewData) result;
        assertNotNull(viewData, "Moving right should return updated view data");
        assertNotNull(viewData.getBrickData(), "View data should contain brick matrix");
    }

    @Test
    void testOnHardDrop() {
        // Arrange
        StubGuiController gui = new StubGuiController();
        StubRenderer renderer = new StubRenderer();
        GameController controller = new GameController(gui, renderer, SoundManager.getInstance(), null, 10, 20);

        // Act
        Object result = controller.onGameEvent(new com.comp2042.event.HardDropEvent());

        // Assert
        assertTrue(result instanceof DownData);
        assertNotNull(result, "Hard drop should return a result");
    }

    @Test
    void testBackToMenuSafeExit() {
        // Arrange
        StubGuiController gui = new StubGuiController();
        GameController controller = new GameController(gui, new StubRenderer(), SoundManager.getInstance(), null, 10, 20);

        // Act & Assert
        assertDoesNotThrow(() -> controller.onGameEvent(new com.comp2042.event.BackToMenuEvent()));
    }

    // --- STUBS (Fake Classes) ---

    // Fake Renderer to avoid JavaFX graphics issues
    static class StubRenderer extends GameBoardRenderer {
        public StubRenderer() { super(null); }
        @Override public void refreshGameBackground(int[][] board) {}
        @Override public void refreshBrick(ViewData brick) {}
        @Override public void initGameView(int[][] boardMatrix) {}
    }

    // Fake GUI Controller to avoid FXML NullPointerExceptions
    static class StubGuiController extends GuiController {
        boolean initCalled = false;
        boolean bindScoreCalled = false;

        // Override methods that touch FXML fields to do nothing or record flags
        @Override
        public void setEventListener(InputEventListener listener) {
            // Do nothing
        }

        @Override
        public void initGameView(int[][] boardMatrix, ViewData brick) {
            initCalled = true;
        }

        @Override
        public void bindScore(IntegerProperty scoreProp) {
            bindScoreCalled = true;
        }

        @Override
        public void gameOver() {
            // Do nothing
        }
    }
}