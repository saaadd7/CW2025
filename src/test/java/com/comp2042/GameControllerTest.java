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
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {}
    }

    @Test
    void testGameInitialization() {
        StubGuiController gui = new StubGuiController();
        StubRenderer renderer = new StubRenderer();

        GameController controller = new GameController(gui, renderer, SoundManager.getInstance(), null, 10, 20);

        assertTrue(gui.initCalled, "Controller should call initGameView on startup");
        assertTrue(gui.bindScoreCalled, "Controller should bind the score property");
    }

    @Test
    void testOnRightEvent() {
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
        StubGuiController gui = new StubGuiController();
        GameController controller = new GameController(gui, new StubRenderer(), SoundManager.getInstance(), null, 10, 20);

        // Act & Assert
        assertDoesNotThrow(() -> controller.onGameEvent(new com.comp2042.event.BackToMenuEvent()));
    }

    // --- STUBS (Fake Classes) ---

    static class StubRenderer extends GameBoardRenderer {
        public StubRenderer() { super(null); }
        @Override public void refreshGameBackground(int[][] board) {}
        @Override public void refreshBrick(ViewData brick) {}
        @Override public void initGameView(int[][] boardMatrix) {}
    }

    static class StubGuiController extends GuiController {
        boolean initCalled = false;
        boolean bindScoreCalled = false;

        @Override public void setEventListener(InputEventListener listener) {}
        @Override public void initGameView(int[][] boardMatrix, ViewData brick) { initCalled = true; }
        @Override public void bindScore(IntegerProperty scoreProp) { bindScoreCalled = true; }
        @Override public void gameOver() {}

        // Added to prevent NullPointerException in onBackToMenuEvent test if needed
        @Override public javafx.scene.Parent getViewRoot() { return null; }
    }
}