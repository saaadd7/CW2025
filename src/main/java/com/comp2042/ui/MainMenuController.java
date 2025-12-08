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
import javafx.scene.input.MouseEvent;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;

/**
 * Controller for the main menu of the Tetris game.
 * Manages interactions on the main menu, such as starting the game,
 * opening settings, and displaying help.
 */
public class MainMenuController {

    /**
     * Constructs a new MainMenuController.
     */
    public MainMenuController() {
    }

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

    /**
     * Sets the primary stage for the main menu.
     * @param stage The JavaFX Stage for this scene.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the main application instance, allowing navigation between scenes.
     * @param app The main application.
     */
    public void setMainApp(Main app) {
        this.mainApp = app;
    }

    /**
     * Sets the SoundManager instance to control sounds within the menu.
     * @param soundManager The SoundManager instance.
     */
    public void setSoundManager(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up button actions and binds background image properties.
     */
    @FXML
    public void initialize() {
        settingsButton.setOnAction(e -> openSettings());
        helpButton.setOnAction(e -> handleHelpButton(e));

        SoundManager soundManager = SoundManager.getInstance();
        Button[] menuButtons = {
                startButton,
                settingsButton,
                helpButton
        };

        for (Button btn : menuButtons) {
            if (btn != null) {

                btn.setFocusTraversable(false);


                btn.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                    soundManager.playClickSound();
                });


                btn.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {

                });
            }
        }


        if (rootPane != null && backgroundImage != null) {
            backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
            backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        }
    }

    /**
     * Handles the action to start the game.
     * Loads the game scene using the main application instance.
     */
    @FXML
    public void startGame() {
        System.out.println("Start button was clicked!");

        // 1. Find the file using the correct Classpath
        var file = getClass().getResource("/fxml/GameMode.fxml");

        System.out.println("GameMode file found? " + (file != null ? "YES: " + file : "NO"));

        if (file == null) {
            System.err.println("STOPPING: Cannot continue without the FXML file.");
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(file);

            Parent root = loader.load();

            GameModeController controller = loader.getController();
            controller.setMainApp(this.mainApp);
            controller.setStage(this.stage);

            stage.setScene(new Scene(root, 800, 800));
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Could not load /fxml/GameMode.fxml");
        }
    }
    /**
     * Opens the settings window as a modal dialog.
     * Initializes the {@link SettingsController} with the current {@link SoundManager}.
     */
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

    /**
     * Handles the action when the help button is clicked, making the help overlay visible.
     * @param event The ActionEvent generated by the button click.
     */
    @FXML
    public void handleHelpButton(ActionEvent event) {
        if (helpOverlay != null) {
            helpOverlay.setVisible(true);
        }
    }

    /**
     * Handles the action to close the help overlay, making it invisible.
     * @param event The ActionEvent generated by the button click.
     */
    @FXML
    private void handleCloseHelp(ActionEvent event) {
        if (helpOverlay != null) {
            helpOverlay.setVisible(false);
        }
    }
}