package com.comp2042.ui;

import com.comp2042.event.GameEvent;
import com.comp2042.event.InputEventListener;
import com.comp2042.event.ViewData;
import com.comp2042.sounds.ISoundManager;
import com.comp2042.sounds.SoundManager;
import com.comp2042.ui.GameOverPanel;
import com.comp2042.ui.NotificationPanel;
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
import javafx.scene.layout.Pane;
import com.comp2042.ui.ParticleEffect;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private ISoundManager soundManager;
    private InputEventListener eventListener;

    private GameBoardRenderer gameBoardRenderer;
    private GameInfoPanelController gameInfoPanelController;
    private GameFlowController gameFlowController;
    private InputHandler inputHandler;

    @FXML private Pane particlePane;
    private ParticleEffect particleEffect;
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

        if (soundManager == null) {
            soundManager = SoundManager.getInstance();
        }
        Button[] allButtons = {startButton, settingsButton, helpButton, pauseButton};

        if (particlePane != null) {
            particleEffect = new ParticleEffect(particlePane);
        }

        for (Button btn : allButtons) {
            if (btn != null) {
                btn.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    soundManager.playClickSound();
                });
            }
        }
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        gameBoardRenderer = new GameBoardRenderer(gamePanel);
        gameInfoPanelController = new GameInfoPanelController(scoreLabel, levelLabel, nextGrid);
        gameFlowController = new GameFlowController(
                gameBoardRenderer, gameInfoPanelController, groupNotification, pauseButton, gameOverPanel);
        inputHandler = new InputHandler(gameFlowController, gameBoardRenderer);
        particleEffect = new ParticleEffect(gamePanel);
        gameFlowController.setParticleEffect(particleEffect);

        // Game Panel UI setup
        gamePanel.setHgap(0);
        gamePanel.setVgap(0);
        StackPane.setAlignment(gamePanel, Pos.CENTER);
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(gamePanel.widthProperty());
        clip.heightProperty().bind(gamePanel.heightProperty());
        gamePanel.setClip(clip);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(inputHandler::handleKeyPress);

        pauseButton.setFocusTraversable(false);
        pauseButton.setMnemonicParsing(false);

        gameOverPanel.setVisible(false);

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
        gameFlowController.setEventListener(eventListener);
        inputHandler.setEventListener(eventListener);
    }

    public void bindScore(IntegerProperty scoreProp) {
        gameInfoPanelController.bindScore(scoreProp);
    }

    public void gameOver() {
        gameFlowController.gameOver();
    }

    @FXML
    public void newGame(ActionEvent e) {
        if (eventListener != null) {
            eventListener.onGameEvent(new com.comp2042.event.NewGameEvent());
        }
        gamePanel.requestFocus();
    }

    @FXML
    public void pauseGame(ActionEvent e) {
        soundManager.playClickSound();
        gameFlowController.pauseGame();
        gamePanel.requestFocus();
    }

    @FXML
    public void onSettingsClicked(ActionEvent e) {
        soundManager.playClickSound();
    }

    @FXML
    public void onHelpClicked(ActionEvent e) {
        soundManager.playClickSound();
    }

    @FXML
    private void backToMainMenu() {
        if (eventListener != null) {
            eventListener.onGameEvent(new com.comp2042.event.BackToMenuEvent());
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