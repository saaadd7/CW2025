package com.comp2042.MainScreenui;

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
            String status = soundManager.isSoundsEnabled() ? "On" : "Off";
            soundToggleButton.setText("Sounds: " + status);
        } else {
            soundToggleButton.setText("Sounds: N/A");
        }
    }

    /**
     * FXML method called when the soundToggleButton is clicked.
     * Toggles the sound state and updates the button text.
     */
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