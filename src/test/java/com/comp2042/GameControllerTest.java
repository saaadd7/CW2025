package com.comp2042;

import com.comp2042.event.DownData;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.ViewData;
import com.comp2042.sounds.SoundManager;
import com.comp2042.ui.GameBoardRenderer;
import com.comp2042.ui.GameFlowController;
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
        StubGameFlowController gameFlow = new StubGameFlowController();

        // Act
        GameController controller = new GameController(gui, renderer, gameFlow, SoundManager.getInstance(), null, 10, 20);

        // Assert
        assertTrue(gui.initCalled, "Controller should call initGameView on startup");
        assertTrue(gui.bindScoreCalled, "Controller should bind the score property");
    }

    @Test
    void testOnRightEvent() {
        // Arrange
        StubGuiController gui = new StubGuiController();
        StubRenderer renderer = new StubRenderer();
        StubGameFlowController gameFlow = new StubGameFlowController();
        GameController controller = new GameController(gui, renderer, gameFlow, SoundManager.getInstance(), null, 10, 20);

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
        StubGameFlowController gameFlow = new StubGameFlowController();
        GameController controller = new GameController(gui, renderer, gameFlow, SoundManager.getInstance(), null, 10, 20);

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
        StubGameFlowController gameFlow = new StubGameFlowController();
        GameController controller = new GameController(gui, new StubRenderer(), gameFlow, SoundManager.getInstance(), null, 10, 20);

        // Act & Assert
        assertDoesNotThrow(() -> controller.onGameEvent(new com.comp2042.event.BackToMenuEvent()));
    }

    @Test
    void testNewGameResetsLevel() {
        // Arrange
        StubGuiController gui = new StubGuiController();
        StubRenderer renderer = new StubRenderer();
        StubGameFlowController gameFlow = new StubGameFlowController();
        GameController controller = new GameController(gui, renderer, gameFlow, SoundManager.getInstance(), null, 10, 20);

        // Act - Trigger a new game event
        controller.onGameEvent(new com.comp2042.event.NewGameEvent());

        // Assert
        assertTrue(gameFlow.newGameCalled, "GameFlowController.newGame() should be called on NEW_GAME event");
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

        // Add getter for GameFlowController to satisfy GameController constructor in tests
        @Override
        public GameFlowController getGameFlowController() {
            return new StubGameFlowController(); // Return a stub here too
        }
    }

    // Fake GameFlowController to track newGame calls
    static class StubGameFlowController extends GameFlowController {
        boolean newGameCalled = false;

        public StubGameFlowController() {
            super(null, null, null, null, null); // Pass nulls for dependencies not needed in stub
        }

        @Override
        public void newGame() {
            // No need to call super.newGame() in the stub, just track the call
            newGameCalled = true;
        }
    }
}