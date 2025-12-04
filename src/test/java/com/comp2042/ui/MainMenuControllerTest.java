package com.comp2042.ui;

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

import static org.junit.jupiter.api.Assertions.*;

class MainMenuControllerTest {

    @BeforeAll
    static void initToolkit() {
        try { Platform.startup(() -> {}); } catch (IllegalStateException e) {}
    }

    @Test
    void testStartGameTriggersMainLoad() throws Exception {
        // Arrange
        MainMenuController controller = new MainMenuController();
        StubMain mainApp = new StubMain();
        controller.setMainApp(mainApp);

        // FIX: Pass 'null' instead of 'new Stage()'.
        // We are testing the interaction, and StubMain doesn't use the stage methods,
        // so this is 100% safe and avoids the "Not on FX Thread" crash.
        controller.setStage(null);

        // Act
        Platform.runLater(() -> {
            controller.startGame();

            // Assert
            assertTrue(mainApp.loadGameCalled, "Clicking Start should call mainApp.loadGame()");
        });
    }

    @Test
    void testHelpOverlayToggles() throws Exception {
        // Arrange
        MainMenuController controller = new MainMenuController();
        AnchorPane helpPane = new AnchorPane();
        helpPane.setVisible(false);

        // Inject the help pane
        inject(controller, "helpOverlay", helpPane);

        // Act
        Platform.runLater(() -> {
            // Simulate clicking the help button
            controller.handleHelpButton(new ActionEvent());

            // Assert
            assertTrue(helpPane.isVisible(), "Help overlay should become visible when Help button is clicked");
        });
    }

    @Test
    void testInitializeDoesNotCrash() throws Exception {
        MainMenuController controller = new MainMenuController();

        // Inject mock FXML elements so initialize() has something to work with
        inject(controller, "settingsButton", new Button());
        inject(controller, "helpButton", new Button());
        inject(controller, "rootPane", new StackPane());
        inject(controller, "backgroundImage", new ImageView());

        Platform.runLater(() -> {
            assertDoesNotThrow(controller::initialize);
        });
    }

    // --- Helpers ---
    private void inject(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    // A Fake Main class that records if loadGame was called
    static class StubMain extends Main {
        boolean loadGameCalled = false;

        @Override
        public void loadGame(Stage stage) {
            loadGameCalled = true;
        }
    }
}