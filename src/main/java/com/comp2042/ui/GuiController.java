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
import javafx.scene.control.Label;
import javafx.application.Platform;
import com.comp2042.GameMode;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {


    private GameMode currentMode = GameMode.CLASSIC;

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
    @FXML private Label modeNameLabel;
    @FXML private Label modeDetailsLabel;
    @FXML
    private Button modeButton;


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
        gameOverPanel.setVisible(false);
        gameBoardRenderer.initGameView(boardMatrix);
        gameInfoPanelController.updatePreviews(brick);
        gameFlowController.start();
        gameFlowController.newGame(this.currentMode);
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

    /**
     * NEW METHOD: Updates the two-line mode status display.
     * @param modeName The name of the mode (e.g., "Sprint", "Classic")
     * @param details The specific stats (e.g., "Lines: 1/20", "Score: 1500")
     */
    /**
     * Updates the mode display.
     * Fix: Classic Mode shows the Big Score Box, but hides the small yellow details.
     */
    public void updateModeStatus(String modeName, String details) {
        Platform.runLater(() -> {

            // 1. Update Top Label (e.g., "Mode: Classic")
            if (modeNameLabel != null) {
                modeNameLabel.setText("Mode: " + modeName);
            }

            // 2. Update Bottom Label (The small yellow text)
            if (modeDetailsLabel != null) {
                // If Classic, we CLEAR this line so we don't duplicate the score
                if (modeName.equalsIgnoreCase("Classic")) {
                    modeDetailsLabel.setText("");
                } else {
                    // For Sprint (Lines: 1/20), we show it
                    modeDetailsLabel.setText(details);
                }
            }

            // 3. Ensure the Big Score Box is ALWAYS VISIBLE
            // (I previously hid this, which was the mistake)
            if (scoreLabel != null) {
                scoreLabel.setVisible(true);
            }
        });
    }

    public void gameOver() {
        gameFlowController.gameOver();
    }

    @FXML
    public void newGame(ActionEvent e) {
        gameOverPanel.setVisible(false);
        gameFlowController.newGame(this.currentMode);;

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


    @FXML
    public void switchMode() {
        // Cycle through the modes: Classic -> Sprint -> Ultra -> Classic
        switch (currentMode) {
            case CLASSIC:
                currentMode = GameMode.SPRINT;
                break;
            case SPRINT:
                currentMode = GameMode.ULTRA;
                break;
            case ULTRA:
                currentMode = GameMode.CLASSIC;
                break;
        }

        // Update the labels to reflect the change
        updateModeDisplay();

        // OPTIONAL: Restart the game immediately when mode changes
        // so the user plays the new mode right away.
        newGame(null);
    }

    private void updateModeDisplay() {
        // Update the big Mode Title
        modeNameLabel.setText("Mode: " + currentMode.toString());

        // Update the sub-text to explain the rules
        switch (currentMode) {
            case CLASSIC:
                modeDetailsLabel.setText("Endless Marathon");
                break;
            case SPRINT:
                modeDetailsLabel.setText("Clear 40 Lines");
                break;
            case ULTRA:
                modeDetailsLabel.setText("2 Minute Timer");
                break;
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

    public GameFlowController getGameFlowController() {
        return gameFlowController;
    }
}