package com.comp2042.ui;

import com.comp2042.GameMode;
import com.comp2042.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GameModeController {

    private Main mainApp; // Reference to Main
    private Stage stage;

    @FXML private Button classicButton;
    @FXML private Button sprintButton;
    @FXML private Button ultraButton;
    @FXML private Button backButton;

    // 1. We need this method to get the Main application reference
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    // 2. We need the stage reference
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        classicButton.setOnAction(e -> launchGame(GameMode.CLASSIC));
        sprintButton.setOnAction(e -> launchGame(GameMode.SPRINT));
        ultraButton.setOnAction(e -> launchGame(GameMode.ULTRA));

        backButton.setOnAction(e -> returnToMenu());
    }

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