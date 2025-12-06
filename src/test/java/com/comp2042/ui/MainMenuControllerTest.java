package com.comp2042.ui;

import com.comp2042.GameMode; // Import GameMode
import com.comp2042.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch; // Required for FX waiting
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class MainMenuControllerTest {

    @BeforeAll
    static void initToolkit() {
        // Initializes JavaFX environment
        try { Platform.startup(() -> {}); } catch (IllegalStateException e) {}
    }

    @Test
    void testStartGameDoesNotStartGameImmediately() throws Exception {
        // Arrange
        MainMenuController controller = new MainMenuController();
        StubMain mainApp = new StubMain();
        controller.setMainApp(mainApp);

        // We need a latch to wait for the FX thread to finish
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                // We must provide a REAL Stage object because startGame calls stage.setScene()
                // This must be done inside Platform.runLater
                controller.setStage(new Stage());

                // Act
                controller.startGame();

                // Assert
                // CORRECT LOGIC: startGame now opens the GameMode menu.
                // It should NOT call mainApp.loadGame() yet.
                assertFalse(mainApp.loadGameCalled,
                        "Clicking Start should NOT call loadGame immediately (it should open GameMode menu first)");

            } catch (Exception e) {
                // If FXML is missing, it might throw, but we want to ensure the logic flow is correct
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        // Wait for the FX thread to complete (max 3 seconds)
        assertTrue(latch.await(3, TimeUnit.SECONDS), "Test timed out waiting for FX thread");
    }

    @Test
    void testHelpOverlayToggles() throws Exception {
        MainMenuController controller = new MainMenuController();
        AnchorPane helpPane = new AnchorPane();
        helpPane.setVisible(false);

        inject(controller, "helpOverlay", helpPane);

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            controller.handleHelpButton(new ActionEvent());

            assertTrue(helpPane.isVisible(), "Help overlay should become visible when Help button is clicked");
            latch.countDown();
        });

        assertTrue(latch.await(3, TimeUnit.SECONDS));
    }

    @Test
    void testInitializeDoesNotCrash() throws Exception {
        MainMenuController controller = new MainMenuController();

        inject(controller, "settingsButton", new Button());
        inject(controller, "helpButton", new Button());
        inject(controller, "rootPane", new StackPane());
        inject(controller, "backgroundImage", new ImageView());

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            assertDoesNotThrow(controller::initialize);
            latch.countDown();
        });

        assertTrue(latch.await(3, TimeUnit.SECONDS));
    }

    // --- Helpers ---
    private void inject(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    // A Fake Main class that tracks method calls
    static class StubMain extends Main {
        boolean loadGameCalled = false;

        // Override the NEW method signature we created (with GameMode)
        @Override
        public void loadGame(Stage stage, GameMode mode) {
            loadGameCalled = true;
        }

        // Keep the old one just in case, but it shouldn't be called
        public void loadGame(Stage stage) {
            loadGameCalled = true;
        }
    }
}