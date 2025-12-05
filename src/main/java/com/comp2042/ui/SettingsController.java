package com.comp2042.ui;

import com.comp2042.sounds.ISoundManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for the settings menu.
 * Manages sound and background music toggling, and provides functionality to close the settings window.
 */
public class SettingsController {

    /**
     * FXML-injected button to toggle sound effects.
     * Matches the fx:id="soundToggleButton" in settingsMenu.fxml.
     */
    @FXML
    private Button soundToggleButton;

    /**
     * FXML-injected button to toggle background music.
     */
    @FXML
    private Button backgroundMusicToggleButton;

    private ISoundManager soundManager;
    private Stage settingsStage; // Reference to the window this controller manages

    /**
     * Sets the SoundManager instance and initializes the button text.
     * This is called by MainMenuController when the settings window is opened.
     *
     * @param soundManager The SoundManager instance to be used for sound control.
     */
    public void setSoundManager(ISoundManager soundManager) {

    /**
     * Sets the stage (window) associated with this controller.
     * Used to close the window when the user clicks 'Back'.
     *
     * @param stage The JavaFX Stage object for the settings window.
     */
    public void setSettingsStage(Stage stage) {
        this.settingsStage = stage;
    }

    /**
     * Updates the text and style of the sound toggle button based on the current sound state.
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

    /**
     * Updates the text and style of the background music toggle button based on the current music state.
     */
    private void updateBackgroundMusicButtonText() {
        if (soundManager != null) {
            boolean enabled = soundManager.isBackgroundMusicEnabled();
            String status = enabled ? "On" : "Off";
            backgroundMusicToggleButton.setText("Music: " + status);

            // Change button color based on state
            if (enabled) {
                backgroundMusicToggleButton.setStyle("-fx-background-color: linear-gradient(to bottom, #27ae60 0%, #229954 50%, #1e8449 100%);" +
                        " -fx-background-radius: 10px; -fx-text-fill: white; -fx-font-family: 'Arial Black', 'Arial', sans-serif;" +
                        " -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: #1e8449;" +
                        " -fx-border-width: 3px; -fx-border-radius: 10px;" +
                        " -fx-effect: dropshadow(gaussian, rgba(39, 174, 96, 0.5), 12, 0.5, 0, 4), innershadow(gaussian, rgba(255, 255, 255, 0.3), 5, 0.3, 0, 1);");
            } else {
                backgroundMusicToggleButton.setStyle("-fx-background-color: linear-gradient(to bottom, #95a5a6 0%, #7f8c8d 50%, #5d6d7e 100%);" +
                        " -fx-background-radius: 10px; -fx-text-fill: white; -fx-font-family: 'Arial Black', 'Arial', sans-serif;" +
                        " -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: #5d6d7e;" +
                        " -fx-border-width: 3px; -fx-border-radius: 10px;" +
                        " -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 12, 0.5, 0, 4), innershadow(gaussian, rgba(255, 255, 255, 0.2), 5, 0.3, 0, 1);");
            }
        }
    }

    /**
     * Toggles the sound effects on or off via the SoundManager.
     * This method is called when the soundToggleButton is clicked.
     */
    @FXML
    private void toggleSounds() {
        if (soundManager != null) {
            soundManager.toggleSounds();
            updateSoundButtonText(); // Update the button text after toggling
        }
    }

    /**
     * Toggles the background music on or off via the SoundManager.
     * This method is called when the backgroundMusicToggleButton is clicked.
     */
    @FXML
    private void toggleBackgroundMusic() {
        if (soundManager != null) {
            soundManager.toggleBackgroundMusic();
            updateBackgroundMusicButtonText(); // Update the button text after toggling
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