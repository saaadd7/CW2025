package com.comp2042.ui;

import com.comp2042.event.*;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest {

    // 1. Initialize JavaFX Toolkit
    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already running
        }
    }

    // 2. The Tests
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

        // Set the game to "Paused" manually via the Stub
        // This tests that IF the game is paused, keys don't work.
        // It does NOT test the 'P' key itself anymore.
        flow.setPaused(true);

        // Act: Try to move RIGHT
        handler.handleKeyPress(createKeyEvent(KeyCode.RIGHT));

        // Assert: Should NOT have triggered an event
        assertNull(spy.lastEventType, "Movement should be ignored when paused");
    }

    // REMOVED: testPauseKey() has been deleted.

    // --- Helper to create KeyEvents ---
    private KeyEvent createKeyEvent(KeyCode code) {
        return new KeyEvent(KeyEvent.KEY_PRESSED, "", "", code,
                false, false, false, false);
    }

    // --- STUBS (Fake classes to isolate the test) ---

    static class StubRenderer extends GameBoardRenderer {
        public StubRenderer() { super(null); }
        @Override public void refreshBrick(ViewData brick) { /* Do nothing */ }
    }

    static class StubFlow extends GameFlowController {
        boolean paused = false;
        // removed pauseMethodCalled flag as it is no longer tested

        public StubFlow() { super(null, null, null, null, null); }

        @Override public boolean isPaused() { return paused; }
        @Override public boolean isGameOver() { return false; }
        @Override public void handleDropResult(DownData data) { /* Do nothing */ }

        @Override public void pauseGame() {
            // Logic removed or kept empty, as InputHandler shouldn't call this via 'P' anymore
        }

        public void setPaused(boolean p) { this.paused = p; }
    }

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