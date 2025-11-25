package com.comp2042;

import com.comp2042.ui.MainMenuController;
import com.comp2042.sounds.SoundManager;
import com.comp2042.GuiController;
import com.comp2042.GameController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String MAIN_MENU_FXML = "/fxml/mainMenu.fxml";
    public static final String GAME_LAYOUT_FXML = "/gameLayout.fxml"; // your real path

    // NEW: Declare the SoundManager instance at the class level
    private SoundManager soundManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TetrisJFX");

        // NEW: Initialize the SoundManager before any FXML is loaded
        // This is where the SoundManager constructor (with the corrected paths) is called.
        // It should only be initialized once.
        soundManager = new SoundManager();

        showMainMenu(primaryStage);
    }

    public void showMainMenu(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_MENU_FXML));
        Parent root = loader.load();

        MainMenuController controller = loader.getController();
        controller.setStage(stage);
        controller.setMainApp(this);

        // CRUCIAL NEW LINE: Pass the single SoundManager instance to the controller
        controller.setSoundManager(soundManager);

        stage.setScene(new Scene(root, 600, 800));
        stage.show();
    }

    public void loadGame(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(GAME_LAYOUT_FXML));
        Parent gameRoot = loader.load();

        // Assuming GuiController and GameController are properly defined

        // 1. Get the controller instance and cast it to the correct type
        GuiController gui = loader.getController();

        // 2. CORRECTED LINE 60: Pass the instance variable 'soundManager'
        GameController game = new GameController(gui, soundManager);

        stage.setScene(new Scene(gameRoot, 600, 800));
        stage.show();

        // keyboard focus
        gameRoot.requestFocus();
    }

    // REMOVED: public void openSettings(Stage stage) - This functionality is now handled by MainMenuController

    public static void main(String[] args) {
        launch(args);
    }
}