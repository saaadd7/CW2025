package com.comp2042.MainScreenui;

import com.comp2042.Main;
import com.comp2042.sounds.SoundManager;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox; // Import VBox for the settings pane

public class MainMenuController {

    @FXML private Button startButton;
    @FXML private Button settingsButton;
    // @FXML private Button soundToggleButton; // REMOVED - This button is now in settingsMenu.fxml

    private Stage stage;     // we store stage to switch scenes
    private Main mainApp;    // reference to Main to call show/load functions
    private SoundManager soundManager; // Reference to the shared SoundManager instance

    // 1. New Method to receive the SoundManager instance from Main.java
    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    @FXML
    public void startGame() {
        try {
            mainApp.loadGame(stage); // This will switch the scene to the game screen
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Set the stage (from Main class)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Set the Main reference (for game loading)
    public void setMainApp(Main app) {
        this.mainApp = app;
    }

    // Initialize the controller
    @FXML
    public void initialize() {
        // NOTE: SoundManager initialization has been moved to Main.java

        // Start Game Button
        startButton.setOnAction(e -> {
            try {
                mainApp.loadGame(stage); // Calls the loadGame() method in Main to switch scenes
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Settings Button - NEW LOGIC to open the settings window
        settingsButton.setOnAction(e -> openSettings());

        // OLD LOGIC REMOVED: soundToggleButton.setOnAction(e -> toggleSounds());
        // OLD LOGIC REMOVED: updateSoundButtonText();
    }

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
        }
    }

    // OLD LOGIC REMOVED: @FXML private void toggleSounds() {...}
    // OLD LOGIC REMOVED: private void updateSoundButtonText() {...}
}