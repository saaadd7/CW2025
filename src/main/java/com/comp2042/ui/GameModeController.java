package com.comp2042.ui;

import com.comp2042.GameMode;
import com.comp2042.Main;
import com.comp2042.sounds.SoundManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

/**
 * Controller for the game mode selection screen.
 * This class handles user interaction for choosing a game mode (Classic, Sprint, Ultra)
 * and launching the game accordingly.
 */
public class GameModeController {

    /**
     * Constructs a new GameModeController.
     */
    public GameModeController() {
    }

    private Main mainApp; // Reference to Main
    private Stage stage;

    @FXML private Button classicButton;
    @FXML private Button sprintButton;
    @FXML private Button ultraButton;
    @FXML private Button backButton;

    /**
     * Sets the main application instance.
     *
     * @param mainApp The main application instance.
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Sets the stage for this controller.
     *
     * @param stage The stage to be used.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        classicButton.setOnAction(e -> launchGame(GameMode.CLASSIC));
        sprintButton.setOnAction(e -> launchGame(GameMode.SPRINT));
        ultraButton.setOnAction(e -> launchGame(GameMode.ULTRA));

        backButton.setOnAction(e -> returnToMenu());

        SoundManager soundManager = SoundManager.getInstance();
        Button[] modeButtons = {
                classicButton,
                sprintButton,
                ultraButton,
                backButton
        };

        // 2. Apply the Sound & Focus fix
        for (Button btn : modeButtons) {
            if (btn != null) {
                btn.setFocusTraversable(false);

                btn.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                    soundManager.playClickSound();
                });
            }
        }

    }

    /**
     * Launches the game with the selected mode.
     *
     * @param mode The game mode to launch.
     */
    private void launchGame(GameMode mode) {
        try {
            if (mainApp != null) {
                // Call the NEW method in Main
                mainApp.loadGame(stage, mode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns to the main menu.
     */
    private void returnToMenu() {
        try {
            if (mainApp != null) {
                mainApp.showMainMenu(stage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}