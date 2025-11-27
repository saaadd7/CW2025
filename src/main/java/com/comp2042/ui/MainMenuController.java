package com.comp2042.ui;

import com.comp2042.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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

    public void handleHelpButton() {
        showHelpDialog();
    }

    // Method to show the help dialog with control instructions
    private void showHelpDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Controls");
        alert.setHeaderText("Controls Help");
        alert.setContentText(
                "Game Controls:\n" +
                        "- ↓: Move piece down\n" +
                        "- ↑ / W: Rotate piece\n" +
                        "- ← / → / A / D: Move piece left/right\n" +
                        "- Space: Hard Drop\n" +
                        "- P: Pause game\n" +
                        "- Esc: Exit game"
        );

        // Show and wait for the user to close the dialog
        alert.showAndWait();
    }
}

