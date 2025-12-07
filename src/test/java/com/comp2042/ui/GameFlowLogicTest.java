package com.comp2042.ui;

import com.comp2042.GameMode;
import com.comp2042.event.ClearRow;
import com.comp2042.event.DownData;
import com.comp2042.event.ViewData;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameFlowLogicTest {

    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit is already running
        }
    }

    @Test
    void testSprintModeEndsAt20Lines() {
        StubRenderer renderer = new StubRenderer();
        StubInfoPanel infoPanel = new StubInfoPanel();
        GameFlowController flow = new GameFlowController(
                renderer, infoPanel, new StackPane(), new Button(), new StubGameOverPanel()
        );

        flow.newGame(GameMode.SPRINT);

        for (int i = 0; i < 3; i++) {
            // --- FIX 1: ClearRow now has 4 arguments (added null at the end) ---
            ClearRow fakeClear = new ClearRow(10, null, 1000, null);

            // --- FIX 2: ViewData has 6 arguments ---
            DownData data = new DownData(fakeClear, new ViewData(null, 0, 0, null, null, null));

            flow.handleDropResult(data);
        }

        assertTrue(flow.isGameOver(), "Sprint mode should trigger Game Over (Victory) after 30 lines");
    }

    @Test
    void testClassicModeDoesNotEndAt20Lines() {
        StubRenderer renderer = new StubRenderer();
        StubInfoPanel infoPanel = new StubInfoPanel();
        GameFlowController flow = new GameFlowController(
                renderer, infoPanel, new StackPane(), new Button(), new StubGameOverPanel()
        );

        flow.newGame(GameMode.CLASSIC);

        for (int i = 0; i < 3; i++) {
            // --- FIX 1: ClearRow now has 4 arguments ---
            ClearRow fakeClear = new ClearRow(10, null, 1000, null);

            // --- FIX 2: ViewData has 6 arguments ---
            DownData data = new DownData(fakeClear, new ViewData(null, 0, 0, null, null, null));

            flow.handleDropResult(data);
        }

        assertFalse(flow.isGameOver(), "Classic mode should not end after 30 lines");
        assertTrue(infoPanel.level > 1, "Level should have increased in Classic mode");
    }

    // --- STUBS ---
    static class StubRenderer extends GameBoardRenderer {
        public StubRenderer() { super(new GridPane()); }
        @Override public void refreshBrick(ViewData data) {}
        @Override public void refreshGameBackground(int[][] board) {}
    }

    static class StubInfoPanel extends GameInfoPanelController {
        int level = 1;
        public StubInfoPanel() { super(null, null, null); }
        @Override public void setLevel(int level) { this.level = level; }
        @Override public void updatePreviews(ViewData data) {}
    }

    static class StubGameOverPanel extends GameOverPanel {
        public StubGameOverPanel() {}
    }
}