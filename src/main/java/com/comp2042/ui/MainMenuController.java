package com.comp2042.ui;

import com.comp2042.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML private Button startButton;
    @FXML private Button settingsButton;

    private Stage stage;     // we store stage to switch scenes
    private Main mainApp;    // reference to Main to call show/load functions

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainApp(Main app) {
        this.mainApp = app;
    }

    @FXML
    public void initialize() {
        startButton.setOnAction(e -> {
            try {
                mainApp.loadGame(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        settingsButton.setOnAction(e -> {
            mainApp.openSettings(stage);
        });
    }
}
