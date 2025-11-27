package com.comp2042.ui;

import com.comp2042.Main;
import com.comp2042.sounds.SoundManager;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent; // Added for explicit event handling
import javafx.scene.layout.AnchorPane;// Added for the helpOverlay (if using FXML approach)

public class MainMenuController {

    // --- FXML ELEMENTS ---
    @FXML private Button startButton;
    @FXML private Button settingsButton;
    @FXML private Button helpButton;
    @FXML private AnchorPane helpOverlay;// The Help button added in FXML

    // If you decide to use the FXML overlay method, you must include this:
    // @FXML private AnchorPane helpOverlay;


    // --- CLASS MEMBERS ---
    private Stage stage;     // we store stage to switch scenes
    private Main mainApp;    // reference to Main to call show/load functions
    private SoundManager soundManager; // Reference to the shared SoundManager instance

    // --- INITIALIZATION AND SETTERS ---

    // Set the stage (from Main class)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Set the Main reference (for game loading)
    public void setMainApp(Main app) {
        this.mainApp = app;
    }

    // New Method to receive the SoundManager instance from Main.java
    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    // Initialize the controller
    @FXML
    public void initialize() {
        // Start Game Button action set up in the FXML now uses the startGame() method directly
        // startButton.setOnAction(e -> { ... }); // Removed duplicate action setup

        // Settings Button - NEW LOGIC to open the settings window
        settingsButton.setOnAction(e -> openSettings());

        // Help Button action is set up in the FXML now uses handleHelpButton()
    }

    // --- GAME ACTIONS ---

    @FXML
    public void startGame() {
        try {
            mainApp.loadGame(stage); // This will switch the scene to the game screen
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // --- SETTINGS WINDOW ---

    // NEW METHOD to open the settings window
    private void openSettings() {
        if (soundManager == null) {
            System.err.println("Error: SoundManager not injected into MainMenuController.");
            return;
        }

        try {
            // 1. Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settingsMenu.fxml"));
            VBox settingsPane = loader.load();

            // 2. Get the new controller and pass necessary data
            SettingsController settingsController = loader.getController();

            // Pass the SoundManager instance and the stage to the SettingsController
            settingsController.setSoundManager(this.soundManager);

            // 3. Create a new window (Stage) for the settings
            Stage settingsStage = new Stage();
            settingsStage.setTitle("Settings");
            settingsStage.initModality(Modality.APPLICATION_MODAL);
            settingsStage.setScene(new Scene(settingsPane, 300, 200));

            // Pass the stage to the controller so it can close itself
            settingsController.setSettingsStage(settingsStage);

            settingsStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load settings menu.");
            // You might want to show an Alert here instead of just printing
        }
    }

    // --- HELP BUTTON FUNCTIONALITY ---

    // Method called when the FXML Help button is pressed
    @FXML
    public void handleHelpButton(ActionEvent event) {
        showHelpDialog();
        // If you used the FXML overlay approach: helpOverlay.setVisible(true);
    }

    // Method to show the help dialog with control instructions
    private void showHelpDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Controls");
        alert.setHeaderText("Game Controls");

        // Final, simplified control list
        alert.setContentText(
                "Game Controls:\n\n" +
                        "- ← (Left Arrow): Move piece left\n" +
                        "- → (Right Arrow): Move piece right\n" +
                        "- ↑ (Up Arrow) / X: Rotate piece Clockwise ONLY\n" +
                        "- ↓ (Down Arrow): Soft Drop (Speed up)\n" +
                        "- Space: Hard Drop (Instantly place)\n"

        );

        // Show and wait for the user to close the dialog
        alert.showAndWait();
    }


    @FXML
    private void handleCloseHelp(ActionEvent event) {
        if (helpOverlay != null) {
            helpOverlay.setVisible(false);
            // Re-enable the main menu buttons if you disabled them in handleHelpButton
        }
    }

}