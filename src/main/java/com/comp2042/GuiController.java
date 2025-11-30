package com.comp2042;

import com.comp2042.sounds.SoundManager;
import com.comp2042.ui.GameBoardRenderer;
import com.comp2042.ui.GameFlowController;
import com.comp2042.ui.GameInfoPanelController;
import com.comp2042.ui.InputHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private SoundManager soundManager;
    private InputEventListener eventListener;

    // UI Controllers
    private GameBoardRenderer gameBoardRenderer;
    private GameInfoPanelController gameInfoPanelController;
    private GameFlowController gameFlowController;
    private InputHandler inputHandler;

    // FXML Components
    @FXML private GridPane gamePanel;
    @FXML private StackPane groupNotification;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private Label scoreLabel;
    @FXML private Button pauseButton;
    @FXML private Label levelLabel;
    @FXML private Button startButton;
    @FXML private Button settingsButton;
    @FXML private Button helpButton;
    @FXML private GridPane nextGrid;
    @FXML private Parent viewRoot;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        soundManager = new SoundManager();
        Button[] allButtons = {startButton, settingsButton, helpButton, pauseButton};

        for (Button btn : allButtons) {
            if (btn != null) {
                btn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    soundManager.playClickSound();
                });
            }
        }
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        // Initialize sub-controllers
        gameBoardRenderer = new GameBoardRenderer(gamePanel);
        gameInfoPanelController = new GameInfoPanelController(scoreLabel, levelLabel, nextGrid);
        gameFlowController = new GameFlowController(gameBoardRenderer, gameInfoPanelController, groupNotification, pauseButton, gameOverPanel);
        inputHandler = new InputHandler(gameFlowController, gameBoardRenderer);

        // Game Panel UI setup
        gamePanel.setHgap(0);
        gamePanel.setVgap(0);
        StackPane.setAlignment(gamePanel, Pos.CENTER);
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(gamePanel.widthProperty());
        clip.heightProperty().bind(gamePanel.heightProperty());
        gamePanel.setClip(clip);

        // Input handling
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(inputHandler::handleKeyPress);

        // Pause button setup
        pauseButton.setFocusTraversable(false);
        pauseButton.setMnemonicParsing(false);

        // Game Over Panel
        gameOverPanel.setVisible(false);

        // Reflection effect (if still desired)
        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        gameBoardRenderer.initGameView(boardMatrix);
        gameInfoPanelController.updatePreviews(brick);
        gameFlowController.start();
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
        // Pass the event listener to the game flow controller as well
        gameFlowController.setEventListener(eventListener); // Assuming you'll add this setter in GameFlowController
        inputHandler.setEventListener(eventListener); // Assuming you'll add this setter in InputHandler
    }

    public void bindScore(IntegerProperty scoreProp) {
        gameInfoPanelController.bindScore(scoreProp);
    }

    public void gameOver() {
        gameFlowController.gameOver();
    }

    @FXML
    public void newGame(ActionEvent e) {
        soundManager.playClickSound();
        gameFlowController.newGame();
        gamePanel.requestFocus(); // Ensure gamePanel has focus after new game
    }

    @FXML
    public void pauseGame(ActionEvent e) {
        soundManager.playClickSound();
        gameFlowController.pauseGame();
        gamePanel.requestFocus(); // Ensure gamePanel has focus after pause/resume
    }

    @FXML
    public void onSettingsClicked(ActionEvent e) {
        soundManager.playClickSound();
        System.out.println("Settings Button Clicked");
        // TODO: Add logic to switch to settings screen
    }

    @FXML
    public void onHelpClicked(ActionEvent e) {
        soundManager.playClickSound();
        System.out.println("Help Button Clicked");
        // TODO: Add logic to show help
    }

    @FXML
    private void backToMainMenu() {
        if (eventListener != null) {
            eventListener.onBackToMenuEvent();
        }
    }

    public Parent getViewRoot() {
        return viewRoot;
    }

    public GameBoardRenderer getGameBoardRenderer() {
        return gameBoardRenderer;
    }

    public GameInfoPanelController getGameInfoPanelController() {
        return gameInfoPanelController;
    }
}