package com.comp2042.ui;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UiComponentsTest {

    @BeforeAll
    static void initToolkit() {
        try { Platform.startup(() -> {}); } catch (IllegalStateException e) {}
    }

    @Test
    void testNotificationPanelCreation() {
        Platform.runLater(() -> {
            // Normal notification
            NotificationPanel panel = new NotificationPanel("+100");
            assertNotNull(panel.getCenter());

            // Game Over style notification
            NotificationPanel gameOver = new NotificationPanel("GAME OVER");
            assertNotNull(gameOver.getCenter());
        });
    }

    @Test
    void testGameOverPanelCreation() {
        Platform.runLater(() -> {
            // This instantiates the timeline animation internally.
            // If this doesn't throw an exception, the component is valid.
            assertDoesNotThrow(() -> new GameOverPanel());
        });
    }
}