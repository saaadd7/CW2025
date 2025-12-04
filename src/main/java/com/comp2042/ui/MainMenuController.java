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

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;

public class MainMenuController {

    @FXML private Button startButton;
    @FXML private Button settingsButton;
    @FXML private Button helpButton;

    @FXML private AnchorPane helpOverlay;

    @FXML
    private StackPane rootPane;

    @FXML
    private ImageView backgroundImage;

    private Main mainApp;
    private SoundManager soundManager;
    private Stage stage;

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
        settingsButton.setOnAction(e -> openSettings());
        helpButton.setOnAction(e -> handleHelpButton(e));

        if (rootPane != null && backgroundImage != null) {
            backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
            backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        }
    }

    @FXML
    public void startGame() {
        try {
            if (mainApp != null) {
                mainApp.loadGame(stage);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openSettings() {
        if (soundManager == null) {
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

            Scene scene = new Scene(settingsPane, 500, 600);
            settingsStage.setScene(scene);

            settingsStage.setResizable(true);

            settingsStage.setMinWidth(400);
            settingsStage.setMinHeight(500);

            settingsController.setSettingsStage(settingsStage);

            settingsStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleHelpButton(ActionEvent event) {
        if (helpOverlay != null) {
            helpOverlay.setVisible(true);
        }
    }

    @FXML
    private void handleCloseHelp(ActionEvent event) {
        if (helpOverlay != null) {
            helpOverlay.setVisible(false);
        }
    }
}