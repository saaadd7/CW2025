package com.comp2042;

import com.comp2042.ui.MainMenuController;
import com.comp2042.sounds.SoundManager;
import com.comp2042.ui.GuiController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class for the TetrisJFX application.
 * This class extends {@link javafx.application.Application} and is responsible for
 * initializing the game, managing sound, and switching between the main menu and the game itself.
 */
public class Main extends Application {

    public static final String MAIN_MENU_FXML = "/fxml/mainMenu.fxml";
    public static final String GAME_LAYOUT_FXML = "/gameLayout.fxml"; // your real path

    private SoundManager soundManager;

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     the primary stage.
     * @throws Exception if something goes wrong during application start-up.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TetrisJFX");

        soundManager = SoundManager.getInstance();
        soundManager.playBackgroundMusic();

        showMainMenu(primaryStage);
    }

    /**
     * Displays the main menu of the game.
     *
     * @param stage the primary stage where the main menu will be displayed.
     * @throws Exception if the FXML file cannot be loaded.
     */
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
        
            /**
             * Loads and displays the main game board.
             *
             * @param stage the primary stage where the game will be displayed.
             * @throws Exception if the FXML file cannot be loaded.
             */
            public void loadGame(Stage stage, GameMode mode) throws Exception {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(GAME_LAYOUT_FXML));
                Parent gameRoot = loader.load();
        
                GuiController gui = loader.getController();
        
                GameController game = new GameController(
                        gui,
                        gui.getGameBoardRenderer(),
                        gui.getGameFlowController(),
                        soundManager,
                        this,
                        22, 10);
                game.initGame(mode);
        
                stage.setScene(new Scene(gameRoot, 800, 800));
                stage.show();
            }






    /**
     * The main method of the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}