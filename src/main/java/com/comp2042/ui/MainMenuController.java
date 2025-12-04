package com.comp2042.ui;

import com.comp2042.Main;
import com.comp2042.sounds.SoundManager;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView; // Added Missing Import
import javafx.scene.layout.StackPane; // Added Missing Import
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;

public class MainMenuController {

    // --- FXML ELEMENTS ---
    @FXML private Button startButton;
    @FXML private Button settingsButton;
    @FXML private Button helpButton;

    // The Help button overlay (Only needed if using the Overlay approach, but keeping it to prevent errors)
    @FXML private AnchorPane helpOverlay;

    @FXML
    private StackPane rootPane;

    @FXML
    private ImageView backgroundImage;

    // --- CLASS MEMBERS ---
    private Stage stage;
    private Main mainApp;
    private SoundManager soundManager;

    // --- INITIALIZATION AND SETTERS ---

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainApp(Main app) {
        this.mainApp = app;
    }

    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    @FXML
    public void initialize() {
        // Start Game Button is handled via FXML onAction="#startGame" usually,
        // or you can set it here: startButton.setOnAction(e -> startGame());

        // Settings Button logic
        settingsButton.setOnAction(e -> openSettings());

        // FIX: Added comment slashes below so this is not read as code
        // Bind background image to root pane size for dynamic scaling
        if (rootPane != null && backgroundImage != null) {
            backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
            backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        }
    }

    // --- GAME ACTIONS ---

    @FXML
    public void startGame() {
        try {
            if (mainApp != null) {
                mainApp.loadGame(stage);
            } else {
                System.err.println("MainApp reference is null in Controller");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // --- SETTINGS WINDOW ---

    private void openSettings() {
        if (soundManager == null) {
            System.err.println("Error: SoundManager not injected into MainMenuController.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settingsMenu.fxml"));
            StackPane settingsPane = loader.load();

            SettingsController settingsController = loader.getController();
            settingsController.setSoundManager(this.soundManager);

            Stage settingsStage = new Stage();
            settingsStage.setTitle("Settings");
            settingsStage.initModality(Modality.APPLICATION_MODAL);

            // Set initial size
            Scene scene = new Scene(settingsPane, 500, 600);
            settingsStage.setScene(scene);

            // Allow window to be resizable
            settingsStage.setResizable(true);

            // Set minimum window size (optional but recommended)
            settingsStage.setMinWidth(400);
            settingsStage.setMinHeight(500);

            settingsController.setSettingsStage(settingsStage);

            settingsStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load settings menu.");
        }
    }

    // --- HELP BUTTON FUNCTIONALITY ---

    @FXML
    public void handleHelpButton(ActionEvent event) {
        // Currently configured to use the Alert Dialog approach
        showHelpDialog();

        // If you want to use the overlay instead, comment out the line above and uncomment below:
        // if (helpOverlay != null) helpOverlay.setVisible(true);
    }

    private void showHelpDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Controls");
        alert.setHeaderText("Game Controls");

        alert.setContentText(
                "Game Controls:\n\n" +
                        "- ← (Left Arrow): Move piece left\n" +
                        "- → (Right Arrow): Move piece right\n" +
                        "- ↑ (Up Arrow) / X: Rotate piece Clockwise ONLY\n" +
                        "- ↓ (Down Arrow): Soft Drop (Speed up)\n" +
                        "- Space: Hard Drop (Instantly place)\n"
        );

        alert.showAndWait();
    }

    // This method is used only if you use the FXML Overlay approach (AnchorPane)
    @FXML
    private void handleCloseHelp(ActionEvent event) {
        if (helpOverlay != null) {
            helpOverlay.setVisible(false);
        }
    }
}