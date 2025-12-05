package com.comp2042.ui;

import com.comp2042.event.*;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest {

    // 1. Initialize JavaFX Toolkit (prevents "Toolkit not initialized" errors)
    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already running
        }
    }

    // 2. The Test
    @Test
    void testMovementKeysTriggerEvents() {
        // Arrange
        StubRenderer renderer = new StubRenderer();
        StubFlow flow = new StubFlow();
        InputHandler handler = new InputHandler(flow, renderer);

        // Connect our "Spy" to listen to the handler
        SpyListener spy = new SpyListener();
        handler.setEventListener(spy);

        // Act: Simulate pressing LEFT
        KeyEvent leftKey = createKeyEvent(KeyCode.LEFT);
        handler.handleKeyPress(leftKey);

        // Assert
        assertEquals(EventType.LEFT, spy.lastEventType, "Pressing LEFT should trigger a LEFT event");
    }

    @Test
    void testPausePreventsMovement() {
        // Arrange
        StubRenderer renderer = new StubRenderer();
        StubFlow flow = new StubFlow();
        InputHandler handler = new InputHandler(flow, renderer);
        SpyListener spy = new SpyListener();
        handler.setEventListener(spy);

        // Set the game to "Paused" (manually setting state to ensure logic holds)
        flow.setPaused(true);

        // Act: Try to move RIGHT
        handler.handleKeyPress(createKeyEvent(KeyCode.RIGHT));

        // Assert: Should NOT have triggered an event
        assertNull(spy.lastEventType, "Movement should be ignored when paused");
    }

    // --- Helper to create KeyEvents ---
    private KeyEvent createKeyEvent(KeyCode code) {
        return new KeyEvent(KeyEvent.KEY_PRESSED, "", "", code,
                false, false, false, false);
    }

    // --- STUBS (Fake classes to isolate the test) ---

    // A fake renderer that does nothing (avoids JavaFX graphics errors)
    static class StubRenderer extends GameBoardRenderer {
        public StubRenderer() { super(null); } // Pass null to parent
        @Override public void refreshBrick(ViewData brick) { /* Do nothing */ }
    }

    // A fake flow controller to control game state
    static class StubFlow extends GameFlowController {
        boolean paused = false;
        // Removed 'pauseMethodCalled' variable as it is no longer tested

        public StubFlow() { super(null, null, null, null, null); }

        @Override public boolean isPaused() { return paused; }
        @Override public boolean isGameOver() { return false; }
        @Override public void handleDropResult(DownData data) { /* Do nothing */ }

        @Override public void pauseGame() {
            // Logic removed since we aren't testing the 'P' key trigger anymore
        }

        public void setPaused(boolean p) { this.paused = p; }
    }

    // A "Spy" listener that records what the InputHandler does
    static class SpyListener implements InputEventListener {
        EventType lastEventType;

        @Override
        public Object onGameEvent(GameEvent event) {
            lastEventType = event.getType();
            if (event.getType() == EventType.DOWN || event.getType() == EventType.HARD_DROP) {
                return new DownData(null, null);
            }
            return null;
        }
    }
}