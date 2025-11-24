package com.comp2042;

import com.comp2042.ui.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static final String MAIN_MENU_FXML = "/fxml/mainMenu.fxml";
    public static final String GAME_LAYOUT_FXML = "/gameLayout.fxml"; // your real path

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TetrisJFX");
        showMainMenu(primaryStage);
    }

    public void showMainMenu(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_MENU_FXML));
        Parent root = loader.load();

        MainMenuController controller = loader.getController();
        controller.setStage(stage);
        controller.setMainApp(this);

        stage.setScene(new Scene(root, 600, 800));
        stage.show();
    }

    public void loadGame(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(GAME_LAYOUT_FXML));
        Parent gameRoot = loader.load();

        // get the GUI controller from the FXML
        GuiController gui = loader.getController();

        // VERY IMPORTANT: creates board, pieces, scores, timeline, controls
        GameController game = new GameController(gui);

        stage.setScene(new Scene(gameRoot, 600, 800));
        stage.show();

        // keyboard focus
        gameRoot.requestFocus();
    }


    public void openSettings(Stage stage) {
        System.out.println("Settings screen coming soon!");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
