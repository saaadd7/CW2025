package com.comp2042;

import com.comp2042.ui.MainMenuController;
import com.comp2042.sounds.SoundManager;
import com.comp2042.ui.GuiController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String MAIN_MENU_FXML = "/fxml/mainMenu.fxml";
    public static final String GAME_LAYOUT_FXML = "/gameLayout.fxml"; // your real path

    private SoundManager soundManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TetrisJFX");

        soundManager = new SoundManager();
        soundManager.playBackgroundMusic();

        showMainMenu(primaryStage);
    }

    public void showMainMenu(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_MENU_FXML));
        Parent root = loader.load();

        MainMenuController controller = loader.getController();
        controller.setStage(stage);
                controller.setMainApp(this);
        
                controller.setSoundManager(soundManager);
        
                stage.setScene(new Scene(root, 800, 800));
                stage.show();
            }
        
            public void loadGame(Stage stage) throws Exception {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(GAME_LAYOUT_FXML));
                Parent gameRoot = loader.load();
        
                GuiController gui = loader.getController();
        
                GameController game = new GameController(gui, gui.getGameBoardRenderer(), soundManager, this);
        
                stage.setScene(new Scene(gameRoot, 800, 800));
                stage.show();
            }






    public static void main(String[] args) {
        launch(args);
    }
}