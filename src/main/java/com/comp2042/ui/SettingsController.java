package com.comp2042.ui;

import com.comp2042.sounds.SoundManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SettingsController {

    // Matches the fx:id="soundToggleButton" in settingsMenu.fxml
    @FXML
    private Button soundToggleButton;

    private SoundManager soundManager;
    private Stage settingsStage; // Reference to the window this controller manages

    /**
     * Sets the SoundManager instance and initializes the button text.
     * This is called by MainMenuController when the settings window is opened.
     */
    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
        updateSoundButtonText(); // Set initial text
    }

    /**
     * Sets the stage (window) associated with this controller.
     * Used to close the window when the user clicks 'Back'.
     */
    public void setSettingsStage(Stage stage) {
        this.settingsStage = stage;
    }

    /**
     * Updates the text of the sound toggle button based on the current sound state.
     */
    private void updateSoundButtonText() {
        if (soundManager != null) {
            boolean enabled = soundManager.isSoundsEnabled();
            String status = enabled ? "On" : "Off";
            soundToggleButton.setText("Sounds: " + status);

            // Change button color based on state
            if (enabled) {
                soundToggleButton.setStyle("-fx-background-color: linear-gradient(to bottom, #27ae60 0%, #229954 50%, #1e8449 100%);" +
                        " -fx-background-radius: 10px; -fx-text-fill: white; -fx-font-family: 'Arial Black', 'Arial', sans-serif;" +
                        " -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: #1e8449;" +
                        " -fx-border-width: 3px; -fx-border-radius: 10px;" +
                        " -fx-effect: dropshadow(gaussian, rgba(39, 174, 96, 0.5), 12, 0.5, 0, 4), innershadow(gaussian, rgba(255, 255, 255, 0.3), 5, 0.3, 0, 1);");
            } else {
                soundToggleButton.setStyle("-fx-background-color: linear-gradient(to bottom, #95a5a6 0%, #7f8c8d 50%, #5d6d7e 100%);" +
                        " -fx-background-radius: 10px; -fx-text-fill: white; -fx-font-family: 'Arial Black', 'Arial', sans-serif;" +
                        " -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: #5d6d7e;" +
                        " -fx-border-width: 3px; -fx-border-radius: 10px;" +
                        " -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 12, 0.5, 0, 4), innershadow(gaussian, rgba(255, 255, 255, 0.2), 5, 0.3, 0, 1);");
            }
        }
    }
    @FXML
    private void toggleSounds() {
        if (soundManager != null) {
            soundManager.toggleSounds();
            updateSoundButtonText(); // Update the button text after toggling
        }
    }

    /**
     * FXML method called when the 'Back to Main Menu' button is clicked.
     * Closes the settings window.
     */
    @FXML
    private void closeSettings() {
        if (settingsStage != null) {
            settingsStage.close();
        }
    }
}